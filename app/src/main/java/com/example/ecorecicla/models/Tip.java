package com.example.ecorecicla.models;

import java.util.List;

public class Tip {
    private int id;
    private String title;
    private String stikerImage;
    private List<Message> messages;

    public Tip(int id, String title, String stikerImage, List<Message> messages) {
        this.id = id;
        this.title = title;
        this.stikerImage = stikerImage;
        this.messages = messages;

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStikerImage() {
        return stikerImage;
    }

    public List<Message> getMessages() {
        return messages;
    }

}

