package com.sofiane.envol.squarehangoutapp.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.sofiane.envol.squarehangoutapp.Activities.AllCommentActivity;
import com.sofiane.envol.squarehangoutapp.Adapters.CommentListAdapter;
import com.sofiane.envol.squarehangoutapp.R;

/**
 * Created by HP-450G3 on 29/06/2016.
 */
public class CommentFragment extends Fragment {

    private ListView listViewComment;
    private Query queryRef_Comment;
    private CommentListAdapter listAdapterComment;
    private String user, place;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.comment_fragment_layout, null);
        listViewComment = (ListView) v.findViewById(R.id.list_comment);
        TextView more = (TextView) v.findViewById(R.id.more);

        user = getArguments().getString("user");
        place = getArguments().getString("place");

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllCommentActivity.class);
                intent.putExtra("place", place);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Firebase fire_menu = new Firebase("https://hangoutenvol.firebaseio.com/Comment/");
        queryRef_Comment = fire_menu.limitToLast(3).orderByChild("name_place").equalTo(place);
        listAdapterComment = new CommentListAdapter(queryRef_Comment, getActivity(), R.layout.list_comment, getActivity());
        listViewComment.setAdapter(listAdapterComment);
        listAdapterComment.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listViewComment.setSelection(listAdapterComment.getCount() - 1);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}