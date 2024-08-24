package cn.yizhimcqiu.simplex;

import org.jetbrains.annotations.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Objects;
import java.util.function.Consumer;

/**Simple Socket<br>
 * 一个便捷的套接字工具<br>
 * 包含了客户端和服务端的套接字创建及处理<br>
 * 比java.net.Socket更加简单，所有的异常均在方法内处理
 * @author YiZhiMCQiu
 * @version 1.0
 * @see java.net.Socket
 * @see java.net.ServerSocket
 */

public class SimpleSocket {
    /**
     * 创建一个客户端套接字实例
     * @param address 要创建的套接字的地址
     * @param port 要创建的套接字的端口
     * @return 创建的套接字。若创建过程中出现异常，返回null
     * @see java.net.Socket#Socket(String, int)
     */
    @Nullable
    public static Socket createSocket(@NotNull String address, int port) {
        try {
            return new Socket(address, port);
        } catch (IOException e) {
            System.err.print("IOException at SimpleSocket#createSocket(String, int): ");
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * 创建一个服务端套接字实例
     * @param port 要监听的端口
     * @return 创建的套接字。若过程中出现异常，返回null
     */
    @Nullable
    public static ServerSocket createServerSocket(int port) {
        try {
            return new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * 在指定服务端套接字中监听客户端连接并处理，同时不影响主线程继续执行<br>
     * 执行完操作后会自动刷新客户端连接的输出流<br>
     * @param serverSocket 要监听的服务端套接字
     * @param consumer 要执行的操作
     */
    public static void addListener(@NotNull ServerSocket serverSocket, @NotNull Consumer<Socket> consumer) {
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    consumer.accept(socket);
                    if (socket != null && !socket.isClosed()) {
                        Objects.requireNonNull(getWriter(socket)).flush();
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    /**
     * 安全的关闭指定的套接字
     * @param socket 要关闭的套接字
     */
    public static void closeSocket(@NotNull Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            try {
                socket.close();
            } catch (IOException ignored) {

            }
        }
    }

    /**
     * 获取一个套接字实例的BufferedReader作为输入
     * @param socket 要获取BufferedReader的套接字
     * @return 获取到的BufferedReader。若获取过程中出现异常，返回null
     * @see SimpleSocket#readLine(Socket)
     */
    @Nullable
    public static BufferedReader getReader(@NotNull Socket socket) {
        try {
            return new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            closeSocket(socket);
            return null;
        }
    }

    /**
     * 获取一个套接字实例的PrintWriter作为输出
     * @param socket 要获取PrintWriter的套接字
     * @return 获取到的PrintWriter
     * @see SimpleSocket#println(Socket, String)
     */
    @Nullable
    public static PrintWriter getWriter(@NotNull Socket socket) {
        try {
            return new PrintWriter(new PrintWriter(socket.getOutputStream(), true));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            closeSocket(socket);
            return null;
        }
    }

    /**
     * 安全的读取一行内容。
     * @param socket 要读取的套接字
     * @return 读取到的内容。若读取时出现异常，返回null
     * @see SimpleSocket#getReader(Socket)
     */
    public static String readLine(@NotNull Socket socket) {
        if (socket.isClosed()) return null;
        try {
            return Objects.requireNonNull(getReader(socket)).readLine();
        } catch (IOException e) {
            if (!e.getMessage().equals("Connection reset")) {
                System.err.println(e.getMessage());
                closeSocket(socket);
            }
            return null;
        }
    }
    /**
     * 安全的发送一行内容
     * @param socket 要读取的套接字
     * @param message 要发送的内容
     * @see SimpleSocket#getWriter(Socket)
     */
    public static void println(@NotNull Socket socket, @NotNull String message) {
        if (socket.isClosed()) return;
        PrintWriter writer = Objects.requireNonNull(getWriter(socket));
        writer.println(message);
        writer.flush();
    }
}
