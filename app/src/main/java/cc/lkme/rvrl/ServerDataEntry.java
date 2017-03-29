package cc.lkme.rvrl;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by LinkedME06 on 24/03/2017.
 */

public class ServerDataEntry implements Parcelable {
    private int pageIndex;
    private int pageCount;
    private ArrayList<ListDataEntry> data;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public ArrayList<ListDataEntry> getData() {
        return data;
    }

    public void setData(ArrayList<ListDataEntry> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pageIndex);
        dest.writeInt(this.pageCount);
        dest.writeTypedList(this.data);
    }

    public ServerDataEntry() {
    }

    protected ServerDataEntry(Parcel in) {
        this.pageIndex = in.readInt();
        this.pageCount = in.readInt();
        this.data = in.createTypedArrayList(ListDataEntry.CREATOR);
    }

    public static final Creator<ServerDataEntry> CREATOR = new Creator<ServerDataEntry>() {
        @Override
        public ServerDataEntry createFromParcel(Parcel source) {
            return new ServerDataEntry(source);
        }

        @Override
        public ServerDataEntry[] newArray(int size) {
            return new ServerDataEntry[size];
        }
    };
}
