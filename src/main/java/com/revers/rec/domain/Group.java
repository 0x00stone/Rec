package com.revers.rec.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
public class Group {
    private String groupId = "";
    private String groupName = "";
}
