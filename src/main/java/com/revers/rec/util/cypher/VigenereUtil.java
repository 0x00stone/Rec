package com.revers.rec.util.cypher;

public class VigenereUtil {
    //声明并初始化一个String将字母与数字对应表内容存起来
    public static String word = "abcdefghijklmnopqrstuvwxyz1234567890";

    //Vigenere的加密算法
    public static String enVigenere(String str, String key) {
        String miwen = "";
        //统一将所有输入的转为小写
        int a = 0;
        int b = 0;
        try {
            key = key.toLowerCase();
            key = key.replaceAll("", "");//将字符串中的空格去掉
            str = str.toLowerCase();
            str = str.replaceAll("", "");

            for (int i = 0; i < str.length(); i++) {
                a = word.indexOf(str.charAt(i));//首先根据下标在key和str里找下标字符，再根据字符在word里找下标
                b = word.indexOf(key.charAt(i % (key.length())));//用字符下标对key的长度求余，并用charAt方法取出key中该下标的字符
                miwen = miwen + String.valueOf(word.charAt((a + b) % 36));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("vigenere error");
            System.out.println(a);
            System.out.println(b);
        }
        return miwen;
    }

    /*
     * 接收用户输入的密文进行解密
     * */
    public static String deVigenere(String str, String key) {
        //统一将所有输入的转为小写
        key = key.toLowerCase();
        key = key.replaceAll("", "");
        str = str.toLowerCase();
        str = str.replaceAll("", "");
        String mingwen = "";
        for (int i = 0; i < str.length(); i++) {
            int a = word.indexOf(str.charAt(i));
            int b = word.indexOf(key.charAt(i % (key.length())));
            if ((a - b) < 0) {
                mingwen = mingwen + String.valueOf(word.charAt((a - b + 36) % 36));
            } else {
                mingwen = mingwen + String.valueOf(word.charAt((a - b) % 36));
            }
        }
        return mingwen;
    }
}
