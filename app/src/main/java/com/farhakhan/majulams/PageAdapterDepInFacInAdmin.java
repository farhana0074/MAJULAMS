package com.farhakhan.majulams;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageAdapterDepInFacInAdmin extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapterDepInFacInAdmin(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                return new AdminHLFragment();

            case 1:
                return new AdminFLFragment();

            case 2:
                return new AdminLWPFragment();

            case 3:
                return new AdminSLFragment();

            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String title = null;
        if (position== 0)
            title ="Half Leaves";
        else if(position ==1)
            title = "Full Leaves";
        else if (position ==2)
            title= "Leaves Without Pay";
        else
            title = "Summer Leaves";
        return title;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
