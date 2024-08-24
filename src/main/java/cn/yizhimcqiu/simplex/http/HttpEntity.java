package cn.yizhimcqiu.simplex.http;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class HttpEntity {
    public Method method;
    public ContentType contentType;
    public String body;
    protected String methodAndTarget;
    protected String header;
    public HttpEntity(Method method, String target, ContentType contentType, String body) {
        this.method = method;
        this.contentType = contentType;
        this.body = body;
        this.methodAndTarget = method.name + " target HTTP/1.1".replace("target", target);
        this.header = methodAndTarget;
        if (!this.contentType.toString().equals("")) {
            this.header += "\n" + contentType;
        }
    }
    public HttpEntity(Method method, String target, ContentType... accept) {
        this(method, target, ContentType.OTHER, "");
        this.header += "\nAccept: " + buildAcceptText(accept);
    }
    public void send(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        System.out.println("awa");
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write(toString());
        bufferedWriter.flush();
        socket.close();
    }
    @Override
    public String toString() {
        String str = this.header;
        if (!this.body.equals("")) {
            str += "\n\n" + body;
        }
        return str;
    }
    public static HttpEntity get(String target, ContentType... accept) {
        HttpEntity httpEntity = new HttpEntity(Method.GET, target, accept);
        if (SimpleHttp.DEBUG_MODE) {
            System.out.println(httpEntity);
        }
        return httpEntity;
    }
    private static String buildAcceptText(ContentType[] contentTypes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0;i < contentTypes.length;i++) {
            sb.append(contentTypes[i].name);
            if (i < contentTypes.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
