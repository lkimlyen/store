package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.ProductListEntity;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public class GetProductForPalletUsecase extends BaseUseCase<BaseListResponse<ProductListEntity>> {
    private static final String TAG = GetProductForPalletUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public GetProductForPalletUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<ProductListEntity>> buildUseCaseObservable() {
        long orderId = ((RequestValue) requestValues).orderId;
        int turn = ((RequestValue) requestValues).turn;
        return remoteRepository.getProductForPallet(orderId, turn);
    }
    @Override
    protected DisposableObserver<BaseListResponse<ProductListEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<ProductListEntity>>() {
            @Override
            public void onNext(BaseListResponse<ProductListEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                List<ProductListEntity> result = data.getData();
                if (result != null && data.getStatus() == 1) {
                    useCaseCallback.onSuccess(new ResponseValue(result));
                } else {
                    useCaseCallback.onError(new ErrorValue(data.getDescription()));
                }
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.toString());
                if (useCaseCallback != null) {
                    useCaseCallback.onError(new ErrorValue(e.toString()));
                }
            }
        };
    }


    public static final class RequestValue implements RequestValues {
        private final long orderId;
        private final int turn;

        public RequestValue(long orderId, int turn) {
            this.orderId = orderId;
            this.turn = turn;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<ProductListEntity> entity;

        public ResponseValue(List<ProductListEntity> entity) {
            this.entity = entity;
        }

        public List<ProductListEntity> getEntity() {
            return entity;
        }
    }

    public static final class ErrorValue implements ErrorValues {
        private String description;

        public ErrorValue(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
