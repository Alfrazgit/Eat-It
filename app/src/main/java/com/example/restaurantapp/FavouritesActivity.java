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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FavouritesActivity extends AppCompatActivity {

    Toolbar toolbar_fav;
    RecyclerView list;
    FirebaseDatabase database;
    DatabaseReference favRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        User favUser = CurrentUser.currentUser;
        String s = CurrentUser.userId;

        database = FirebaseDatabase.getInstance();
        favRef = database.getReference("users").child(s).child("favourites");
        toolbar_fav = findViewById(R.id.fav_toolbar);
        toolbar_fav.setTitle(favUser.getName() + "'s favourites");
        list = findViewById(R.id.list);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);

        FirebaseRecyclerOptions<Fav> options = new FirebaseRecyclerOptions.Builder<Fav>()
                .setQuery(favRef, Fav.class)
                .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Fav, FavouritesActivity.FavViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FavouritesActivity.FavViewHolder holder, final int position, @NonNull final Fav model) {

                holder.textName.setText(model.getName());
                holder.textCost.setText(model.getCost());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[] {
                                "Open",
                                "Remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(FavouritesActivity.this);
                        builder.setTitle("Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0) {
                                    Intent intent = new Intent(FavouritesActivity.this, ItemActivity.class);
                                    intent.putExtra("category", model.getCategory());
                                    intent.putExtra("visit_item_id", model.getId());
                                    startActivity(intent);
                                }
                                if (which == 1) {
                                    favRef.child(model.getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(FavouritesActivity.this, "Item Removed", Toast.LENGTH_SHORT).show();
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
            public FavouritesActivity.FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fav_item, parent, false);
                return new FavouritesActivity.FavViewHolder(view);
            }
        };

        adapter.startListening();
        list.setAdapter(adapter);

    }

    class FavViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textCost;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.item_name);
            textCost = itemView.findViewById(R.id.item_price);
        }

    }

}