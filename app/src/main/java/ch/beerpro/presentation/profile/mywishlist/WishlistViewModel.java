package ch.beerpro.presentation.profile.mywishlist;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import java.util.List;

import ch.beerpro.data.repositories.BeersRepository;
import ch.beerpro.data.repositories.CurrentUser;
import ch.beerpro.data.repositories.FridgeRepository;
import ch.beerpro.data.repositories.WishlistRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Wish;

public class WishlistViewModel extends ViewModel implements CurrentUser {

    private static final String TAG = "WishlistViewModel";

    private final MutableLiveData<String> currentUserId = new MutableLiveData<>();
    private final WishlistRepository wishlistRepository;
    private final FridgeRepository fridgeRepository;
    private final BeersRepository beersRepository;

    public WishlistViewModel() {
        wishlistRepository = new WishlistRepository();
        fridgeRepository = new FridgeRepository();
        beersRepository = new BeersRepository();

        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<List<Pair<Wish, Beer>>> getMyWishlistWithBeers() {
        return wishlistRepository.getMyWishlistWithBeers(currentUserId, beersRepository.getAllBeers());
    }

    public Task<Void> toggleItemInWishlist(String itemId) {
        return wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), itemId);
    }

    public boolean toggleItemInFridgelistWithDelete(String itemId) {
        return fridgeRepository.toggleUserFridgelistItemWithDelete(getCurrentUser().getUid(), itemId);
    }

    public Task<Void> toggleItemInFridgelist(String itemId){
        return fridgeRepository.toggleUserFridgelistItem(getCurrentUser().getUid(), itemId);
    }

    /*public boolean beerIsInFridge(String itemId){
        return fridgeRepository.beerIsInFridge(getCurrentUser().getUid(), itemId);
    }*/

}