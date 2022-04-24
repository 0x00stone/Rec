package com.revers.rec;

import com.revers.rec.cli.Login;
import com.revers.rec.cli.Menu;
import com.revers.rec.cli.ScanThread;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.config.optionConfig;
import com.revers.rec.net.Server.Server;
import com.revers.rec.service.user.UserServiceImpl;
import com.revers.rec.util.BeanContextUtil;
import com.revers.rec.util.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@PropertySource(encoding = "UTF-8", value = {"classpath:application.properties"})
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
@Slf4j
public class RecApplication implements CommandLineRunner {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ConfigurableApplicationContext run = SpringApplication.run(RecApplication.class, args);
        new BeanContextUtil().setApplicationContext(run);;


        Login login = new Login();
        login.run();
//        测试阶段默认登录a1
//        UserServiceImpl userService;
//        userService = BeanContextUtil.getBean(UserServiceImpl.class);
//        log.info(userService.login("a1", "123456").getMsg());

        new Thread(new Server()).start();

        new Thread(new ScanThread()).start();

        Menu.printMenu();
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("系统启动");

        String localIPv6Address = null;
        try {
            localIPv6Address = NetworkUtil.getLocalIPv6Address();
        } catch (Exception e) {
            log.info("获取本地ip地址异常");
            return;
        }

        if (localIPv6Address != null) {
            log.info("获取本地ipv6地址: " + localIPv6Address);
            AccountConfig.setIpv6(localIPv6Address);
        }else {
            log.info("获取本地ipv6地址失败");
        }

        AccountConfig.setIpv6Port(optionConfig.getServerListenPort());
    }
}
