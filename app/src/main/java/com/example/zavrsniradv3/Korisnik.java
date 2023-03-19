package com.example.zavrsniradv3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Korisnik implements Parcelable {
    int id,brojPratitelji,brojPratim,slika;
    String ime,prezime,email,lozinka,opis;

    public Korisnik(int id, String ime, String prezime, String email, String lozinka,int brojPratitelji,int brojPratim,int slika,String opis) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.lozinka = lozinka;
        this.brojPratitelji=brojPratitelji;
        this.brojPratim=brojPratim;
        this.slika=slika;
        this.opis=opis;
    }

    protected Korisnik(Parcel in) {
        id = in.readInt();
        ime = in.readString();
        prezime = in.readString();
        email = in.readString();
        lozinka = in.readString();
        brojPratitelji=in.readInt();
        brojPratim=in.readInt();
        slika=in.readInt();
        opis=in.readString();
    }


    public static final Creator<Korisnik> CREATOR = new Creator<Korisnik>() {
        @Override
        public Korisnik createFromParcel(Parcel in) {
            return new Korisnik(in);
        }

        @Override
        public Korisnik[] newArray(int size) {
            return new Korisnik[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime=prezime;
    }

    public String getEmail() {
        return email;
    }

    public int getBrojPratitelji() {
        return brojPratitelji;
    }

    public int getBrojPratim() {
        return brojPratim;
    }

    public int getSlika() {
        return slika;
    }

    public String getOpis() {
        return opis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(ime);
        parcel.writeString(prezime);
        parcel.writeString(email);
        parcel.writeString(lozinka);
        parcel.writeInt(brojPratitelji);
        parcel.writeInt(brojPratim);
        parcel.writeInt(slika);
        parcel.writeString(opis);
    }
}
