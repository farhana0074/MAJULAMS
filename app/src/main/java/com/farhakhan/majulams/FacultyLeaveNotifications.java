package com.farhakhan.majulams;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farhakhan.majulams.model_classes.FLnLWPnSLEmpHistory;
import com.farhakhan.majulams.model_classes.HLEmpHistory;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FacultyLeaveNotifications extends BackableFragment {


    public FacultyLeaveNotifications() {
        // Required empty public constructor
    }
    public String person_email, empFaculty, empDepartment, empDomain;
    public String strAppDateTimeOutHL, strAppDateTimeInHL, strAppDateTimeInFL,
            strAppDateTimeOutFL, strAppDateTimeInLWP, strAppDateTimeOutLWP, strAppDateTimeInSL,
            strAppDateTimeOutSL;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    private RecyclerView mRecyclerViewHL;
    private RecyclerView mRecyclerViewFL;
    private RecyclerView mRecyclerViewLWP;
    private RecyclerView mRecyclerViewSL;
    private LinearLayoutManager mLayoutManagerHL;
    private LinearLayoutManager mLayoutManagerFL;
    private LinearLayoutManager mlayoutManagerLWP;
    private LinearLayoutManager mlayoutManagerSL;
    FirebaseRecyclerAdapter<HLEmpHistory, LeaveAppNotifViewHolder> mFirebaseAdapterHL;
    FirebaseRecyclerAdapter<FLnLWPnSLEmpHistory, LeaveAppNotifViewHolder> mFirebaseAdapterFL;
    FirebaseRecyclerAdapter<FLnLWPnSLEmpHistory, LeaveAppNotifViewHolder> mFirebaseAdapterLWP;
    FirebaseRecyclerAdapter<FLnLWPnSLEmpHistory, LeaveAppNotifViewHolder> mFirebaseAdapterSL;
    private TextView tvNoNotifs;
    Date ApplyingDateTimeHL, ApplyingDateTimeFL, ApplyingDateTimeLWP, ApplyingDateTimeSL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View view = inflater.inflate(R.layout.fragment_faculty_leave_notifications, container, false);
        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();
       Bundle bundle = getArguments();

       if(bundle!= null) {
           person_email = bundle.getString("EmailID");
           empFaculty = bundle.getString("Faculty");
           empDepartment = bundle.getString("Department");
           empDomain = bundle.getString("Domain");

         tvNoNotifs = view.findViewById(R.id.tv_no_leave_notifs);

           final SimpleDateFormat AppDateTimeOutFormat = new SimpleDateFormat("dd MMM, yyyy | EEEE | h:mm a");
           final SimpleDateFormat AppDateTimeInFormat = new SimpleDateFormat("yyyy-MM-dd, h:mm");


           Query queryLeavesMain= mDbReference.child(empFaculty).child(empDepartment).child(empDomain)
                   .child(person_email).child("LeavesHistory");

           mRecyclerViewHL = view.findViewById(R.id.recycler_view_hl_notif);
           mLayoutManagerHL = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
           mRecyclerViewHL.setLayoutManager(mLayoutManagerHL);
           Query queryHL= ((DatabaseReference) queryLeavesMain).child("HalfLeaves").orderByChild("Seen")
                   .equalTo("No");

           FirebaseRecyclerOptions<HLEmpHistory> optionsHL = new FirebaseRecyclerOptions.Builder<HLEmpHistory>()
                   .setQuery(queryHL, HLEmpHistory.class)
                   .build();
           mFirebaseAdapterHL = new FirebaseRecyclerAdapter<HLEmpHistory, LeaveAppNotifViewHolder>(optionsHL) {
               @Override
               public int getItemCount() {
                   return super.getItemCount();
               }

               @Override
               protected void onBindViewHolder(@NonNull LeaveAppNotifViewHolder holder, int position, @NonNull HLEmpHistory model) {
                   if(getItemCount() == 0)
                       viewNoNotifs();
                   else
                       hideNoNotifs();
                   holder.tvLeaveType.setText("Half Leave ("+model.LeaveType+" )");
                   holder.tvLeaveApprovalStatus.setText(model.LeaveApprovalStatus);
                   strAppDateTimeInHL = model.LeaveApplyingDate+ ", "+model.LeaveApplyingTime;
                   try {
                       ApplyingDateTimeHL = AppDateTimeInFormat.parse(strAppDateTimeInHL);
                       if(ApplyingDateTimeHL !=null)
                           strAppDateTimeOutHL = AppDateTimeOutFormat.format(ApplyingDateTimeHL);
                   } catch (ParseException e) { e.printStackTrace(); }

                   holder.tvLeaveApplyingTime.setText("Applied: "+" " + strAppDateTimeOutHL);

                   final String HLKey = this.getRef(position).getKey();
                   holder.view.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                       }
                   });
               }

               @NonNull
               @Override
               public LeaveAppNotifViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                   View view1 = LayoutInflater.from(viewGroup.getContext())
                           .inflate(R.layout.leave_notification_item, viewGroup, false);

                   return new LeaveAppNotifViewHolder(view1);
               }
           };
           mRecyclerViewHL.setAdapter(mFirebaseAdapterHL);

           Query queryFL= ((DatabaseReference) queryLeavesMain).child("FullLeaves").orderByChild("Seen")
                   .equalTo("No");
           mRecyclerViewFL = view.findViewById(R.id.recycler_view_fl_notif);
           mLayoutManagerFL = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
           mRecyclerViewFL.setLayoutManager(mLayoutManagerFL);

           FirebaseRecyclerOptions<FLnLWPnSLEmpHistory> optionsFL =
                   new FirebaseRecyclerOptions.Builder<FLnLWPnSLEmpHistory>()
                           .setQuery(queryFL, FLnLWPnSLEmpHistory.class)
                           .build();
           mFirebaseAdapterFL = new FirebaseRecyclerAdapter<FLnLWPnSLEmpHistory, LeaveAppNotifViewHolder>(optionsFL) {
               @Override
               public int getItemCount() {
                   return super.getItemCount();
               }

               @Override
               protected void onBindViewHolder(@NonNull LeaveAppNotifViewHolder holder, int position, @NonNull FLnLWPnSLEmpHistory model) {
                   if(getItemCount() == 0)
                       viewNoNotifs();
                   else
                       hideNoNotifs();
                   holder.tvLeaveType.setText("Full Leave ("+model.LeaveType+" )");
                   holder.tvLeaveApprovalStatus.setText(model.LeaveApprovalStatus);
                   strAppDateTimeInFL = model.LeaveApplyingDate+", "+model.LeaveApplyingTime;
                   try {
                       ApplyingDateTimeFL = AppDateTimeInFormat.parse(strAppDateTimeInFL);
                       if(ApplyingDateTimeFL !=null)
                           strAppDateTimeOutFL = AppDateTimeOutFormat.format(ApplyingDateTimeFL);
                   } catch (ParseException e) { e.printStackTrace(); }

                   holder.tvLeaveApplyingTime.setText("Applied: "+" " + strAppDateTimeOutFL);

                   final String FLKey = this.getRef(position).getKey();
                   holder.view.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           }
                   }); }

               @NonNull
               @Override
               public LeaveAppNotifViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                   View view1 = LayoutInflater.from(viewGroup.getContext())
                           .inflate(R.layout.leave_notification_item, viewGroup, false);

                   return new LeaveAppNotifViewHolder(view1);
               }
           };
           mRecyclerViewFL.setAdapter(mFirebaseAdapterFL);

           mRecyclerViewLWP = view.findViewById(R.id.recycler_view_lwp_notif);
           mlayoutManagerLWP =new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false );
           mRecyclerViewLWP.setLayoutManager(mlayoutManagerLWP);
           Query queryLWP= ((DatabaseReference) queryLeavesMain).child("LeavesWithoutPay").orderByChild("Seen")
                   .equalTo("No");

           FirebaseRecyclerOptions<FLnLWPnSLEmpHistory> optionsLWP =
                   new FirebaseRecyclerOptions.Builder<FLnLWPnSLEmpHistory>()
                           .setQuery(queryLWP, FLnLWPnSLEmpHistory.class)
                           .build();
           mFirebaseAdapterLWP = new FirebaseRecyclerAdapter<FLnLWPnSLEmpHistory, LeaveAppNotifViewHolder>(optionsLWP) {
               @Override
               public int getItemCount() {
                   return super.getItemCount();
               }

               @Override
               protected void onBindViewHolder(@NonNull LeaveAppNotifViewHolder holder, int position, @NonNull FLnLWPnSLEmpHistory model) {
                   if(getItemCount() == 0)
                       viewNoNotifs();
                   else
                       hideNoNotifs();
                   holder.tvLeaveType.setText("Leave Without Pay");
                   holder.tvLeaveApprovalStatus.setText(model.LeaveApprovalStatus);
                   strAppDateTimeInLWP = model.LeaveApplyingDate+", "+model.LeaveApplyingTime;
                   try {
                       ApplyingDateTimeLWP = AppDateTimeInFormat.parse(strAppDateTimeInLWP);
                       if(ApplyingDateTimeLWP !=null)
                           strAppDateTimeOutLWP = AppDateTimeOutFormat.format(ApplyingDateTimeLWP);
                   } catch (ParseException e) { e.printStackTrace(); }

                   holder.tvLeaveApplyingTime.setText("Applied: "+" " + strAppDateTimeOutLWP);
                   final String LWPKey = this.getRef(position).getKey();
                   holder.view.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                       }});
               }

               @NonNull
               @Override
               public LeaveAppNotifViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                   View view1 = LayoutInflater.from(viewGroup.getContext())
                           .inflate(R.layout.leave_notification_item, viewGroup, false);

                   return new LeaveAppNotifViewHolder(view1);
               }};

           mRecyclerViewLWP.setAdapter(mFirebaseAdapterLWP);

           mRecyclerViewSL = view.findViewById(R.id.recycler_view_sl_notif);
           mlayoutManagerSL=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false );
           mRecyclerViewSL.setLayoutManager(mlayoutManagerSL);
           Query querySL= ((DatabaseReference) queryLeavesMain).child("SummerLeaves").orderByChild("Seen")
                   .equalTo("No");

           FirebaseRecyclerOptions<FLnLWPnSLEmpHistory> optionsSL =
                   new FirebaseRecyclerOptions.Builder<FLnLWPnSLEmpHistory>()
                           .setQuery(querySL, FLnLWPnSLEmpHistory.class)
                           .build();

           mFirebaseAdapterSL = new FirebaseRecyclerAdapter<FLnLWPnSLEmpHistory, LeaveAppNotifViewHolder>(optionsSL) {
               @Override
               public int getItemCount() {
                   return super.getItemCount();
               }

               @Override
               protected void onBindViewHolder(@NonNull LeaveAppNotifViewHolder holder, int position, @NonNull FLnLWPnSLEmpHistory model) {
                   if(getItemCount() == 0)
                       viewNoNotifs();
                   else
                       hideNoNotifs();
                   holder.tvLeaveType.setText("Summer Leave");
                   holder.tvLeaveApprovalStatus.setText(model.LeaveApprovalStatus);
                   strAppDateTimeInSL = model.LeaveApplyingDate+", "+model.LeaveApplyingTime;
                   try {
                       ApplyingDateTimeSL = AppDateTimeInFormat.parse(strAppDateTimeInSL);
                       if(ApplyingDateTimeSL !=null)
                           strAppDateTimeOutSL = AppDateTimeOutFormat.format(ApplyingDateTimeSL);
                   } catch (ParseException e) { e.printStackTrace(); }

                   holder.tvLeaveApplyingTime.setText("Applied: "+" " + strAppDateTimeOutSL);
                   final String SLKey = this.getRef(position).getKey();
                   holder.view.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                       }
                   });
               }

               @NonNull
               @Override
               public LeaveAppNotifViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                   View view1 = LayoutInflater.from(viewGroup.getContext())
                           .inflate(R.layout.leave_notification_item, viewGroup, false);

                   return new LeaveAppNotifViewHolder(view1);
               }
           };

           mRecyclerViewSL.setAdapter(mFirebaseAdapterSL);
       }

       return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mFirebaseAdapterHL.startListening();
        mFirebaseAdapterFL.startListening();
        mFirebaseAdapterLWP.startListening();
        mFirebaseAdapterSL.startListening();
    }

    @Override
    public void onBackButtonPressed() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(FacultyLeaveNotifications.this).commit();
        ((FacultyMainActivity) getActivity()).showFloatingActionButton();


    }

    public static class LeaveAppNotifViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvLeaveApplyingTime;
        TextView tvLeaveType;
        TextView tvLeaveApprovalStatus;

        public LeaveAppNotifViewHolder(View v) {
            super(v);
            view = v;
            tvLeaveType = v.findViewById(R.id.txt_leaveTypNotif);
            tvLeaveApplyingTime = v.findViewById(R.id.txtI_app_timeNotif);
            tvLeaveApprovalStatus = v.findViewById(R.id.txtI_ApprovalNotif);
        }
    }

    public void viewNoNotifs() {
        tvNoNotifs.setVisibility(View.VISIBLE);
    }

    public void  hideNoNotifs() {
        tvNoNotifs.setVisibility(View.INVISIBLE);
    }

}
