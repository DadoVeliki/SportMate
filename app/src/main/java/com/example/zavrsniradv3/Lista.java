package com.example.zavrsniradv3;

public class Lista {
    String name,desc;


    public Lista(String name, String desc) {
        this.name = name;
        this.desc = desc;

    }
    public String getData(){
        return name;
    }
    public void setData(String name,String desc){
        this.name=name;
        this.desc=desc;
    }
}
