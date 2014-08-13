package util;/**
 * Created by dell on 2014/8/7.
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author dengqg
 */
public class WeChatConfUtil {
    public static final String FILE = "conf.properties";
    public static final String WECHAT_TOKEN = "wechat.token";
    public static final String WECHAT_APPID = "wechat.appid";
    public static final String WECHAT_SECRET = "wechat.appsecret";
    public static final String WECHAT_SUBSCRIBE_WORDS = "wechat.subscribe_words";

    public static final String WECHAT_BUTTON1_NAME = "wechat.button1_name";
    public static final String WECHAT_SUBBUTTON1_NAME = "wechat.sub_button1_name";
    public static final String WECHAT_SUBBUTTON1_URL = "wechat.sub_button1_url";
    public static final String WECHAT_SUBBUTTON2_NAME = "wechat.sub_button2_name";
    public static final String WECHAT_SUBBUTTON2_URL = "wechat.sub_button2_url";

    public static final String WECHAT_BUTTON2_NAME = "wechat.button2_name";
    public static final String WECHAT_BUTTON2_URL = "wechat.button2_url";
    public static final String WECHAT_BUUTON2_KEY = "wechat.button2_key";

    public static final String WECHAT_ARTICLE1_TITLE = "wechat.article1_title";
    public static final String WECHAT_ARTICLE1_DESCRIPTION = "wechat.article1_description";
    public static final String WECHAT_ARTICLE1_PIC_URL = "wechat.article1_pic_url";
    public static final String WECHAT_ARTICLE1_URL = "wechat.article1_url";
    public static final String WECHAT_ARTICLE2_TITLE = "wechat.article2_title";
    public static final String WECHAT_ARTICLE2_DESCRIPTION = "wechat.article2_description";
    public static final String WECHAT_ARTICLE2_PIC_URL = "wechat.article2_pic_url";
    public static final String WECHAT_ARTICLE2_URL = "wechat.article2_url";

    public static String getAppId() {
        return getValue(WECHAT_APPID);
    }

    public static String getAppSecret() {
        return getValue(WECHAT_SECRET);
    }

    public static String getWechatToken() {
        return getValue(WECHAT_TOKEN);
    }

    public static String getWechatButton1Name() {
        return getValue(WECHAT_BUTTON1_NAME);
    }

    public static String getWechatSubbutton1Name() {
        return getValue(WECHAT_SUBBUTTON1_NAME);
    }

    public static String getWechatSubbutton1URL() {
        return getValue(WECHAT_SUBBUTTON1_URL);
    }

    public static String getWechatSubbutton2Name() {
        return getValue(WECHAT_SUBBUTTON2_NAME);
    }

    public static String getWechatSubbutton2URL() {
        return getValue(WECHAT_SUBBUTTON2_URL);
    }

    public static String getWechatButton2Name() {
        return getValue(WECHAT_BUTTON2_NAME);
    }

    public static String getWechatButton2URL() {
        return getValue(WECHAT_BUTTON2_URL);
    }

    public static String getWechatSubscribeWords() {
        return getValue(WECHAT_SUBSCRIBE_WORDS);
    }

    public static String getWechatBuuton2Key() {
        return getValue(WECHAT_BUUTON2_KEY);
    }

    public static String getWechatArticle1Title() {
        return getValue(WECHAT_ARTICLE1_TITLE);
    }

    public static String getWechatArticle1Description() {
        return getValue(WECHAT_ARTICLE1_DESCRIPTION);
    }

    public static String getWechatArticle1PicUrl() {
        return getValue(WECHAT_ARTICLE1_PIC_URL);
    }

    public static String getWechatArticle1Url() {
        return getValue(WECHAT_ARTICLE1_URL);
    }

    public static String getWechatArticle2Title() {
        return getValue(WECHAT_ARTICLE2_TITLE);
    }

    public static String getWechatArticle2Description() {
        return getValue(WECHAT_ARTICLE2_DESCRIPTION);
    }

    public static String getWechatArticle2PicUrl() {
        return getValue(WECHAT_ARTICLE2_PIC_URL);
    }

    public static String getWechatArticle2Url() {
        return getValue(WECHAT_ARTICLE2_URL);
    }

    public static String getValue(String key) {
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(FILE);
            Properties p = new Properties();
            p.load(in);
            return p.getProperty(key);
        } catch (IOException ex) {
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {

                }
            }
        }
    }
}
