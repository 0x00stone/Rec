package com.revers.rec.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.swing.text.html.ImageView;
import java.io.Serializable;

@Component
@Getter
@Setter
@ToString
public class Friend implements Serializable {

    private String id = "";
    private String myId = "";
    private String friendPublicKey = "";
    private String friendName = "";
    private Long createTime = 0L;
    private String groupId = "";
    private String portrait = ""; // 头像
    private String sign = "";
    private String friendId = "";
    private String status = "";

}
