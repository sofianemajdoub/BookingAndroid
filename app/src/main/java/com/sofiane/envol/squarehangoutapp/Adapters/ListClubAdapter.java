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

import com.sofiane.envol.squarehangoutapp.Activities.DetailClubActivity;
import com.sofiane.envol.squarehangoutapp.Entities.NightClubs;
import com.sofiane.envol.squarehangoutapp.R;

import java.util.ArrayList;

public class   ListClubAdapter extends RecyclerView.Adapter<ListClubHolder> {

    private Context context;
    private ArrayList<NightClubs> nightClubs;

    public ListClubAdapter(Context context, ArrayList<NightClubs> nightClubs) {
        this.context = context;
        this.nightClubs = nightClubs;
    }

    @Override
    public ListClubHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_club_card, parent, false);
        ListClubHolder holder = new ListClubHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ListClubHolder holder, final int position) {

        holder.nameTxt.setText(nightClubs.get(position).getName());
        final Bitmap myBitmapAgain = decodeBase64(nightClubs.get(position).getImage());
        Bitmap resizedImage = Bitmap.createScaledBitmap(myBitmapAgain, 349, 233, true);
        holder.img.setImageBitmap(resizedImage);
        holder.img.setImageBitmap(myBitmapAgain);

        holder.card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), DetailClubActivity.class);
                intent.putExtra("type", "NightClubs");
                intent.putExtra("name_club", nightClubs.get(position).getName());
                intent.putExtra("address_club", nightClubs.get(position).getAddress());
                intent.putExtra("close_club", nightClubs.get(position).getClose_time());
                intent.putExtra("open_club", nightClubs.get(position).getOpen_time());
                intent.putExtra("lat_club", nightClubs.get(position).getLat());
                intent.putExtra("lng_club", nightClubs.get(position).getLng());
                intent.putExtra("nbrBooking_club", nightClubs.get(position).getNbr_booking());
                intent.putExtra("phone_club", nightClubs.get(position).getPhone());
                intent.putExtra("image_club", nightClubs.get(position).getImage());
                intent.putExtra("ID_club", nightClubs.get(position).getId());
                v.getContext().startActivity(intent);
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return nightClubs.size();
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
