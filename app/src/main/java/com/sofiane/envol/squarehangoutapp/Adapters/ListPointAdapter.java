package com.sofiane.envol.squarehangoutapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sofiane.envol.squarehangoutapp.Entities.Points;
import com.sofiane.envol.squarehangoutapp.R;

import java.util.ArrayList;

public class ListPointAdapter extends RecyclerView.Adapter<ListPointHolder> {

    private Context context;
    private ArrayList<Points> points;


    public ListPointAdapter(Context context, ArrayList<Points> points) {
        this.context = context;
        this.points = points;
    }

    @Override
    public ListPointHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_point_card, parent, false);
        ListPointHolder holder = new ListPointHolder(v);
        return holder;
    }

    public void swap(ArrayList<Points> pts) {
        points.clear();
        points.addAll(pts);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ListPointHolder holder, final int position) {
        holder.nameTxt.setText(points.get(position).getName());
        holder.pointTxt.setText(points.get(position).getPoint() + "pts");
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
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
                */
            }
        });
    }

    @Override
    public int getItemCount() {
        return points.size();
    }


}