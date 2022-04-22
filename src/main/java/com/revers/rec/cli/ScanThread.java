package com.revers.rec.cli;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * 监听输入
 *
 * @author Revers.
 * @date 2022/04/22 23:09
 **/

@Slf4j
public class ScanThread implements Runnable{

    public ScanThread() {

    }

    @Override
    public void run() {

        while (true) {

            String[] choice = new Scanner(System.in).nextLine().split(" ");
            if("/l".equals(choice[0])){
                log.info("/l");
                continue;
            }

            if("/t".equals(choice[0])){
                log.info("/t");
                continue;
            }

            if("/r".equals(choice[0])){
                log.info("/r");
                continue;
            }

            if("/f".equals(choice[0])){
                log.info("/f");
                continue;
            }

            if("/s".equals(choice[0])){
                log.info("/s");
                continue;
            }

            if("/h".equals(choice[0])){
                h();
                continue;
            }

        }
    }

    private void l(){}

    private void t(){}

    private void r(){}

    private void f(){}

    private void s(){}

    private void h(){
        Menu.printMenu();
    }
}
