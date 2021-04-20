package com.example.restaurantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {

    Toolbar cartToolbar;
    RecyclerView list;
    FirebaseDatabase database;
    DatabaseReference cartRef, userRef, orderRef;
    float totalQuantity;
    TextView text_total_quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        User cartUser = CurrentUser.currentUser;
        String s = CurrentUser.userId;

        totalQuantity = 0.0f;
        database = FirebaseDatabase.getInstance();
        cartRef = database.getReference("users").child(s).child("cart");
        orderRef = database.getReference("users").child(s).child("orders");

        text_total_quantity = findViewById(R.id.total_quantity);
        cartToolbar = findViewById(R.id.cart_toolbar);
        cartToolbar.setTitle(cartUser.getName() + "'s cart");
        list = findViewById(R.id.list);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);

        FirebaseRecyclerOptions<CartItem> options = new FirebaseRecyclerOptions.Builder<CartItem>()
                .setQuery(cartRef, CartItem.class)
                .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<CartItem, CartActivity.CartItemViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull CartItemViewHolder holder, final int position, @NonNull final CartItem model) {

                holder.textName.setText(model.getpName());
                holder.textCost.setText(model.getpCost());
                holder.textQuantity.setText(model.getpQuantity());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Edit",
                                "Remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent = new Intent(CartActivity.this, ItemActivity.class);
                                    intent.putExtra("category", model.getpCategory());
                                    intent.putExtra("visit_item_id", model.getpId());
                                    startActivity(intent);
                                }
                                if (which == 1) {
                                    cartRef.child(model.getpName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(CartActivity.this, "Item Removed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartActivity.CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cart_item, parent, false);
                return new CartActivity.CartItemViewHolder(view);
            }
        };

        adapter.startListening();
        list.setAdapter(adapter);

        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChildren()) {
                    for (final DataSnapshot itemSnapShot : snapshot.getChildren()) {
                        String quantity = itemSnapShot.child("pQuantity").getValue().toString();
                        String cost = itemSnapShot.child("pCost").getValue().toString();
                        float costOfEach = Integer.parseInt(cost) * Integer.parseInt(quantity);
                        totalQuantity += costOfEach;
                    }

                    text_total_quantity.setText(String.valueOf(totalQuantity));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void toOrder(View view) {

        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChildren()) {
                    for (final DataSnapshot cartSnapshot : snapshot.getChildren()) {
                        String quantity = cartSnapshot.child("pQuantity").getValue().toString();
                        String cost = cartSnapshot.child("pCost").getValue().toString();
                        String id = cartSnapshot.child("pId").getValue().toString();
                        String name = cartSnapshot.child("pName").getValue().toString();
                        String category = cartSnapshot.child("pCategory").getValue().toString();

                        String saveCurrentDate, saveCurrentTime;
                        Calendar calForDate = Calendar.getInstance();

                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
                        saveCurrentDate = currentDate.format(calForDate.getTime());

                        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
                        saveCurrentTime = currentTime.format(calForDate.getTime());

                        String path = saveCurrentDate + " " + saveCurrentTime + "/" + name;

                        final HashMap<String, Object> orderMap = new HashMap<>();
                        orderMap.put(path + "/pCategory", category);
                        orderMap.put(path + "/pId", id);
                        orderMap.put(path + "/pName", name);
                        orderMap.put(path + "/pCost", cost);
                        orderMap.put(path + "/pQuantity", quantity);

                        if (Integer.parseInt(quantity) > 0) {
                            orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textCost, textQuantity;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textQuantity = itemView.findViewById(R.id.item_quantity);
            textName = itemView.findViewById(R.id.item_name);
            textCost = itemView.findViewById(R.id.item_price);
        }

    }

}