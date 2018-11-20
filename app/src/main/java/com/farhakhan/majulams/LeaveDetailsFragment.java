package com.farhakhan.majulams;


import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LeaveDetailsFragment extends Fragment {


    public LeaveDetailsFragment() {
        // Required empty public constructor
    }

    int rb_id, rb_idMakeup, rb_idFixture;
    RadioGroup radioGroup;
    EditText reason;
    Button btnSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_leave_details, container, false);
        ((FacultyMainActivity) getActivity()).hideFloatingActionButton();
        radioGroup = view.findViewById(R.id.rgCoverOptions);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rBMakeup:
                        rb_idMakeup = view.getId();
                        break;
                    case R.id.rbFixture:
                        rb_idFixture = view.getId();
                        break;
                }
            }
        });
        reason = view.findViewById(R.id.etReason);
        btnSubmit = view.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Leave Application Submitted", Toast.LENGTH_LONG).show();
                ((FacultyMainActivity)getActivity()).clearBackstack();
                   }
        });
        return view;
    }
}