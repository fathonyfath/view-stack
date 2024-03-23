package dev.fathony.viewstack;

import android.os.Bundle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Navigator {
    @NotNull
    private static final String BackstackKey = "BackstackKey";

    @NotNull
    private final BackstackHandler handler;

    @NotNull
    private Backstack backstack;

    private boolean willHandleNavigation = false;

    @NotNull
    private final List<WillHandleNavigationChangedListener> willHandleNavigationChangedListeners =
            new ArrayList<>();

    public Navigator(@NotNull BackstackHandler handler, @NotNull Backstack backstack) {
        this.handler = handler;
        this.backstack = backstack;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        final Backstack oldBackstack = this.backstack.clone();
        final NavigationCommand command = NavigationCommand.Initialize;

        if (savedInstanceState != null) {
            this.backstack = savedInstanceState.getParcelable(BackstackKey);
        }

        this.handler.handleBackstackChange(
                this, oldBackstack, this.backstack, command);

        updateWillHandleNavigation(false);
    }

    public void onSaveInstanceState(@NotNull Bundle outBundle) {
        outBundle.putParcelable(BackstackKey, this.backstack);
    }

    public void push(@NotNull ViewKey... viewKeys) {
        final Backstack oldBackstack = this.backstack.clone();
        for (ViewKey key : viewKeys) {
            this.backstack.pushKey(key);
        }

        this.handler.handleBackstackChange(
                this, oldBackstack, this.backstack, NavigationCommand.Push);

        updateWillHandleNavigation(true);
    }

    public boolean pop() {
        final Backstack oldBackstack = this.backstack.clone();
        final boolean isPopped = this.backstack.popKey();
        if (isPopped) {
            this.handler.handleBackstackChange(
                    this, oldBackstack, this.backstack, NavigationCommand.Pop);

            updateWillHandleNavigation(true);
            return true;
        }

        return false;
    }

    public void replace(@NotNull Backstack backstack) {
        final Backstack oldBackstack = this.backstack.clone();
        this.backstack = backstack;
        this.handler.handleBackstackChange(
                this, oldBackstack, this.backstack, NavigationCommand.Replace);

        updateWillHandleNavigation(true);
    }

    public boolean willHandleNavigation() {
        return this.backstack.count() > 1;
    }

    public void addWillHandleNavigationChangedListener(@NotNull WillHandleNavigationChangedListener listener) {
        this.willHandleNavigationChangedListeners.add(listener);
    }

    public void removeWillHandleNavigationChangedListener(@NotNull WillHandleNavigationChangedListener listener) {
        this.willHandleNavigationChangedListeners.remove(listener);
    }

    private void updateWillHandleNavigation(boolean notifyListeners) {
        boolean previousValue = this.willHandleNavigation;
        this.willHandleNavigation = willHandleNavigation();

        if (notifyListeners && previousValue != this.willHandleNavigation) {
            notifyWillHandleNavigationChangedListeners(this.willHandleNavigation);
        }
    }

    private void notifyWillHandleNavigationChangedListeners(boolean willHandleNavigation) {
        List<WillHandleNavigationChangedListener> copy = new ArrayList<>(this.willHandleNavigationChangedListeners);
        for (WillHandleNavigationChangedListener listener : copy) {
            listener.onWillHandleNavigationChanged(willHandleNavigation);
        }
    }
}
