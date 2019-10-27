package com.demo.architect.data.repository.base.account.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Skull on 04/01/2018.
 */

public interface AuthApiInterface {

    @FormUrlEncoded
    @POST
    Call<UserResponse> login(@Url String url, @Field("pUserType") String key, @Field("pUserName") String username, @Field("pPassWord") String password);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse> changePassWord(@Url String url, @Field("pUserID") String userId, @Field("pOldPass") String oldPass,
                                          @Field("pNewPass") String newPass);


    @GET("http://sp2t9.imark.com.vn/WS/api/GetDate?pAppCode=ids")
    Call<String> getDateServer();

    @FormUrlEncoded
    @POST
    Call<BaseListResponse> updateSoft(@Url String url, @Field("pAppCode") String appCode, @Field("pUserID") long userId,
                                      @Field("pVersion") String version, @Field("pNumberRecodeNotUpdate") int numNotUpdate,
                                      @Field("pDateServer") String dateServer,
                                      @Field("pDeviceIme") String imeDevice);
}
