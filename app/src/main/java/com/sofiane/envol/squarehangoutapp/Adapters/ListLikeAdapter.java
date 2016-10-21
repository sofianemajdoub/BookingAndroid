package com.sofiane.envol.squarehangoutapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;
import com.sofiane.envol.squarehangoutapp.Activities.DetailRestaurantActivity;
import com.sofiane.envol.squarehangoutapp.Entities.Restaurants;
import com.sofiane.envol.squarehangoutapp.R;

import java.util.ArrayList;

public class ListLikeAdapter extends RecyclerView.Adapter<ListLikeHolder> {

    private Context context;
    private ArrayList<Restaurants> restaurantes;

    public ListLikeAdapter(Context context, ArrayList<Restaurants> restaurantes) {
        this.context = context;
        this.restaurantes = restaurantes;
    }

    @Override
    public ListLikeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_like_card, parent, false);
        ListLikeHolder holder = new ListLikeHolder(v);
        return holder;
    }

    public void swap(ArrayList<Restaurants> restau) {
        restaurantes.clear();
        restaurantes.addAll(restau);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ListLikeHolder holder, final int position) {
        holder.nameTxt.setText(restaurantes.get(position).getName());
        Bitmap myBitmapAgain = ListRestaurantAdapter.decodeBase64(restaurantes.get(position).getImage());
        Bitmap resizedImage = Bitmap.createScaledBitmap(myBitmapAgain, 349, 233, true);
        holder.img.setImageBitmap(resizedImage);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = context.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
                final String id_user = pref.getString("Id_User", "");
                Firebase likeRef = new Firebase("https://hangoutenvol.firebaseio.com/like/").child(id_user).child(restaurantes.get(position).getId());
                likeRef.setValue(null);
                swap(restaurantes);

            }
        });
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), DetailRestaurantActivity.class);

                i.putExtra("name_resto", restaurantes.get(position).getName());
                i.putExtra("address_resto", restaurantes.get(position).getAddress());
                i.putExtra("cuisine_resto", restaurantes.get(position).getCuisine());
                i.putExtra("close_resto", restaurantes.get(position).getClose_time());
                i.putExtra("open_resto", restaurantes.get(position).getOpen_time());
                i.putExtra("lat_resto", restaurantes.get(position).getLat());
                i.putExtra("lng_resto", restaurantes.get(position).getLng());
                i.putExtra("nbrBooking_resto", restaurantes.get(position).getNbr_booking());
                i.putExtra("phone_resto", restaurantes.get(position).getPhone());
                i.putExtra("image_resto", restaurantes.get(position).getImage());
                i.putExtra("ID_resto", restaurantes.get(position).getId());

                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurantes.size();
    }


}