package com.example.peter.shike_app;

/**
 * Created by Administrator on 2018/6/9.
 */

public class Tag {
    private int ID;
    private String name;
    private int type;
    private String typeName;

    public Tag(int ID) {
        this.ID = ID;
        this.name = PreferenceUtil.tag[ID];
        if(ID >= 0 && ID <= 11)
            this.type = 0;
        else if(ID >= 12 && ID <= 18)
            this.type = 1;
        else if(ID >= 19 && ID <= 23)
            this.type = 2;
        else if(ID == 24)
            this.type = 3;
        else if(ID >= 25 && ID <= 26)
            this.type = 4;
        else if(ID >= 27 && ID <= 30)
            this.type = 5;
        else
            this.type = -1;
        if(this.type != -1)
            typeName = PreferenceUtil.tagType[this.type];
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
