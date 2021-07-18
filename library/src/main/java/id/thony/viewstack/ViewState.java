package id.thony.viewstack;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import androidx.annotation.NonNull;

public final class ViewState implements Parcelable {
    public static final Creator<ViewState> CREATOR = new Creator<ViewState>() {
        @Override
        public ViewState createFromParcel(Parcel in) {
            return new ViewState(in);
        }

        @Override
        public ViewState[] newArray(int size) {
            return new ViewState[size];
        }
    };

    @NonNull
    private final SparseArray<Parcelable> hierarchyState;

    protected ViewState() {
        this.hierarchyState = new SparseArray<>();
    }

    protected ViewState(Parcel in) {
        this.hierarchyState = in.readSparseArray(getClass().getClassLoader());
    }

    @NonNull
    public SparseArray<Parcelable> getHierarchyState() {
        return hierarchyState;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSparseArray(this.hierarchyState);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
