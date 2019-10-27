package com.demo.architect.data.repository.base.product.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.CodePalletEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductListEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Skull on 04/01/2018.
 */

public interface ProductApiInterface {

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<ProductListEntity>> getProductForPallet(@Url String url, @Field("pOrderID") long orderId,
                                                                       @Field("pTurn") int turn);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<CodePalletEntity>> getCodePallet(@Url String url, @Field("pOrderID") long orderId,
                                                                 @Field("pTurn") int turn, @Field("pFloorID") int floorId);

}
