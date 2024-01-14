package com.example.zavrsniradv3;

public class Comment {
    private int id,idOb,idAkt,idUsera;
    private String tekst;

    public Comment(int id, int idOb, int idAkt, int idUsera, String tekst) {
        this.id = id;
        this.idOb = idOb;
        this.idAkt = idAkt;
        this.idUsera = idUsera;
        this.tekst = tekst;
    }

    public int getId() {
        return id;
    }

    public int getIdOb() {
        return idOb;
    }

    public int getIdAkt() {
        return idAkt;
    }

    public int getIdUsera() {
        return idUsera;
    }

    public String getTekst() {
        return tekst;
    }
}
