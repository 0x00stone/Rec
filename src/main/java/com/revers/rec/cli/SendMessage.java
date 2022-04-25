package com.revers.rec.cli;

import com.revers.rec.domain.Data;
import com.revers.rec.net.Client.ClientOperation;
import com.revers.rec.service.message.MessageService;
import com.revers.rec.util.BeanContextUtil;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.ResultUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Scanner;

@Slf4j
public class SendMessage implements Runnable{
    @Autowired
    MessageService messageService;

    private String toPublicKey;

    public SendMessage(String toPublicKey) {
        this.toPublicKey = toPublicKey;
        messageService = BeanContextUtil.getBean(MessageService.class);
    }

    @SneakyThrows
    @Override
    public void run() {
        System.out.print("请输入消息内容：");
        String content = "";

        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNextLine()){}
        content = scanner.nextLine();

        ResultUtil communicate = null;
        try {
            communicate = ClientOperation.communicate(toPublicKey, content);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(communicate != null || !communicate.getFlag()){
                if(ConstantUtil.COMMUNICATE_SUCCESS.equals(((Data)communicate.getData()).getData())){
                    //存储消息
                    messageService.saveMessage(content,toPublicKey,true);

                    log.info("已接收");
                }else {
                    log.info("未接收");
                }
            }else {
                log.info("发送失败");
            }
        }
    }
}
