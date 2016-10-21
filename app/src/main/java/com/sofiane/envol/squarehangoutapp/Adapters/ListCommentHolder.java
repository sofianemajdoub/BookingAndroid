package com.sofiane.envol.squarehangoutapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sofiane.envol.squarehangoutapp.R;

public class ListCommentHolder extends RecyclerView.ViewHolder  {

    TextView nameTxt;
    public ListCommentHolder(View itemView) {
        super(itemView);
        nameTxt= (TextView) itemView.findViewById(R.id.nameuser);
    }
}
