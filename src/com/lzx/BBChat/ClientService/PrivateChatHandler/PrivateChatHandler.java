package com.lzx.BBChat.ClientService.PrivateChatHandler;

import com.lzx.BBChat.ClientService.Menu.Menu;
import com.lzx.BBChat.ClientService.UserOnlinePrivateChatHandler.UserOnlinePrivateChatHandler;
import com.lzx.BBChat.Common.Message.Message;
import com.lzx.BBChat.Common.Message.MessageType;
import com.lzx.BBChat.Common.User.User;
import com.lzx.BBChat.Common.Utils.Utils;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/**
 * 这个类是用于实现用户进行私聊的功能
 * 1.向服务器端发送进行私聊的请求
 * 2.接收服务器端的反馈 （同意/不同意）
 * 3.不同意：提示系统繁忙 -》返回到选功能的界面
 * 4.同意:提示用户输入想要和谁私聊（做一个存在、在线验证）
 * 5.在线直接发
 * 6.不在线留言
 * 7.用户不存在-》重新向服务器发送私聊请求
 */
public class PrivateChatHandler {
    public static void handlePrivateChat(String userName,ObjectOutputStream oos , ObjectInputStream ois) {
        try {
            while (true) {
                Scanner sc = new Scanner(System.in);
                //向服务器发送私聊请求
                //创建发送请求的Message
                Message message_private_chat_request = new Message(userName, "Server", "私聊请求", Utils.getCurrentTime(), MessageType.MESSAGE_PRIVATE_CHAT_REQUEST);
                //向服务器发送请求私聊Message
                oos.writeObject(message_private_chat_request);
                oos.flush();
                //接收服务器的反馈
                Message message_receive_private_chat_request_result_from_Server = (Message)ois.readObject();

                //判断服务器的反馈
                if (message_receive_private_chat_request_result_from_Server.getMesType().equals(MessageType.MESSAGE_ALLOW_PRIVATE_CHAT_REQUEST)) {
                    //服务器同意私聊请求
                    //显示私聊的菜单
                    Menu.showPrivateChatMenu(userName);

                    //提示用户输入想要私聊的用户的用户名
                    System.out.println("请输入您想要私聊的用户的用户名 ： ");
                    //收集该名字 ->转成User 发给服务器验证
                    User targetUser = new User();
                    targetUser.setUserName(Utils.getString(0,20));

                    //发送给服务器验证
                    oos.writeObject(targetUser);
                    oos.flush();
                    //接收服务器验证情况
                    Message message_receive_check_user_state  = (Message) ois.readObject();

                    //判断验证情况
                    if(message_receive_check_user_state.getMesType().equals(MessageType.MESSAGE_PRIVATE_USER_IS_NOT_EXIST)){
                        //该用户不存在
                        System.out.println("该用户不存在！请检查用户名是否正确！");
                        //重新向服务器发送私聊请求(不退出此方法)
                        //服务器端返回等待响应状态了

                    }else if(message_receive_check_user_state.getMesType().equals(MessageType.MESSAGE_PRIVATE_USER_ONLINE)){
                        //该用户在线
                        //开启在线聊天功能
                        UserOnlinePrivateChatHandler.handleUserOnlinePrivateChat(userName, targetUser.getUserName(),oos,ois);
                        //结束在线聊天功能以后客户端返回到选择功能界面
                        return;
                    }else if(message_receive_check_user_state.getMesType().equals(MessageType.MESSAGE_PRIVATE_USER_OFFLINE)){
                        //该用户离线
                        //让用户继续选择是否选择继续发送消息

                        System.out.println("该用户离线，是否继续发送消息？请输入Yes/No");
                        String option;
                        while (true) {
                             option = Utils.getString(0,4);
                             if(option.equals("No")||option.equals("no")){
                                 //退出聊天
                                 //告诉服务器
                                 Message message_leave = new Message(userName, "Server", "退出私聊", Utils.getCurrentTime(), MessageType.MESSAGE_PRIVATE_CHAT_EXIT);
                                 oos.writeObject(message_leave);
                                 //这里直接退出到选择功能界面
                                 return;
                             }else if(option.equals("Yes")||option.equals("YES")){
                                 //继续聊天
                                 //开启离线发送消息功能(功能和在线的是一样的)
                                 UserOnlinePrivateChatHandler.handleUserOnlinePrivateChat(userName, targetUser.getUserName(),oos,ois);
                                 //这里直接退出到选择功能界面
                                 return;
                             }else {
                                 System.out.println("请检查输入!");
                             }
                        }

                    }


                }else{
                    //服务器拒绝私聊请求
                    System.out.println("服务器拒绝了你的私聊请求！");
                    //关闭流
                    sc.close();
                    //退出到选功能界面
                    return;
                }


            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
