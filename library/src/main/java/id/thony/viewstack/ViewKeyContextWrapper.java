package id.thony.viewstack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ViewKeyContextWrapper extends ContextWrapper {
    private static final String ViewKey = "ViewKey";

    @Nullable
    private LayoutInflater layoutInflater;

    @NonNull
    private final ViewKey viewKey;

    public ViewKeyContextWrapper(@NonNull Context base, @NonNull ViewKey viewKey) {
        super(base);
        this.viewKey = viewKey;
    }

    @Override
    public Object getSystemService(String name) {
        if (name.equals(Context.LAYOUT_INFLATER_SERVICE)) {
            if (this.layoutInflater == null) {
                this.layoutInflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return this.layoutInflater;
        }
        if (name.equals(ViewKey)) {
            return viewKey;
        }
        return super.getSystemService(name);
    }

    @SuppressLint("WrongConstant")
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T extends ViewKey> T getViewKey(@NonNull Context context) {
        return (T) context.getSystemService(ViewKey);
    }
}
