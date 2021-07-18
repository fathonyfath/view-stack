package id.thony.viewstack;

import android.os.Bundle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Navigator {
    @NotNull
    private static final String BackstackKey = "BackstackKey";

    @NotNull
    private final BackstackHandler handler;

    @NotNull
    private Backstack backstack;

    public Navigator(@NotNull BackstackHandler handler, @NotNull Backstack backstack) {
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

    public void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        this.backstack = savedInstanceState.getParcelable(BackstackKey);
    }

    public void onSaveInstanceState(@NotNull Bundle outBundle) {
        outBundle.putParcelable(BackstackKey, this.backstack);
    }

    public boolean onBackPressed() {
        return this.pop();
    }

    public void onDestroy() {
    }

    public void push(@NotNull ViewKey... viewKeys) {
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

    public void replace(@NotNull Backstack backstack) {
        final Backstack oldBackstack = this.backstack.clone();
        this.backstack = backstack;
        this.handler.handleBackstackChange(
                this, oldBackstack, this.backstack, NavigationDirection.Replace);
    }

    @NotNull
    protected ViewState obtainViewState(@NotNull ViewKey viewKey) {
        return this.backstack.obtainViewState(viewKey);
    }
}
