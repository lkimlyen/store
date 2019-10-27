package com.demo.architect.data.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SOEntity {
    @SerializedName("OrderID")
    @Expose
    private long orderId;

    @SerializedName("CodeSO")
    @Expose
    private String codeSO;


    @SerializedName("CusName")
    @Expose
    private String customerName;

    @SerializedName("ListTurn")
    @Expose
    private List<Turn> turnList;

    public SOEntity() {
    }

    public long getOrderId() {
        return orderId;
    }

    public String getCodeSO() {
        return codeSO;
    }

    public String getCustomerName() {
        return customerName;
    }


    public class Turn{
        @Expose
        @SerializedName("Turn")
        private int turn;

        public int getTurn() {
            return turn;
        }

        @NonNull
        @Override
        public String toString() {
            return String.valueOf(turn);
        }
    }

    public List<Turn> getTurnList() {
        return turnList;
    }

    @Override
    public String toString() {
        return codeSO;
    }
}
