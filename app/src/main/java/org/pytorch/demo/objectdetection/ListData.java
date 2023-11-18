package org.pytorch.demo.objectdetection;

public class ListData{
    private String name,roomno;

    public ListData(String name, String roomno) {
        this.name = name;
        this.roomno = roomno;
    }
    public String getName() {
        return name;
    }
    public void setName(String description) {
        this.name = description;
    }
    public String getRoomno() {
        return roomno;
    }
    public void setImgId(String roomno) {
        this.roomno = roomno;
    }
}
