package com.demo.architect.data.model;

import java.util.List;

public class ProductUploadEntity {
    private long PalletID;
    private long ProductID;
    private List<DetailProductEntity> ListCodeDetailProductScan;

    public ProductUploadEntity(long palletID, long productID) {
        PalletID = palletID;
        ProductID = productID;
    }

    public ProductUploadEntity(long palletID, long productID, List<DetailProductEntity> listCodeDetailProductScan) {
        PalletID = palletID;
        ProductID = productID;
        ListCodeDetailProductScan = listCodeDetailProductScan;
    }

   public static class DetailProductEntity {
        private String CodeScan;
        private long Quantity;
        private String Pack;
        private int Turn;

       public DetailProductEntity(String codeScan, long quantity, String pack, int turn) {
           CodeScan = codeScan;
           Quantity = quantity;
           Pack = pack;
           Turn = turn;
       }
   }

    public void setListCodeDetailProductScan(List<DetailProductEntity> listCodeDetailProductScan) {
        ListCodeDetailProductScan = listCodeDetailProductScan;
    }
}
