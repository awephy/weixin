package service.impl;

import common.BaseLogger;
import mapper.WechatMapper;
import model.AccessToken;
import model.Button;
import model.WechatMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.WechatService;
import util.WeChatConfUtil;
import util.WechatAPIUtil;

import javax.annotation.PostConstruct;


/**
 * @author dengqg
 */

@Service
public class WechatServiceImpl implements BaseLogger, WechatService {

    private static final Logger logger = LogManager.getLogger(WechatServiceImpl.class);
    @Autowired
    WechatMapper mapper;

    @PostConstruct
    public final void initWechatMenu() {
        AccessToken accessToken = getAccessToken();
        WechatMenu menu = new WechatMenu();
        Button buttons[] = new Button[2];
        buttons[0] = new Button();
        buttons[0].setName(WeChatConfUtil.getWechatButton1Name());
        buttons[0].setUrl(WeChatConfUtil.getWechatButton2URL());
        buttons[0].setType(Button.ButtonType.view.name());
        Button subButton1[] = new Button[2];
        subButton1[0] = new Button();
        subButton1[0].setName(WeChatConfUtil.getWechatSubbutton1Name());
        subButton1[0].setUrl(WeChatConfUtil.getWechatSubbutton1URL());
        subButton1[0].setType(Button.ButtonType.view.name());
        subButton1[1] = new Button();
        subButton1[1].setName(WeChatConfUtil.getWechatSubbutton2Name());
        subButton1[1].setUrl(WeChatConfUtil.getWechatSubbutton2URL());
        subButton1[1].setType(Button.ButtonType.view.name());
        buttons[0].setSub_button(subButton1);
        buttons[1] = new Button();
        buttons[1].setName(WeChatConfUtil.getWechatButton2Name());
        buttons[1].setType(Button.ButtonType.click.name());
        buttons[1].setKey(WeChatConfUtil.getWechatBuuton2Key());
        menu.setButton(buttons);
        int errcode = WechatAPIUtil.createMenu(menu, accessToken.getToken());
        if (errcode == 42001) {
            accessToken = getNewAccessToken();
            WechatAPIUtil.createMenu(menu, accessToken.getToken());
        }
    }

    @Override
    public AccessToken getAccessToken() {
        return mapper.getAccessToken();
    }

    @Override
    public AccessToken getNewAccessToken() {
        AccessToken accessToken = WechatAPIUtil.getAccessToken(WeChatConfUtil.getAppId(), WeChatConfUtil.getAppSecret());
        mapper.updateAccessToken(accessToken);
        return accessToken;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
