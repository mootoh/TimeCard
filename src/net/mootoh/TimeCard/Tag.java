package net.mootoh.TimeCard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Tag {
    public String id;
    public String name;
    public String color;
    public Date timeStamp;
    public boolean isOn;

    public Tag(String id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Tag(String id, String name, String color, String touchedAt, boolean isOn) throws ParseException {
        this.id = id;
        this.name = name;
        this.color = color;
        this.isOn = isOn;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.timeStamp = formatter.parse(touchedAt);
    }
}