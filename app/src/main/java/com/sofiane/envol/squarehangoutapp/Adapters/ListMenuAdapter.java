package com.sofiane.envol.squarehangoutapp.Adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;
import com.sofiane.envol.squarehangoutapp.Entities.Menus;
import com.sofiane.envol.squarehangoutapp.R;


public class ListMenuAdapter extends FirebaseListAdapter<Menus> {

    public ListMenuAdapter(Query ref, Activity activity, int layout) {
        super(ref, Menus.class, layout, activity);

    }

    @Override
    protected void populateView(View view, Menus m) {
        String author = m.getMenu_name();
        TextView authorText = (TextView) view.findViewById(R.id.author);
        authorText.setText(author);
        ((TextView) view.findViewById(R.id.message)).setText(m.getMenu_price());
        ((TextView) view.findViewById(R.id.description)).setText(m.getMenu_description());
    }
}
