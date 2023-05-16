package com.efortshub.zikr.models;

public class History {
    long id;
    int duaId;

    int milliseconds;

    public long getId() {
        return id;
    }

    public int getDuaId() {
        return duaId;
    }

    public int getMilliseconds() {
        return milliseconds;
    }


    public History(long id, int duaId, int milliseconds) {
        this.id = id;
        this.duaId = duaId;
        this.milliseconds = milliseconds;
    }
}
