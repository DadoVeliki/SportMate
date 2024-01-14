package com.example.zavrsniradv3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class Aktivnost implements Parcelable {
    public int id,idUsera,nmv,brojLajkova;
    public String imePrezime,datum,naslov,vrijeme,vrsta,oprema,tipAkt;
    public float udaljenost,avgBrzina;

    public Aktivnost(int id,int idUsera,String imePrezime, String datum, String naslov, float udaljenost, int nmv, String vrijeme, int brojLajkova,String vrsta,float avgBrzina,String oprema,String tipAkt) {
        this.id=id;
        this.idUsera=idUsera;
        this.imePrezime = imePrezime;
        this.datum = datum;
        this.naslov = naslov;
        this.udaljenost = udaljenost;
        this.nmv = nmv;
        this.vrijeme = vrijeme;
        this.brojLajkova = brojLajkova;
        this.vrsta=vrsta;
        this.avgBrzina=avgBrzina;
        this.oprema=oprema;
        this.tipAkt=tipAkt;
    }

    protected Aktivnost(Parcel in) {
        id = in.readInt();
        idUsera = in.readInt();
        imePrezime = in.readString();
        datum = in.readString();
        naslov = in.readString();
        udaljenost = in.readFloat();
        nmv = in.readInt();
        vrijeme = in.readString();
        brojLajkova = in.readInt();
        vrsta=in.readString();
        avgBrzina=in.readFloat();
        oprema=in.readString();
        tipAkt=in.readString();
    }

    public static final Creator<Aktivnost> CREATOR = new Creator<Aktivnost>() {
        @Override
        public Aktivnost createFromParcel(Parcel in) {
            return new Aktivnost(in);
        }

        @Override
        public Aktivnost[] newArray(int size) {
            return new Aktivnost[size];
        }
    };

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

    public float getUdaljenost() {
        return udaljenost;
    }

    public int getNmv() {
        return nmv;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public int getBrojLajkova() {
        return brojLajkova;
    }

    public int getId() {
        return id;
    }

    public String getVrsta() {
        return vrsta;
    }

    public float getAvgBrzina() {
        return avgBrzina;
    }

    public String getOprema() {
        return oprema;
    }

    public String getTipAkt() {
        return tipAkt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(idUsera);
        parcel.writeString(imePrezime);
        parcel.writeString(datum);
        parcel.writeString(naslov);
        parcel.writeFloat(udaljenost);
        parcel.writeInt(nmv);
        parcel.writeString(vrijeme);
        parcel.writeInt(brojLajkova);
        parcel.writeString(vrsta);
        parcel.writeFloat(avgBrzina);
        parcel.writeString(oprema);
        parcel.writeString(tipAkt);
    }
}
