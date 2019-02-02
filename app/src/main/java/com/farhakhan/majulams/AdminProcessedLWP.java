package com.farhakhan.majulams;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AdminProcessedLWP extends BackableFragment {


    public AdminProcessedLWP() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_processed_lwp, container, false);
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();


        return view;
    }

    @Override
    public void onBackButtonPressed() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(AdminProcessedLWP.this).commit();

    }
}
