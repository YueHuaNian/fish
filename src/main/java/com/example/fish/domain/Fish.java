package com.example.fish.domain;
public class Fish {
    private int fishId;
    private String fishName;

    public Fish(int fishId, String fishName) {
        this.fishId = fishId;
        this.fishName = fishName;
    }

    public String getFishName() {
        return fishName;
    }

    public void setFishName(String fishName) {
        this.fishName = fishName;
    }

    public int getFishId() {
        return fishId;
    }

    public void setFishId(int fishId) {
        this.fishId = fishId;
    }

}
