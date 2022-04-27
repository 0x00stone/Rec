package com.revers.rec.cli;

import com.revers.rec.service.message.MessageService;
import com.revers.rec.util.BeanContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Scanner;

/**
 * 监听输入
 *
 * @author Revers.
 * @date 2022/04/22 23:09
 **/

@Slf4j
public class ScanThread implements Runnable {

    @Autowired
    MessageService messageService;

    public ScanThread(){
        if(messageService == null){
            messageService = BeanContextUtil.getBean(MessageService.class);
        }
    }

    @Override
    public void run() {
        ScanThreadMethod scanThreadMethod = new ScanThreadMethod();
        while (true) {
            Menu.printTips();
            String[] choice = new Scanner(System.in).nextLine().split(" ");
            if ("/m".equals(choice[0])) {
                log.info("/m");
                if (choice.length != 3) {
                    log.info("参数错误");
                } else {
                    scanThreadMethod.m(choice);
                }
            }
            if ("/r".equals(choice[0])) {
                scanThreadMethod.r();
            }

            if ("/u".equals(choice[0])) {
                scanThreadMethod.u();
            }

            if ("/l".equals(choice[0])) {
                if (choice.length == 3) {
                    scanThreadMethod.l(choice[1], choice[2]);
                } else {
                    log.info("参数错误");
                }
            }

            if ("/b".equals(choice[0])) {
                scanThreadMethod.b();
            }

            if ("/t".equals(choice[0])) {
                scanThreadMethod.t();
            }

            if ("/ff".equals(choice[0])) {
                if (choice.length == 2) {
                    scanThreadMethod.ff(choice[1]);
                } else {
                    log.info("参数错误");
                }
            }
            if ("/fa".equals(choice[0])) {
                if (choice.length == 2) {
                    scanThreadMethod.fa(choice[1]);
                } else {
                    log.info("参数错误");
                }
            }

            if ("/h".equals(choice[0])) {
                scanThreadMethod.h();
            }

            if ("/rn".equals(choice[0])) {
                scanThreadMethod.rn();
            }

            if ("/sf".equals(choice[0])) {
                scanThreadMethod.sf(choice[1]);
            }

            if ("/sa".equals(choice[0])) {
                scanThreadMethod.sa(choice[1]);
            }
        }
    }
}
