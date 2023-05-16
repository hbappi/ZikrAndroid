package com.efortshub.zikr.models;

public class History {
    long id;
    int duaId;

    long milliseconds;
    String segmentId;

    public History(long id, int duaId, long milliseconds, String segmentId) {
        this.id = id;
        this.duaId = duaId;
        this.milliseconds = milliseconds;
        this.segmentId = segmentId;
    }

    public long getId() {
        return id;
    }

    public int getDuaId() {
        return duaId;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public String getSegmentId() {
        return segmentId;
    }
}
