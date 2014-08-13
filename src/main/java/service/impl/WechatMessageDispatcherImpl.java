package service.impl;

import entity.msg.resp.RespNewsMessage;
import entity.msg.resp.RespTextMessage;
import model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.WechatMessageDispatcher;
import util.MessageUtil;
import util.WeChatConfUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author dengqg
 */
@Service
public class WechatMessageDispatcherImpl implements WechatMessageDispatcher {

    @Autowired
    WechatServiceImpl wechatService;

    @Override
    public String processRequest(HttpServletRequest request) {

        String respContent = null;
        String respMessage = null;
        try {
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");
            // 文本消息
            RespTextMessage respTextMessage = new RespTextMessage();
            respTextMessage.setToUserName(fromUserName);
            respTextMessage.setFromUserName(toUserName);
            respTextMessage.setCreateTime(new Date().getTime());
            respTextMessage.setMsgType(MessageUtil.respType.text.toString());
            respTextMessage.setFuncFlag(0);
            MessageUtil.reqType reqType = MessageUtil.reqType.valueOf(msgType);
            switch (reqType) {
                case text:
                    respContent = fromUserName;
                    break;
                case event:
                    String Event = requestMap.get("Event");
                    MessageUtil.eventType eventType = MessageUtil.eventType.valueOf(Event);
                    //首次关注
                    switch (eventType) {
                        case subscribe:
                            respTextMessage.setToUserName(fromUserName);
                            respTextMessage.setFromUserName(toUserName);
                            respContent = WeChatConfUtil.getWechatSubscribeWords();
                            break;
                        case unsubscribe:
                            break;
                        case CLICK:
                            String eventKey = requestMap.get("EventKey");
                            if (eventKey.equals(WeChatConfUtil.getWechatBuuton2Key())) {
                                RespNewsMessage newsMessage = new RespNewsMessage();
                                newsMessage.setToUserName(fromUserName);
                                newsMessage.setFromUserName(toUserName);
                                newsMessage.setCreateTime(new Date().getTime());
                                newsMessage.setMsgType(MessageUtil.respType.news.toString());

                                List<Article> articleList = new ArrayList<Article>();
                                Article article1 = new Article();
                                article1.setTitle(WeChatConfUtil.getWechatArticle1Title());
                                article1.setDescription(WeChatConfUtil.getWechatArticle1Description());
                                article1.setPicUrl(WeChatConfUtil.getWechatArticle1PicUrl());
                                article1.setUrl(WeChatConfUtil.getWechatArticle1Url());

                                Article article2 = new Article();
                                article2.setTitle(WeChatConfUtil.getWechatArticle2Title());
                                article2.setDescription(WeChatConfUtil.getWechatArticle2Description());
                                article2.setPicUrl(WeChatConfUtil.getWechatArticle2PicUrl());
                                article2.setUrl(WeChatConfUtil.getWechatArticle2Url());
                                articleList.add(article1);
                                articleList.add(article2);

                                newsMessage.setArticleCount(articleList.size());
                                newsMessage.setArticles(articleList);
                                respMessage = MessageUtil.messageToXml(newsMessage);
                                return respMessage;
                            }
                            break;
                    }
            }

            respTextMessage.setContent(respContent);
            respMessage = MessageUtil.messageToXml(respTextMessage);
            return respMessage;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}