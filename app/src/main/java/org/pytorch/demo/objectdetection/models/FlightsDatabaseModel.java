package org.pytorch.demo.objectdetection.models;

public class FlightsDatabaseModel {
    private int card_image;
    private String flightnum,deptime,depdest,arrtime,arrdest,price;


    // Constructor

    public FlightsDatabaseModel(){   }
    public FlightsDatabaseModel(int card_image, String flightnum, String deptime, String depdest, String arrtime, String arrdest, String price) {
        this.card_image = card_image;

        this.flightnum = flightnum;
        this.deptime=deptime;
        this.depdest=depdest;
        this.arrtime=arrtime;
        this.arrdest=arrdest;
        this.price=price;
    }

    // Getter and Setter
    public int getcard_image() {
        return card_image;
    }

    public void setcard_image(int card_image) {
        this.card_image = card_image;
    }



    public String getflightnum() {
        return flightnum;
    }

    public void setflightnum(String flightnum) {
        this.flightnum = flightnum;
    }

    public String getDeptime() {
        return deptime;
    }

    public void setDeptime(String deptime) {
        this.deptime = deptime;
    }
    public String getArrdest() {
        return arrdest;
    }

    public void setArrdest(String arrdest) {
        this.arrdest = arrdest;
    }
    public String getArrtime() {
        return arrtime;
    }

    public void setArrtime(String arrtime) {
        this.arrtime = arrtime;
    }
    public String getDepdest() {
        return depdest;
    }

    public void setDepdest(String depdest) {
        this.depdest = depdest;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
