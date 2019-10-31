package ch.beerpro.presentation.profile.myFridge;

import android.widget.ImageView;

import ch.beerpro.domain.models.Beer;

public interface OnFridgeItemInteractionListener {

    void onMoreClickedListener(ImageView photo, Beer beer);

    void onFridgeClickedListener(Beer beer);

    int onPlusClickedListener(Beer beer, int amount);

    int onMinusClickedListener(Beer beer, int amount);
}
