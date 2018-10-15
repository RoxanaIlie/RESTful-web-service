package server.model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Item {
    private int id;
    private Timestamp timestamp;

    public Item() {
    }

    public Item(int id, Timestamp timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public Item(int id, String timestamp) {
        this.id = id;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSXXX");

        Date parsedTimeStamp = null;
        try {
            parsedTimeStamp = dateFormat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            parsedTimeStamp = new Date();
        }
        this.timestamp = new Timestamp(parsedTimeStamp.getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(Date timestamp) {
        this.timestamp = new Timestamp(timestamp.getTime());
    }

}
