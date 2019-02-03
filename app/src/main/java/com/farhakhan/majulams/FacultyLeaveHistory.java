package com.farhakhan.majulams;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FacultyLeaveHistory extends BackableFragment implements View.OnClickListener {


    public FacultyLeaveHistory() {
    }
    String person_email, empFaculty, empDepartment, empDomain;
    String TAG;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faculty_leave_history, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            person_email = bundle.getString("EmailID");
            empFaculty = bundle.getString("Faculty");
            empDepartment = bundle.getString("Department");
            empDomain = bundle.getString("Domain");

            Button btnHL = view.findViewById(R.id.btn_HL);
            btnHL.setOnClickListener(this);
            Button btnFL = view.findViewById(R.id.btn_FL);
            btnFL.setOnClickListener(this);
            Button btnLWP = view.findViewById(R.id.btn_LWP);
            btnLWP.setOnClickListener(this);
            Button btnSL = view.findViewById(R.id.btn_SL);
            btnSL.setOnClickListener(this);

        }

        return view;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle bundle1 = new Bundle();
        bundle1.putString("person_email", person_email);
        bundle1.putString("empDomain", empDomain);
        bundle1.putString("empDepartment", empDepartment);
        bundle1.putString("empFaculty", empFaculty);
        switch (v.getId())
        {
            case R.id.btn_HL:
                TAG = "HL";
                bundle1.putString("TAG", TAG);
                Fragment facultyHistoryDetails = new FacultyHistoryDetails();
                facultyHistoryDetails.setArguments(bundle1);
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.container_faculty, facultyHistoryDetails)
                        .addToBackStack(null).commit();
                  break;

            case R.id.btn_FL:
                TAG = "FL";
                bundle1.putString("TAG", TAG);
                Fragment facultyHistoryDetails1 = new FacultyHistoryDetails();
                facultyHistoryDetails1.setArguments(bundle1);
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.container_faculty, facultyHistoryDetails1)
                        .addToBackStack(null).commit();
                break;

            case R.id.btn_LWP:
                TAG = "LWP";
                bundle1.putString("TAG", TAG);
                Fragment facultyHistoryDetails2 = new FacultyHistoryDetails();
                facultyHistoryDetails2.setArguments(bundle1);
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.container_faculty, facultyHistoryDetails2)
                        .addToBackStack(null).commit();
                break;

            case R.id.btn_SL:
                TAG = "SL";
                bundle1.putString("TAG", TAG);
                Fragment facultyHistoryDetails3 = new FacultyHistoryDetails();
                facultyHistoryDetails3.setArguments(bundle1);
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.container_faculty, facultyHistoryDetails3)
                        .addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void onBackButtonPressed() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(FacultyLeaveHistory.this).commit();

    }
}
