package com.lzx.BBChat.ClientService.ClientExitAppHandler;

import com.lzx.BBChat.Common.Message.Message;
import com.lzx.BBChat.Common.Message.MessageType;
import com.lzx.BBChat.Common.Utils.Utils;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * 这个类用于 客户端退出软件，通知服务器端结束线程！
 */
public class ClientExitAppHandler {
    public static void handleClientExitApp(ObjectOutputStream oos){
        //此时的服务器端处于待响应状态，可以向服务器端发送一个Message
        //创建Message，用于通知服务器关闭线程
        Message message_exit = new Message("Client","Server","关闭软件", Utils.getCurrentTime(), MessageType.MESSAGE_EXIT);
        //发送Message
        try {
            oos.writeObject(message_exit);
        } catch (IOException e) {
            System.out.println("软件关闭中。。通知服务器关闭线程失败！");
        }
        //结束
        System.out.println("您关闭了软件。。。。");
    }
}
