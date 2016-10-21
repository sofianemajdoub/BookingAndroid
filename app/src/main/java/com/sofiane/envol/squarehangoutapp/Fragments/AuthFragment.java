package com.sofiane.envol.squarehangoutapp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.sofiane.envol.squarehangoutapp.Activities.MainTabActivity;
import com.sofiane.envol.squarehangoutapp.Entities.Booking;
import com.sofiane.envol.squarehangoutapp.Entities.Users;
import com.sofiane.envol.squarehangoutapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AuthFragment extends Fragment {
    private static final String FIREBASE_URL = "https://hangoutenvol.firebaseio.com/";
    private Firebase userRef ;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private String id;
    private String imgUrl;
    private String email;
    private String birthday;
    private String name;
    private String gender;
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", response.toString());

                            // Application code
                            try {
                                email = object.getString("email");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                id = object.getString("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                birthday = object.getString("birthday");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                name = object.getString("name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                gender = object.getString("gender");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            imgUrl = "https://graph.facebook.com/" + id + "/picture?type=large";
                            openProfileInfo(email, id, birthday, name, gender, imgUrl);
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday, picture");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            // App code
            Log.v("LoginActivity", "cancel");

        }

        @Override
        public void onError(FacebookException e) {
            Log.v("LoginActivity", e.getCause().toString());
        }
    };

    public AuthFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_auth, container, false);



        if (AccessToken.getCurrentAccessToken() != null) {
            Intent intent = new Intent(getActivity(), MainTabActivity.class);

            startActivity(intent);
        }



        return v;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("cnx", Context.MODE_PRIVATE).edit();
        editor.putBoolean("connected", true);
        editor.commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void displayMessage(Profile profile) {
        if (profile != null) {
            Log.d("chk", profile.toString());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }


    private void openProfileInfo(final String email, final String id, final String birthday, final String name, String gender, String imgUrl) {
        userRef = new Firebase(FIREBASE_URL).child("Users").child(id);


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()){

                    Map<String, String> user = new HashMap<String, String>();
                    user.put("id", id);
                    user.put("name", name);
                    user.put("email", email);
                    user.put("birthday", birthday);
                    user.put("address", "");
                    user.put("phone", "");

                    userRef.setValue(user);
                }

                    }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        Intent intent = new Intent(getActivity(), MainTabActivity.class);

        SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("Id_User", id);
        editor.putString("Name_User", name);
        editor.putString("Image_User", imgUrl);
        editor.commit();
        startActivity(intent);
    }


}

