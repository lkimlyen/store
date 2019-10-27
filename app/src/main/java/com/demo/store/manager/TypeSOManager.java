package com.demo.store.manager;

import java.util.ArrayList;

public class TypeSOManager {
    private ArrayList<TypeSO> listTypeSO;

    private static TypeSOManager instance;

    public static TypeSOManager getInstance() {
        if (instance == null) {
            instance = new TypeSOManager();
        }
        return instance;
    }

    public ArrayList<TypeSO> getListType() {
        listTypeSO = new ArrayList<>();
        listTypeSO.add(new TypeSO("Hàng công trình", 1));
      //  listTypeSO.add(new TypeSO("Hàng Cửa", 4));
       // listTypeSO.add(new TypeSO("Hàng Lẻ", 2));
//        listTypeSO.add(new TypeSO("Hàng Phụ Kiện", 3));
//        listTypeSO.add(new TypeSO("Hàng Phụ Kiện Cửa", 5));
        return listTypeSO;
    }

    public int getValueByPositon(int position) {
        TypeSO typeSO = listTypeSO.get(position);
        return typeSO.getValue();
    }

    public class TypeSO {
        private String name;
        private int value;

        public TypeSO(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
