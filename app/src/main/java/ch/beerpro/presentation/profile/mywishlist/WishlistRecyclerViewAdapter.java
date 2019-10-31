package ch.beerpro.presentation.profile.mywishlist;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.GlideApp;
import ch.beerpro.R;
import ch.beerpro.data.repositories.FridgeRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeBeer;
import ch.beerpro.domain.models.MyBeerFromWhatever;
import ch.beerpro.domain.models.WhateverBeer;
import ch.beerpro.domain.models.Wish;
import ch.beerpro.presentation.utils.DrawableHelpers;
import ch.beerpro.presentation.utils.EntityPairDiffItemCallback;

import static ch.beerpro.presentation.utils.DrawableHelpers.setDrawableTint;

public class WishlistRecyclerViewAdapter extends ListAdapter<Pair<Wish, Beer>, WishlistRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "WishlistRecyclerViewAda";

    private static final DiffUtil.ItemCallback<Pair<Wish, Beer>> DIFF_CALLBACK = new EntityPairDiffItemCallback<>();

    private final OnWishlistItemInteractionListener listener;

    public WishlistRecyclerViewAdapter(OnWishlistItemInteractionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_my_wishlist_listentry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Pair<Wish, Beer> item = getItem(position);
        holder.bind(item.first, item.second, listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.manufacturer)
        TextView manufacturer;

        @BindView(R.id.category)
        TextView category;

        @BindView(R.id.photo)
        ImageView photo;

        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        @BindView(R.id.numRatings)
        TextView numRatings;

        @BindView(R.id.addedAt)
        TextView addedAt;

        @BindView(R.id.removeFromWishlist)
        Button remove;

        @BindView(R.id.removeFromFridge2)
        Button removeFromFridge;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(Wish wish, Beer item, OnWishlistItemInteractionListener listener) {
            name.setText(item.getName());
            manufacturer.setText(item.getManufacturer());
            category.setText(item.getCategory());
            name.setText(item.getName());
            GlideApp.with(itemView).load(item.getPhoto()).apply(new RequestOptions().override(240, 240).centerInside())
                    .into(photo);
            ratingBar.setNumStars(5);
            ratingBar.setRating(item.getAvgRating());
            numRatings.setText(itemView.getResources().getString(R.string.fmt_num_ratings, item.getNumRatings()));
            itemView.setOnClickListener(v -> listener.onMoreClickedListener(photo, item));

            String formattedDate =
                    DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(wish.getAddedAt());
            addedAt.setText(formattedDate);
            remove.setOnClickListener(v -> listener.onWishClickedListener(item));
            removeFromFridge.setOnClickListener(v -> {
                listener.onFridgeClickedListener(item);
                //toggleButtonView(null, removeFromFridge);

            });

            /*String id = wish.getBeerId();
            if()
            if(((MyBeerFromWhatever)entry).isInFridge()){
                DrawableHelpers
                        .setDrawableTint(removeFromFridge, itemView.getResources().getColor(R.color.colorPrimary));
                //onTheListSince.setText("im Kühlschrank seit");
            }
            else{
                DrawableHelpers
                        .setDrawableTint(removeFromFridge, itemView.getResources().getColor(android.R.color.darker_gray));
            }*/
        }

        private void toggleButtonView(WhateverBeer beer, Button button) {
            DrawableHelpers
                    .setDrawableTint(button, itemView.getResources().getColor(R.color.colorPrimary));
            button.setText("Entfernen");
        }

    }
}
