package ch.beerpro.data.repositories;

import androidx.lifecycle.LiveData;

import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.models.FridgeBeer;
import ch.beerpro.domain.models.MyBeer;
import ch.beerpro.domain.models.MyBeerFromFridge;
import ch.beerpro.domain.models.MyBeerFromRating;
//import ch.beerpro.domain.models.MyBeerFromWhatever;
import ch.beerpro.domain.models.MyBeerFromWhatever;
import ch.beerpro.domain.models.MyBeerFromWishlist;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.WhateverBeer;
import ch.beerpro.domain.models.Wish;

import static androidx.lifecycle.Transformations.map;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class MyBeersRepository {

    /*private static List<MyBeer> getMyBeers(Triple<List<Wish>, List<Rating>, HashMap<String, Beer>> input) {
        List<Wish> wishlist = input.getLeft();
        List<Rating> ratings = input.getMiddle();
        HashMap<String, Beer> beers = input.getRight();

        ArrayList<MyBeer> result = new ArrayList<>();
        Set<String> beersAlreadyOnTheList = new HashSet<>();
        for (Wish wish : wishlist) {
            String beerId = wish.getBeerId();
            result.add(new MyBeerFromWishlist(wish, beers.get(beerId)));
            beersAlreadyOnTheList.add(beerId);
        }

        for (Rating rating : ratings) {
            String beerId = rating.getBeerId();
            if (beersAlreadyOnTheList.contains(beerId)) {
                // if the beer is already on the wish list, don't add it again
            } else {
                result.add(new MyBeerFromRating(rating, beers.get(beerId)));
                // we also don't want to see a rated beer twice
                beersAlreadyOnTheList.add(beerId);
            }
        }
        Collections.sort(result, (r1, r2) -> r2.getDate().compareTo(r1.getDate()));
        return result;
    }*/

    private static List<MyBeer> getMyBeers(Triple<List<Wish>, List<FridgeBeer>, HashMap<String, Beer>> input) {
        List<Wish> wishlist = input.getLeft();
        List<FridgeBeer> fridgeBeers = input.getMiddle();
        HashMap<String, Beer> beers = input.getRight();

        ArrayList<MyBeer> result = new ArrayList<>();
        Map<String, MyBeer> beersAlreadyOnTheList = new HashMap<>();
        for (WhateverBeer wish : wishlist) {
            String beerId = wish.getBeerId();
            MyBeerFromWhatever x = new MyBeerFromWhatever(wish, false, true, beers.get(beerId));
            result.add(x);
            beersAlreadyOnTheList.put(beerId, x);
        }

        for (WhateverBeer fridgeBeer : fridgeBeers) {
            String beerId = fridgeBeer.getBeerId();
            if (beersAlreadyOnTheList.containsKey(beerId)) {
                // if the beer is already on the wish list, don't add it again
                result.remove(beersAlreadyOnTheList.get(beerId));
                result.add(new MyBeerFromWhatever(fridgeBeer, true, true, beers.get(beerId)));
            } else {
                MyBeerFromWhatever x = new MyBeerFromWhatever(fridgeBeer, true, false, beers.get(beerId));
                result.add(x);
                // we also don't want to see a fridged beer twice
                beersAlreadyOnTheList.put(beerId, x);
            }
        }
        Collections.sort(result, (r1, r2) -> r2.getDate().compareTo(r1.getDate()));
        return result;
    }

    public LiveData<List<MyBeer>> getMyBeers(LiveData<List<Beer>> allBeers, LiveData<List<Wish>> myWishlist,
                                             LiveData<List<FridgeBeer>> myFridge) {
        return map(combineLatest(myWishlist, myFridge, map(allBeers, Entity::entitiesById)),
                MyBeersRepository::getMyBeers);
    }

    /*public LiveData<List<MyBeer>> getMyBeers(LiveData<List<Beer>> allBeers, LiveData<List<Wish>> myWishlist,
                                             LiveData<List<Rating>> myRatings, LiveData<List<FridgeBeer>> myFridgeBeers) {
        return map(combineLatest(myWishlist, myRatings, map(allBeers, Entity::entitiesById)),
                MyBeersRepository::getMyBeers);
    }*/
}
