package com.revers.rec.util;
public class ResultUtil {
    private boolean flag ;
    private String msg = "success";
    private Object data;

    public ResultUtil(boolean flag, String msg, Object data) {
        this.flag = flag;
        this.msg = msg;
        this.data = data;
    }

    public ResultUtil(boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }

    public ResultUtil(Object data) {
        this.data = data;
    }

    public ResultUtil(){};

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
