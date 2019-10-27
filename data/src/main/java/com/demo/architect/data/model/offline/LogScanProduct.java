package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogScanProduct extends RealmObject {
    @PrimaryKey
    private long id;

    private long groupId;
    private long productId;
    private int numberTotal;
    private int numberScanned;
    private int numberRest;
    private int status;

    public LogScanProduct() {

    }

    public LogScanProduct(long groupId, long productId, int numberTotal, int numberScanned, int numberRest) {
        this.numberRest = numberRest;
        id = UUID.randomUUID().getMostSignificantBits();
        this.groupId = groupId;
        this.productId = productId;
        this.numberTotal = numberTotal;
        this.numberScanned = numberScanned;
        this.numberRest = numberRest;
        this.status = Constants.DOING;

    }

    public static LogScanProduct createLog(Realm realm, LogScanProduct log) {
        log = realm.copyToRealm(log);
        return log;
    }

    public long getId() {
        return id;
    }

    public long getGroupId() {
        return groupId;
    }

    public long getProductId() {
        return productId;
    }

    public int getNumberScanned() {
        return numberScanned;
    }

    public int getNumberTotal() {
        return numberTotal;
    }

    public int getNumberRest() {
        return numberRest;
    }

    public int getStatus() {
        return status;
    }

    public void setNumberTotal(int numberTotal) {
        this.numberTotal = numberTotal;
    }

    public void setNumberScanned(int numberScanned) {
        this.numberScanned = numberScanned;
    }

    public void setNumberRest(int numberRest) {
        this.numberRest = numberRest;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
