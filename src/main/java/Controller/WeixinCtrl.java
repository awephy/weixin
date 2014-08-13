package Controller;

import common.BaseLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import service.impl.WechatMessageDispatcherImpl;
import util.SignUtil;
import util.WeChatConfUtil;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by dell on 2014/8/7.
 */
@Controller
@RequestMapping(value = "/WeixinCtrl")
public class WeixinCtrl implements BaseLogger {

    private static final Logger logger = LogManager.getLogger(WeixinCtrl.class);

    @Autowired
    WechatMessageDispatcherImpl messageDispatcher;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public
    @ResponseBody
    void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        long t = System.currentTimeMillis();
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        // 调用核心业务类接收消息、处理消息
        String respMessage = messageDispatcher.processRequest(request);
        PrintWriter out = response.getWriter();
        out.print(respMessage);
        out.close();
        getLogger().info("wechat total time:" + (System.currentTimeMillis() - t));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        PrintWriter out = null;
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            out = response.getWriter();
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");
            // 随机字符串
            String echostr = request.getParameter("echostr");
            //
//            String hash = request.getParameter("hash");
//            WechatModel wechatModel = wechatService.readByToUserName(hash);
            String tokenString = WeChatConfUtil.getWechatToken();

            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            if (SignUtil.checkSignature(tokenString, signature, timestamp, nonce)) {
                out.print(echostr);
            } else {
                out.print("");
            }
        } catch (Exception e) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html");
            out.print("<h1>这是一个微信接口，用于与微信公众平台通信。</h1>"
                    + "<p>请将此链接添加到微信公众平台开发模式服务器配置栏中。</p>");

        } finally {
            if (out != null) {
                out.close();
                out = null;
            }

        }

    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}

