package com.sofiane.envol.squarehangoutapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sofiane.envol.squarehangoutapp.Entities.Comments;
import com.sofiane.envol.squarehangoutapp.R;

import java.util.ArrayList;

public class ListCommentAdapter extends RecyclerView.Adapter<ListCommentHolder> {

    private Context context;
    private ArrayList<Comments> comments;

    public ListCommentAdapter(Context context, ArrayList<Comments> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public ListCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_comment_card, parent, false);


        ListCommentHolder holder = new ListCommentHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ListCommentHolder holder, final int position) {
        holder.nameTxt.setText(comments.get(position).getName_user());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


}
