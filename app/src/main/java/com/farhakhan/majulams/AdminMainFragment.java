package com.farhakhan.majulams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AdminMainFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            // Please note the third parameter should be false, otherwise a java.lang.IllegalStateException maybe thrown.
            View view = inflater.inflate(R.layout.fragment_admin__main, container, false);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy ");
            String strDate = dateFormat.format(calendar.getTime());
            TextView txt_date= view.findViewById(R.id.Admin_Date);
            txt_date.setText(strDate);
            SimpleDateFormat day = new SimpleDateFormat("EEEE");
            String strDay=day.format(calendar.getTime());
            TextView txt_day = view.findViewById(R.id.Admin_Day);
            txt_day.setText(strDay);
            return view;
        }
    }