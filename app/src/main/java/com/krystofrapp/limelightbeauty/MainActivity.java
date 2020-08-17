package com.krystofrapp.limelightbeauty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    LinearLayoutManager layoutManager, layoutManager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_items);
       // recyclerView.setHasFixedSize(true);
        RecyclerView recyclerView2 = findViewById(R.id.recycler_view_items2);
        //recyclerView2.setHasFixedSize(true);
        ArrayList<ModelTopOrdered> itemsList = new ArrayList<>();


        //List of images for horizontal scroll view.
        itemsList.add(new ModelTopOrdered(R.drawable.bg0, "Fashion Tips"));
        itemsList.add(new ModelTopOrdered(R.drawable.bg1, "Facial Treatment"));
        itemsList.add(new ModelTopOrdered(R.drawable.bg2, "Makeup"));
        itemsList.add(new ModelTopOrdered(R.drawable.bg3, "Makeup Session"));
        itemsList.add(new ModelTopOrdered(R.drawable.bg4, "Personal Facials"));
        itemsList.add(new ModelTopOrdered(R.drawable.bg5, "Epic Nails"));
        itemsList.add(new ModelTopOrdered(R.drawable.bg7, "Skin Care"));
        itemsList.add(new ModelTopOrdered(R.drawable.bg8, "Hair Braiding"));

        ItemsRecyclerViewAdapter adapter2 = new ItemsRecyclerViewAdapter(this, itemsList);
        recyclerView2.setAdapter(adapter2);

        ItemsRecyclerViewAdapter adapter = new ItemsRecyclerViewAdapter(this, itemsList);
        recyclerView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(layoutManager2);
    }

    private OnNavigationItemSelectedListener navListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        return true;
                    case R.id.navigation_services:
                        startActivity(new Intent(MainActivity.this, Services.class));
                        return true;
                    case R.id.navigation_cart:
                        startActivity(new Intent(MainActivity.this, CartFragment.class));
                        return true;
                    case R.id.navigation_account:
                        startActivity(new Intent(MainActivity.this, AccountFragment.class));
                        return true;
                    default:
                        break;
                }
                return false;
            };
}