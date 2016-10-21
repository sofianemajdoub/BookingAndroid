package com.sofiane.envol.squarehangoutapp.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sofiane.envol.squarehangoutapp.R;

public class ListLikeHolder extends RecyclerView.ViewHolder {

    TextView nameTxt;
    ImageView img;
    Button btn;
    CardView card;

    public ListLikeHolder(View itemView) {
        super(itemView);
        nameTxt = (TextView) itemView.findViewById(R.id.restTxt);
        img = (ImageView) itemView.findViewById(R.id.restImage);
        card = (CardView) itemView.findViewById(R.id.card);
        btn = (Button) itemView.findViewById(R.id.likeposTxt);
    }
}
