package com.example.zavrsniradv3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Odnos implements Parcelable {
    public int id,idKojiPrati,idPracen;
    public Odnos(int id, int idKojiPrati, int idPracen) {
        this.id = id;
        this.idKojiPrati = idKojiPrati;
        this.idPracen = idPracen;
    }

    protected Odnos(Parcel in) {
        id = in.readInt();
        idKojiPrati = in.readInt();
        idPracen = in.readInt();
    }

    public static final Creator<Odnos> CREATOR = new Creator<Odnos>() {
        @Override
        public Odnos createFromParcel(Parcel in) {
            return new Odnos(in);
        }

        @Override
        public Odnos[] newArray(int size) {
            return new Odnos[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getIdKojiPrati() {
        return idKojiPrati;
    }

    public int getIdPracen() {
        return idPracen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(idKojiPrati);
        parcel.writeInt(idPracen);
    }
}