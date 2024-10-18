package com.lzx.BBChat.ClientService.ClientLoginHandler;

import com.lzx.BBChat.ClientService.ClientManagerUserHandler.ClientManagerUserHandler;
import com.lzx.BBChat.ClientService.ClientNormalUserHandler.ClientNormalUserHandler;
import com.lzx.BBChat.ClientService.Menu.Menu;
import com.lzx.BBChat.Common.Message.Message;
import com.lzx.BBChat.Common.Message.MessageType;
import com.lzx.BBChat.Common.User.User;
import com.lzx.BBChat.Common.Utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 这个类是用于处理用户的登录请求：
 * 1.向服务器发送登录请求
 * 2.响应服务器返回对于登录请求的结果
 * 3.如果可以登录，就收集用户的账号密码，发送user
 * 4.响应登录结果
 */
public class ClientLoginHandler {
    public static void handClientLogin(ObjectOutputStream oos,ObjectInputStream ois){
        try {

            while (true) {
                //创建Message，向服务器发送登录请求
                Message message_sendLoginRequestToServer = new Message("Clinet","Server","客户端请求登录", Utils.getCurrentTime(), MessageType.MESSAGE_LOGIN_REQUEST);

                //发送Message
                oos.writeObject(message_sendLoginRequestToServer);
                oos.flush();//处理缓存

                //等待服务器端的回应
                Message message_receiveLoginRequestResponseFromServer = (Message)ois.readObject();
                if(message_receiveLoginRequestResponseFromServer.getMesType().equals(MessageType.MESSAGE_ALLOW_CLIENT_LOGIN_REQUEST)){
                    //服务器同意用户登录
                    Menu.showLoginMenu();//退出还没做
                    //收集用户的账号密码
                    User user = new User();
                    System.out.println("请输入您的账号：");
                    user.setUserName(Utils.getString(0,20));
                    if(user.getUserName().equals("\\back")){
                        //用户想退出，但是服务器端还在等待一个User对象，、
                        // 这里直接自己传一个User对象过去，让服务器返回等待响应状态
                        User user_exit = new User("\\back", "\\back");
                        //发送给服务器，通知他返回到等待状态
                        oos.writeObject(user_exit);
                        oos.flush();
                        //客户端返回到初始化界面
                        return;
                    }
                    System.out.println("请输入您的密码: ");
                    user.setPassword(Utils.getString(0,20));

                    if(user.getUserName().equals("\\back")){
                        //用户想退出，但是服务器端还在等待一个User对象，、
                        // 这里直接自己传一个User对象过去，让服务器返回等待响应状态
                        User user_exit = new User("\\back", "\\back");
                        //发送给服务器，通知他返回到等待状态
                        oos.writeObject(user_exit);
                        oos.flush();
                        //客户端返回到初始化界面
                        return;
                    }

                    //将该User发送到客户端进行验证
                    oos.writeObject(user);
                    oos.flush();

                    //接收验证消息 -如果登录成功（调用功能选择界面）-如果登录失败（提示消息，并重新输入）
                    Message message_receiveLoginResultFromServer = (Message)ois.readObject();

                    /*
                     * 普通用户登录成功
                     */
                    if(message_receiveLoginResultFromServer.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED_NORMAL_USER)){
                        //普通登录成功 -> 进行普通用户使用处理
                        //调用ClientUseHandler,处理用户下一步的选择
                        System.out.println("登录成功");
                        ClientNormalUserHandler.handleClientNormalUser(user.getUserName(),oos,ois);
                        //如果用户从功能界面返回，则直接退回到登录界面，继续提示输密码。。。。

                    }else if(message_receiveLoginResultFromServer.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED_MANAGER)){
                        /*
                            管理人员登录成功
                         */
                        //管理人员登录成功 -》进行管理人员使用处理
                        ClientManagerUserHandler.handleManagerUser();
                        //如果用户从功能界面返回，则直接退回到登录界面，继续提示输密码。。。。

                    }else if(message_receiveLoginResultFromServer.getMesType().equals(MessageType.MESSAGE_LOGIN_FAILED)){
                        //登录失败
                        System.out.println("账号或密码有误！请重新输入：");
                        //开始循环，重新开始向服务器发送登录请求
                        //loop......
                    }

                }else{
                    //服务器拒绝登录 ---- 功能待做
                    System.out.println("服务器拒绝你的登录！");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
