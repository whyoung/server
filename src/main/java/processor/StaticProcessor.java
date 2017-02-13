package processor;

import request.Request;
import response.Response;

/**
 * 静态资源处理器
 */
public class StaticProcessor implements Processor {

    public void process(Request request, Response response) {
        response.setRequest(request);
        response.sendStaticResource();
    }
}
