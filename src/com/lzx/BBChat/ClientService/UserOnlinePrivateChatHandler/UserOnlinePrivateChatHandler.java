package com.lzx.BBChat.ClientService.UserOnlinePrivateChatHandler;

import com.lzx.BBChat.Common.Message.Message;
import com.lzx.BBChat.Common.Message.MessageType;
import com.lzx.BBChat.Common.Utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * 这个类用于处理用户 与其他在线用户进行私聊
 * 前提：
 * 客户端已经收集了 想要发送的人员的用户名
 * 并且已经得到了，服务器的反馈：该用户存在，并且，该用户在线！
 * 现在服务器端正在不断地等待着客户端想要发送的信息

 * 现在客户端需要收集用户想要发送的信息，并且发送给服务器进行转发
 * 还需要得到服务器反馈回来的值（即是否发送成功）
 */
public class UserOnlinePrivateChatHandler {
    public static void handleUserOnlinePrivateChat(String userName, String targetName, ObjectOutputStream oos, ObjectInputStream ois){
        try {
            String content;
            Message message_to_targetUser;
            Message message_receive_send_message_result_from_Server;
            while(true){
                //不断地从用户键盘的到想要发送的信息
                System.out.println("请输入您想发送给 " + targetName + " 的消息(输入\\back退出！)： ");
                content = Utils.getString(0,100);

                //检查用户发送的消息是否是想退出
                if(content.equals("\\back")){
                    //用户想停止私聊
                    //创建停止私聊的Message
                    message_to_targetUser = new Message(userName, targetName, content, Utils.getCurrentTime(), MessageType.MESSAGE_PRIVATE_CHAT_EXIT);
                    //发送给服务器
                    oos.writeObject(message_to_targetUser);
                    //结束在线聊天功能以后客户端返回到选择功能界面
                    return;
                }else {
                    //用户不想退出，发送正常的消息
                    //建立发送的Message
                     message_to_targetUser = new Message(userName, targetName, content, Utils.getCurrentTime(), MessageType.MESSAGE_PRIVATE_CHAT_TXT);
                    //发送给服务器
                    oos.writeObject(message_to_targetUser);
                    //等待服务器发送回来的结果
                    message_receive_send_message_result_from_Server = (Message)ois.readObject();
                    if (message_receive_send_message_result_from_Server.getMesType().equals(MessageType.MESSAGE_PRIVATE_CHAT_SEND_SUCCEED)){
                        System.out.println("发送成功！");
                    }else if(message_receive_send_message_result_from_Server.getMesType().equals(MessageType.MESSAGE_PRIVATE_CHAT_SEND_FAILED)){
                        System.out.println("发送失败！");
                    }
                }


            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
