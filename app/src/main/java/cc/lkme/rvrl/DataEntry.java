package cc.lkme.rvrl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LinkedME06 on 16/11/10.
 */

public class DataEntry implements Parcelable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public DataEntry() {
    }

    protected DataEntry(Parcel in) {
        this.name = in.readString();
    }

    public static final Creator<DataEntry> CREATOR = new Creator<DataEntry>() {
        @Override
        public DataEntry createFromParcel(Parcel source) {
            return new DataEntry(source);
        }

        @Override
        public DataEntry[] newArray(int size) {
            return new DataEntry[size];
        }
    };
}
