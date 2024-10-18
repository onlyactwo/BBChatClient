package com.lzx.BBChat.Client;

import com.lzx.BBChat.ClientHandler.ClientHandler;

import java.io.IOException;
import java.net.Socket;

/**
 * 客户端运行这个类，当这个类的main方法启动的时候，尝试与服务器进行连接，如果连接成功，则启动ClientHandler线程
 */
public class Client {
    private static final String ServerIp = "172.27.31.187";
    private static final int ServerPort = 9999;
    public static void main(String[] args) {
      startClient();
    }
    public static void startClient(){
        try {

            //尝试连接到服务器
            Socket socket = new Socket(ServerIp, ServerPort);
            //连接成功
            //启动ClientHandler线程
            ClientHandler clientHandler = new ClientHandler(socket);
            Thread clientHandlerThread = new Thread(clientHandler);
            clientHandlerThread.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
