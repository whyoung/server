package response;

import request.Request;
import server.HttpServer;

import java.io.OutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

public class Response {

    private static final int BUFFER_SIZE = 1024;

    /**
     * 主页地址
     */
    private static final String INDEX_PAGE = "index.html";

    /**
     * 404页面
     */
    private static final String ERROR_MESSAGE = "<h1>File Not Found</h1>";

    private Request request;
    private OutputStream output;

    public Response(OutputStream output) {

        this.output = output;
    }

    public void setRequest(Request request) {

        this.request = request;
    }

    public void sendStaticResource() {
        FileInputStream fis = null;
        try {
            File file;
            if(request.getUri() == null || "/".equals(request.getUri())) {
                file = new File(HttpServer.WEB_ROOT, INDEX_PAGE);
            } else {
                file = new File(HttpServer.WEB_ROOT, request.getUri());
            }
            if (file.exists()) {
                byte[] bytes = new byte[BUFFER_SIZE];
                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1) {
                    output.write(bytes, 0, ch);
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }

            } else {
                output.write(ERROR_MESSAGE.getBytes());
            }

        } catch (IOException e) {

            System.out.println(e.toString());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 响应servlet请求
     */
    public void responseServlet() {
        try {
            output.write(("<h1>Servlet Request:"+request.getUri()+"</h1>").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
