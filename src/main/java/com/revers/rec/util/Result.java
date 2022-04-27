package com.revers.rec.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
    private Integer flag ;
    private String msg = "";
    private Object data;

    public Result(Integer flag, String msg, Object data) {
        this.flag = flag;
        this.msg = msg;
        this.data = data;
    }

    public Result(Integer flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }

    public Result(Object data) {
        this.data = data;
    }

    public Result(){
        flag = ConstantUtil.SUCCESS;
        msg = ConstantUtil.SUCCESS_MESSAGE;
    };
}
