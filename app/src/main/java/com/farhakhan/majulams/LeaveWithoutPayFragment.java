package com.farhakhan.majulams;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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
    String strDateFrom, strDateTill, outFormattedDateFrom, outFormattedDateTill, strDateToday, strT;
    Date dateFrom, dateTill, dateToday;

    SimpleDateFormat inFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy");
    Calendar calendar= Calendar.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_leave_without_pay, container, false);

        strDateToday = outFormat.format(calendar.getTime());
        strT = inFormat.format(calendar.getTime());
        try {
            dateToday= inFormat.parse(strT);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        btnDateFrom = view.findViewById(R.id.get_date_from);
        btnDateFrom.setText(strDateToday);
        btnDateFrom.setOnClickListener(this);

        btnDateTill = view.findViewById(R.id.get_date_till);
        btnDateTill.setText(strDateToday);
        btnDateTill.setOnClickListener(this);

        btnGoFurther = view.findViewById(R.id.go_further);
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
                strDateFrom =String.valueOf(dayOfMonth)+ "/" + String.valueOf(monthOfYear)
                        + "/" + String.valueOf(year);
                try {
                    dateFrom = inFormat.parse(strDateFrom);
                    if(dateFrom!=null)
                    {
                    outFormattedDateFrom = outFormat.format(dateFrom);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                btnDateFrom.setText(outFormattedDateFrom);
                btnDateFrom.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            else if (id_btn==id_btn_till) {
                strDateTill =  String.valueOf(dayOfMonth)+ "/" + String.valueOf(monthOfYear)
                        + "/" + String.valueOf(year);
                try {
                    dateTill = inFormat.parse(strDateTill);
                    if(dateTill!=null)
                    {
                        outFormattedDateTill = outFormat.format(dateTill);
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                btnDateTill.setText(outFormattedDateTill);
                btnDateTill.setTextColor(getResources().getColor(R.color.colorAccent));
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
                final AlertDialog.Builder adBuilder = new AlertDialog.Builder(getContext());
                adBuilder.setIcon(R.drawable.my_alert_icon);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if(dateFrom!=null && dateTill!=null && dateToday!=null) {
                    if (dateFrom.equals(dateToday)|| dateFrom.after(dateToday))
                    {
                    if (dateFrom.before(dateTill) || dateFrom.equals(dateTill)) {
                        transaction.replace(R.id.container_faculty, new LeaveDetailsFragment())
                                .addToBackStack(null).commit();
                    } else if (dateFrom.after(dateTill)) {
                        adBuilder.setTitle("Invalid Date Input")
                                .setMessage("Leave starting date is after leave ending date!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        adBuilder.show();
                        ResetBtnText();
                    }
                }
                else
                    {
                        adBuilder.setTitle("Leave Starting Date Error")
                                .setMessage("Leave starting date cannot be before current date")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        adBuilder.show();
                        ResetBtnText();
                    }
                }

                else
                {
                        adBuilder.setTitle("Input Date Error")
                                .setMessage("You have not entered the Leave Starting Date and Ending Date Properly")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        adBuilder.show();
                    ResetBtnText();



                }
        }
        id_btn =v.getId();
    }

    public void ResetBtnText()
    {
        dateFrom=null;
        btnDateFrom.setTextColor(getResources().getColor(R.color.DarkerGray));
        btnDateFrom.setText(strDateToday);
        dateTill=null;
        btnDateTill.setTextColor(getResources().getColor(R.color.DarkerGray));
        btnDateTill.setText(strDateToday);
    }

}