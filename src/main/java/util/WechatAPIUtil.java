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
package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import common.BaseLogger;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 公众平台通用接口工具类
 *
 * @author zhanghj
 */
public class WechatAPIUtil implements BaseLogger {

    private static final Logger LOGGER = LogManager.getLogger(WechatAPIUtil.class);

    // 获取access_token的接口地址（GET） 限200（次/天）
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    /**
     * 获取access_token
     *
     * @param appid     凭证
     * @param appsecret 密钥
     * @return
     */
    public static AccessToken getAccessToken(String appid, String appsecret) {
        AccessToken accessToken = null;
        if (appid == null || appsecret == null) {
            LOGGER.error("不正确的appid和appsecret");
            return accessToken;
        }
        String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            if (jsonObject.toJSONString().indexOf("errcode") >= 0) {
                return null;
            }
            try {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInteger("expires_in"));
            } catch (Exception e) {
                accessToken = null;
                // 获取token失败
                LOGGER.error(e.getMessage(), e);
            }
        }
        return accessToken;
    }

    // 菜单创建（POST） 限100（次/天）
    public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /**
     * 创建菜单
     *
     * @param menu        菜单实例
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static int createMenu(WechatMenu menu, String accessToken) {
        int result = 0;
        if (accessToken == null) {
            LOGGER.error("，创建菜单失败,不正确的accessToken");
            return 40001;
        }
        // 拼装创建菜单的url
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
        // 将菜单对象转换成json字符串
        String jsonMenu = JSONObject.toJSONString(menu);
        // 调用接口创建菜单
        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

        if (null != jsonObject) {
            if (0 != jsonObject.getInteger("errcode")) {
                result = jsonObject.getInteger("errcode");
            }
        }

        return result;
    }

    // 菜单查询（GET）
    public final static String menu_get_url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

    /**
     * 查询菜单
     *
     * @param accessToken 凭证
     * @return
     */
    public static String getMenu(String accessToken) {
        String result = null;
        if (accessToken == null) {
            LOGGER.error("获得菜单失败,没有正确的accessToken");
            return result;
        }
        String requestUrl = menu_get_url.replace("ACCESS_TOKEN", accessToken);
        // 发起GET请求查询菜单
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            result = jsonObject.toString();
        }
        return result;
    }

    // 菜单删除（GET）
    public final static String menu_delete_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

    /**
     * 删除菜单
     *
     * @param accessToken 凭证
     * @return true成功 false失败
     */
    public static boolean deleteMenu(String accessToken) {
        boolean result = false;
        if (accessToken == null) {
            LOGGER.error("删除菜单失败,没有正确的accessToken");
            return result;
        }
        String requestUrl = menu_delete_url.replace("ACCESS_TOKEN", accessToken);
        // 发起GET请求删除菜单
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);

        if (null != jsonObject) {
            int errorCode = jsonObject.getInteger("errcode");
            String errorMsg = jsonObject.getString("errmsg");
            if (0 == errorCode) {
                result = true;
            } else {
                result = false;
                LOGGER.error("删除菜单失败");
            }
        }
        return result;
    }

    //发送客服消息
    public static String message_custom_send_url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

    public static int sendCustom(String accessToken, String jsonMsg) {
        int result = 0;
        if (accessToken == null) {
            LOGGER.error("发送客服信息失败,没有正确的accessToken");
            return 40001;
        }
        String url = message_custom_send_url.replace("ACCESS_TOKEN", accessToken);

        JSONObject jsonObject = httpRequest(url, "POST", jsonMsg);
        if (null != jsonObject) {
            if (0 != jsonObject.getInteger("errcode")) {
                result = jsonObject.getInteger("errcode");
                LOGGER.error("发送客服信息失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return result;
    }

    //上传图文消息素材
    public static String upload_news_url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=ACCESS_TOKEN";

    public static NewsUploadResult uploadNews(String accessToken, String jsonNew) {
        int result = 0;
        if (accessToken == null) {
            LOGGER.error("图文上传失败,没有正确的accessToken");
            return null;
        }
        String url = upload_news_url.replace("ACCESS_TOKEN", accessToken);

        JSONObject jsonObject = httpRequest(url, "POST", jsonNew);
        if ((jsonObject.toJSONString()).indexOf("errcode") >= 0) {
            return null;
        }
        NewsUploadResult newsUploadResult = new NewsUploadResult();
        newsUploadResult.setCreated_at(jsonObject.getString("created_at"));
        newsUploadResult.setMedia_id(jsonObject.getString("media_id"));
        newsUploadResult.setType(jsonObject.getString("type"));

        return newsUploadResult;
    }

    //分组群发
    public static String mass_send_by_groupid_url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";

    public static int massSendByGroupId(String accessToken, String jsonMsg) {
        int result = 0;
        if (accessToken == null) {
            LOGGER.error("分组群发失败,没有正确的accessToken");
            return 40001;
        }
        String url = mass_send_by_groupid_url.replace("ACCESS_TOKEN", accessToken);

        JSONObject jsonObject = httpRequest(url, "POST", jsonMsg);
        if (null != jsonObject) {
            if (0 != jsonObject.getInteger("errcode")) {
                result = jsonObject.getInteger("errcode");
                LOGGER.error("分组群发失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return result;
    }

    //群发
    public static String mass_send_by_openid_url = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";

    public static int massSendByOpenId(String accessToken, String jsonMsg) {
        int result = 0;
        if (accessToken == null) {
            LOGGER.error("群发失败,没有正确的accessToken");
            return 40001;
        }
        String url = mass_send_by_openid_url.replace("ACCESS_TOKEN", accessToken);

        JSONObject jsonObject = httpRequest(url, "POST", jsonMsg);
        if (null != jsonObject) {
            if (0 != jsonObject.getInteger("errcode")) {
                result = jsonObject.getInteger("errcode");
                LOGGER.error("群发失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return result;
    }

    /**
     * 获取用户信息
     *
     * @param accessToken 接口访问凭证
     * @param openId      用户标识
     * @return WeixinUserInfo
     */
    public static WechatUserInfo getUserInfo(String accessToken, String openId) {
        WechatUserInfo wechatUserInfo = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        // 获取用户信息
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);

        if (null != jsonObject) {
            try {
                wechatUserInfo = new WechatUserInfo();
                // 用户的标识
                wechatUserInfo.setOpenId(jsonObject.getString("openid"));
                // 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
                wechatUserInfo.setSubscribe(jsonObject.getInteger("subscribe"));
                // 用户关注时间
                wechatUserInfo.setSubscribeTime(jsonObject.getString("subscribe_time"));
                // 昵称
                wechatUserInfo.setNickname(jsonObject.getString("nickname"));
                // 用户的性别（1是男性，2是女性，0是未知）
                wechatUserInfo.setSex(jsonObject.getInteger("sex"));
                // 用户所在国家
                wechatUserInfo.setCountry(jsonObject.getString("country"));
                // 用户所在省份
                wechatUserInfo.setProvince(jsonObject.getString("province"));
                // 用户所在城市
                wechatUserInfo.setCity(jsonObject.getString("city"));
                // 用户的语言，简体中文为zh_CN
                wechatUserInfo.setLanguage(jsonObject.getString("language"));
                // 用户头像
                wechatUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
            } catch (Exception e) {
                if (0 == wechatUserInfo.getSubscribe()) {
                    LOGGER.error("用户{}已取消关注", wechatUserInfo.getOpenId());
                } else {
                    int errorCode = jsonObject.getInteger("errcode");
                    String errorMsg = jsonObject.getString("errmsg");
                    LOGGER.error("获取用户信息失败 errcode:{} errmsg:{}", errorCode, errorMsg);
                }
            }
        }
        return wechatUserInfo;
    }

    /**
     * 获取关注者列表
     *
     * @param accessToken 调用接口凭证
     * @param nextOpenId  第一个拉取的openId，不填默认从头开始拉取
     * @return WeixinUserList
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    public static WechatUserList getUserList(String accessToken, String nextOpenId) {
        WechatUserList wechatUserList = null;

        if (null == nextOpenId) {
            nextOpenId = "";
        }

        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("NEXT_OPENID", nextOpenId);
        // 获取关注者列表
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                wechatUserList = new WechatUserList();
                wechatUserList.setTotal(jsonObject.getInteger("total"));
                wechatUserList.setCount(jsonObject.getInteger("count"));
                wechatUserList.setNextOpenId(jsonObject.getString("next_openid"));
                JSONObject dataObject = (JSONObject) jsonObject.get("data");
                if (0 == wechatUserList.getCount()) {
                    return null;
                }
                wechatUserList.setOpenIdList(JSONArray.toJavaObject(dataObject.getJSONArray("openid"), List.class));
            } catch (JSONException e) {
                wechatUserList = null;
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                LOGGER.error("获取关注者列表失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return wechatUserList;
    }

    /**
     * 查询分组
     *
     * @param accessToken 调用接口凭证
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    public static List<WechatGroup> getGroups(String accessToken) {
        List<WechatGroup> wechatGroupList = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        // 查询分组
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);

        if (null != jsonObject) {
            try {
                wechatGroupList = JSON.parseArray(jsonObject.getString("groups"), WechatGroup.class);
            } catch (JSONException e) {
                wechatGroupList = null;
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                LOGGER.error("查询分组失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return wechatGroupList;
    }

    /**
     * 查询用户所在分组
     *
     * @param accessToken 接口访问凭证
     * @param OpenId      用户id
     * @return
     */
    public static Integer queryUserGroupId(String accessToken, String OpenId) {
        Integer groupId = 0;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        // 需要提交的json数据
        String jsonData = "{\"openid\":\"%s\"}";
        JSONObject jsonObject = httpRequest(requestUrl, "POST", String.format(jsonData, OpenId));
        if (null != jsonObject) {
            try {
                groupId = jsonObject.getInteger("groupid");
            } catch (JSONException e) {
                groupId = null;
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                LOGGER.error("查询用户所在分组失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return groupId;
    }

    /**
     * 创建分组
     *
     * @param accessToken 接口访问凭证
     * @param groupName   分组名称
     * @return
     */
    public static WechatGroup createGroup(String accessToken, String groupName) {
        WechatGroup weixinGroup = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        // 需要提交的json数据
        String jsonData = "{\"group\" : {\"name\" : \"%s\"}}";
        // 创建分组
        JSONObject jsonObject = httpRequest(requestUrl, "POST", String.format(jsonData, groupName));

        if (null != jsonObject) {
            try {
                weixinGroup = new WechatGroup();
                weixinGroup.setId(jsonObject.getJSONObject("group").getInteger("id"));
                weixinGroup.setName(jsonObject.getJSONObject("group").getString("name"));
            } catch (JSONException e) {
                weixinGroup = null;
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                LOGGER.error("创建分组失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return weixinGroup;
    }

    /**
     * 修改分组名
     *
     * @param accessToken 接口访问凭证
     * @param groupId     分组id
     * @param groupName   修改后的分组名
     * @return true | false
     */
    public static boolean updateGroup(String accessToken, int groupId, String groupName) {
        boolean result = false;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        // 需要提交的json数据
        String jsonData = "{\"group\": {\"id\": %d, \"name\": \"%s\"}}";
        // 修改分组名
        JSONObject jsonObject = httpRequest(requestUrl, "POST", String.format(jsonData, groupId, groupName));

        if (null != jsonObject) {
            int errorCode = jsonObject.getInteger("errcode");
            String errorMsg = jsonObject.getString("errmsg");
            if (0 == errorCode) {
                result = true;
                LOGGER.info("修改分组名成功 errcode:{} errmsg:{}", errorCode, errorMsg);
            } else {
                LOGGER.error("修改分组名失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return result;
    }

    /**
     * 移动用户分组
     *
     * @param accessToken 接口访问凭证
     * @param openId      用户标识
     * @param groupId     分组id
     * @return true | false
     */
    public static boolean updateMemberGroup(String accessToken, String openId, int groupId) {
        boolean result = false;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        // 需要提交的json数据
        String jsonData = "{\"openid\":\"%s\",\"to_groupid\":%d}";
        // 移动用户分组
        JSONObject jsonObject = httpRequest(requestUrl, "POST", String.format(jsonData, openId, groupId));

        if (null != jsonObject) {
            int errorCode = jsonObject.getInteger("errcode");
            String errorMsg = jsonObject.getString("errmsg");
            if (0 == errorCode) {
                result = true;
                LOGGER.info("移动用户分组成功 errcode:{} errmsg:{}", errorCode, errorMsg);
            } else {
                LOGGER.error("移动用户分组失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return result;
    }

    /**
     * 上传媒体文件
     *
     * @param accessToken  接口访问凭证
     * @param type         媒体文件类型（image、voice、video和thumb）
     * @param mediaFileUrl 媒体文件的url
     * @return
     */
    public static WechatMedia uploadMedia(String accessToken, String type, String mediaFileUrl) {
        WechatMedia wechatMedia = null;
        // 拼装请求地址
        String uploadMediaUrl = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
        uploadMediaUrl = uploadMediaUrl.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);

        // 定义数据分隔符
        String boundary = "------------7da2e536604c8";
        try {
            URL uploadUrl = new URL(uploadMediaUrl);
            HttpURLConnection uploadConn = (HttpURLConnection) uploadUrl.openConnection();
            uploadConn.setDoOutput(true);
            uploadConn.setDoInput(true);
            uploadConn.setRequestMethod("POST");
            // 设置请求头Content-Type
            uploadConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            // 获取媒体文件上传的输出流（往微信服务器写数据）
            OutputStream outputStream = uploadConn.getOutputStream();

            URL mediaUrl = new URL(mediaFileUrl);
            HttpURLConnection meidaConn = (HttpURLConnection) mediaUrl.openConnection();
            meidaConn.setDoOutput(true);
            meidaConn.setRequestMethod("GET");

            // 从请求头中获取内容类型
            String contentType = meidaConn.getHeaderField("Content-Type");
            // 根据内容类型判断文件扩展名
            String fileExt = getFileExt(contentType);
            // 请求体开始
            outputStream.write(("--" + boundary + "\r\n").getBytes());
            outputStream.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"file1%s\"\r\n", fileExt).getBytes());
            outputStream.write(String.format("Content-Type: %s\r\n\r\n", contentType).getBytes());

            // 获取媒体文件的输入流（读取文件）
            BufferedInputStream bis = new BufferedInputStream(meidaConn.getInputStream());
            byte[] buf = new byte[8096];
            int size = 0;
            while ((size = bis.read(buf)) != -1) {
                // 将媒体文件写到输出流（往微信服务器写数据）
                outputStream.write(buf, 0, size);
            }
            // 请求体结束
            outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
            outputStream.close();
            bis.close();
            meidaConn.disconnect();

            // 获取媒体文件上传的输入流（从微信服务器读数据）
            InputStream inputStream = uploadConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer buffer = new StringBuffer();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            uploadConn.disconnect();

            // 使用JSON-lib解析返回结果
            JSONObject jsonObject = JSONObject.parseObject(buffer.toString());
            wechatMedia = new WechatMedia();
            wechatMedia.setType(jsonObject.getString("type"));
            // type等于thumb时的返回结果和其它类型不一样
            if ("thumb".equals(type)) {
                wechatMedia.setMediaId(jsonObject.getString("thumb_media_id"));
            } else {
                wechatMedia.setMediaId(jsonObject.getString("media_id"));
            }
            wechatMedia.setCreatedAt(jsonObject.getInteger("created_at"));
        } catch (Exception e) {
            wechatMedia = null;
            LOGGER.error("上传媒体文件失败：{}", e);
        }
        return wechatMedia;
    }

    /**
     * 下载媒体文件
     *
     * @param accessToken 接口访问凭证
     * @param mediaId     媒体文件标识
     * @param savePath    文件在服务器上的存储路径
     * @return
     */
    public static String getMedia(String accessToken, String mediaId, String savePath) {
        String filePath = null;
        // 拼接请求地址
        String requestUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
        System.out.println(requestUrl);
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");

            if (!savePath.endsWith("/")) {
                savePath += "/";
            }
            // 根据内容类型获取扩展名
            String fileExt = getFileExt(conn.getHeaderField("Content-Type"));
            // 将mediaId作为文件名
            filePath = savePath + mediaId + fileExt;

            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            byte[] buf = new byte[8096];
            int size = 0;
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.close();
            bis.close();

            conn.disconnect();
            LOGGER.info("下载媒体文件成功，filePath=" + filePath);
        } catch (Exception e) {
            filePath = null;
            LOGGER.error("下载媒体文件失败：{}", e);
        }
        return filePath;
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ex) {
            LOGGER.error("Weixin server connection timed out.", ex);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return jsonObject;
    }

    /**
     * URL编码（utf-8）
     *
     * @param source
     * @return
     */
    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据内容类型判断文件扩展名
     *
     * @param contentType 内容类型
     * @return
     */
    public static String getFileExt(String contentType) {
        String fileExt = "";
        if ("image/jpeg".equals(contentType)) {
            fileExt = ".jpg";
        } else if ("audio/mpeg".equals(contentType)) {
            fileExt = ".mp3";
        } else if ("audio/amr".equals(contentType)) {
            fileExt = ".amr";
        } else if ("video/mp4".equals(contentType)) {
            fileExt = ".mp4";
        } else if ("video/mpeg4".equals(contentType)) {
            fileExt = ".mp4";
        }
        return fileExt;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }
}
