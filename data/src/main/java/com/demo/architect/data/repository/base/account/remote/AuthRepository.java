package com.demo.architect.data.repository.base.account.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.UserResponse;

import io.reactivex.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface AuthRepository {
    Observable<UserResponse> login(String type, String username, String password);
    Observable<BaseListResponse> changePassword(String userId, String oldPass, String newPass);
    Observable<BaseListResponse> updateSoft(String appCode, long userId, String version, int numNotUpdate,
                                            String dateServer, String ime);

    Observable<String> getDateServer();
}
