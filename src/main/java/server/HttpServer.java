package server;

import request.Request;
import response.Response;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.InputStream;
import java.io.OutputStream;
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

    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

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
            InputStream input;
            OutputStream output;
            try {
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();
                Request request = new Request(input);
                request.parse();
                System.out.println(request.getParamMaps());
                Response response = new Response(output);
                response.setRequest(request);
                response.sendStaticResource();
                socket.close();
                shutdown = SHUTDOWN_COMMAND.equals(request.getUri());
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
}