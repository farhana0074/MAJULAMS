package com.farhakhan.majulams;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class AddEmployee extends BackableFragment implements View.OnClickListener {


    public AddEmployee() {

    }
    private Spinner spinnerFac, spinnerDep;
    private Button btnAddEmployee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.fragment_add_employee, container, false);

        spinnerFac = view.findViewById(R.id.spinnerEmp);
        spinnerDep = view.findViewById(R.id.spinner2Emp);

        btnAddEmployee = view.findViewById(R.id.btn_submit_Emp);
        btnAddEmployee.setOnClickListener(this);
        ArrayAdapter<CharSequence> arrayAdapterFac =ArrayAdapter.createFromResource(getContext(), R.array.Faculties,
                android.R.layout.simple_list_item_1);

        final ArrayAdapter<CharSequence> arrayAdapterDepFOCE = ArrayAdapter.createFromResource(getContext(),R.array.DepartmentsFOCE,
                android.R.layout.simple_list_item_1);

        final ArrayAdapter<CharSequence> arrayAdapterDepFOBA = ArrayAdapter.createFromResource(getContext(), R.array.DepartmentsFOBA,
                android.R.layout.simple_list_item_1);

        final ArrayAdapter<CharSequence> arrayAdapterDepFOLS = ArrayAdapter.createFromResource(getContext(), R.array.DepartmentsFOLS,
                android.R.layout.simple_list_item_1);

        spinnerFac.setAdapter(arrayAdapterFac);
        spinnerFac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorAccent));
                long selectedItem = spinnerFac.getItemIdAtPosition(position+1);
                if (selectedItem == 1)
                    spinnerDep.setAdapter(arrayAdapterDepFOCE);
                else if (selectedItem == 2)
                    spinnerDep.setAdapter(arrayAdapterDepFOBA);
                else if(selectedItem == 3)
                    spinnerDep.setAdapter(arrayAdapterDepFOLS);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorAccent));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    return view;
    }

    @Override
    public void onBackButtonPressed() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(AddEmployee.this).commit();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(AddEmployee.this).commit();
    }
}
