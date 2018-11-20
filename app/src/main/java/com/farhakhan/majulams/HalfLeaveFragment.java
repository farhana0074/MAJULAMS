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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HalfLeaveFragment extends Fragment
implements View.OnClickListener {
    Button btnOnDate, btnGoFurther;
    Spinner spnSlotFrom, spnSlotTill;
    SimpleDateFormat inFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy");
    Calendar calendar= Calendar.getInstance();
    String[] items1 = new String[]{"1", "2", "3", "4", "5"};
    String[] items2 = new String[]{"2","3", "4", "5"};
    int startSlot, endSlot;
    String strOnDate, outFormattedOnDate, strDateToday, strT;
    Date onLeaveDate, dateToday;

    public HalfLeaveFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_half_leave, container, false);
        strDateToday = outFormat.format(calendar.getTime());
        strT = inFormat.format(calendar.getTime());
        try {
            dateToday= inFormat.parse(strT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spnSlotFrom= view.findViewById(R.id.spinner1);

        btnOnDate =view.findViewById(R.id.get_onDate);
        btnOnDate.setText(strDateToday);
        btnOnDate.setOnClickListener(this);

        btnGoFurther = view.findViewById(R.id.go_further_hl);
        btnGoFurther.setOnClickListener(this);

        spnSlotTill =view.findViewById(R.id.spinner2);
        initSpinner1();
        initSpinner2();

        return view;   }
    private void initSpinner1() {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, items1);
        spnSlotFrom.setAdapter(arrayAdapter);
        spnSlotFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorAccent));
                startSlot= Integer.parseInt(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        }
        private void initSpinner2()
        {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, items2);
            spnSlotTill.setAdapter(arrayAdapter);
            spnSlotTill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorAccent));
                    endSlot= Integer.parseInt(parent.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
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
            monthOfYear = monthOfYear + 1;

            strOnDate=String.valueOf(dayOfMonth)+ "/" + String.valueOf(monthOfYear)
                    + "/" + String.valueOf(year);
            try {
                onLeaveDate = inFormat.parse(strOnDate);
                if(onLeaveDate !=null)
                {
                    outFormattedOnDate = outFormat.format(onLeaveDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            btnOnDate.setText(outFormattedOnDate);
            btnOnDate.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.get_onDate:
                showDatePicker();
                break;

            case R.id.go_further_hl:
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                final AlertDialog.Builder adBuilder = new AlertDialog.Builder(getContext());
                adBuilder.setIcon(R.drawable.my_alert_icon);
                if(onLeaveDate!= null && dateToday!=null)
                {
                    if (onLeaveDate.before(dateToday) && (startSlot<endSlot))
                    {
                        adBuilder.setTitle("Date Input Error")
                                .setMessage("Leave date cannot be before current date")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        adBuilder.show();
                        ResetBtnText();
                    }
                    else if(onLeaveDate.after(dateToday) && (startSlot<endSlot))
                    {
                        transaction.replace(R.id.container_faculty, new LeaveDetailsFragment())
                         .addToBackStack(null).commit();
                    }
                    else if ((onLeaveDate.equals(dateToday)|| onLeaveDate.after(dateToday))&& (startSlot>endSlot))
                    {
                        adBuilder.setTitle("Slot Input Error")
                                .setMessage("Leave starting slot cannot be after Leave ending slot")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        adBuilder.show();
                    }
                    else
                    {
                        adBuilder.setTitle("Input Error")
                                .setMessage("You have not inserted input values properly!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        adBuilder.show();
                    }
                }
                break;
        }
    }
    public void ResetBtnText ()
    {
        btnOnDate.setText(strDateToday);
        btnOnDate.setTextColor(getResources().getColor(R.color.DarkerGray));
    }
}
