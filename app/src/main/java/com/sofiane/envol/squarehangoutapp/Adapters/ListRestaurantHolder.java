package com.sofiane.envol.squarehangoutapp.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sofiane.envol.squarehangoutapp.R;
import com.sofiane.envol.squarehangoutapp.Utils.BlurredLayout;

public class ListRestaurantHolder extends RecyclerView.ViewHolder {

    TextView nameTxt;
    ImageView img;
    TextView cuisineTxt;
    CardView card;

    public ListRestaurantHolder(View itemView) {
        super(itemView);

        nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
        img = (ImageView) itemView.findViewById(R.id.movieImage);
        cuisineTxt = (TextView) itemView.findViewById(R.id.cuisineTxt);
        card = (CardView) itemView.findViewById(R.id.cardRestau);

        BlurredLayout layout = (BlurredLayout) itemView.findViewById(R.id.blurred);
        layout.setBlurRadius(1, 4);

    }

}
