package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.CodePalletEntity;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public class GetCodePalletUsecase extends BaseUseCase<BaseListResponse<CodePalletEntity>> {
    private static final String TAG = GetCodePalletUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public GetCodePalletUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<CodePalletEntity>> buildUseCaseObservable() {
        long orderId = ((RequestValue) requestValues).orderId;
        int turn = ((RequestValue) requestValues).turn;
        int floorId = ((RequestValue) requestValues).floorId;
        return remoteRepository.getCodePallet(orderId, turn,floorId);
    }

    @Override
    protected DisposableObserver<BaseListResponse<CodePalletEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<CodePalletEntity>>() {
            @Override
            public void onNext(BaseListResponse<CodePalletEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                List<CodePalletEntity> result = data.getData();
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
        private final int floorId;

        public RequestValue(long orderId, int turn, int floorId) {
            this.orderId = orderId;
            this.turn = turn;
            this.floorId = floorId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<CodePalletEntity> entity;

        public ResponseValue(List<CodePalletEntity> entity) {
            this.entity = entity;
        }

        public List<CodePalletEntity> getEntity() {
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
