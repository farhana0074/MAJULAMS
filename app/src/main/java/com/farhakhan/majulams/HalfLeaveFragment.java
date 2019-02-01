package com.farhakhan.majulams;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HalfLeaveFragment extends BackableFragment
implements View.OnClickListener {
    Button btnOnDate, btnGoFurther, btnBegnTime, btnEndnTime;
    RadioGroup radioGroup;
    SimpleDateFormat inFormatDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat outFormatDate = new SimpleDateFormat("dd MMM, yyyy");
    SimpleDateFormat inFormatTime = new SimpleDateFormat("H:mm");
    SimpleDateFormat outFormatTime = new SimpleDateFormat("h:mm a");
    SimpleDateFormat BnEcheck= new SimpleDateFormat("yyyy-MM-dd, H:mm" );
    Calendar calendar= Calendar.getInstance();
    Calendar calAftr1hr = Calendar.getInstance();
    Calendar cal8_30am = Calendar.getInstance();
    Calendar cal9pm = Calendar.getInstance();
    Calendar cal10pm =Calendar.getInstance();
    int id_btn, id_btn_begin, id_btn_end;
    String strOnDate, outFormattedOnDate, outFormattedDateToday, outFormattedBTime,
            outFormattedETime, outFormattedNTime, strTimeEnd, strTimeBegin,
            strDnBTcheck, strDnETcheck, leaveType;
    String person_email, empFaculty, empDepartment, empDomain, empDesignation;
    Date onLeaveDate, timeBegin, timeEnd, timeAftr1hr, dateBefore10days,
            DnBTcheck, DnETcheck, time8_30am, time10pm, time9pm;


    public HalfLeaveFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_half_leave, container, false);
        ((FacultyMainActivity) getActivity()).hideFloatingActionButton();

        Bundle bundle = getArguments();
        if (bundle!= null)
        {
            person_email = bundle.getString("EmailID");
            empFaculty = bundle.getString("Faculty");
            empDepartment= bundle.getString("Department");
            empDomain = bundle.getString("Domain");
            empDesignation = bundle.getString("Designation");
        }

        outFormattedDateToday = outFormatDate.format(calendar.getTime());

        btnOnDate =view.findViewById(R.id.get_onDate);
        btnOnDate.setText(outFormattedDateToday);
        btnOnDate.setOnClickListener(this);

        outFormattedNTime = outFormatTime.format(calendar.getTime());

        btnBegnTime = view.findViewById(R.id.btn_begnTime);
        btnBegnTime.setText(outFormattedNTime);
        btnBegnTime.setOnClickListener(this);

        btnEndnTime = view.findViewById(R.id.btn_endTime);
        btnEndnTime.setText(outFormattedNTime);
        btnEndnTime.setOnClickListener(this);

        btnGoFurther = view.findViewById(R.id.go_further_hl);
        btnGoFurther.setOnClickListener(this);

        radioGroup = view.findViewById(R.id.rgLeaveOpt_hl);
        radioGroup.check(R.id.rbSick_hl);
        RadioButton RbSick = view.findViewById(R.id.rbSick_hl);
        leaveType = RbSick.getText().toString();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = view.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.rbSick_hl:
                        leaveType = radioButton.getText().toString();
                        break;
                    case R.id.rbCasual_hl:
                        leaveType = radioButton.getText().toString();
                        break;
                    case R.id.rbOfVisit_hl:
                        leaveType = radioButton.getText().toString();
                        break;
                }
            }
        });

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
            strOnDate=String.valueOf(year)+ "-" + String.valueOf(monthOfYear)
                    + "-" + String.valueOf(dayOfMonth);
            Calendar calBefore10days = Calendar.getInstance();
            calBefore10days.set(Calendar.DAY_OF_MONTH, calBefore10days.get(Calendar.DAY_OF_MONTH)-10);
            dateBefore10days = calBefore10days.getTime();
            try {
                onLeaveDate = inFormatDate.parse(strOnDate);
                if(onLeaveDate !=null)
                    outFormattedOnDate = outFormatDate.format(onLeaveDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(onLeaveDate.equals(dateBefore10days)|| onLeaveDate.after(dateBefore10days)){
                btnOnDate.setText(outFormattedOnDate);
                btnOnDate.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            else {
                Toast.makeText(getContext(),"Leave Date Cannot be older than 10 days from the Current Date", Toast.LENGTH_LONG).show();
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
        timePicker.setArguments(args);
        timePicker.setCallBack(ontime);
        timePicker.show(getFragmentManager(), "Time Picker");
    }

    TimePickerDialog.OnTimeSetListener ontime =(new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            if (id_btn == id_btn_begin) {
                strTimeBegin = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);

                strDnBTcheck = strOnDate+", "+strTimeBegin;
                try {
                    DnBTcheck = BnEcheck.parse(strDnBTcheck);
                    timeBegin = inFormatTime.parse(strTimeBegin);
                    if (timeBegin != null)
                        outFormattedBTime = outFormatTime.format(timeBegin);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal8_30am.setTime(onLeaveDate);
                cal8_30am.set(Calendar.HOUR_OF_DAY, 8);
                cal8_30am.set(Calendar.MINUTE, 30);
                time8_30am= cal8_30am.getTime();

                cal9pm.setTime(onLeaveDate);
                cal9pm.set(Calendar.HOUR_OF_DAY, 21);
                time9pm= cal9pm.getTime();

          if(DnBTcheck.after(time8_30am) || DnBTcheck.equals(time8_30am))
              {
               if(DnBTcheck.before(time9pm)|| DnBTcheck.equals(time9pm))
              {
                        btnBegnTime.setText(outFormattedBTime);
                    btnBegnTime.setTextColor(getResources().getColor(R.color.colorAccent));
                   }
              else {
                    Toast.makeText(getContext(), "Leave Beginning Time should be at least 1 hr before 10:00 pm", Toast.LENGTH_LONG).show();
                       resetBegnTime();
                    }
                }
               else{
                    Toast.makeText(getContext(), "Leave Beginning Time should be after 8:30 am", Toast.LENGTH_LONG).show();
                  resetBegnTime();
              }
            }

            else if (id_btn == id_btn_end){
                strTimeEnd = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                strDnETcheck = strOnDate+", "+strTimeEnd;
                try {
                    DnETcheck = BnEcheck.parse(strDnETcheck);
                    timeEnd = inFormatTime.parse(strTimeEnd);
                    if (timeEnd != null)
                        outFormattedETime = outFormatTime.format(timeEnd);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal10pm.setTime(onLeaveDate);
                cal10pm.set(Calendar.HOUR_OF_DAY, 22);
                time10pm= cal10pm.getTime();

                if(DnETcheck.before(time10pm)|| DnETcheck.equals(time10pm)) {
                    btnEndnTime.setText(outFormattedETime);
                    btnEndnTime.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                else
                {
                    Toast.makeText(getContext(),"Leave ending time cannot be after 10:00 pm", Toast.LENGTH_LONG).show();
                    resetEndnTime();
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
                LeaveDetailsFragmentHL leaveDetailsFragment = new LeaveDetailsFragmentHL();
                Bundle bundle = new Bundle();
                bundle.putString("EmailID", person_email);
                bundle.putString("Faculty", empFaculty);
                bundle.putString("Department", empDepartment);
                bundle.putString("Domain", empDomain);
                bundle.putString("Designation", empDesignation);

                if(onLeaveDate== null || timeBegin == null )
                    Toast.makeText(getContext()," Leave Date & Leave Beginning Time must be selected", Toast.LENGTH_LONG).show();
                else if(timeEnd==null)
                {
                    bundle.putString("LeaveType", leaveType);
                    bundle.putString("LeaveDate", strOnDate );
                    bundle.putString("LeaveBeginningTime", strTimeBegin);
                    bundle.putString("LeaveEndingTime", "Nil");
                    leaveDetailsFragment.setArguments(bundle);
                   transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            .add(R.id.container_faculty, leaveDetailsFragment)
                            .addToBackStack(null).commit();
                }
                else
                {
                    calAftr1hr.setTime(DnBTcheck);
                    calAftr1hr.set(Calendar.HOUR_OF_DAY, calAftr1hr.get(Calendar.HOUR_OF_DAY) + 1);
                    timeAftr1hr = calAftr1hr.getTime();

                    if(DnETcheck.before (timeAftr1hr)) {
                        Toast.makeText(getContext(), "Leave Ending Time should be at least 1 hr later than" +
                                " the Leave Beginning Time", Toast.LENGTH_LONG).show();
                        resetEndnTime();
                    }
                    else
                    {
                        bundle.putString("LeaveType", leaveType);
                        bundle.putString("LeaveDate", strOnDate );
                        bundle.putString("LeaveBeginningTime", strTimeBegin);
                        bundle.putString("LeaveEndingTime", strTimeEnd);
                        leaveDetailsFragment.setArguments(bundle);
                        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                                .add(R.id.container_faculty, leaveDetailsFragment)
                                .addToBackStack(null).commit();
                    }
                }
                break;

            case R.id.btn_begnTime:
                if(onLeaveDate!=null)
                {
                    showTimePicker();
                    id_btn_begin = v.getId();}
                else
                    Toast.makeText(getContext(),"Set Leave Date First", Toast.LENGTH_LONG).show();
                break;

            case R.id.btn_endTime:
                if(onLeaveDate!=null && timeBegin!=null)
                {
                    showTimePicker();
                    id_btn_end = v.getId();}
                else
                    Toast.makeText(getContext(),"Set Leave Date and Leave Beginning Time First", Toast.LENGTH_LONG).show();
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
        timeBegin=null;
        btnBegnTime.setText(outFormattedNTime);
        btnBegnTime.setTextColor(getResources().getColor(R.color.Gray));
    }
    public void resetEndnTime()
    {
        timeEnd=null;
        btnEndnTime.setText(outFormattedNTime);
        btnEndnTime.setTextColor(getResources().getColor(R.color.Gray));
    }

    @Override
    public void onBackButtonPressed() {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(HalfLeaveFragment.this).commit();
        ((FacultyMainActivity) getActivity()).showFloatingActionButton();
    }
}