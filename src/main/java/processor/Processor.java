package processor;

import request.Request;
import response.Response;

/**
 * 资源处理器
 */
public interface Processor {

    void process(Request request, Response response);
}
