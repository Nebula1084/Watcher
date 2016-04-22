package em.watcher.mouse;

import em.watcher.device.Device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class MouseSession extends Thread {
    private Socket socket;
    private BufferedReader bufferedReader;
    private OutputStream outputStream;
    private Device device;

    final public static int ACK = 0;
    final public static int NACK = 1;
    final public static int LOGIN = 2;
    final public static int REPORT = 3;
    final public static int CONTROL = 4;
    final public static int LOGOUT = 5;

    public MouseSession(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("connection");
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            int MESSAGE_TYPE;
            while (true) {
                MESSAGE_TYPE = bufferedReader.read();
                switch (MESSAGE_TYPE) {
                    case LOGIN:
                        login();
                        break;
                    case REPORT:
                        report();
                        break;
                    case LOGOUT:
                        logout();
                        return;
                }
            }
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

    protected void login() throws IOException {
        System.out.println("login()");
        MouseBuffer buffer;
        buffer = MouseBufferFactory.produceLittlelEndianBuffer();
        for (int i = 0; i < 4; i++) {
            byte c = (byte) bufferedReader.read();
            buffer.put(c);
        }
        int ret = buffer.getInt();
        System.out.println(ret);
        for (int i = 0; i < 32; i++) {
            byte c = (byte) bufferedReader.read();
        }
    }

    protected void report() {

    }

    protected void control() {

    }

    protected void logout() throws IOException {
        socket.close();
        System.out.println();
    }
}
