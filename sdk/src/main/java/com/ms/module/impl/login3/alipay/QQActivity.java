package com.ms.module.impl.login3.alipay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * QQ 登录
 */
public class QQActivity extends Activity {

    private Tencent tencent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tencent = Tencent.createInstance("", this.getApplicationContext());
        BaseUiListener uiListener = new BaseUiListener();
        tencent.checkLogin(uiListener);
        tencent.login(this, "all", uiListener);

    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {

            try {
                JSONObject jb = (JSONObject) o;

                String openID = jb.getString("openid");  //openid用户唯一标识
                String access_token = jb.getString("access_token");
                String expires = jb.getString("expires_in");

                tencent.setOpenId(openID);

                tencent.setAccessToken(access_token, expires);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }

    private void getUserInfo() {
        QQToken token = tencent.getQQToken();
        UserInfo userInfo = new UserInfo(this, token);
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object object) {
                JSONObject jb = (JSONObject) object;
                try {
                    // 昵称
                    String nickname = jb.getString("nickname");
                    //头像图片的url
                    String figureurl_qq_2 = jb.getString("figureurl_qq_2");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
            }

            @Override
            public void onCancel() {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        tencent.onActivityResult(requestCode, resultCode, data);
    }
}
