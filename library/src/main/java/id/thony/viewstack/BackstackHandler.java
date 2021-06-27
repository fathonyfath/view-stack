package id.thony.viewstack;

import androidx.annotation.NonNull;

public interface BackstackHandler {

    void handleBackstackChange(@NonNull Navigator navigator,
                               @NonNull Backstack oldStack,
                               @NonNull Backstack newStack,
                               @NonNull NavigationDirection direction);
}
