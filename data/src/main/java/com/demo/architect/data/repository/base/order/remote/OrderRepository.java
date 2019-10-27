package com.demo.architect.data.repository.base.order.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.SOEntity;

import io.reactivex.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OrderRepository {
    Observable<BaseListResponse<SOEntity>> getListSO(String key, int orderType);

    Observable<BaseResponse<String>> addPallet(long userId, String json);

    Observable<BaseResponse> addScanRequestByPallet(long userId, String code);

}
