package com.lzx.BBChat.ClientService.UserCheckOnlinePeopleHandler;

import com.lzx.BBChat.Common.Message.Message;
import com.lzx.BBChat.Common.Message.MessageType;
import com.lzx.BBChat.Common.Utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * 这个类是用于 处理用户拉取在线人员名单的
 *
 */
public class UserCheckOnlinePeopleHandler {
    public static void handleUserCheckOnlinePeople(String userName,ObjectOutputStream oos, ObjectInputStream ois){
        try {
            //建立Message，向服务器发送查看信箱请求
            Message message_check_message_inbox_request = new Message(userName, "Server", "请求查看信箱", Utils.getCurrentTime(), MessageType.MESSAGE_USER_CHECK_ONLINE_PEOPLE_REQUEST);
            //发送给服务器
            oos.writeObject(message_check_message_inbox_request);

            //等待服务器的验证
            Message message_receive_check_people_online_request_result = (Message) ois.readObject();
            //判断结果
            if(message_receive_check_people_online_request_result.getMesType().equals(MessageType.MESSAGE_ALLOW_USER_CHECK_ONLINE_PEOPLE_REQUEST)){
                //同意
                //接收服务器返回的keySet
                HashSet keySet = (HashSet)ois.readObject();
                //输出keySet
                System.out.println("==========在线用户========");
                for(Object name : keySet){
                    String name1 = (String)name;
                    System.out.println(Utils.getCurrentTime() + "   " + name1);
                }
            }else{
                System.out.println("服务器拒绝了你查看在线用户的请求!");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
