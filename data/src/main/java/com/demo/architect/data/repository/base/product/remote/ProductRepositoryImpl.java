package com.demo.architect.data.repository.base.product.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.CodePalletEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductListEntity;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;
import io.reactivex.Observable;
/**
 * Created by Skull on 04/01/2018.
 */

public class ProductRepositoryImpl implements ProductRepository {
    private final static String TAG = ProductRepositoryImpl.class.getName();

    private ProductApiInterface mRemoteApiInterface;
    private Context context;
    private String server;

    public ProductRepositoryImpl(ProductApiInterface mRemoteApiInterface, Context context) {
        this.mRemoteApiInterface = mRemoteApiInterface;
        this.context = context;
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
    }
    private void handleProductResponse(Call<BaseListResponse<ProductEntity>> call, ObservableEmitter<BaseListResponse<ProductEntity>> emitter) {
        try {
            BaseListResponse<ProductEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }
    private void handleProductListResponse(Call<BaseListResponse<ProductListEntity>> call, ObservableEmitter<BaseListResponse<ProductListEntity>> emitter) {
        try {
            BaseListResponse<ProductListEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleCodePalletListResponse(Call<BaseListResponse<CodePalletEntity>> call, ObservableEmitter<BaseListResponse<CodePalletEntity>> emitter) {
        try {
            BaseListResponse<CodePalletEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }
    private void handleBaseResponse(Call<BaseResponse> call, ObservableEmitter<BaseResponse> emitter) {
        try {
            BaseResponse response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleIntegerResponse(Call<BaseResponse<Integer>> call, ObservableEmitter<BaseResponse<Integer>> emitter) {
        try {
            BaseResponse<Integer> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleStringResponse(Call<BaseResponse<String>> call, ObservableEmitter<BaseResponse<String>> emitter) {
        try {
            BaseResponse<String> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }




    @Override
    public Observable<BaseListResponse<ProductListEntity>> getProductForPallet(final long orderId, final int turn) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<ProductListEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<ProductListEntity>> emitter) throws Exception {
                handleProductListResponse(mRemoteApiInterface.getProductForPallet(
                        server + "/WS/api/GetAllCodePallet",orderId,turn), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<CodePalletEntity>> getCodePallet(final long orderId, final int turn, final int floorId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<CodePalletEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<CodePalletEntity>> emitter) throws Exception {
                handleCodePalletListResponse(mRemoteApiInterface.getCodePallet(
                        server + "/WS/api/GetPalletHistory",orderId,turn,floorId), emitter);
            }
        });
    }
}
