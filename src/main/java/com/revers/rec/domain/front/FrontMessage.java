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
        String string = "type = " + type;
        if(mine != null){
            string += ", mine = " + mine.toString();
        }
        if(to != null){
            string += ", to = " + to.toString();
        }
        return string;
    }
}
