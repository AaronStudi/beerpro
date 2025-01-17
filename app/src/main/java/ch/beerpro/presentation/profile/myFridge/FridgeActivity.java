package ch.beerpro.presentation.profile.myFridge;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeBeer;
import ch.beerpro.presentation.details.DetailsActivity;

public class FridgeActivity extends AppCompatActivity implements OnFridgeItemInteractionListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyView)
    View emptyView;

    private FridgeViewModel model;
    private FridgeRecyclerViewAdapter adapter;
    private int amount_of_beers = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fridgelist);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Fridge");


        model = ViewModelProviders.of(this).get(FridgeViewModel.class);
        model.getMyFridgelistWithBeers().observe(this, this::updateFridge);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FridgeRecyclerViewAdapter(this);

        recyclerView.setAdapter(adapter);

    }

    private void updateFridge(List<Pair<FridgeBeer, Beer>> entries) {
        adapter.submitList(entries);
        if (entries.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMoreClickedListener(ImageView animationSource, Beer beer) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.ITEM_ID, beer.getId());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, animationSource, "image");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onFridgeClickedListener(Beer beer) {
        model.toggleItemInFridgelistWithDelete(beer.getId());
    }

    @Override
    public int onPlusClickedListener(Beer beer, int amount) {
        //int amount = getAmount(beer); //get Amount from FireBase
        amount++;
        //setAmount(amount, beer);          //set Amount to FireBase
        return amount;
    }

    @Override
    public int onMinusClickedListener(Beer beer, int amount) {
        //int amount = getAmount(beer);   //get Amount from FireBase
        if(amount >= 2) {
            amount--;
        }
        //setAmount(amount, beer);          //set Amount to FireBase
        return amount;
    }

/*    private int getAmount(Beer beer){                        //get Amount from FireBase
        LiveData<List<Pair<FridgeBeer, Beer>>> allFridgeBeers = model.getMyFridgelistWithBeers();
        allFridgeBeers.observe();
        return amount_of_beers;
    }

    private void setAmount(int amount, Beer beer){             //set Amount to FireBase
        amount_of_beers = amount;
    }*/


}
