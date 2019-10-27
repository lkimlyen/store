package com.demo.architect.data.model.offline;

import com.demo.architect.utils.view.ConvertUtils;
import com.demo.architect.utils.view.DateUtils;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class ExportModel extends RealmObject {
    @PrimaryKey
    private long id;
    private long orderId;
    private int batch;
    private int floorId;

    private String dateExport;
    private Date date;
    private String code;

    public ExportModel() {

    }

    public ExportModel(long orderId, int batch, int floorId, String dateExport, Date date, String code) {
        this.orderId = orderId;
        this.batch = batch;
        this.floorId = floorId;
        id = UUID.randomUUID().getMostSignificantBits();
        this.dateExport = dateExport;
        this.date = date;
        this.code = code;
    }

    public static void create(Realm realm, long orderId, int batch, int floorId, String code) {
        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
        ExportModel model = new ExportModel(orderId, batch, floorId, DateUtils.getDateTimeCurrent(), dateCurrent, code);
        realm.copyToRealm(model);
    }

    public static RealmResults<ExportModel> getListExportByDate(Realm realm, long orderId, int batch, int floorId) {
        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
        RealmResults<ExportModel> results = realm.where(ExportModel.class).equalTo("date", dateCurrent)
                .equalTo("orderId", orderId).equalTo("batch", batch).equalTo("floorId", floorId).findAll();
        return results;

    }

    public long getId() {
        return id;
    }

    public String getDateExport() {
        return dateExport;
    }

    public Date getDate() {
        return date;
    }

    public String getCode() {
        return code;
    }
}
