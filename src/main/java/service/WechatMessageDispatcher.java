package service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dell on 2014/8/7.
 */

public interface WechatMessageDispatcher {
    String processRequest(HttpServletRequest request);
}
