package processor;

import request.Request;
import response.Response;
import servlet.Servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 动态资源处理器
 */
public class ServletProcessor implements Processor {

    /**
     * servlet映射
     */
    private static final Map<String, String> servletMap = new HashMap<String, String>();

    //初始化servlet信息
    static {
        Properties properties = new Properties();
        try {
            InputStream in = new FileInputStream("/Users/whyoung/IdeaProjects/demo/server/src/main/resources/servlet.properties");
            properties.load(in);
            Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();
            Iterator<Map.Entry<Object, Object>> iterator = entrySet.iterator();
            while(iterator.hasNext()) {
                Map.Entry<Object, Object> entry = iterator.next();
                servletMap.put("/"+entry.getKey(), entry.getValue().toString());
            }
            System.out.println(properties);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void process(Request request, Response response) {
        String servletPath = servletMap.get(request.getUri());
        if(servletPath == null) {
            response.responseServlet("<h1> can not find servlet "+request.getUri()+"</h1>");
        } else {
            try {
                Servlet servlet = (Servlet) Class.forName(servletPath).newInstance();
                servlet.service(request, response);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
