package dev.fathony.viewstack;

import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultBackstackHandler implements BackstackHandler {

    @NotNull
    private final Context context;

    @NotNull
    private final ViewGroup container;

    protected DefaultBackstackHandler(@NotNull Context context, @NotNull ViewGroup container) {
        this.context = context;
        this.container = container;
    }

    @Override
    public void handleBackstackChange(@NotNull Navigator navigator,
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

    @Nullable
    protected Animation enterAnimation() {
        return null;
    }

    @Nullable
    protected Animation exitAnimation() {
        return null;
    }

    @Nullable
    protected Animation popEnterAnimation() {
        return null;
    }

    @Nullable
    protected Animation popExitAnimation() {
        return null;
    }

    protected final void handleInitializeNavigationCommand(@NotNull Backstack newStack) {
        this.container.removeAllViews();
        final ViewKey nextKey = newStack.peekKey();
        final View nextView = buildView(nextKey, newStack.obtainViewState(nextKey));
        this.container.addView(nextView);
        final Animation enterAnimation = enterAnimation();
        if (enterAnimation != null) nextView.startAnimation(enterAnimation);
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
                animateRemoveViewSideEffect(previousView, exitAnimation());
            } else {
                this.container.removeAllViews();
            }
        }

        final ViewKey upcomingKey = newStack.peekKey();
        final View upcomingView = buildView(upcomingKey, newStack.obtainViewState(upcomingKey));
        this.container.addView(upcomingView);
        final Animation enterAnimation = enterAnimation();
        if (enterAnimation != null) upcomingView.startAnimation(enterAnimation);
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
                animateRemoveViewSideEffect(previousView, popExitAnimation());
            } else {
                this.container.removeAllViews();
            }
        }

        final ViewKey upcomingKey = newStack.peekKey();
        final View upcomingView = buildView(upcomingKey, newStack.obtainViewState(upcomingKey));
        this.container.addView(upcomingView);
        final Animation popEnterAnimation = popEnterAnimation();
        if (popEnterAnimation != null) upcomingView.startAnimation(popEnterAnimation);
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

    private void animateRemoveViewSideEffect(@NotNull View view,
                                             @Nullable Animation exitAnimation) {
        if (exitAnimation != null) {
            exitAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    DefaultBackstackHandler.this.container.removeView(view);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            view.startAnimation(exitAnimation);
        } else {
            DefaultBackstackHandler.this.container.removeView(view);
        }
    }
}
