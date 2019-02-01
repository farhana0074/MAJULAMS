package com.farhakhan.majulams;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AdminDepWiseLeaves extends Fragment {


    public AdminDepWiseLeaves() {
        // Required empty public constructor
    }

    String departmentName;
    String facultyName;
    SharedPreferences sharedPreferences;
    public static final String MY_SHARED_PREFERENCES = "MySharedPrefs";
    public static final String Faculty = "FacKey";
    public static final String Department = "DepKey";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view= inflater.inflate(R.layout.fragment_admin_dep_wise_leaves, container, false);

       Bundle bundle= getArguments();
       if (bundle!= null) {
           departmentName = bundle.getString("DepNameFromKey");
           facultyName = bundle.getString("Faculty");
       }
       sharedPreferences = getContext().getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putString(Faculty, facultyName);
       editor.putString(Department, departmentName);
       editor.commit();

        TextView tvDepartmentName = view.findViewById(R.id.tvDepName);
        tvDepartmentName.setText("Department of "+ departmentName);

       Toolbar toolbarFOCEDep = view.findViewById(R.id.toolbarFOCEDep);
        TabLayout tablayoutFOCEDep = view.findViewById(R.id.tablayoutFOCEDep);
        TabItem tabHLFOCE = view.findViewById(R.id.tabHLFOCE);
        TabItem tabFLFOCE = view.findViewById(R.id.tabFLFOCE);
        TabItem tabLWPFOCE = view.findViewById(R.id.tabLWPFOCE);
        TabItem tabSLFOCE = view.findViewById(R.id.tabSLFOCE);
        ViewPager viewPagerFOCEDep = view.findViewById(R.id.viewPagerFOCEDep);
        PageAdapterDepInFacInAdmin pageAdapter = new PageAdapterDepInFacInAdmin(getChildFragmentManager(), tablayoutFOCEDep.getTabCount());
        viewPagerFOCEDep.setAdapter(pageAdapter);
        viewPagerFOCEDep.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayoutFOCEDep));
        tablayoutFOCEDep.setupWithViewPager(viewPagerFOCEDep);

        return view;
    }
}
