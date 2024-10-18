package com.lzx.BBChat.ClientService.Menu;

public class Menu {
    public static String appName = "====== BBChat -version 1.0 ======";
    public static String blankSpace = "\t\t\t";

    //初始化界面
    public static void showInitMenu() {
        System.out.println(appName);
        System.out.println("请选择：");
        System.out.println(blankSpace + "1.登录");
        System.out.println(blankSpace + "2.注册");
        System.out.println(blankSpace + "3.关闭");
    }

    //登陆界面
    public static void showLoginMenu() {
        System.out.println(appName);
        System.out.println(blankSpace + "登录");
        System.out.println("输入\\back可返回上一级菜单 ： ");
    }

    //注册界面
    public static void showRegistrationMenu() {
        System.out.println(appName);
        System.out.println(blankSpace + "注册");
        System.out.println("输入\\back可返回上一级菜单 ： ");
    }

    //普通用户功能界面
    public static void showNormalFunctionMenu(String username) {
        System.out.println(appName);
        System.out.println("尊敬的" + username + "欢迎您的使用! -.-");
        System.out.println("请选择您想使用的功能 ： ");
        System.out.println(blankSpace + "1.私聊");
        System.out.println(blankSpace + "2.群聊");
        System.out.println(blankSpace + "3.查看在线人员");
        System.out.println("输入\\back可返回上一级菜单 ： ");

    }

    //管理人员功能界面
    public static void showManagerFunctionMenu(String username) {
        System.out.println(appName);
        System.out.println("尊敬的" + username + "欢迎您的使用! -.-");
        System.out.println("请选择您想使用的功能 ： ");
        System.out.println(blankSpace + "1.私聊");
        System.out.println(blankSpace + "2.群聊");
        System.out.println(blankSpace + "3.查看在线人员");
        System.out.println("输入\\back可返回上一级菜单 ： ");

    }

    public static void showPrivateChatMenu(String userName) {
        System.out.println(appName);
        System.out.println(blankSpace + "尊敬的用户 " + userName + " 欢迎私聊");
        System.out.println("输入\\back可返回上一级菜单 ： ");
    }

    public static void showGroupMenu() {
        System.out.println(appName);
        System.out.println(blankSpace + "群聊");
        System.out.println("输入\\back可返回上一级菜单 ： ");
    }

    public static void showOnlineMenu() {
        System.out.println(appName);
        System.out.println("以下是在线人员： ");
        System.out.println("输入 \\flush,可刷新");
        System.out.println("输入\\back可返回上一级菜单 ： ");
    }

}
