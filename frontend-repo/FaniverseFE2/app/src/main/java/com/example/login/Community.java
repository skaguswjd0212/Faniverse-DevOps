package com.example.login;

public class Community {
    private int id;
    private String name;
    private String description;
    private int imageResourceId;

    public Community(int id, String name, String description, int imageResourceId) {
        this.id=id;
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
