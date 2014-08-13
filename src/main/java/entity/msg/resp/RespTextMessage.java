/**
 * *****************************************************
 * Copyright (C) Dayan techology Co.ltd - All Rights Reserved
 *
 * This file is part of Dayan techology Co.ltd property.
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * *****************************************************
 */
package entity.msg.resp;


import entity.msg.req.ReqTextMessage;
import util.MessageUtil;

/**
 * @author zhanghj
 */
public class RespTextMessage extends RespBaseMessage {

    // 回复的消息内容
    private String Content;


    public RespTextMessage() {
        this.setCreateTime(System.currentTimeMillis());
        this.setMsgType(MessageUtil.respType.text.toString());
    }

    public RespTextMessage(ReqTextMessage req, int funcFlag) {
        this();
        this.setFromUserName(req.getToUserName());
        this.setToUserName(req.getFromUserName());
        this.setFuncFlag(funcFlag);
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
