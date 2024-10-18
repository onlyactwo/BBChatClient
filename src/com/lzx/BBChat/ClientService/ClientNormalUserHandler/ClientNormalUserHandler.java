package com.lzx.BBChat.ClientService.ClientNormalUserHandler;


import com.lzx.BBChat.ClientService.Menu.Menu;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/**
 * 这个类是用于处理 普通用户 登录后想要使用的功能
 *主要功能：
 * 1.私聊
 * 2.群聊
 * 3.查看在线人员
 * 4.退出到登录界面
 *
 * 需要：
 * oos ois 与服务器进行通信
 * 需要该 User 的名字(私聊群聊菜单显示都会需要)
 */
public class ClientNormalUserHandler {
    public static void handleClientNormalUser(String userName,ObjectOutputStream oos , ObjectInputStream ois){
       //普通用户成功登录，显示普通用户功能菜单
        Menu.showNormalFunctionMenu(userName);
        //等待用户请求（选择功能）
        Scanner sc = new Scanner(System.in);
        String userOption;
        while (true){
            userOption = sc.nextLine();
            if(userOption.equals("1")){
                //用户使用私聊功能

            }else if(userOption.equals("2")){
                //用户使用群聊功能

            }else if(userOption.equals("3")){
                //用户使用查看在线人数功能

            }else if(userOption.equals("\\back")){
                //用户想要返回登录界面
                return;
            }else{
                //用户输入错误
                System.out.println("请检查输入！");
            }

        }
    }

}
