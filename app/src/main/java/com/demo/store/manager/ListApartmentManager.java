package com.demo.store.manager;

import com.demo.architect.data.model.ApartmentEntity;

import java.util.List;

public class ListApartmentManager {
    private List<ApartmentEntity> list;
    private static ListApartmentManager instance;

    public static ListApartmentManager getInstance() {
        if (instance == null) {
            instance = new ListApartmentManager();
        }
        return instance;
    }

    public void setListDepartment(List<ApartmentEntity> list) {
        this.list = list;
    }

    public List<ApartmentEntity> getListApartment() {
        return list;
    }


    public ApartmentEntity getApartmentById(int apartmentId) {
        ApartmentEntity apartmentEntity = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == apartmentId) {
                apartmentEntity = list.get(i);
                break;
            }
        }
        return apartmentEntity;

    }
}
