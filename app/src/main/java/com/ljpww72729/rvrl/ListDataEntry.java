package com.ljpww72729.rvrl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LinkedME06 on 16/11/10.
 */

public class ListDataEntry implements Parcelable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    private String img_url;

    public ListDataEntry() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.img_url);
    }

    protected ListDataEntry(Parcel in) {
        this.name = in.readString();
        this.img_url = in.readString();
    }

    public static final Creator<ListDataEntry> CREATOR = new Creator<ListDataEntry>() {
        @Override
        public ListDataEntry createFromParcel(Parcel source) {
            return new ListDataEntry(source);
        }

        @Override
        public ListDataEntry[] newArray(int size) {
            return new ListDataEntry[size];
        }
    };
}
