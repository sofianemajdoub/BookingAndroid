package com.sofiane.envol.squarehangoutapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Query;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.sofiane.envol.squarehangoutapp.Entities.Comments;
import com.sofiane.envol.squarehangoutapp.R;
import com.squareup.picasso.Picasso;


public class CommentListAdapter extends FirebaseListAdapter<Comments> {

    private Context mContext;


    public CommentListAdapter(Query ref, Activity activity, int layout,Context context) {
        super(ref, Comments.class, layout, activity);
        mContext = context;
    }

    @Override
    protected void populateView(View view, final Comments m) {
        String img_user = m.getImg_user();
        ImageView img_profil = (ImageView) view.findViewById(R.id.pic_comment);
        Picasso.with(mContext).load(img_user).into(img_profil);

        final String author = m.getName_user();
        TextView authorText = (TextView) view.findViewById(R.id.name_user);
        authorText.setText(author);

        String descrption = m.getDescription();
        TextView descText = (TextView) view.findViewById(R.id.description);
        descText.setText(descrption);

        String date = m.getDate();
        TextView dateText = (TextView) view.findViewById(R.id.date);
        dateText.setText(date);

        String note = m.getRating();
        TextView ratingText = (TextView) view.findViewById(R.id.note);
        ratingText.setText(note);

        String like = m.getLike();
        TextView likeText = (TextView) view.findViewById(R.id.nbr_like);
        likeText.setText(like);

        final ImageView pic_like_outline = (ImageView) view.findViewById(R.id.pic_like_outline);
        final ImageView pic_like = (ImageView) view.findViewById(R.id.pic_like);

        final Firebase ref_like = new Firebase("https://hangoutenvol.firebaseio.com/Comment/");
        final Firebase ref_like_comm = new Firebase("https://hangoutenvol.firebaseio.com/Comment_liked/");

        SharedPreferences pref = mContext.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        final String id_user = pref.getString("Id_User", "");

        pic_like_outline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pic_like.setVisibility(View.VISIBLE);
                pic_like_outline.setVisibility(View.GONE);
                ref_like_comm.child(m.getId()).child(id_user).child("liked_by").setValue(m.getName_user());

                ref_like.child(m.getId()).child("like").runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        if (mutableData.getValue() == null) {
                            mutableData.setValue(1);
                        } else {
                            long size = (Long) mutableData.getValue() + 1;
                            mutableData.setValue(size);
                        }
                        return Transaction.success(mutableData);
                    }

                    public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                        if (firebaseError != null) {
                            System.out.print("Firebase counter increment failed.");
                        } else {
                            System.out.print("Firebase counter increment succeeded.");
                        }
                    }
                });
            }
        });

        pic_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pic_like.setVisibility(View.GONE);
                pic_like_outline.setVisibility(View.VISIBLE);
                ref_like_comm.child(m.getId()).child(id_user).setValue(null);

                ref_like.child(m.getId()).child("like").runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        if (mutableData.getValue() == null) {
                            mutableData.setValue(0);
                        } else {
                            long size = (Long) mutableData.getValue() - 1;
                            mutableData.setValue(size);
                        }
                        return Transaction.success(mutableData);
                    }

                    public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                        if (firebaseError != null) {
                            System.out.print("Firebase counter increment failed.");
                        } else {
                            System.out.print("Firebase counter increment succeeded.");
                        }
                    }
                });

            }
        });

        ref_like_comm.child(m.getId()).child(id_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("+++" + snapshot.getValue());
                if (snapshot.getValue() != null) {
                    pic_like.setVisibility(View.VISIBLE);
                    pic_like_outline.setVisibility(View.GONE);
                }else
                {
                    pic_like.setVisibility(View.GONE);
                    pic_like_outline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

}
