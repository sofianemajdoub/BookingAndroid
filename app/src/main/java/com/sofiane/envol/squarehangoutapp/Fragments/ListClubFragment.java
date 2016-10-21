package com.sofiane.envol.squarehangoutapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.sofiane.envol.squarehangoutapp.Activities.MapSearchActivity;
import com.sofiane.envol.squarehangoutapp.Entities.NightClubs;
import com.sofiane.envol.squarehangoutapp.R;
import com.sofiane.envol.squarehangoutapp.Utils.FireBaseClient;

import java.util.ArrayList;
import java.util.List;


public class ListClubFragment extends Fragment {

    final static String DB_URL = "https://hangoutenvol.firebaseio.com/NightClubs";
    private RecyclerView rv_club;
    private FireBaseClient client_club;
    private static ListClubFragment fragment = new ListClubFragment();
    private AutoCompleteTextView inputText_name;
    private List<String> lst_name;
    private NightClubs nightClubs_entity;
    private Firebase ref_club;
    private String input_name;

    public ListClubFragment() {
    }

    public static ListClubFragment getInstance() {
        return fragment;
    }

    public static ListClubFragment newInstance(String param1, String param2) {
        ListClubFragment fragment = new ListClubFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_list_club, container, false);


        inputText_name = (AutoCompleteTextView) v.findViewById(R.id.Input_name);
        lst_name = new ArrayList<String>();
        ref_club = new Firebase(DB_URL);
        ref_club.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    nightClubs_entity = postSnapshot.getValue(NightClubs.class);
                    lst_name.add(String.valueOf(nightClubs_entity.getName()));
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
            }
        });
        rv_club = (RecyclerView) v.findViewById(R.id.mRecylcer_Club);
        rv_club.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        client_club = new FireBaseClient(getActivity(), DB_URL, rv_club);
        client_club.getNightClub();
        ArrayAdapter adapter_name = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, lst_name);
        inputText_name.setAdapter(adapter_name);
        inputText_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                input_name = inputText_name.getText().toString();
                if (!input_name.equals("")) {
                    client_club.clear_restaurant();
                    client_club.findRestaurantByName(input_name);
                }
                inputText_name.setText("");
                client_club.find();
            }
        });
        inputText_name.setThreshold(1);
        return v;
    }

}