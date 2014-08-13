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
package model;

/**
 *
 * @author zhanghj
 */
public class WechatUserInfo {

    // 用户的标识
    String openId;
    // 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
    Integer subscribe;
    // 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
    String subscribeTime;
    // 昵称
    String nickname;
    // 用户的性别（1是男性，2是女性，0是未知）
    Integer sex;
    // 用户所在国家
    String country;
    // 用户所在省份
    String province;
    // 用户所在城市
    String city;
    // 用户的语言，简体中文为zh_CN
    String language;
    // 用户头像
    String headImgUrl;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }

    public String getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(String subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    @Override
    public String toString() {
        return "WechatUserInfo{" + "openId=" + openId + ", subscribe=" + subscribe + ", subscribeTime=" + subscribeTime + ", nickname=" + nickname + ", sex=" + sex + ", country=" + country + ", province=" + province + ", city=" + city + ", language=" + language + ", headImgUrl=" + headImgUrl + '}';
    }

}
