package servlet;

import request.Request;
import response.Response;

public interface Servlet {

    void service(Request request, Response response);
}
