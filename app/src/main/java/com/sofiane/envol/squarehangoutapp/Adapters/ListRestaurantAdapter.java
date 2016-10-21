package com.sofiane.envol.squarehangoutapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sofiane.envol.squarehangoutapp.Activities.DetailRestaurantActivity;
import com.sofiane.envol.squarehangoutapp.Entities.Restaurants;
import com.sofiane.envol.squarehangoutapp.R;

import java.util.ArrayList;

public class ListRestaurantAdapter extends RecyclerView.Adapter<ListRestaurantHolder> {

    private Context context;
    private ArrayList<Restaurants> restaurantes;

    public ListRestaurantAdapter(Context context, ArrayList<Restaurants> restaurantes) {
        this.context = context;
        this.restaurantes = restaurantes;
    }

    @Override
    public ListRestaurantHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_card_restaurant, parent, false);
        ListRestaurantHolder holder = new ListRestaurantHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ListRestaurantHolder holder, final int position) {

        holder.nameTxt.setText(restaurantes.get(position).getName());
        Bitmap myBitmapAgain = decodeBase64(restaurantes.get(position).getImage());
        Bitmap resizedImage = Bitmap.createScaledBitmap(myBitmapAgain, 349, 233, true);
        holder.img.setImageBitmap(resizedImage);
        holder.cuisineTxt.setText("Cuisine " + restaurantes.get(position).getCuisine());


        holder.card.setOnClickListener(new View.OnClickListener()

                                       {

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
                                               ((Activity) context).finish();
                                           }
                                       }
        );
    }

    @Override
    public int getItemCount() {
        return restaurantes.size();
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


}
