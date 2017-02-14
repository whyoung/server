package processor;

import request.Request;
import request.RequestFacade;
import response.Response;
import response.ResponseFacade;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
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
        /**
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        //servletName = "servlet/" + servletName;
        URLClassLoader loader = null;
        try {
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constant.WEB_ROOT);
            String repository =(new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);
        } catch (IOException e) {
            System.out.println(e.toString() );
        }

        Class myClass = null;
        try {
            myClass = loader.loadClass(servletName);
        } catch (ClassNotFoundException e) {

            System.out.println(e.toString());
        }
        Servlet servlet;
        try {
            servlet = (Servlet) myClass.newInstance();
            servlet.service(request, response);
        } catch (Exception e) {
            System.out.println(e.toString());
        } catch (Throwable e) {
            System.out.println(e.toString());
        }
        */

        String servletPath = servletMap.get(request.getUri());
        if(servletPath == null) {
            response.responseServlet("<h1> can not find servlet "+request.getUri()+"</h1>");
        } else {
            try {
                Servlet servlet = (Servlet) Class.forName(servletPath).newInstance();
                servlet.service(new RequestFacade(request), new ResponseFacade(response));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
