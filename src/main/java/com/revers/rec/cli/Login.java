package com.revers.rec.cli;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.service.user.UserServiceImpl;
import com.revers.rec.util.BeanContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.Scanner;

@Slf4j
@Controller
public class Login implements Runnable {

    UserServiceImpl userService;

    //预先加载用户(登录或注册)
    @Override
    public void run(){
        this.userService = BeanContext.getBean(UserServiceImpl.class);
        try {
            while (AccountConfig.getPublicKey() == null) {
                System.out.println("选择登录还是注册:");
                System.out.println("1.登录");
                System.out.println("2.注册");
                Scanner scanner = new Scanner(System.in);
                if (scanner.hasNext()) {
                    int choice = Integer.valueOf(scanner.nextLine());
                    if (choice == 1) {
                        System.out.print("请输入用户名: ");
                        while (!scanner.hasNextLine()) {
                        }
                        String username = scanner.nextLine();
                        System.out.print("请输入密码: ");
                        while (!scanner.hasNextLine()) {
                        }
                        String password = scanner.nextLine();

                        log.info(userService.login(username, password).getMsg());
                    }
                    if (choice == 2) {
                        System.out.print("请输入用户名: ");
                        while (!scanner.hasNextLine()) {
                        }
                        String username = scanner.nextLine();
                        System.out.print("请输入密码: ");
                        while (!scanner.hasNextLine()) {
                        }
                        String password = scanner.nextLine();
                        System.out.println("正在注册" + username + "中,请稍等...");

                        log.info(userService.register(username, password).getMsg());

                    }
                }
            }
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("===========================================================================================================================");
            System.out.println("欢迎用户 " + AccountConfig.getUsername() + " 登录");

        }catch (Exception e){
            e.printStackTrace();
            log.info("登录注册用户时发生异常,请阅读日志文件");
            return;
        }
    }

}
