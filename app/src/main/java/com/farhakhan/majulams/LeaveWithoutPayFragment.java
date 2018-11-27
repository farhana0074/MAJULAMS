package com.farhakhan.majulams;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

public class LeaveWithoutPayFragment extends Fragment
implements View.OnClickListener{

    public LeaveWithoutPayFragment() { }

    Button btnDateFrom;
    Button btnDateTill;
    Button btnGoFurther;
    int id_btn, id_btn_from, id_btn_till;
    String strDateFrom, strDateTill, outFormattedDateFrom, outFormattedDateTill,
            strDateToday, outFormattedDateToday;
    Date dateFrom, dateTill, dateToday;

    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy");
    Calendar calendar= Calendar.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_leave_without_pay, container, false);

        outFormattedDateToday= outFormat.format(calendar.getTime());

        btnDateFrom = view.findViewById(R.id.get_date_from);
        btnDateFrom.setText(outFormattedDateToday);
        btnDateFrom.setOnClickListener(this);
        btnDateFrom.setEnabled(true);

        btnDateTill = view.findViewById(R.id.get_date_till);
        btnDateTill.setText(outFormattedDateToday);
        btnDateTill.setOnClickListener(this);
        btnDateTill.setEnabled(false);

        btnGoFurther = view.findViewById(R.id.go_further);
        btnGoFurther.setOnClickListener(this);
        btnGoFurther.setEnabled(false);

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
            strDateToday = inFormat.format(calendar.getTime());
            if(id_btn== id_btn_from)
            {
                strDateFrom =String.valueOf(year)+ "-" + String.valueOf(monthOfYear)
                        + "-" + String.valueOf(dayOfMonth);
                try {
                    dateToday = inFormat.parse(strDateToday);
                    dateFrom = inFormat.parse(strDateFrom);
                    if(dateFrom!=null)
                    {
                    outFormattedDateFrom = outFormat.format(dateFrom);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(dateFrom.equals(dateToday))
                {
                    Toast.makeText(getContext(),"Full Leave can be applied at least 1 day prior",
                            Toast.LENGTH_LONG).show();
                    btnDateFrom.setEnabled(true);
                    ResetDateFrom();
                }
                else if(dateFrom.before(dateToday))
                {
                    Toast.makeText(getContext(), "Leave Beginning date cannot be before Current date",
                            Toast.LENGTH_LONG).show();
                    btnDateFrom.setEnabled(true);
                    ResetDateFrom();
                }
                else {
                    btnDateFrom.setText(outFormattedDateFrom);
                    btnDateFrom.setTextColor(getResources().getColor(R.color.colorAccent));
                    btnDateFrom.setEnabled(false);
                    btnDateTill.setEnabled(true);
                }
                    }
            else if (id_btn==id_btn_till) {
                strDateTill =  String.valueOf(year)+ "-" + String.valueOf(monthOfYear)
                        + "-" + String.valueOf(dayOfMonth);
                try {
                    dateTill = inFormat.parse(strDateTill);
                    if(dateTill!=null)
                    {
                        outFormattedDateTill = outFormat.format(dateTill);
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                if(dateTill.equals(dateFrom)||dateTill.after(dateFrom)) {
                    btnDateTill.setText(outFormattedDateTill);
                    btnDateTill.setTextColor(getResources().getColor(R.color.colorAccent));
                    btnDateTill.setEnabled(false);
                    btnGoFurther.setEnabled(true);
                }
                else
                {
                    Toast.makeText(getContext(), "Leave Ending Date Cannot be before Leave Beginnig date",
                            Toast.LENGTH_LONG).show();
                    btnDateTill.setEnabled(true);
                    ResetDateTill();
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.get_date_from:
                showDatePicker();
                id_btn_from=v.getId();
                break;

            case R.id.get_date_till:
                showDatePicker();
                id_btn_till=v.getId();
                break;

            case R.id.go_further:
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container_faculty, new LeaveDetailsFragment())
                        .addToBackStack(null).commit();
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

}