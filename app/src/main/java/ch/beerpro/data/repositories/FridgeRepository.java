package ch.beerpro.data.repositories;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ch.beerpro.MyApplication;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.models.FridgeBeer;
import ch.beerpro.domain.utils.FirestoreQueryLiveData;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;
import ch.beerpro.presentation.details.DetailsActivity;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class FridgeRepository {
    private static LiveData<List<FridgeBeer>> getFridgeBeersByUser(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(FridgeBeer.COLLECTION)
                .orderBy(FridgeBeer.FIELD_ADDED_AT, Query.Direction.DESCENDING).whereEqualTo(FridgeBeer.FIELD_USER_ID, userId),
                FridgeBeer.class);
    }
    private static LiveData<FridgeBeer> getUserFridgeListFor(Pair<String, Beer> input) {
        String userId = input.first;
        Beer beer = input.second;
        DocumentReference document = FirebaseFirestore.getInstance().collection(FridgeBeer.COLLECTION)
                .document(FridgeBeer.generateId(userId, beer.getId()));
        return new FirestoreQueryLiveData<>(document, FridgeBeer.class);
    }

    public Task<Void> toggleUserFridgelistItem(String userId, String itemId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String FridgeBeerId = FridgeBeer.generateId(userId, itemId);

        DocumentReference FridgeBeerEntryQuery = db.collection(FridgeBeer.COLLECTION).document(FridgeBeerId);

        return FridgeBeerEntryQuery.get().continueWithTask(task -> {
            /*if (task.isSuccessful() && task.getResult().exists()) {
                return FridgeBeerEntryQuery.delete();
            } else if (task.isSuccessful()) {
                return FridgeBeerEntryQuery.set(new FridgeBeer(userId, itemId, new Date()));
            } else {
                throw task.getException();
            }*/
            if (task.isSuccessful() && task.getResult().exists()) {
                //bier ist bereits im kuehlschrank
                Context context = MyApplication.getAppContext();
                Toast toast = Toast.makeText(context, "ist bereit im Kühlschrank", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("AARON", "Ist bereits im Kühlschrank");
                return null;
            } else if (task.isSuccessful()) {
                //bier zum kuehlschrank hinzufügen
                Context context = MyApplication.getAppContext();
                Toast toast = Toast.makeText(context, "zum Kühlschrank hinzugefügt", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("AARON", "Zum Kühlschrank hinzugefügt");
                return FridgeBeerEntryQuery.set(new FridgeBeer(userId, itemId, new Date()));
            } else {
                Log.d("AARON", "TaskException in FridgeRepository");
                throw task.getException();
            }
        });
    }

    public Task<Void> toggleFridgelistItemInMyBeers(String userId, String itemId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String FridgeBeerId = FridgeBeer.generateId(userId, itemId);

        DocumentReference FridgeBeerEntryQuery = db.collection(FridgeBeer.COLLECTION).document(FridgeBeerId);

        return FridgeBeerEntryQuery.get().continueWithTask(task -> {
            Log.d("AARON", task.toString());
            /*if (task.isSuccessful() && task.getResult().exists()) {
                return FridgeBeerEntryQuery.delete();
            } else if (task.isSuccessful()) {
                return FridgeBeerEntryQuery.set(new FridgeBeer(userId, itemId, new Date()));
            } else {
                throw task.getException();
            }*/
            if (task.isSuccessful() && task.getResult().exists()) {
                //bier ist bereits im kuehlschrank
                Context context = MyApplication.getAppContext();
                Toast toast = Toast.makeText(context, "ist bereit im Kühlschrank", Toast.LENGTH_SHORT);
                toast.show();
                return null;
            } else if (task.isSuccessful()) {
                //bier zum kuehlschrank hinzufügen
                Context context = MyApplication.getAppContext();
                Toast toast = Toast.makeText(context, "zum Kühlschrank hinzugefügt", Toast.LENGTH_SHORT);
                toast.show();
                return FridgeBeerEntryQuery.set(new FridgeBeer(userId, itemId, new Date()));
            } else {
                throw task.getException();
            }
        });
    }

    public Task<Void> toggleUserFridgelistItemWithDelete(String userId, String itemId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String FridgeBeerId = FridgeBeer.generateId(userId, itemId);

        DocumentReference FridgeBeerEntryQuery = db.collection(FridgeBeer.COLLECTION).document(FridgeBeerId);

        return FridgeBeerEntryQuery.get().continueWithTask(task -> {
            /*if (task.isSuccessful() && task.getResult().exists()) {

            } else if (task.isSuccessful()) {
                return FridgeBeerEntryQuery.set(new FridgeBeer(userId, itemId, new Date()));
            } else {
                throw task.getException();
            }*/
            if (task.isSuccessful() && task.getResult().exists()) {
                //bier ist bereits im kuehlschrank
                return FridgeBeerEntryQuery.delete();
            } else if (task.isSuccessful()) {
                //bier zum kuehlschrank hinzufügen
                /*Context context = MyApplication.getAppContext();
                Toast toast = Toast.makeText(context, "zum Kühlschrank hinzugefügt", Toast.LENGTH_SHORT);
                toast.show();*/
                return FridgeBeerEntryQuery.set(new FridgeBeer(userId, itemId, new Date()));
            } else {
                throw task.getException();
            }
        });
    }

    public LiveData<List<Pair<FridgeBeer, Beer>>> getMyFridgeWithBeers(LiveData<String> currentUserId,
                                                                   LiveData<List<Beer>> allBeers) {
        return map(combineLatest(getMyFridgelist(currentUserId), map(allBeers, Entity::entitiesById)), input -> {
            List<FridgeBeer> FridgeBeers = input.first;
            HashMap<String, Beer> beersById = input.second;

            ArrayList<Pair<FridgeBeer, Beer>> result = new ArrayList<>();
            for (FridgeBeer FridgeBeer : FridgeBeers) {
                Beer beer = beersById.get(FridgeBeer.getBeerId());
                result.add(Pair.create(FridgeBeer, beer));
            }
            return result;
        });
    }

    public LiveData<List<FridgeBeer>> getMyFridgelist(LiveData<String> currentUserId) {
        return switchMap(currentUserId, FridgeRepository::getFridgeBeersByUser);
    }


    public LiveData<FridgeBeer> getMyFridgeForBeer(LiveData<String> currentUserId, LiveData<Beer> beer) {
        return switchMap(combineLatest(currentUserId, beer), FridgeRepository::getUserFridgeListFor);
    }


}

