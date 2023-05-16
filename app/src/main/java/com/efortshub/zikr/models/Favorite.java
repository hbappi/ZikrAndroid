package com.efortshub.zikr.models;

public class Favorite {
    long id;
    int duaId;
    String segmentId;

    public Favorite(long id, int duaId, String segmentId) {
        this.id = id;
        this.duaId = duaId;
        this.segmentId = segmentId;
    }

    public long getId() {
        return id;
    }

    public int getDuaId() {
        return duaId;
    }

    public String getSegmentId() {
        return segmentId;
    }
}
