package com.revers.rec.domain.json;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class JsonGroup implements Serializable {
    private String groupname;
    private String id;
    private String online;
    private List<JsonUser> list = new ArrayList<>();

    public JsonGroup(String groupname, String id, String online, List<JsonUser> list) {
        this.groupname = groupname;
        this.id = id;
        this.online = online;
        this.list = list;
    }

    @Override
    public String toString() {
        return "JsonGroup [groupname=" + groupname + ", id=" + id + ", online=" + online + ", list=" + list + "]";
    }
}
