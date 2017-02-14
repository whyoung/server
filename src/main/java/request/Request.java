package request;

import utils.Constant;

import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private InputStream input;
    private String uri;

    /**
     * 是否是servlet资源
     */
    private boolean servlet;

    /**
     * servlet命名规范
     */
    private static final String SERVLET_PATTERN = "^/\\w+Servlet$";
    /**
     * 参数字典
     */
    private Map<String, String> paramMaps;

    public Request(InputStream input) {
        this.input = input;
        this.paramMaps = new HashMap<String, String>();
    }

    /**
     * 解析http请求头，获取用户请求路径
     */
    public void parse() {
        StringBuilder request = new StringBuilder(2048);
        byte[] buffer = new byte[2048];
        try {
            int i = input.read(buffer);
            for (int j=0; j<i; j++) {
                request.append((char) buffer[j]);
            }
            uri = setUri(request.toString());
            System.out.println(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析uri和参数
     * @param requestString
     * @return
     */
    private String setUri(String requestString) {
        int start = requestString.indexOf(' ');
        if (start != Constant.EOF) {
            int end = requestString.indexOf(' ', start + 1);
            if (end > start) {
                String requestUri = requestString.substring(start + 1, end);
                int paramIndex = requestUri.indexOf("?");
                if(paramIndex == Constant.EOF) {
                    servlet = requestUri.matches(SERVLET_PATTERN);
                    return requestUri;
                } else {
                    String[] paramList = requestUri.substring(paramIndex+1).split("&");
                    if(paramList == null || paramList.length == 0) {
                        servlet = requestUri.matches(SERVLET_PATTERN);
                        return requestUri;
                    } else {
                        for(String param : paramList) {
                            String [] kv = param.split("=");
                            if(kv != null && kv.length == 2) {
                                paramMaps.put(kv[0], kv[1]);
                            }
                        }
                    }
                    requestUri = requestUri.substring(0, paramIndex);
                    servlet = requestUri.matches(SERVLET_PATTERN);
                    return requestUri;
                }
            }
        }
        return null;
    }

    public String getUri() {
        return uri;
    }

    public Map<String, String> getParamMaps() {
        return this.paramMaps;
    }

    public boolean isServlet() {
        return servlet;
    }
}