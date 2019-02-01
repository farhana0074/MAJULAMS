package com.farhakhan.majulams;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class TimePickerFragment extends DialogFragment {

    public TimePickerFragment(){}

    TimePickerDialog.OnTimeSetListener ontimeSet;
    private int hours, mins, am_pm;
    private boolean is24HourView;
    public void setCallBack(TimePickerDialog.OnTimeSetListener ontime){
        ontimeSet=ontime;
    }
    @SuppressLint("NewApi")
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        hours = args.getInt("hours");
        mins = args.getInt("mins");
        is24HourView =args.getBoolean("is24HourView");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return  new TimePickerDialog(getActivity(), ontimeSet, hours, mins,is24HourView);
    }
}
