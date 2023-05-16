package com.efortshub.zikr.models;

import java.io.Serializable;

public class DuaDetailsWithHistory implements Serializable {
    String dua_segment_id;
    String top;
    String arabic_diacless;
    String arabic;
    String transliteration;
    String translations;
    String bottom;
    String reference;
    String title;
    private int segmentIndex;
    long timestamp;

    public String getDua_global_id() {
        return dua_global_id;
    }

    String dua_global_id;

    public DuaDetailsWithHistory(String dua_segment_id, String top, String arabic_diacless, String arabic, String transliteration, String translations, String bottom, String reference, String dua_global_id, String title, int segmentIndex, long timestamp) {
        this.dua_segment_id = dua_segment_id;
        this.top = top;
        this.arabic_diacless = arabic_diacless;
        this.arabic = arabic;
        this.transliteration = transliteration;
        this.translations = translations;
        this.bottom = bottom;
        this.reference = reference;
        this.dua_global_id = dua_global_id;
        this.title = title;
        this.segmentIndex = segmentIndex;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDua_segment_id() {
        return dua_segment_id;
    }

    public String getTop() {
        return top;
    }

    public int getSegmentIndex() {
        return segmentIndex;
    }

    public String getArabic_diacless() {
        return arabic_diacless;
    }

    public String getArabic() {
        return arabic;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public String getTranslations() {
        return translations;
    }

    public String getBottom() {
        return bottom;
    }

    public String getReference() {
        return reference;
    }

    public String getTitle() {
        return title;
    }
}
