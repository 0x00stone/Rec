package com.revers.rec.util;
public class Result {
    private boolean flag ;
    private String msg = "success";
    private Object data;

    public Result(boolean flag, String msg, Object data) {
        this.flag = flag;
        this.msg = msg;
        this.data = data;
    }

    public Result(boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }

    public Result(Object data) {
        this.data = data;
    }

    public Result(){};

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
