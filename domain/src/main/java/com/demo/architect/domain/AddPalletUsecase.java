package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public class AddPalletUsecase extends BaseUseCase<BaseResponse<String>> {
    private static final String TAG = AddPalletUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public AddPalletUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse<String>> buildUseCaseObservable() {
        long userId = ((RequestValue) requestValues).userId;      String json = ((RequestValue) requestValues).json;
        return remoteRepository.addPallet(userId,json);
    }

    @Override
    protected DisposableObserver<BaseResponse<String>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse<String>>() {
            @Override
            public void onNext(BaseResponse<String> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                   String result = data.getData();
                    if (result != null && data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue(result));
                    } else {
                        useCaseCallback.onError(new ErrorValue(data.getDescription()));
                    }
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
        private final long userId;
        private final String json;

        public RequestValue(long userId, String json) {
            this.userId = userId;
            this.json = json;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private String code;

        public ResponseValue(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
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
