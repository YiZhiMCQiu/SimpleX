package cn.yizhimcqiu.simplex.http;

import java.util.HashMap;
import java.util.Map;

public enum Method {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    CONNECT("CONNECT"),
    UNKNOWN("unknown");
    private static final Map<String, Method> methods = new HashMap<>();
    public final String name;
    static {
        for (Method method : values()) {
            methods.put(method.name, method);
        }
    }
    Method(String name) {
        this.name = name;
    }
   public static Method getMethod(String method) {
        return methods.getOrDefault(method, UNKNOWN);
   }
}
