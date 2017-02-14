package servlet;

import request.Request;
import response.Response;

public class FirstServlet implements Servlet {

    public void service(Request request, Response response) {
        System.out.println(request.getUri());
        System.out.println(request.getParamMaps());
        response.setRequest(request);
        response.responseServlet();
    }
}
