package com.demo.store.manager;

import com.demo.architect.data.model.CodePalletEntity;

import java.util.List;

public class ListCodePalletManager {
    private List<CodePalletEntity> list;
    private static ListCodePalletManager instance;

    public static ListCodePalletManager getInstance() {
        if (instance == null) {
            instance = new ListCodePalletManager();
        }
        return instance;
    }

    public void setList(List<CodePalletEntity> list) {
        this.list = list;
    }

    public List<CodePalletEntity> getList() {
        return list;
    }


    public CodePalletEntity getPalletByCode(String code) {
        CodePalletEntity codePalletEntity = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCode().equals(code)) {
                codePalletEntity = list.get(i);
                break;
            }
        }
        return codePalletEntity;

    }

    public void updateStatusPallet(String code) {
        CodePalletEntity codePalletEntity = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCode().equals(code)) {
                codePalletEntity = list.get(i);
                codePalletEntity.setStatus(3);
                list.set(i, codePalletEntity);
                break;
            }
        }

    }
}
