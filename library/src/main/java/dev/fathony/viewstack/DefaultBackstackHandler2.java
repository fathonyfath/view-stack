package dev.fathony.viewstack;


import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import org.jetbrains.annotations.NotNull;

public abstract class DefaultBackstackHandler2 implements BackstackHandler {

    @NotNull
    private final Context context;

    @NotNull
    private final ViewGroup container;

    protected DefaultBackstackHandler2(@NotNull Context context, @NotNull ViewGroup container) {
        this.context = context;
        this.container = container;
    }

    @Override
    public final void handleBackstackChange(@NotNull Navigator navigator,
                                            @NotNull Backstack oldStack,
                                            @NotNull Backstack newStack,
                                            @NotNull NavigationCommand command) {
        switch (command) {
            case Initialize:
                handleInitializeNavigationCommand(newStack);
                break;
            case Push:
            case Replace:
                handlePushNavigationCommand(oldStack, newStack);
                break;
            case Pop:
                handlePopNavigationCommand(oldStack, newStack);
                break;
        }
    }

    @NotNull
    protected abstract Animation enterAnimation();

    @NotNull
    protected abstract Animation exitAnimation();

    @NotNull
    protected abstract Animation popEnterAnimation();

    @NotNull
    protected abstract Animation popExitAnimation();

    protected final void handleInitializeNavigationCommand(@NotNull Backstack newStack) {
        this.container.removeAllViews();
        final ViewKey nextKey = newStack.peekKey();
        final View nextView = buildView(nextKey, newStack.obtainViewState(nextKey));
        this.container.addView(nextView);
        nextView.startAnimation(enterAnimation());
    }

    protected final void handlePushNavigationCommand(
            @NotNull Backstack oldStack,
            @NotNull Backstack newStack
    ) {
        if (oldStack.peekKey() == newStack.peekKey()) return;

        final ViewKey previousKey = oldStack.peekKey();
        final View previousView = this.container.findViewById(previousKey.hashCode());
        if (previousView != null) {
            saveViewState(previousView, oldStack.obtainViewState(getViewKey(previousView)));

            if (this.container.getChildCount() == 1) {
                final Animation exitAnimation = removeViewSideEffectAnimation(previousView, exitAnimation());
                previousView.startAnimation(exitAnimation);
            } else {
                this.container.removeAllViews();
            }
        }

        final ViewKey upcomingKey = newStack.peekKey();
        final View upcomingView = buildView(upcomingKey, newStack.obtainViewState(upcomingKey));
        this.container.addView(upcomingView);
        upcomingView.startAnimation(enterAnimation());
    }

    protected final void handlePopNavigationCommand(
            @NotNull Backstack oldStack,
            @NotNull Backstack newStack
    ) {
        if (oldStack.peekKey() == newStack.peekKey()) return;

        final ViewKey previousKey = oldStack.peekKey();
        final View previousView = this.container.findViewById(previousKey.hashCode());
        if (previousView != null) {
            if (this.container.getChildCount() == 1) {
                final Animation exitAnimation = removeViewSideEffectAnimation(previousView, popExitAnimation());
                previousView.startAnimation(exitAnimation);
            } else {
                this.container.removeAllViews();
            }
        }

        final ViewKey upcomingKey = newStack.peekKey();
        final View upcomingView = buildView(upcomingKey, newStack.obtainViewState(upcomingKey));
        this.container.addView(upcomingView);
        upcomingView.startAnimation(popEnterAnimation());
    }

    protected final View buildView(@NotNull ViewKey viewKey, @NotNull ViewState viewState) {
        final ViewKeyContextWrapper context = new ViewKeyContextWrapper(this.context, viewKey);
        final View view = viewKey.buildView(context);
        view.restoreHierarchyState(viewState.getHierarchyState());
        view.setId(viewKey.hashCode());
        return view;
    }

    protected final void saveViewState(@NotNull View view, @NotNull ViewState viewState) {
        final SparseArray<Parcelable> hierarchyState = viewState.getHierarchyState();
        hierarchyState.clear();
        view.saveHierarchyState(hierarchyState);
    }

    protected final ViewKey getViewKey(@NotNull View view) {
        return ViewKeyContextWrapper.getViewKey(view.getContext());
    }

    private Animation removeViewSideEffectAnimation(@NotNull View view,
                                                    @NotNull Animation animation) {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                DefaultBackstackHandler2.this.container.removeView(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }
}
