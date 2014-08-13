package service;

import model.AccessToken;

/*
 * @author dengqg
 */

public interface WechatService {
    AccessToken getAccessToken();

    AccessToken getNewAccessToken();
}
