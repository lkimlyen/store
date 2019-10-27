package com.demo.store.app.di.module;


import com.demo.architect.data.repository.base.account.remote.AuthRepository;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;
import com.demo.architect.domain.AddExportPalletUsecase;
import com.demo.architect.domain.AddPalletUsecase;
import com.demo.architect.domain.ChangePasswordUsecase;
import com.demo.architect.domain.GetCodePalletUsecase;
import com.demo.architect.domain.GetListFloorUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.GetProductForPalletUsecase;
import com.demo.architect.domain.LoginUsecase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by uyminhduc on 12/16/16.
 */
@Module
public class UseCaseModule {
    public UseCaseModule() {
    }


    @Provides
    LoginUsecase provideLoginUsecase(AuthRepository remoteRepository) {
        return new LoginUsecase(remoteRepository);
    }

    @Provides
    ChangePasswordUsecase provideChangePasswordUsecase(AuthRepository remoteRepository) {
        return new ChangePasswordUsecase(remoteRepository);
    }

    @Provides
    GetListSOUsecase provideGetListSOUsecase(OrderRepository remoteRepository) {
        return new GetListSOUsecase(remoteRepository);
    }

    @Provides
    GetListFloorUsecase provideGetListFloorUsecase(OtherRepository otherRepository) {
        return new GetListFloorUsecase(otherRepository);
    }

    @Provides
    GetProductForPalletUsecase provideGetProductForPalletUsecase(ProductRepository otherRepository) {
        return new GetProductForPalletUsecase(otherRepository);
    }

    @Provides
    GetCodePalletUsecase provideGetCodePalletUsecase(ProductRepository otherRepository) {
        return new GetCodePalletUsecase(otherRepository);
    }

    @Provides
    AddPalletUsecase provideAddPalletUsecase(OrderRepository orderRepository){
        return new AddPalletUsecase((orderRepository));
    }

    @Provides
    AddExportPalletUsecase provideAddExportPalletUsecase(OrderRepository orderRepository){
        return new AddExportPalletUsecase((orderRepository));
    }
}

