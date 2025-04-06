package com.example.fish.domain;

import java.sql.Timestamp;

public class FishingRecord {
    private int recordId;
    private int userId;
    private int fishId;
    private int locationId;
    private Timestamp catchTime;
    private boolean approved;
    private Timestamp createdAt;

    public FishingRecord(int recordId, int userId, int fishId, int locationId, Timestamp catchTime, boolean approved, Timestamp createdAt) {
        this.recordId = recordId;
        this.userId = userId;
        this.fishId = fishId;
        this.locationId = locationId;
        this.catchTime = catchTime;
        this.approved = approved;
        this.createdAt = createdAt;
    }

    public Timestamp getCatchTime() {
        return catchTime;
    }

    public void setCatchTime(Timestamp catchTime) {
        this.catchTime = catchTime;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFishId() {
        return fishId;
    }

    public void setFishId(int fishId) {
        this.fishId = fishId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
