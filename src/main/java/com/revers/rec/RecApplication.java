package com.revers.rec;

import com.revers.rec.cli.Login;
import com.revers.rec.cli.Menu;
import com.revers.rec.cli.ScanThread;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.util.BeanContextUtil;
import com.revers.rec.util.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@PropertySource(encoding = "UTF-8", value = {"classpath:application.properties"})
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
@Slf4j
public class RecApplication implements CommandLineRunner {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(RecApplication.class, args);
        new BeanContextUtil().setApplicationContext(run);;

        /*Thread login = new Thread(new Login());
        login.start();*/
        Login login = new Login();
        login.run();

        Menu.printMenu();

        new Thread(new ScanThread()).start();
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
    }
}
