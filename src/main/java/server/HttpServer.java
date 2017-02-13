package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    /**
     * 静态资源存储地址
     */
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    private static final int BUFFER_SIZE = 2018;

    private static boolean shutdown = false;

    public static void main(String [] args) throws IOException {
        HttpServer httpServer = new HttpServer();
        httpServer.start();
    }


    private void start() throws IOException {
        System.out.println(WEB_ROOT);
        ServerSocket server = new ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));
        while (!shutdown) {
            Socket client = server.accept();
            File file = new File(WEB_ROOT, "index1.html");
            if(file.exists()) {
                byte [] data = new byte[BUFFER_SIZE];
                InputStream in = new FileInputStream(file);
                OutputStream out = client.getOutputStream();
                int ch = in.read(data,0,BUFFER_SIZE);
                while(ch != -1) {
                    out.write(data, 0, ch);
                    ch = in.read(data,0,BUFFER_SIZE);
                }
                in.close();
                client.close();
            }else {
                OutputStream out = client.getOutputStream();
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 23\r\n" +
                        "\r\n" +
                        "<h1>File Not Found</h1>";
                out.write(errorMessage.getBytes());
            }
            shutdown = shutdown();
        }
    }

    private boolean shutdown() {
        return false;
    }
}
