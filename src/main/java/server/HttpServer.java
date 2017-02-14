package server;

import processor.Processor;
import processor.ServletProcessor;
import processor.StaticProcessor;
import request.Request;
import response.Response;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.File;

public class HttpServer {

    /**
     * 静态文件存放地址
     */
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    /**
     * 默认端口号
     */
    private static final int PORT = 8080;

    private boolean shutdown = false;

    public static void main(String[] args) {

        HttpServer server = new HttpServer();

        server.start();
    }

    private void start() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            if(serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println("server start failed");
            return;
        }
        while (!shutdown) {
            Socket socket;
            try {
                socket = serverSocket.accept();
                Processor processor;
                Request request = new Request(socket.getInputStream());
                request.parse();
                if(request.isServlet()) {
                    processor = new ServletProcessor();
                } else {
                    processor = new StaticProcessor();
                }
                processor.process(request, new Response(socket.getOutputStream()));
                socket.close();
                shutdown = shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否关闭
     * @return
     */
    private boolean shutdown() {
        return false;
    }
}