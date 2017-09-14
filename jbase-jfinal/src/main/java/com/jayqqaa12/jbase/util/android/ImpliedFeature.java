package com.jayqqaa12.jbase.util.android;

public class ImpliedFeature {
    private String feature;
    private String implied;

    public ImpliedFeature() {
    }

    public ImpliedFeature(String feature, String implied) {
        this.feature = feature;
        this.implied = implied;
    }

    public String getFeature() {
        return this.feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getImplied() {
        return this.implied;
    }

    public void setImplied(String implied) {
        this.implied = implied;
    }

    public String toString() {
        return "Feature [feature=" + this.feature + ", implied=" + this.implied + "]";
    }
}

 