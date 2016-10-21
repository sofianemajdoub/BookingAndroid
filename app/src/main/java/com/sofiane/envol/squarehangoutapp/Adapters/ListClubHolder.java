package com.sofiane.envol.squarehangoutapp.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sofiane.envol.squarehangoutapp.R;
import com.sofiane.envol.squarehangoutapp.Utils.BlurredLayout;

public class ListClubHolder extends RecyclerView.ViewHolder {

    TextView nameTxt;
    ImageView img;
    CardView card;

    public ListClubHolder(View itemView) {
        super(itemView);

        nameTxt = (TextView) itemView.findViewById(R.id.ClubnameTxt);
        img = (ImageView) itemView.findViewById(R.id.ClubImage);
        card = (CardView) itemView.findViewById(R.id.cardClub);

        BlurredLayout layout = (BlurredLayout) itemView.findViewById(R.id.blurred);
        layout.setBlurRadius(1, 4);


    }
}
