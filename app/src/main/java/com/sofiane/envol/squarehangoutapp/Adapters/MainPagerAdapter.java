package com.sofiane.envol.squarehangoutapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sofiane.envol.squarehangoutapp.Fragments.ListClubFragment;
import com.sofiane.envol.squarehangoutapp.Fragments.ListRestaurantFragment;

/**
 * Created by HP-450G3 on 01/06/2016.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return ListRestaurantFragment.getInstance();
            case 1:
                return ListClubFragment.getInstance();

        }
        return  null ;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Restaurants";
            case 1:
                return "NightClubs";
        }
        return null;
    }
}