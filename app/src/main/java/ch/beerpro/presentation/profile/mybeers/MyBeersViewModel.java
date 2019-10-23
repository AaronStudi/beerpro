package ch.beerpro.presentation.profile.mybeers;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.beerpro.data.repositories.BeersRepository;
import ch.beerpro.data.repositories.CurrentUser;
import ch.beerpro.data.repositories.FridgeRepository;
import ch.beerpro.data.repositories.MyBeersRepository;
import ch.beerpro.data.repositories.RatingsRepository;
import ch.beerpro.data.repositories.WishlistRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeBeer;
import ch.beerpro.domain.models.MyBeer;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;

import static androidx.lifecycle.Transformations.map;
import static ch.beerpro.domain.utils.LiveDataExtensions.zip;

public class MyBeersViewModel extends ViewModel implements CurrentUser {

    private static final String TAG = "MyBeersViewModel";
    private final MutableLiveData<String> searchTerm = new MutableLiveData<>();
    private final FridgeRepository fridgeRepository;
    private final WishlistRepository wishlistRepository;
    private final LiveData<List<MyBeer>> myFilteredBeers;

    public MyBeersViewModel() {

        wishlistRepository = new WishlistRepository();
        fridgeRepository = new FridgeRepository();
        BeersRepository beersRepository = new BeersRepository();
        MyBeersRepository myBeersRepository = new MyBeersRepository();
        RatingsRepository ratingsRepository = new RatingsRepository();

        LiveData<List<Beer>> allBeers = beersRepository.getAllBeers();
        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        LiveData<List<Wish>> myWishlist = wishlistRepository.getMyWishlist(currentUserId);
        LiveData<List<FridgeBeer>> myFridgeBeers = fridgeRepository.getMyFridgelist(currentUserId);
        LiveData<List<Rating>> myRatings = ratingsRepository.getMyRatings(currentUserId);

        LiveData<List<MyBeer>> myBeers = myBeersRepository.getMyBeers(allBeers, myWishlist, myFridgeBeers);
        //LiveData<List<MyBeer>> myBeers = myBeersRepository.getMyBeers(myWishlist, myFridgeBeers, myRatings, allBeers);

        myFilteredBeers = map(zip(searchTerm, myBeers), MyBeersViewModel::filter);

        currentUserId.setValue(getCurrentUser().getUid());
    }

    private static List<MyBeer> filter(Pair<String, List<MyBeer>> input) {
        String searchTerm1 = input.first;
        List<MyBeer> myBeers = input.second;
        if (Strings.isNullOrEmpty(searchTerm1)) {
            return myBeers;
        }
        if (myBeers == null) {
            return Collections.emptyList();
        }
        ArrayList<MyBeer> filtered = new ArrayList<>();
        for (MyBeer beer : myBeers) {
            if (beer.getBeer().getName().toLowerCase().contains(searchTerm1.toLowerCase())) {
                filtered.add(beer);
            }
        }
        return filtered;
    }

    public LiveData<List<MyBeer>> getMyFilteredBeers() {
        return myFilteredBeers;
    }

    public void toggleItemInWishlist(String beerId) {
        wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), beerId);
    }

    public Task<Void> toggleItemInFridgelistWithDelete(String itemId) {
        return fridgeRepository.toggleUserFridgelistItemWithDelete(getCurrentUser().getUid(), itemId);
    }

    public void toggleWishlistItemInMyBeers(String beerId) {
        //wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), beerId);
        wishlistRepository.toggleWishlistItemInMyBeers(getCurrentUser().getUid(), beerId);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String wishId = Wish.generateId(getCurrentUser().getUid(), beerId);
        DocumentReference wishEntryQuery = db.collection(Wish.COLLECTION).document(wishId);
        wishEntryQuery.get().continueWithTask(task -> {
            if(!(task.isSuccessful() && task.getResult().exists())){ //nicht in wunschliste
                String FridgeBeerId = FridgeBeer.generateId(getCurrentUser().getUid(), beerId);
                DocumentReference FridgeBeerEntryQuery = db.collection(FridgeBeer.COLLECTION).document(FridgeBeerId);
                FridgeBeerEntryQuery.get().continueWithTask(task2 -> {
                    if(!(task2.isSuccessful() && task2.getResult().exists())){//auch nicht in Kuehlschrank
                        Log.d("AARON", "Nicht mehr in Fridge und Wish!!");
                    }
                    return null;
                });
            }
            return null;
        });
    }

    public void toggleFridgelistItemInMyBeers(String itemId) {
        //return fridgeRepository.toggleUserFridgelistItemWithDelete(getCurrentUser().getUid(), itemId);
        fridgeRepository.toggleUserFridgelistItemWithDelete(getCurrentUser().getUid(), itemId);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String wishId = Wish.generateId(getCurrentUser().getUid(), itemId);
        DocumentReference wishEntryQuery = db.collection(Wish.COLLECTION).document(wishId);
        wishEntryQuery.get().continueWithTask(task -> {
            if(!(task.isSuccessful() && task.getResult().exists())){ //nicht in wunschliste
                String FridgeBeerId = FridgeBeer.generateId(getCurrentUser().getUid(), itemId);
                DocumentReference FridgeBeerEntryQuery = db.collection(FridgeBeer.COLLECTION).document(FridgeBeerId);
                FridgeBeerEntryQuery.get().continueWithTask(task2 -> {
                    if(!(task2.isSuccessful() && task2.getResult().exists())){//auch nicht in Kuehlschrank
                        Log.d("AARON", "Nicht mehr in Fridge und Wish!!");
                    }
                    return null;
                });
            }
            return null;
        });
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm.setValue(searchTerm);
    }
}