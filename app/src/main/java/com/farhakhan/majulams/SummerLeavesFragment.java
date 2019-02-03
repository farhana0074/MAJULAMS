package com.farhakhan.majulams;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SummerLeavesFragment extends BackableFragment implements
        View.OnClickListener {

    public SummerLeavesFragment() { }

    Button btnDateFrom, btnDateTill, btnGoFurther;
    int id_btn, id_btn_from, id_btn_till;
    String strDateFrom, strDateTill, outFormattedDateFrom, outFormattedDateTill,
            outFormattedDateToday, strdateBefore10days;
    String person_email, person_name, person_pic, empFaculty, empDepartment, empDomain, empDesignation;
    String TAG = "SummerLeaves";
    Date dateFrom, dateTill, dateBefore10days;
    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy");
    Calendar calendar= Calendar.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View view = inflater.inflate(R.layout.fragment_summer_leaves, container, false);

        ((FacultyMainActivity) getActivity()).hideFloatingActionButton();

        Bundle bundle = getArguments();
        if (bundle!= null)
        {
            person_email = bundle.getString("EmailID");
            person_name = bundle.getString("Name");
            person_pic = bundle.getString("Picture");
            empFaculty = bundle.getString("Faculty");
            empDepartment= bundle.getString("Department");
            empDomain = bundle.getString("Domain");
            empDesignation = bundle.getString("Designation");
        }

        outFormattedDateToday = outFormat.format(calendar.getTime());

        btnDateFrom = view.findViewById(R.id.get_date_from_sl);
        btnDateFrom.setText(outFormattedDateToday);
        btnDateFrom.setOnClickListener(this);

        btnDateTill= view.findViewById(R.id.get_date_till_sl);
        btnDateTill.setText(outFormattedDateToday);
        btnDateTill.setOnClickListener(this);

        btnGoFurther = view.findViewById(R.id.go_further_sl);
        btnGoFurther.setOnClickListener(this);

        return view;
    }
    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt("year", calendar.get(Calendar.YEAR));
        args.putInt("month", calendar.get(Calendar.MONTH));
        args.putInt("day", calendar.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            monthOfYear=monthOfYear+1;
            if(id_btn== id_btn_from)
            {
                strDateFrom =String.valueOf(year)+ "-" + String.valueOf(monthOfYear)
                        + "-" + String.valueOf(dayOfMonth);
                Calendar calBefore10days = Calendar.getInstance();
                calBefore10days.set(Calendar.DAY_OF_MONTH, calBefore10days.get(Calendar.DAY_OF_MONTH)-10);
                strdateBefore10days = inFormat.format(calBefore10days.getTime());

                try {
                    dateBefore10days = inFormat.parse(strdateBefore10days);
                    dateFrom = inFormat.parse(strDateFrom);
                    if(dateFrom!=null)
                        outFormattedDateFrom = outFormat.format(dateFrom);
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
                if(dateFrom.equals(dateBefore10days)|| dateFrom.after(dateBefore10days)){
                    btnDateFrom.setText(outFormattedDateFrom);
                    btnDateFrom.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                else {
                    Toast.makeText(getContext(),"Leave Date Cannot be older than 10 days from the Current Date", Toast.LENGTH_LONG).show();
                    ResetDateFrom(); }
            }
            else if (id_btn==id_btn_till) {
                strDateTill =  String.valueOf(year)+ "-" + String.valueOf(monthOfYear)
                        + "-" + String.valueOf(dayOfMonth);
                try {
                    dateTill = inFormat.parse(strDateTill);
                    if(dateTill!=null)
                        outFormattedDateTill = outFormat.format(dateTill);

                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                if(dateTill.equals(dateFrom)||dateTill.after(dateFrom)) {
                    btnDateTill.setText(outFormattedDateTill);
                    btnDateTill.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                else
                {
                    Toast.makeText(getContext(), "Leave Ending Date Cannot be before Leave Beginnig date",
                            Toast.LENGTH_LONG).show();
                    ResetDateTill(); }
            }
        }
    };

    @Override
    public void onClick(View v) {
    switch (v.getId())
    {
        case R.id.get_date_from_sl:
            showDatePicker();
            id_btn_from=v.getId();
            break;

        case R.id.get_date_till_sl:
            if(dateFrom!= null)
            {showDatePicker();
                id_btn_till=v.getId();}
            else
                Toast.makeText(getContext(),"Set Leave Beginning Date First", Toast.LENGTH_LONG).show();
            break;

        case R.id.go_further_sl:
            if(dateFrom== null || dateTill == null )
                Toast.makeText(getContext(),"Leave Beginning and Ending Dates must be selected", Toast.LENGTH_LONG).show();

            else {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                LeaveDetailsFragmentFLnLWPnSL leaveDetailsFragment = new LeaveDetailsFragmentFLnLWPnSL();
                Bundle bundle = new Bundle();
                bundle.putString("TAG", TAG);
                bundle.putString("EmailID", person_email);
                bundle.putString("Name", person_name);
                bundle.putString("Picture", person_pic);
                bundle.putString("Faculty", empFaculty);
                bundle.putString("Department", empDepartment);
                bundle.putString("Domain", empDomain);
                bundle.putString("Designation", empDesignation);
                bundle.putString("LeaveFromDate", strDateFrom );
                bundle.putString("LeaveTillDate", strDateTill);
                leaveDetailsFragment.setArguments(bundle);
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .add(R.id.container_faculty, leaveDetailsFragment)
                        .addToBackStack(null).commit();
            }
            break;

    }
    id_btn =v.getId();
}
    public void ResetDateFrom()
    {
        dateFrom=null;
        btnDateFrom.setTextColor(getResources().getColor(R.color.Gray));
        btnDateFrom.setText(outFormattedDateToday);
    }

    public void ResetDateTill()
    {
        dateTill=null;
        btnDateTill.setTextColor(getResources().getColor(R.color.Gray));
        btnDateTill.setText(outFormattedDateToday);
    }

    @Override
    public void onBackButtonPressed() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(SummerLeavesFragment.this).commit();
        ((FacultyMainActivity) getActivity()).showFloatingActionButton();

    }
}