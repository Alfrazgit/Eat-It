package com.example.restaurantapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar_main;
    CardView tSandwiches, tBurgers, tPasta, tPizza, tChinese, tHotDrinks, tColdDrinks, tExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar_main = findViewById(R.id.main_toolbar);
        toolbar_main.setTitle("Home");
        toolbar_main.inflateMenu(R.menu.main_menu);

        toolbar_main.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cart:
                Intent toCartActivity = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(toCartActivity);
                        break;
                    case R.id.fav:
                        Intent toFavActivity = new Intent(getApplicationContext(), FavouritesActivity.class);
                        startActivity(toFavActivity);
                    }

                return false;
            }
        });

        tSandwiches = findViewById(R.id.sandwiches);
        tSandwiches.setOnClickListener(this);
        tBurgers = findViewById(R.id.burgers);
        tBurgers.setOnClickListener(this);
        tPasta = findViewById(R.id.pasta);
        tPasta.setOnClickListener(this);
        tPizza = findViewById(R.id.pizza);
        tPizza.setOnClickListener(this);
        tChinese = findViewById(R.id.chinese);
        tChinese.setOnClickListener(this);
        tHotDrinks = findViewById(R.id.hot_drinks);
        tHotDrinks.setOnClickListener(this);
        tColdDrinks = findViewById(R.id.cold_drinks);
        tColdDrinks.setOnClickListener(this);
        tExtras = findViewById(R.id.extras);
        tExtras.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sandwiches:
                Intent sandwichIntent = new Intent(getApplicationContext(), CategoryActivity.class);
                sandwichIntent.putExtra("category", "Sandwich");
                startActivity(sandwichIntent);
                break;
            case R.id.burgers:
                Intent burgerIntent = new Intent(getApplicationContext(), CategoryActivity.class);
                burgerIntent.putExtra("category", "Burger");
                startActivity(burgerIntent);
                break;
            case R.id.pizza:
                Intent pizzaIntent = new Intent(getApplicationContext(), CategoryActivity.class);
                pizzaIntent.putExtra("category", "Pizza");
                startActivity(pizzaIntent);
                break;
            case R.id.pasta:
                Intent pastaIntent = new Intent(getApplicationContext(), CategoryActivity.class);
                pastaIntent.putExtra("category", "Pasta");
                startActivity(pastaIntent);
                break;
            case R.id.hot_drinks:
                Intent hotDrinksIntent = new Intent(getApplicationContext(), CategoryActivity.class);
                hotDrinksIntent.putExtra("category", "Hot Drinks");
                startActivity(hotDrinksIntent);
                break;
            case R.id.cold_drinks:
                Intent coldDrinksIntent = new Intent(getApplicationContext(), CategoryActivity.class);
                coldDrinksIntent.putExtra("category", "Cold Drinks");
                startActivity(coldDrinksIntent);
                break;
            case R.id.chinese:
                Intent chineseIntent = new Intent(getApplicationContext(), CategoryActivity.class);
                chineseIntent.putExtra("category", "Chinese");
                startActivity(chineseIntent);
                break;
            case R.id.extras:
                Intent ExtrasIntent = new Intent(getApplicationContext(), CategoryActivity.class);
                ExtrasIntent.putExtra("category", "Extras");
                startActivity(ExtrasIntent);
                break;
            default:
                Toast.makeText(this, "Activity not found", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}