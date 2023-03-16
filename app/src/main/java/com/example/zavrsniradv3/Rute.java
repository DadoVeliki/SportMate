package com.example.zavrsniradv3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Rute implements Parcelable {
    public int idAkt;
    public double startLat,startLong,endLat,endLong;

    public Rute(int idAkt,double startLat, double startLong, double endLat, double endLong) {
        this.idAkt=idAkt;
        this.startLat = startLat;
        this.startLong = startLong;
        this.endLat = endLat;
        this.endLong = endLong;
    }

    protected Rute(Parcel in) {
        idAkt = in.readInt();
        startLat = in.readDouble();
        startLong = in.readDouble();
        endLat = in.readDouble();
        endLong = in.readDouble();
    }

    public static final Creator<Rute> CREATOR = new Creator<Rute>() {
        @Override
        public Rute createFromParcel(Parcel in) {
            return new Rute(in);
        }

        @Override
        public Rute[] newArray(int size) {
            return new Rute[size];
        }
    };

    public int getIdAkt() {
        return idAkt;
    }

    public double getStartLat() {
        return startLat;
    }

    public double getStartLong() {
        return startLong;
    }

    public double getEndLat() {
        return endLat;
    }

    public double getEndLong() {
        return endLong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(idAkt);
        parcel.writeDouble(startLat);
        parcel.writeDouble(startLong);
        parcel.writeDouble(endLat);
        parcel.writeDouble(endLong);
    }
}
