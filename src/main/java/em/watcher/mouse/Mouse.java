package em.watcher.mouse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Mouse {
    public final static int PORT = 10659;
    private static Mouse instance;

    private ServerSocket serverSocket;
    private Socket socket;

    private static synchronized void initMouse() throws IOException {
        if (instance == null) {
            instance = new Mouse();
            instance.startSocket();
        }
    }

    public static void start() throws IOException {
        if (instance == null) {
            initMouse();
        }
    }

    private Mouse() throws IOException {
        serverSocket = new ServerSocket(Mouse.PORT);
        System.out.println("Mouse::Mouse()");
    }

    private void startSocket() {
        thread.start();
    }

    private Thread thread = new Thread() {

        @Override
        public void run() {
            System.out.println("Mouse Started\n");
            while (true) {
                try {
                    socket = serverSocket.accept();
                    MouseSession session = new MouseSession(socket);
                    session.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };


}
