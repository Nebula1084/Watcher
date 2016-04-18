package em.watcher.mouse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MouseSession extends Thread {
    private Socket socket;

    public MouseSession(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("connection");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String line = null;
            int c, i = 0;
            while ((c = in.read()) != -1) {
                i++;
                System.out.println(i + ":" + c);
            }
            logout();
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

    protected void login() {

    }

    protected void report() {

    }

    protected void control() {

    }

    protected void logout() throws IOException {
        socket.close();
    }
}
