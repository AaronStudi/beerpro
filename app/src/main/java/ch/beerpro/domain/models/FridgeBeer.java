package ch.beerpro.domain.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class FridgeBeer extends WhateverBeer implements Entity{

    public static final String COLLECTION = "FridgeBeers";
    public static final String FIELD_ID = "id";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_BEER_ID = "beerId";
    public static final String FIELD_ADDED_AT = "addedAt";

    /**
     * The id is formed by `$userId_$beerId` to make queries easier.
     */
    @Exclude
    private String id;
    private String userId;
    private String beerId;
    private Date addedAt;
    private int amount;

    public FridgeBeer(String userId, String beerId, Date addedAt, int amount) {
        this.userId = userId;
        this.beerId = beerId;
        this.addedAt = addedAt;
        this.amount = amount;
    }

    public FridgeBeer() {
    }

    public static String generateId(String userId, String beerId) {
        return String.format("%s_%s", userId, beerId);
    }

    public String getId() {
        return this.id;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getBeerId() {
        return this.beerId;
    }

    public Date getAddedAt() {
        return this.addedAt;
    }

    public int getAmount(){ return this.amount; }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setBeerId(String beerId) {
        this.beerId = beerId;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public void setAmount(int amount){ this.amount = amount; }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof FridgeBeer)) return false;
        final FridgeBeer other = (FridgeBeer) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$userId = this.getUserId();
        final Object other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId))
            return false;
        final Object this$beerId = this.getBeerId();
        final Object other$beerId = other.getBeerId();
        if (this$beerId == null ? other$beerId != null : !this$beerId.equals(other$beerId))
            return false;
        final Object this$addedAt = this.getAddedAt();
        final Object other$addedAt = other.getAddedAt();
        return this$addedAt == null ? other$addedAt == null : this$addedAt.equals(other$addedAt);
    }

    private boolean canEqual(final Object other) {
        return other instanceof FridgeBeer;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $userId = this.getUserId();
        result = result * PRIME + ($userId == null ? 43 : $userId.hashCode());
        final Object $beerId = this.getBeerId();
        result = result * PRIME + ($beerId == null ? 43 : $beerId.hashCode());
        final Object $addedAt = this.getAddedAt();
        result = result * PRIME + ($addedAt == null ? 43 : $addedAt.hashCode());
        return result;
    }

    @NonNull
    public String toString() {
        return "FridgeBeer(id=" + this.getId() + ", amount=" + this.amount + ", userId=" + this.getUserId() + ", beerId=" + this.getBeerId() + ", addedAt=" + this.getAddedAt() + ")";
    }
}
