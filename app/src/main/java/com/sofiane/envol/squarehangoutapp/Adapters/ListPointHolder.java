package com.sofiane.envol.squarehangoutapp.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sofiane.envol.squarehangoutapp.R;

public class ListPointHolder extends RecyclerView.ViewHolder {

    TextView nameTxt, pointTxt;
    CardView card;

    public ListPointHolder(View itemView) {
        super(itemView);
        nameTxt = (TextView) itemView.findViewById(R.id.restTxt);
        pointTxt = (TextView) itemView.findViewById(R.id.pointTxt);
        card = (CardView) itemView.findViewById(R.id.card);
    }

}
