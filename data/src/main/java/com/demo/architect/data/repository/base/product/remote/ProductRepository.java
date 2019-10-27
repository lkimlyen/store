package com.demo.architect.data.repository.base.product.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.CodePalletEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductListEntity;

import io.reactivex.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface ProductRepository {


    Observable<BaseListResponse<ProductListEntity>> getProductForPallet(long orderId,
                                                                             int turn);

    Observable<BaseListResponse<CodePalletEntity>> getCodePallet(long orderId,
                                                                       int turn, int floorId);

}
