package com.demo.architect.data.repository.base.other.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.FloorEntity;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OtherApiInterface {


    @FormUrlEncoded
    @POST
    Call<BaseListResponse<FloorEntity>> getListFloor(@Url String url, @Field("pOrderID") long orderId);


}
