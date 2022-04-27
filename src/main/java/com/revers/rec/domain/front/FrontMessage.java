package com.revers.rec.domain.front;

import com.revers.rec.domain.User;
import com.revers.rec.domain.json.JsonUser;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class FrontMessage {
    private String type;
    private HashMap<String,String> mine;
    private HashMap<String,String> to;
    @Override
    public String toString(){
        return "type = " + type + ", mine = " + mine.toString() + ",to = " + to.toString();
    }
}
