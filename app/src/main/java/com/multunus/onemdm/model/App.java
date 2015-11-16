package com.multunus.onemdm.model;

import android.os.Parcel;
import android.os.Parcelable;

public class App implements Parcelable{
    private long id;
    private String packageName;
    private String name;
    private String apkUrl;

    public App(Parcel source) {
        id = source.readLong();
        name = source.readString();
        packageName = source.readString();
        apkUrl = source.readString();
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkURL) {
        this.apkUrl = apkURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(packageName);
        dest.writeString(apkUrl);
    }

    public static final Parcelable.Creator<App> CREATOR = new Parcelable.Creator<App>() {

        @Override
        public App createFromParcel(Parcel source) {
            return new App(source);
        }

        @Override
        public App[] newArray(int size) {
            return new App[size];
        }
    };
}
