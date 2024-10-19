package com.lzx.BBChat.ClientService.UserCheckMessageInBoxHandler;

import com.lzx.BBChat.Common.Message.Message;
import com.lzx.BBChat.Common.Message.MessageInBox;
import com.lzx.BBChat.Common.Message.MessageType;
import com.lzx.BBChat.Common.Utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;


/**
 * 这个类用于处理用户想要查看信箱
 * 向服务器发送查看信箱的请求
 *需要：
 * userName ,oos ois
 */
public class UserCheckMessageInBoxHandler {
    public static void handleUserCheckMessageInBox(String userName,ObjectOutputStream oos, ObjectInputStream ois){
        try {
            //建立Message，向服务器发送查看信箱请求
            Message message_check_message_inbox_request = new Message(userName, "Server", "请求查看信箱", Utils.getCurrentTime(), MessageType.MESSAGE_USER_CHECK_MESSAGE_INBOX_REQUEST);
            //发送给服务器
            oos.writeObject(message_check_message_inbox_request);

            //等待服务器的验证
            Message message_receive_check_message_inbox_request_result = (Message) ois.readObject();
            //判断结果
            if(message_receive_check_message_inbox_request_result.getMesType().equals(MessageType.MESSAGE_ALLOW_USER_CHECK_MESSAGE_INBOX_REQUEST)){
                //服务器同意查看信箱
                //接收服务器返回来的MessageInBox(里面维护了一个Vector)
                MessageInBox messageInBox = (MessageInBox) ois.readObject();
                //客户端再输入到控制台
                Vector<Message> messages = messageInBox.readMessageBox();
                if (!messages.isEmpty()) {
                    for(Message message: messages){
                        System.out.println("[ " + message.getSendTime() + message.getSender() + " : " + message.getContent());
                    }
                } else {
                    System.out.println("您的收件箱为空！");
                }

                //查看完信箱就退出
                while (true){
                    System.out.println("查看完毕，输入\\back返回");
                    if(Utils.getString(0,10).equals("\\back")){
                        return;
                    }
                }
            }else {
                //服务器不同意查看信箱
                System.out.println("服务器拒绝了你查看信箱的请求!");
                //退出到选功能界面
            }


        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
