package com.farhakhan.majulams;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageAdapterFacultyInAdmin extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapterFacultyInAdmin(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                return new AdminFOCEFragment();

            case 1:
                return new AdminFOBAFragment();

            case 2:
                return new AdminFOLSFragment();

            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String title = null;
        if (position== 0)
            title ="FOCE";
        else if(position ==1)
            title = "FOBA";
        else
            title= "FOLS";

        return title;
    }
    @Override
    public int getCount() {
        return numOfTabs;
    }
}