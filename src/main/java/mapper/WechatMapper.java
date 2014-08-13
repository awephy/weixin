
package mapper;
/**
 * Created by dell on 2014/8/11.
 */


import model.AccessToken;

/**
 * @author dengqg
 */

public interface WechatMapper {

    void updateAccessToken(AccessToken accessToken);

    AccessToken getAccessToken();
}

