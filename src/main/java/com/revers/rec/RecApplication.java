package com.revers.rec;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.service.UserService;
import com.revers.rec.util.Network;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Scanner;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
@Slf4j
public class RecApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountConfig accountConfig;

    public static void main(String[] args) {
        SpringApplication.run(RecApplication.class, args);

        System.out.println("client");

    }

    //预先加载用户(登录或注册)
    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("系统启动");

            String localIPv6Address = null;
            try {
                localIPv6Address = Network.getLocalIPv6Address();
            } catch (Exception e) {
                log.info("获取本地ip地址异常");
                return;
            }
            if (localIPv6Address != null) {
                log.info("获取本地ipv6地址: " + localIPv6Address);
            }
            while (accountConfig.getPublicKey() == null) {
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
                        AccountConfig.setIpv6(localIPv6Address);
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
