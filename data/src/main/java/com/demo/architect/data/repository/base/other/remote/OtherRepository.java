package com.demo.architect.data.repository.base.other.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.FloorEntity;

import java.io.File;

import io.reactivex.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OtherRepository {

    Observable<BaseListResponse<FloorEntity>> getListFloor(long orderId);

}
