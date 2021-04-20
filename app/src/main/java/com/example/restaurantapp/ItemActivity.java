package com.example.restaurantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ItemActivity extends AppCompatActivity {

    String item_id, category, userId;
    DatabaseReference itemRef, userRef;
    ImageView imageView;
    TextView name, cost, tQuantity;
    Button order, addToFav, addToCart, decrease, increase;
    int quantity;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        mAuth = FirebaseAuth.getInstance();
        quantity = 0;
        imageView = findViewById(R.id.item_img);
        name = findViewById(R.id.name);
        cost = findViewById(R.id.cost);
        order = findViewById(R.id.order_btn);
        addToFav = findViewById(R.id.add_to_favourites_btn);
        addToCart = findViewById(R.id.add_to_cart_btn);
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");

        decrease = findViewById(R.id.decrease);
        increase = findViewById(R.id.increase);
        tQuantity = findViewById(R.id.tQuantity);

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 0) quantity -= 1;
                tQuantity.setText(String.valueOf(quantity));
            }
        });
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity += 1;
                tQuantity.setText(String.valueOf(quantity));
            }
        });

        item_id = getIntent().getExtras().get("visit_item_id").toString();
        category = getIntent().getExtras().get("category").toString();
        itemRef = FirebaseDatabase.getInstance().getReference(category);

        itemRef.child(item_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String itemName = snapshot.child("Name").getValue().toString();
                    String itemCost = snapshot.child("Cost").getValue().toString();
                    String itemPic = snapshot.child("Pic").getValue().toString();

                    Picasso.get().load(itemPic).into(imageView);
                    name.setText(itemName);
                    cost.setText(itemCost);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = CurrentUser.userId;
                String saveCurrentDate, saveCurrentTime;
                Calendar calForDate = Calendar.getInstance();

                SimpleDateFormat currentDate = new SimpleDateFormat("dd-mm-yy");
                saveCurrentDate = currentDate.format(calForDate.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
                saveCurrentTime = currentTime.format(calForDate.getTime());

                String path = "orders/" + saveCurrentDate + " " + saveCurrentTime + "/" + name.getText().toString();

                final HashMap<String, Object> orderMap = new HashMap<>();
                orderMap.put(path + "/pCategory", category);
                orderMap.put(path + "/pId", item_id);
                orderMap.put(path + "/pName", name.getText().toString());
                orderMap.put(path + "/pCost", cost.getText().toString());
                orderMap.put(path + "/pQuantity", tQuantity.getText().toString());

                if (Integer.parseInt(tQuantity.getText().toString()) > 0) {
                    userRef.child(s).updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Your Order has been placed, we will contact you soon", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }

            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = CurrentUser.userId;
                String path = "cart/" + name.getText().toString();

                final HashMap<String, Object> cartMap = new HashMap<>();
                cartMap.put(path + "/pCategory", category);
                cartMap.put(path + "/pId", item_id);
                cartMap.put(path + "/pName", name.getText().toString());
                cartMap.put(path + "/pCost", cost.getText().toString());
                cartMap.put(path + "/pQuantity", tQuantity.getText().toString());

                if (Integer.parseInt(tQuantity.getText().toString()) > 0) {
                    userRef.child(s).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ItemActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }

            }
        });

        addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = CurrentUser.userId;
                String path = "favourites/" + name.getText().toString();

                final HashMap<String, Object> favMap = new HashMap<>();
                favMap.put(path + "/pName", name.getText().toString());
                favMap.put(path + "/pCost", cost.getText().toString());
                favMap.put(path + "/pCategory", category);
                favMap.put(path + "/pId", item_id);

                userRef.child(s).updateChildren(favMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ItemActivity.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), FavouritesActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }

        });

    }

}