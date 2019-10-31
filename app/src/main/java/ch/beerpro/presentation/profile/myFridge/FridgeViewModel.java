package ch.beerpro.presentation.profile.myFridge;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import java.util.List;

import ch.beerpro.data.repositories.BeersRepository;
import ch.beerpro.data.repositories.CurrentUser;
import ch.beerpro.data.repositories.FridgeRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeBeer;

public class FridgeViewModel extends ViewModel implements CurrentUser {
    private static final String TAG = "FridgelistViewModel";

    private final MutableLiveData<String> currentUserId = new MutableLiveData<>();
    private final FridgeRepository fridgeRepository;
    private final BeersRepository beersRepository;

    public FridgeViewModel() {
        fridgeRepository = new FridgeRepository();
        beersRepository = new BeersRepository();

        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<List<Pair<FridgeBeer, Beer>>> getMyFridgelistWithBeers() {
        return fridgeRepository.getMyFridgeWithBeers(currentUserId, beersRepository.getAllBeers());
    }

    /*public Task<Void> toggleItemInFridgelist(String itemId) {
        return fridgeRepository.toggleUserFridgelistItem(getCurrentUser().getUid(), itemId);
    }*/

    public boolean toggleItemInFridgelistWithDelete(String itemId) {
        return fridgeRepository.toggleUserFridgelistItemWithDelete(getCurrentUser().getUid(), itemId);
    }
}