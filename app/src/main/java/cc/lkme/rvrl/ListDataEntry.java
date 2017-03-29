package cc.lkme.rvrl;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public ListDataEntry() {
    }

    protected ListDataEntry(Parcel in) {
        this.name = in.readString();
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
