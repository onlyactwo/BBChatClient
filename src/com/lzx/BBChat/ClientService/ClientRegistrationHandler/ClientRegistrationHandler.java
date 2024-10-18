package com.lzx.BBChat.ClientService.ClientRegistrationHandler;

import com.lzx.BBChat.ClientService.Menu.Menu;
import com.lzx.BBChat.Common.Message.Message;
import com.lzx.BBChat.Common.Message.MessageType;
import com.lzx.BBChat.Common.User.User;
import com.lzx.BBChat.Common.Utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 该类是用于处理用户的注册
 */
public class ClientRegistrationHandler {
    public static void handlerClientRegistration(ObjectOutputStream oos, ObjectInputStream ois){

        try {
            while (true) {
                //创建Message，向服务器端发送注册请求
                Message message_registration_request = new Message("Client","Server","请求登录", Utils.getCurrentTime(), MessageType.MESSAGE_REGISTRATION_REQUEST);
                //发送Message
                oos.writeObject(message_registration_request);
                oos.flush();

                //响应服务器返回的结果
                Message message_receive_registration_request_result_from_Server = (Message)ois.readObject();
                //判断结果
                if(message_receive_registration_request_result_from_Server.getMesType().equals(MessageType.MESSAGE_ALLOW_CLIENT_REGISTRATION_REQUEST)){
                    //服务器同意用户注册请求
                    //显示注册菜单
                    Menu.showRegistrationMenu();
                    //创建一个User，用于收集账号密码发送给服务器
                    User user = new User();
                    //提示用户输入注册信息
                    System.out.println("请输入您要注册的账号（长度小于20）： ");
                    user.setUserName(Utils.getString(0,20));
                    //检测用户是否想退出
                    if(user.getUserName().equals("\\back")){
                        //用户想退出，但是服务器端还在等待一个User对象，、
                        // 这里直接自己传一个User对象过去，让服务器返回等待响应状态
                        User user_exit = new User("\\back", "\\back");
                        //发送给服务器，通知他返回到等待状态
                        oos.writeObject(user_exit);
                        //客户端返回到初始化界面
                        return;
                    }

                    System.out.println("请输入您的密码（长度小于20）:  ");
                    user.setPassword(Utils.getString(0,20));
                    //检测用户是否想退出
                    if(user.getUserName().equals("\\back")){
                        //用户想退出，但是服务器端还在等待一个User对象，、
                        // 这里直接自己传一个User对象过去，让服务器返回等待响应状态
                        User user_exit = new User("\\back", "\\back");
                        //发送给服务器，通知他返回到等待状态
                        oos.writeObject(user_exit);
                        //客户端返回到初始化界面
                        return;
                    }

                    //用户注册信息收集完成，现在将该User发送给服务器端进行验证
                    oos.writeObject(user);
                    oos.flush();

                    //等待接收服务器端返回的结果
                    //创建接收结果的Message
                    Message message_receive_registration_result_from_Server = (Message)ois.readObject();

                    //开始验证结果
                    if(message_receive_registration_result_from_Server.getMesType().equals(MessageType.MESSAGE_ACCOUNT_REGISTRATION_SUCCEED)){
                        //注册成功！
                        System.out.println("账户注册成功！快去登陆吧孩子！");
                        //返回到初始化界面
                        return;

                    }else if(message_receive_registration_result_from_Server.getMesType().equals(MessageType.MESSAGE_ACCOUNT_REGISTRATION_FAILED)){
                        //账户注册失败!
                        System.out.println("账户已存在！请重新输入！");
                        //继续下一次循环
                        //重新向服务器发送账户注册请求
                    }

                }else{
                    //服务器拒绝用户注册请求
                    System.out.println("服务器拒绝你的注册请求！");
                    return;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
