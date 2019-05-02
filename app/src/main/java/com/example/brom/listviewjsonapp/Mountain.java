package com.example.brom.listviewjsonapp;

public class Mountain {
    private String name;
    private String location;
    private int height;

    public Mountain(){
        name="No name specified";
        location="No location specified";
        height=0;
    }

    public Mountain(String n, String l, int h){
        name=n;
        location=l;
        height=h;
    }

    public String info(){
        String tmp=new String();
        tmp+=name+" is located in " +location+ " and reaches "+height+"m above sea level. ";
        return tmp;
    }

    @Override
    public String toString() {
        return name;
    }
}
