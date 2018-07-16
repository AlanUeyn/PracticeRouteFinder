package com.alanueyn.projects.practiceroutefinder.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(indices = {@Index(value = {"checkpointName"},unique = true)})
public class CheckpointEntity implements Parcelable {

    public void setCheckpointId(Long checkpointId) {
        this.checkpointId = checkpointId;
    }

    public void setCheckpointName(String checkpointName) {
        this.checkpointName = checkpointName;
    }

    public void setCheckpointLat(Double checkpointLat) {
        this.checkpointLat = checkpointLat;
    }

    public void setCheckpointLong(Double checkpointLong) {
        this.checkpointLong = checkpointLong;
    }

    public CheckpointEntity(String checkpointName, Double checkpointLat, Double checkpointLong) {
        this.checkpointName = checkpointName;
        this.checkpointLat = checkpointLat;
        this.checkpointLong = checkpointLong;
    }

    @PrimaryKey(autoGenerate = true)
    private Long checkpointId;


    @ColumnInfo(name = "checkpointName")
    private String checkpointName;

    @ColumnInfo(name = "checkpointLat")
    private Double checkpointLat;

    @ColumnInfo(name = "checkpointLong")
    private Double checkpointLong;

    public Long getCheckpointId() {
        return checkpointId;
    }

    public String getCheckpointName() {
        return checkpointName;
    }

    public Double getCheckpointLat() {
        return checkpointLat;
    }

    public Double getCheckpointLong() {
        return checkpointLong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(checkpointName);
        dest.writeDoubleArray(new double[] {checkpointLat,checkpointLong});
    }

    public static final Parcelable.Creator<CheckpointEntity> CREATOR = new Parcelable.Creator<CheckpointEntity>(){
        @Override
        public CheckpointEntity createFromParcel(Parcel source) {
            return new CheckpointEntity(source);
        }

        @Override
        public CheckpointEntity[] newArray(int size) {
            return new CheckpointEntity[size];
        }
    };

    @Ignore
    private CheckpointEntity(Parcel parcel) {
        double[] coordinates = new double[2];
        checkpointName = parcel.readString();
        parcel.readDoubleArray(coordinates);
        checkpointLat = coordinates[0];
        checkpointLong = coordinates[1];
    }





}
