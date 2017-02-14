package utils;

import java.io.File;

public final class Constant {

    private Constant() {}

    /**
     * 定义文件结束符
     */
    public static final int EOF = -1;

    /**
     * 静态文件存放地址
     */
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    /**
     * 默认端口号
     */
    public static final int PORT = 10000;
}
