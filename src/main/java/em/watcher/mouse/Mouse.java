package em.watcher.mouse;

import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class Mouse {
    public static int PORT = 10659;

    private ServerSocket serverSocket;

    private Mouse() {
        while (true) {
            try {
                serverSocket = new ServerSocket(Mouse.PORT);
                break;
            } catch (IOException e) {
                Mouse.PORT += 1;
                e.printStackTrace();
            }
        }

        Thread thread = new Thread() {

            @Override
            public void run() {
                System.out.println("Mouse Started\n");
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        MouseSession session = new MouseSession(socket);
                        session.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

}
