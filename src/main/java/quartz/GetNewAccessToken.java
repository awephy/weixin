package quartz;/**
 * Created by dell on 2014/8/13.
 */

import common.BaseLogger;
import mapper.WechatMapper;
import model.AccessToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import service.WechatService;
import util.WeChatConfUtil;
import util.WechatAPIUtil;


/**
 * @author dengqg
 */
public class GetNewAccessToken implements BaseLogger {
    private final Logger logger = LogManager.getLogger(GetNewAccessToken.class);

    @Autowired
    WechatService wechatService;

    @Autowired
    WechatMapper mapper;

    public void getNewAccessToken() {
        AccessToken accessToken = WechatAPIUtil.getAccessToken(WeChatConfUtil.getAppId(), WeChatConfUtil.getAppSecret());
        mapper.updateAccessToken(accessToken);
        getLogger().info("update successful");
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
