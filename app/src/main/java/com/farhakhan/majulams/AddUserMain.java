package com.farhakhan.majulams;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

public class AddUserMain extends BackableFragment implements View.OnClickListener {


    public AddUserMain() {
        // Required empty public constructor
    }
Button btnAddHoD, btAddEmp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View view =inflater.inflate(R.layout.fragment_add_user_main, container, false);
     btnAddHoD = view.findViewById(R.id.btn_add_hod);
     btnAddHoD.setOnClickListener(this);
     btAddEmp = view.findViewById(R.id.btn_add_faculty);
     btAddEmp.setOnClickListener(this);

     return view;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction= getFragmentManager().beginTransaction();
        switch (v.getId())
        {
            case R.id.btn_add_hod:
                Fragment addHoD = new AddHoD();
                transaction.replace(R.id.container_admin,addHoD)
                        .addToBackStack(null).commit();
                break;
            case R.id.btn_add_faculty:
                Fragment addEmp = new AddEmployee();
                transaction.replace(R.id.container_admin,addEmp)
                        .addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void onBackButtonPressed() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(AddUserMain.this).commit();
    }
}
