package processor;

import request.Request;
import response.Response;

/**
 * 动态资源处理器
 */
public class ServletProcessor implements Processor {

    public void process(Request request, Response response) {
        response.setRequest(request);
        response.responseServlet();
    }
}
