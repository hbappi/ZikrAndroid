package com.efortshub.zikr.models;

public class Favorite {
    long id;
    int duaId;

    public Favorite(long id, int duaId) {
        this.id = id;
        this.duaId = duaId;
    }

    public long getId() {
        return id;
    }

    public int getDuaId() {
        return duaId;
    }
}
