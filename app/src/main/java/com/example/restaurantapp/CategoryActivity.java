package com.example.restaurantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryActivity extends AppCompatActivity {

    Toolbar toolbar_breakfast;
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent intent = getIntent();
        final String str = intent.getStringExtra("category");

        toolbar_breakfast = findViewById(R.id.breakfast_toolbar);
        toolbar_breakfast.setTitle(str);

        list = findViewById(R.id.list);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);

        FirebaseRecyclerOptions<Item> options = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child(str), Item.class)
                .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder,final int position, @NonNull Item model) {

                holder.textName.setText(model.getName());
                holder.textCost.setText(model.getCost());
                holder.textName.setText(model.getName());
                holder.setImg(model.getPic());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String visit_item_id = getRef(position).getKey();

                        Intent intent = new Intent(CategoryActivity.this, ItemActivity.class);
                        intent.putExtra("visit_item_id", visit_item_id);
                        intent.putExtra("category", str);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);
                return new ItemViewHolder(view);
            }
        };

        adapter.startListening();
        list.setAdapter(adapter);

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView textName, textCost;

        public ItemViewHolder (@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.item_img);
            textName = itemView.findViewById(R.id.item_name);
            textCost = itemView.findViewById(R.id.item_price);
        }

        public void setImg(String img) {
            ImageView product_image = itemView.findViewById(R.id.item_img);
            Picasso.get().load(img).into(product_image);
        }
    }

}