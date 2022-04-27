package com.revers.rec.websocket.domain;

/**
 * @description 服务器端WebSocket领域对象
 */
public class Domain {

    /**
     * @description 添加群信息
     */
    class Group {
        Integer groupId;
        String remark;
    }

    /**
     * @description 同意添加好友
     */
    class AgreeAddGroup {
        Integer toUid;
        Integer groupId;
        Integer messageBoxId;
        @Override
        public String toString() {
            return "toUid=" + toUid +
                    ", groupId=" + groupId +
                    ", messageBoxId=" + messageBoxId ;
        }
    }
}
