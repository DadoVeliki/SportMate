package com.example.zavrsniradv3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Oprema implements Parcelable {
    private int id,idCije;
    private String nadimak,marka,model,tip;

    public Oprema(int id, String nadimak, String marka, String model, String tip,int idCije) {
        this.id = id;
        this.nadimak = nadimak;
        this.marka = marka;
        this.model = model;
        this.tip = tip;
        this.idCije=idCije;
    }

    public int getId() {
        return id;
    }

    public String getNadimak() {
        return nadimak;
    }

    public String getMarka() {
        return marka;
    }

    public String getModel() {
        return model;
    }

    public String getTip() {
        return tip;
    }

    public int getIdCije() {
        return idCije;
    }

    protected Oprema(Parcel in) {
    }

    public static final Creator<Oprema> CREATOR = new Creator<Oprema>() {
        @Override
        public Oprema createFromParcel(Parcel in) {
            return new Oprema(in);
        }

        @Override
        public Oprema[] newArray(int size) {
            return new Oprema[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
    }
}