package com.revers.rec.cli;

import com.revers.rec.config.AccountConfig;

/**
 * @author Revers.
 * @date 2022/04/22 23:08
 **/
public class Menu {
    public static void printMenu() {
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("+ -- --=[             Rec      v0.0.1                                         ]");
        System.out.println("+ -- --=[ 有关某个命令的详细信息，请键入 HELP 命令名                                  ]");
        System.out.println("+ -- --=[   /m 好友名称 好友公钥                关联好友                            ]");
        System.out.println("+ -- --=[   /u                               查询好友表                          ]");
        System.out.println("+ -- --=[   /h                               帮助菜单                            ]");
        System.out.println("+ -- --=[   /r                               查询最近消息                         ]");
        System.out.println("+ -- --=[   /rn                               查询未读消息                        ]");
        System.out.println("+ -- --=[   /l <节点ip> <节点端口>             连接服务器进入频道                     ]");
        System.out.println("+ -- --=[   /t                               查询当前路由表                        ]");
        System.out.println("+ -- --=[   /b                               查询当前Bucket表                     ]");
        System.out.println("+ -- --=[   /ff <好友名称>                     查询与好友通信信息                     ]");
        System.out.println("+ -- --=[   /fa <好友名称>                     查询与陌生人通信信息                   ]");
        System.out.println("+ -- --=[   /sf <好友名称>                      向好友发送信息                       ]");
        System.out.println("+ -- --=[   /sa <对方公钥>                      向陌生人发送信息                      ]");
        System.out.println("-------------------------------------------------------------------------------");
    }

    public static void printTips(){
        System.out.print(AccountConfig.getUsername() + "@:");
    }
}
