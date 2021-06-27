package id.thony.viewstack;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Navigator {
    @NonNull
    private static final String BackstackKey = "BackstackKey";

    @NonNull
    private final BackstackHandler handler;

    @NonNull
    private Backstack backstack;

    public Navigator(@NonNull BackstackHandler handler, @NonNull Backstack backstack) {
        this.handler = handler;
        this.backstack = backstack;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        final Backstack oldBackstack = this.backstack.clone();

        if (savedInstanceState != null) {
            this.backstack = savedInstanceState.getParcelable(BackstackKey);
        }

        this.handler.handleBackstackChange(
                this, oldBackstack, this.backstack, NavigationDirection.Replace);
    }

    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        this.backstack = savedInstanceState.getParcelable(BackstackKey);
    }

    public void onSaveInstanceState(@NonNull Bundle outBundle) {
        outBundle.putParcelable(BackstackKey, this.backstack);
    }

    public boolean onBackPressed() {
        return this.pop();
    }

    public void onDestroy() {
    }

    public void push(@NonNull ViewKey... viewKeys) {
        final Backstack oldBackstack = this.backstack.clone();
        for (ViewKey key : viewKeys) {
            this.backstack.pushKey(key);
        }

        this.handler.handleBackstackChange(
                this, oldBackstack, this.backstack, NavigationDirection.Forward);
    }

    public boolean pop() {
        final Backstack oldBackstack = this.backstack.clone();
        final boolean isPopped = this.backstack.popKey();
        if (isPopped) {
            this.handler.handleBackstackChange(
                    this, oldBackstack, this.backstack, NavigationDirection.Backward);
            return true;
        }

        return false;
    }

    public void replace(@NonNull Backstack backstack) {
        final Backstack oldBackstack = this.backstack.clone();
        this.backstack = backstack;
        this.handler.handleBackstackChange(
                this, oldBackstack, this.backstack, NavigationDirection.Replace);
    }

    @NonNull
    protected ViewState obtainViewState(@NonNull ViewKey viewKey) {
        return this.backstack.obtainViewState(viewKey);
    }
}
