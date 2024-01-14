package com.example.zavrsniradv3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ObjavaC implements Parcelable {
    private int id,idUsera,brojLajkova;
    private String imePrezime,datum,naslov,tekst,link;

    public ObjavaC(int id, int idUsera, String imePrezime, String datum, String naslov, String tekst, String link, int brojLajkova) {
        this.id = id;
        this.idUsera = idUsera;
        this.imePrezime = imePrezime;
        this.datum = datum;
        this.naslov = naslov;
        this.tekst = tekst;
        this.link = link;
        this.brojLajkova = brojLajkova;
    }

    public int getId() {
        return id;
    }

    public int getIdUsera() {
        return idUsera;
    }

    public String getImePrezime() {
        return imePrezime;
    }

    public String getDatum() {
        return datum;
    }

    public String getNaslov() {
        return naslov;
    }

    public String getTekst() {
        return tekst;
    }

    public String getLink() {
        return link;
    }

    public int getBrojLajkova() {
        return brojLajkova;
    }

    protected ObjavaC(Parcel in) {
    }

    public static final Creator<ObjavaC> CREATOR = new Creator<ObjavaC>() {
        @Override
        public ObjavaC createFromParcel(Parcel in) {
            return new ObjavaC(in);
        }

        @Override
        public ObjavaC[] newArray(int size) {
            return new ObjavaC[size];
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
