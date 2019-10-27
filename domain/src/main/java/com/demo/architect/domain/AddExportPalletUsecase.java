package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public class AddExportPalletUsecase extends BaseUseCase<BaseResponse> {
    private static final String TAG = AddExportPalletUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public AddExportPalletUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse> buildUseCaseObservable() {
        long userId = ((RequestValue) requestValues).userId;
        String code = ((RequestValue) requestValues).code;
        return remoteRepository.addScanRequestByPallet(userId,code);
    }

    @Override
    protected DisposableObserver<BaseResponse> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {

                    if (data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue());
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
        private final String code;

        public RequestValue(long userId, String code) {
            this.userId = userId;
            this.code = code;
        }
    }

    public static final class ResponseValue implements ResponseValues {

        public ResponseValue() {

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
