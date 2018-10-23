package com.dietncure.friendlychatapp;

public class UserListModel {
    private String name;
    private boolean read;
    private int uid;
    private long timeStamp;
    public UserListModel() {

    }
    public UserListModel(String name, boolean read,long timeStamp, int uid) {
        this.name = name;
        this.read = read;
        this.uid = uid;
        this.timeStamp =timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getuid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
