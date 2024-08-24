package cn.yizhimcqiu.simplex.http;

import java.util.HashMap;
import java.util.Map;

public enum ContentType {
    JSON("text/json"),
    XML("text/xml"),
    TEXT("text/plain"),
    HTML("text/html"),
    OTHER("");
    private static final Map<String, ContentType> contentTypes = new HashMap<>();
    public final String name;
    static {
        for (ContentType ct : values()) {
            contentTypes.put(ct.name, ct);
        }
    }
    ContentType(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        if (!this.equals(OTHER)) {
            return "Content-Type: " + this.name;
        } else {
            return "";
        }
    }

    public static ContentType getContentType(String contentType) {
        return contentTypes.getOrDefault(contentType, OTHER);
    }
}