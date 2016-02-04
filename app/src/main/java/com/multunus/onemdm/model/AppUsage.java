package com.multunus.onemdm.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AppUsage extends RealmObject {
    @PrimaryKey
    private long id;
    private String packageName;
    private long appUsageDurationPerDayInSeconds;
    private String appUsedOn;

    private boolean synced;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getAppUsageDurationPerDayInSeconds() {
        return appUsageDurationPerDayInSeconds;
    }

    public void setAppUsageDurationPerDayInSeconds(long appUsageDurationPerDayInSeconds) {
        this.appUsageDurationPerDayInSeconds = appUsageDurationPerDayInSeconds;
    }

    public String getAppUsedOn() {
        return appUsedOn;
    }

    public void setAppUsedOn(String appUsedOn) {
        this.appUsedOn = appUsedOn;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

}
