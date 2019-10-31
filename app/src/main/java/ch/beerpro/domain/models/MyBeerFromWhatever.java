package ch.beerpro.domain.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class MyBeerFromWhatever implements MyBeer{
    private boolean isInFridge = false;
    private boolean isInWishlist = false;
    private boolean isInRatings = false;

    public boolean isInFridge(){return isInFridge;}
    public boolean isInWishlist(){return isInWishlist;}
    public boolean isInRatings(){return isInRatings;}

    public void isInFridge(boolean x){isInFridge = x;}
    public void isInWishlist(boolean x){isInWishlist = x;}
    public void isInRatings(boolean x){isInRatings = x;}

    private WhateverBeer whateverBeer;
    private Beer beer;

    public MyBeerFromWhatever(WhateverBeer whateverBeer, boolean inFridge, boolean inWishlist, Beer beer) {
        this.whateverBeer = whateverBeer;
        if(whateverBeer instanceof Wish || inWishlist) isInWishlist(true);
        if(whateverBeer instanceof FridgeBeer || inFridge) isInFridge(true);
        this.beer = beer;
    }

    /*public MyBeerFromWhatever(FridgeBeer fridgeBeer, Beer beer){
        isInFridge(true);
        this.whateverBeer = (WhateverBeer) fridgeBeer;

    }*/

    @Override
    public String getBeerId() {
        return whateverBeer.getBeerId();
    }

    @Override
    public Date getDate() {
        return whateverBeer.getAddedAt();
    }

    public WhateverBeer getWhateverBeer() {
        return this.whateverBeer;
    }

    public Beer getBeer() {
        return this.beer;
    }

    public void setWhateverBeer(WhateverBeer whateverBeer) {
        this.whateverBeer = whateverBeer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MyBeerFromWhatever)) return false;
        final MyBeerFromWhatever other = (MyBeerFromWhatever) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$whateverBeer = this.getWhateverBeer();
        final Object other$whateverBeer = other.getWhateverBeer();
        if (this$whateverBeer == null ? other$whateverBeer != null : !this$whateverBeer.equals(other$whateverBeer)) return false;
        final Object this$beer = this.getBeer();
        final Object other$beer = other.getBeer();
        return this$beer == null ? other$beer == null : this$beer.equals(other$beer);
    }

    private boolean canEqual(final Object other) {
        return other instanceof MyBeerFromWhatever;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $whateverBeer = this.getWhateverBeer();
        result = result * PRIME + ($whateverBeer == null ? 43 : $whateverBeer.hashCode());
        final Object $beer = this.getBeer();
        result = result * PRIME + ($beer == null ? 43 : $beer.hashCode());
        return result;
    }

    @NonNull
    public String toString() {
        return "MyBeerFromWhatever(whateverBeer=" + this.getWhateverBeer() + ", beer=" + this.getBeer() + ")";
    }
}
