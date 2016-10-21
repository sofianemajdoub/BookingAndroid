package com.sofiane.envol.squarehangoutapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.sofiane.envol.squarehangoutapp.Activities.MapSearchActivity;
import com.sofiane.envol.squarehangoutapp.Entities.Restaurants;
import com.sofiane.envol.squarehangoutapp.R;
import com.sofiane.envol.squarehangoutapp.Utils.FireBaseClient;

import java.util.ArrayList;
import java.util.List;


public class ListRestaurantFragment extends Fragment {
    final static String DB_URL = "https://hangoutenvol.firebaseio.com/Restaurants";
    private RecyclerView rv_af, rv_fr, rv_am, rv_it, rvALL;
    private FireBaseClient client_af, client_fr, client_am, client_it, client_all;
    private AutoCompleteTextView inputText_name;
    private static ListRestaurantFragment fragment = new ListRestaurantFragment();
    private List<String> lst_name;
    private Restaurants restaurants_entity;
    private Firebase ref_restaurant;
    private String input_name;

    public ListRestaurantFragment() {
    }

    public static ListRestaurantFragment getInstance() {
        return fragment;
    }

    public static ListRestaurantFragment newInstance(String param1, String param2) {
        ListRestaurantFragment fragment = new ListRestaurantFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //   getActivity().finish();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_list_restaurant, container, false);


        inputText_name = (AutoCompleteTextView) v.findViewById(R.id.Input_name);
        lst_name = new ArrayList<String>();
        ref_restaurant = new Firebase("https://hangoutenvol.firebaseio.com/Restaurants");
        ref_restaurant.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    restaurants_entity = postSnapshot.getValue(Restaurants.class);
                    lst_name.add(String.valueOf(restaurants_entity.getName()));
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        v.findViewById(R.id.Place_layout_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapSearchActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        rv_af = (RecyclerView) v.findViewById(R.id.mRecylcer_af);
        rv_af.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        rv_fr = (RecyclerView) v.findViewById(R.id.mRecylcer_fr);
        rv_fr.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        rv_am = (RecyclerView) v.findViewById(R.id.mRecylcer_am);
        rv_am.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        rv_it = (RecyclerView) v.findViewById(R.id.mRecylcer_it);
        rv_it.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        rvALL = (RecyclerView) v.findViewById(R.id.mRecylcer_ALL);
        rvALL.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        client_af = new FireBaseClient(getActivity(), DB_URL, rv_af);
        client_fr = new FireBaseClient(getActivity(), DB_URL, rv_fr);
        client_am = new FireBaseClient(getActivity(), DB_URL, rv_am);
        client_it = new FireBaseClient(getActivity(), DB_URL, rv_it);
        client_all = new FireBaseClient(getActivity(), DB_URL, rvALL);

        client_af.findByCuisine("Africaine");
        client_fr.findByCuisine("Française");
        client_am.findByCuisine("Americaine");
        client_it.findByCuisine("Italienne");
        client_all.find();

        ArrayAdapter adapter_name = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, lst_name);
        inputText_name.setAdapter(adapter_name);
        inputText_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                input_name = inputText_name.getText().toString();
                if (!input_name.equals("")) {
                    client_all.clear_restaurant();
                    client_all.findRestaurantByName(input_name);
                }
                ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
                        .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                inputText_name.setText("");
                client_all.find();
            }
        });
     inputText_name.setThreshold(1);

        return v;
    }

}
