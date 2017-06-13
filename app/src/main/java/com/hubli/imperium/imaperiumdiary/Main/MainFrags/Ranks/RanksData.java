package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Ranks;

/**
 * Created by Faheem on 13-06-2017.
 */

public class RanksData {

    private int rank;
    private String user_number;
    private String name;
    private String vClass;
    private String division;
    private String propic_link;
    private String total;

    public RanksData(int rank,String user_number, String first_name,String last_name, String vClass, String division, String propic_link, String total) {
        this.rank = rank;
        this.user_number = user_number;
        this.name = first_name+" "+last_name;
        this.vClass = vClass;
        this.division = division;
        this.propic_link = propic_link;
        this.total = total;
    }

    public String getUser_number() {
        return user_number;
    }

    public String getName() {
        return name;
    }

    public String getvClass() {
        return vClass;
    }

    public String getDivision() {
        return division;
    }

    public String getPropic_link() {
        return propic_link;
    }

    public int getRank() {
        return rank;
    }

    public String getTotal() {

        return total;
    }
}
