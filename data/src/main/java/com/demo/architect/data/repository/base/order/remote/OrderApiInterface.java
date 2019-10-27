package com.demo.architect.data.repository.base.order.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.SOEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OrderApiInterface {
    @FormUrlEncoded
    @POST
    Call<BaseListResponse<SOEntity>> getListSO(@Url String url, @Field("pKey") String key, @Field("pOrderType") int orderType);
    @FormUrlEncoded
    @POST
    Call<BaseResponse<String>> addPallet(@Url String url, @Field("pUserID") long userId, @Field("pPostData") String json);

    @FormUrlEncoded
    @POST
    Call<BaseResponse> addScanRequestByPallet(@Url String url, @Field("pUserID") long userId, @Field("pCode") String code);
}
