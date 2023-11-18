package org.pytorch.demo.objectdetection.models;

public class Building_Info {
    int _id;
    String _name;
    String _room_no, _type, _floor,_block;
    public Building_Info(){   }
    public Building_Info(int id, String name, String _room_no, String _type, String nationality, String block){
        this._id = id;
        this._name = name;
        this._room_no = _room_no;
        this. _type=_type;
        this._floor=nationality;
        this._block=block;

    }

    public Building_Info(String name, String _room_no, String _type, String floor, String block){
        this._name = name;
        this._room_no = _room_no;
        this. _type=_type;
        this._floor=floor;
        this._block=block;

    }
    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getName(){
        return this._name;
    }

    public void setName(String name){
        this._name = name;
    }

    public String getRoomNo(){
        return this._room_no;
    }

    public void setRoomNo(String _room_no){
        this._room_no = _room_no;
    }
    public String getType(){
        return this._type;
    }

    public void setType(String _type){
        this._type = _type;
    }
    public String getBlocks(){
        return this._block;
    }

    public void setBlocks(String block){
        this._block = block;
    }

    public String getFloors(){
        return this._floor;
    }

    public void setFloors(String floor){
        this._floor = floor;
    }

}