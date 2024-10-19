package com.lzx.BBChat.ClientHandler;

import com.lzx.BBChat.ClientService.ClientExitAppHandler.ClientExitAppHandler;
import com.lzx.BBChat.ClientService.ClientLoginHandler.ClientLoginHandler;
import com.lzx.BBChat.ClientService.ClientRegistrationHandler.ClientRegistrationHandler;
import com.lzx.BBChat.ClientService.Menu.Menu;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 这个类用于
 * 1.处理客户的请求
 * 2.将请求发送给客户端
 * 3.接收客户端的反馈
 * <p>
 * 该线程为了要与服务器进行通信，所以必须持有Socket的IO流
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    //获取与服务器端通信的IO流,在使用构造器的时候进行构建
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    @Override
    public void run() {
        //连接成功，显示初始界面
        Menu.showInitMenu();
        //等待用户请求（选择功能）
        Scanner sc = new Scanner(System.in);
        String userOption;
        while (true) {
            userOption = sc.nextLine();
            if (userOption.equals("1")) {
                //用户请求登录,登录成功以后会返回用户名
                ClientLoginHandler.handClientLogin(oos,ois);

                //如果用户从登录界面退出 -》显示初始界面
                Menu.showInitMenu();
            } else if (userOption.equals("2")) {
                //用于请求注册
                ClientRegistrationHandler.handlerClientRegistration(oos,ois);
                Menu.showInitMenu();
                //如果用户从注册界面退出 -》显示初始界面
            } else if (userOption.equals("3")) {
                //退出软件
                //向服务器发送关闭消息，终止服务器对应的该线程
                ClientExitAppHandler.handleClientExitApp(oos);
                //关闭流
                try {
                    oos.close();
                    ois.close();
                    sc.close();
                    return;
                } catch (IOException e) {
                    System.out.println("退出异常！强制退出");
                    return;
                }
            } else {
                System.out.println("请检查输入！");
            }
        }
    }

    public ClientHandler(Socket socket) {
        this.socket = socket;
        //获取与服务器端通信的IO流
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
