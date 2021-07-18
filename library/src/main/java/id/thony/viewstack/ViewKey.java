package id.thony.viewstack;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.NonNull;

public abstract class ViewKey implements Parcelable {

    @NonNull
    public abstract View buildView(@NonNull Context context);
}
