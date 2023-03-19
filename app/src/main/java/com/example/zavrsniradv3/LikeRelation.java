package com.example.zavrsniradv3;

public class LikeRelation {
    public int id,idAkt,idOb,idUsera;

    public LikeRelation(int id, int idAkt, int idOb, int idUsera) {
        this.id = id;
        this.idAkt = idAkt;
        this.idOb = idOb;
        this.idUsera = idUsera;
    }

    public int getId() {
        return id;
    }

    public int getIdAkt() {
        return idAkt;
    }

    public int getIdUsera() {
        return idUsera;
    }
}
