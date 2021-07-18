package id.thony.viewstack;

import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

public class DefaultBackstackHandler implements BackstackHandler {

    @NotNull
    private final Context context;
    @NotNull
    private final ViewGroup container;

    public DefaultBackstackHandler(@NotNull Context context, @NotNull ViewGroup container) {
        this.context = context;
        this.container = container;
    }

    @Override
    public void handleBackstackChange(@NotNull Navigator navigator,
                                      @NotNull Backstack oldStack,
                                      @NotNull Backstack newStack,
                                      @NotNull NavigationDirection direction) {
        if (direction == NavigationDirection.Replace) {
            this.container.removeAllViews();

            final ViewKey upcomingKey = newStack.peekKey();
            final ViewState upcomingViewState = navigator.obtainViewState(upcomingKey);
            final ViewKeyContextWrapper context = new ViewKeyContextWrapper(this.context, upcomingKey);
            final View view = upcomingKey.buildView(context);
            view.restoreHierarchyState(upcomingViewState.getHierarchyState());

            container.addView(view);
            return;
        }

        if (oldStack.peekKey() != newStack.peekKey()) {
            final View oldView = this.container.getChildAt(0);
            if (oldView == null) {
                return;
            }

            if (direction == NavigationDirection.Forward) {
                final ViewKey currentKey = oldStack.peekKey();
                final ViewState currentViewState = navigator.obtainViewState(currentKey);
                final SparseArray<Parcelable> hierarchyState = currentViewState.getHierarchyState();
                hierarchyState.clear();
                oldView.saveHierarchyState(hierarchyState);
            }

            final ViewKey upcomingKey = newStack.peekKey();
            final ViewState upcomingViewState = navigator.obtainViewState(upcomingKey);
            final ViewKeyContextWrapper context = new ViewKeyContextWrapper(this.context, upcomingKey);
            final View view = upcomingKey.buildView(context);
            view.restoreHierarchyState(upcomingViewState.getHierarchyState());

            container.removeViewAt(0);
            container.addView(view);
        }
    }
}
