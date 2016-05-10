package em.watcher.mouse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MouseSession extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private enum MessageType {
        ACK, NACK, LOGIN, REPORT, CONTROL, LOGOUT
    }

    public MouseSession(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        int c;
        System.out.println("connection");
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ISO-8859-1"));
            out = new PrintWriter(socket.getOutputStream(), true);

            // if
            login();
            // else
            // socket.close();

            while ((c = in.read()) != -1) {
                System.out.println("Head: " + MessageType.values()[c]);
                if (c == MessageType.REPORT.ordinal())
                    report();
                else if (c == MessageType.CONTROL.ordinal())
                    control();
                else if (c == MessageType.LOGOUT.ordinal())
                    if (logout()) break;
            }

            System.out.println("connection finished");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void ack() throws IOException {
        out.print((char)MessageType.ACK.ordinal());
        out.flush();
    }
    private void nack() throws IOException {
        out.print((char)MessageType.NACK.ordinal());
        out.flush();
    }
    private void respond(boolean val) throws IOException {
        if (val) ack();
        else nack();
    }
    private boolean login() throws IOException{
        int c, device_id = 0;
        boolean ret = true;
        char[] key = new char[32];

        if ((c = in.read()) != -1) {
            System.out.println("Head: " + MessageType.values()[c]);
            if (c == MessageType.LOGIN.ordinal()) {
                for (int i = 0; i < 4; i++) {
                    if ((c = in.read()) != -1) {
                        device_id += c<<(8*i);
                    }
                    else {
                        ret = false;
                        break;
                    }
                }
                System.out.println("Device ID: " + device_id);

                for (int i = 0; i < 32; i++) {
                    if ((c = in.read()) != -1) {
                        key[i] = (char)c;
                    }
                    else {
                        ret = false;
                        break;
                    }
                }
                System.out.print("Key: ");
                System.out.println(key);

                //if (table[device_id] != key)
                //ret = false;
            }
        }
        respond(ret);

        return ret;
    }

    protected boolean report() throws IOException {
        int c, id = 0, data_float = 0;
        long data_double = 0;
        boolean ret = true;

        System.out.println("Recv REPORT.");

        for (int i = 0; i < 4; i++) {
            if ((c = in.read()) != -1) {
                id += c<<(8*i);
            }
            else {
                ret = false;
                break;
            }
        }
        System.out.println("Source ID: " + id);

        //Test, float + double
        for (int i = 0; i < 4; i++) {
            if ((c = in.read()) != -1) {
                data_float += c<<(8*i);
            }
            else {
                ret = false;
                break;
            }
        }
        System.out.println("Data1: float: " + Float.intBitsToFloat(data_float));

        for (int i = 0; i < 8; i++) {
            if ((c = in.read()) != -1) {
                data_double += ((long)c)<<(8*i);
            }
            else {
                ret = false;
                break;
            }
        }
        System.out.println("Data2: double: " + Double.longBitsToDouble(data_double));

        respond(ret);

        return ret;
    }

    protected void control() {

    }

    private boolean logout() throws IOException{
        int c;

        System.out.println("Recv LOGOUT.");

        ack();
        socket.close();
        return true;
    }
}