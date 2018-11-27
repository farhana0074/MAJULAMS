package com.farhakhan.majulams;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HalfLeaveFragment extends Fragment
implements View.OnClickListener {
    Button btnOnDate, btnGoFurther, btnBegnTime, btnEndnTime;
    SimpleDateFormat inFormatDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat outFormatDate = new SimpleDateFormat("dd MMM, yyyy");
    SimpleDateFormat inFormatTime = new SimpleDateFormat("h:mm");
    SimpleDateFormat outFormatTime = new SimpleDateFormat("h:mm a");
    Calendar calendar= Calendar.getInstance();
    int id_btn, id_btn_begin, id_btn_end;
    String strOnDate, outFormattedOnDate, outFormattedDateToday, strDateToday,
            outFormattedBTime, outFormattedETime, outFormattedNTime, strTimeEnd,
            strTimeBegin, strTimeAfter1hr, strTimeAfter5hr;
    Date onLeaveDate, dateToday, timeBegin, timeEnd, timeAfter1hr,
            timeAfter5hr;


    public HalfLeaveFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_half_leave, container, false);

        outFormattedDateToday = outFormatDate.format(calendar.getTime());

        btnOnDate =view.findViewById(R.id.get_onDate);
        btnOnDate.setText(outFormattedDateToday);
        btnOnDate.setOnClickListener(this);
        btnOnDate.setEnabled(true);

        outFormattedNTime = outFormatTime.format(calendar.getTime());

        btnBegnTime = view.findViewById(R.id.btn_begnTime);
        btnBegnTime.setText(outFormattedNTime);
        btnBegnTime.setOnClickListener(this);
        btnBegnTime.setEnabled(false);

        btnEndnTime = view.findViewById(R.id.btn_endTime);
        btnEndnTime.setText(outFormattedNTime);
        btnEndnTime.setOnClickListener(this);
        btnEndnTime.setEnabled(false);

        btnGoFurther = view.findViewById(R.id.go_further_hl);
        btnGoFurther.setOnClickListener(this);
        btnGoFurther.setEnabled(false);

        return view;   }

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
            monthOfYear = monthOfYear + 1;
           btnOnDate.setEnabled(true);
            strOnDate=String.valueOf(year)+ "-" + String.valueOf(monthOfYear)
                    + "-" + String.valueOf(dayOfMonth);
            strDateToday = inFormatDate.format(calendar.getTime());
            try {
                dateToday= inFormatDate.parse(strDateToday);
                onLeaveDate = inFormatDate.parse(strOnDate);
                if(onLeaveDate !=null)
                {
                    outFormattedOnDate = outFormatDate.format(onLeaveDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(onLeaveDate.equals(dateToday)|| onLeaveDate.after(dateToday)){
                btnOnDate.setText(outFormattedOnDate);
                btnOnDate.setTextColor(getResources().getColor(R.color.colorAccent));
                btnBegnTime.setEnabled(true);
                btnOnDate.setEnabled(false);
            }
            else {
                Toast.makeText(getContext(),"Leave Date Cannot be before Current Date", Toast.LENGTH_LONG).show();
                resetOnLeaveDate();
            }
            }
    };
    private void showTimePicker()
    {
        TimePickerFragment timePicker= new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("hours", calendar.get(Calendar.HOUR_OF_DAY));
        args.putInt("mins", calendar.get(Calendar.MINUTE));
        args.putBoolean("is24HourView", false);
        args.putInt("am_pm",calendar.get(Calendar.AM_PM));
        timePicker.setArguments(args);
        timePicker.setCallBack(ontime);
        timePicker.show(getFragmentManager(), "Time Picker");
    }

    TimePickerDialog.OnTimeSetListener ontime =(new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                if (id_btn == id_btn_begin) {
                    strTimeBegin = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                    Calendar calAfter1hr = Calendar.getInstance();
                    calAfter1hr.set(Calendar.HOUR_OF_DAY, calAfter1hr.get(Calendar.HOUR_OF_DAY) + 1);
                    strTimeAfter1hr = inFormatTime.format(calAfter1hr.getTime());
                    try {
                        timeAfter1hr = inFormatTime.parse(strTimeAfter1hr);
                        timeBegin = inFormatTime.parse(strTimeBegin);
                        if (timeBegin != null)
                            outFormattedBTime = outFormatTime.format(timeBegin);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (onLeaveDate.equals(dateToday)) {
                        if (timeBegin.before(timeAfter1hr)) {
                            Toast.makeText(getContext(), "Leave Beginning time should be at least 1 hour after Current time", Toast.LENGTH_LONG).show();
                            resetBegnTime();
                        }
                        else {
                            btnBegnTime.setText(outFormattedBTime);
                            btnBegnTime.setTextColor(getResources().getColor(R.color.colorAccent));
                            btnEndnTime.setEnabled(true);
                            btnGoFurther.setEnabled(true);
                            btnBegnTime.setEnabled(false);

                        }
                    }
                    else if(onLeaveDate.after(dateToday))
                    {
                        btnBegnTime.setText(outFormattedBTime);
                        btnBegnTime.setTextColor(getResources().getColor(R.color.colorAccent));
                        btnEndnTime.setEnabled(true);
                        btnGoFurther.setEnabled(true);
                        btnBegnTime.setEnabled(false);
                    }
                }

                else if (id_btn == id_btn_end){
                    strTimeEnd = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                    Calendar calAfter5hr = Calendar.getInstance();
                    calAfter5hr.setTime(timeBegin);
                    calAfter5hr.set(Calendar.HOUR_OF_DAY, calAfter5hr.get(calAfter5hr.HOUR_OF_DAY)+5);
                    strTimeAfter5hr =inFormatTime.format(calAfter5hr.getTime());
                    try {
                        timeAfter5hr=inFormatTime.parse(strTimeAfter5hr);
                        timeEnd = inFormatTime.parse(strTimeEnd);
                        if (timeEnd != null)
                            outFormattedETime = outFormatTime.format(timeEnd);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                        if (timeEnd.before(timeAfter5hr)) {
                            Toast.makeText(getContext(), "Leave ending time should be at least after 5 hours", Toast.LENGTH_LONG).show();
                            resetEndnTime();
                    }
                        else {
                            btnEndnTime.setText(outFormattedETime);
                            btnEndnTime.setTextColor(getResources().getColor(R.color.colorAccent));
                            btnEndnTime.setEnabled(false);
                        }
                }

        }
    });
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.get_onDate:
             showDatePicker();
                break;

            case R.id.go_further_hl:
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container_faculty, new LeaveDetailsFragment())
                        .addToBackStack(null).commit();

                break;

            case R.id.btn_begnTime:
                showTimePicker();
                id_btn_begin = v.getId();
                break;

            case R.id.btn_endTime:
                showTimePicker();
                id_btn_end = v.getId();
                break;
        }

        id_btn = v.getId();
    }
    public void resetOnLeaveDate ()
    {
        onLeaveDate= null;
        btnOnDate.setText(outFormattedDateToday);
        btnOnDate.setTextColor(getResources().getColor(R.color.Gray));
    }

    public void resetBegnTime()
    {
        timeBegin = null;
        btnBegnTime.setText(outFormattedNTime);
        btnBegnTime.setTextColor(getResources().getColor(R.color.Gray));
    }

    public void resetEndnTime()
    {
        timeEnd=null;
        btnEndnTime.setText(outFormattedNTime);
        btnEndnTime.setTextColor(getResources().getColor(R.color.Gray));

    }

}
