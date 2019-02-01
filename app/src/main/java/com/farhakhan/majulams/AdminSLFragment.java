package com.farhakhan.majulams;


import android.content.Context;
import android.content.SharedPreferences;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.farhakhan.majulams.model_classes.SLnLWPforAdminApprovl;
import com.farhakhan.majulams.model_classes.SLnLWPforHodApprovl;
import com.farhakhan.majulams.model_classes.UserNamePic;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminSLFragment extends BackableFragment {


    public AdminSLFragment() { }

    RecyclerView recyclerViewPendingSL;
    RecyclerView recyclerViewProcessedSL;
    LinearLayoutManager lLMgrPendingSL;
    LinearLayoutManager lLMgrProcessedSL;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    FirebaseRecyclerAdapter<SLnLWPforAdminApprovl,LPrHodViewHolderSL> recyclerAdapterLPrSL ;
    FirebaseRecyclerAdapter<SLnLWPforHodApprovl, LPndHodViewHolderSL> recyclerAdapterLPndSL;
    SharedPreferences sharedPreferences;
    public static final String MY_SHARED_PREFERENCES = "MySharedPrefs";
    public static final String Faculty = "FacKey";
    public static final String Department = "DepKey";
    String strFaculty, strDepartment,  strAppDateTimeOutSL, strAppDateTimeInSL;
    Date ApplyingDateTimeSL;
    private TextView tvNoLeaves;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_sl, container, false);

        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();

        sharedPreferences = getContext().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        strFaculty = sharedPreferences.getString(Faculty, null);
        strDepartment = sharedPreferences.getString(Department, null);

        tvNoLeaves = view.findViewById(R.id.tv_no_leave_adminSL);
        final Query UserInfo = mDbReference.child("Users").child("Faculty");

        final SimpleDateFormat AppDateTimeOutFormat = new SimpleDateFormat("dd MMM, yyyy | EEEE | h:mm a");
        final SimpleDateFormat AppDateTimeInFormat = new SimpleDateFormat("yyyy-MM-dd, h:mm");

        recyclerViewProcessedSL = view.findViewById(R.id.rv_processedSL);
        lLMgrProcessedSL =new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false );
        recyclerViewProcessedSL.setLayoutManager(lLMgrProcessedSL);
        recyclerViewProcessedSL.addItemDecoration(new HeaderDecoration(getContext(), recyclerViewProcessedSL, R.layout.header_item_pr_hod));
        Query queryProcessedLWPbyHod = mDbReference.child(strFaculty).child(strDepartment).child("EmployeeLeaves")
                .child("SummerLeaves").orderByChild("LeaveApprovalStatus").equalTo("Processed By HoD");

        FirebaseRecyclerOptions<SLnLWPforAdminApprovl> optionsSLPrAdmin =
                new FirebaseRecyclerOptions.Builder<SLnLWPforAdminApprovl>()
                        .setQuery(queryProcessedLWPbyHod, SLnLWPforAdminApprovl.class)
                        .build();
        recyclerAdapterLPrSL = new FirebaseRecyclerAdapter<SLnLWPforAdminApprovl, LPrHodViewHolderSL>(optionsSLPrAdmin) {

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            protected void onBindViewHolder(@NonNull final LPrHodViewHolderSL holder, int position, @NonNull final SLnLWPforAdminApprovl model) {
                if(getItemCount() == 0)
                    viewNoLeaves();
                else
                    hideNoLeaves();
                holder.tvLeaveTypeLPr.setText("Summer Leave");
                ((DatabaseReference) UserInfo).child(model.EmployeeEmail).child("Domain").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot!= null)
                        {
                            String EmployeeDomain = dataSnapshot.getValue().toString();
                            holder.tvApplicantDomainLPr.setText(EmployeeDomain);
                            Query queryED= mDbReference.child(strFaculty).child(strDepartment).child(EmployeeDomain)
                                    .child(model.EmployeeEmail);

                            ((DatabaseReference) queryED).child("Designation").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                    if (dataSnapshot3 != null) {
                                        String EmployeeDesignation = dataSnapshot3.getValue().toString();
                                        holder.tvApplicantDesignationLPr.setText(EmployeeDesignation);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) { }});

                            ((DatabaseReference)queryED).child("Info").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    if (dataSnapshot2!=null) {
                                        UserNamePic userNamePic =dataSnapshot2.getValue(UserNamePic.class);
                                        String ApplicantName = userNamePic.Name;
                                        String ApplicantPic = userNamePic.PicUrl;
                                        holder.tvApplicantNameLPr.setText(ApplicantName);
                                        Glide.with(getContext())
                                                .load(ApplicantPic)
                                                .apply(new RequestOptions().override(100, 100))
                                                .apply(new RequestOptions().centerCrop())
                                                .into(holder.civApplicantPicLPr); } }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {}});
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }});

                strAppDateTimeInSL = model.LeaveApplyingDate+", "+model.LeaveApplyingTime;
                try {
                    ApplyingDateTimeSL = AppDateTimeInFormat.parse(strAppDateTimeInSL);
                    if(ApplyingDateTimeSL !=null)
                        strAppDateTimeOutSL = AppDateTimeOutFormat.format(ApplyingDateTimeSL);
                } catch (ParseException e) { e.printStackTrace(); }

                holder.tvLeaveApplyingTimeLPr.setText("Applied: "+" " + strAppDateTimeOutSL);

                holder.tvHodApprovalLPr.setText("HoD Approval:  "+ model.HoDApproval);

                final String LWPKey = this.getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }

            @NonNull
            @Override
            public LPrHodViewHolderSL onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.leave_app_processedbyhod_item, viewGroup, false);

                return new LPrHodViewHolderSL(view);
            }
        };
        recyclerViewProcessedSL.setAdapter(recyclerAdapterLPrSL);

        recyclerViewPendingSL = view.findViewById(R.id.rv_pendingSL);
        lLMgrPendingSL =new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false );
        recyclerViewPendingSL.setLayoutManager(lLMgrPendingSL);
        recyclerViewPendingSL.addItemDecoration(new HeaderDecoration(getContext(), recyclerViewPendingSL, R.layout.header_item_pnd_hod));
        Query queryPendingSLbyHod = mDbReference.child(strFaculty).child(strDepartment).child("EmployeeLeaves")
                .child("SummerLeaves").orderByChild("LeaveApprovalStatus").equalTo("Pending");

        FirebaseRecyclerOptions<SLnLWPforHodApprovl> optionsSLPndAdmin =
                new FirebaseRecyclerOptions.Builder<SLnLWPforHodApprovl>()
                        .setQuery(queryPendingSLbyHod, SLnLWPforHodApprovl.class)
                        .build();
        recyclerAdapterLPndSL = new FirebaseRecyclerAdapter<SLnLWPforHodApprovl, LPndHodViewHolderSL>(optionsSLPndAdmin) {

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            protected void onBindViewHolder(@NonNull final LPndHodViewHolderSL holder, int position, @NonNull final SLnLWPforHodApprovl model) {
                if(getItemCount() == 0)
                    viewNoLeaves();
                else
                    hideNoLeaves();
                holder.tvLeaveTypeLPnd.setText("Summer Leave");
                ((DatabaseReference) UserInfo).child(model.EmployeeEmail).child("Domain").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot!= null)
                        {
                            String EmployeeDomain = dataSnapshot.getValue().toString();
                            holder.tvApplicantDomainLPnd.setText(EmployeeDomain);
                            Query queryED= mDbReference.child(strFaculty).child(strDepartment).child(EmployeeDomain)
                                    .child(model.EmployeeEmail);

                            ((DatabaseReference) queryED).child("Designation").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                    if (dataSnapshot3 != null) {
                                        String EmployeeDesignation = dataSnapshot3.getValue().toString();
                                        holder.tvApplicantDesignationLPnd.setText(EmployeeDesignation);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) { }});

                            ((DatabaseReference)queryED).child("Info").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    if (dataSnapshot2!=null) {
                                        UserNamePic userNamePic =dataSnapshot2.getValue(UserNamePic.class);
                                        String ApplicantName = userNamePic.Name;
                                        String ApplicantPic = userNamePic.PicUrl;
                                        holder.tvApplicantNameLPnd.setText(ApplicantName);
                                        Glide.with(getContext())
                                                .load(ApplicantPic)
                                                .apply(new RequestOptions().override(100, 100))
                                                .apply(new RequestOptions().centerCrop())
                                                .into(holder.civApplicantPicLPnd); } }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {}});
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }});

                strAppDateTimeInSL = model.LeaveApplyingDate+", "+model.LeaveApplyingTime;
                try {
                    ApplyingDateTimeSL = AppDateTimeInFormat.parse(strAppDateTimeInSL);
                    if(ApplyingDateTimeSL !=null)
                        strAppDateTimeOutSL = AppDateTimeOutFormat.format(ApplyingDateTimeSL);
                } catch (ParseException e) { e.printStackTrace(); }

                holder.tvLeaveApplyingTimeLPnd.setText("Applied: "+" " + strAppDateTimeOutSL);
                final String LWPKey = this.getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }

            @NonNull
            @Override
            public LPndHodViewHolderSL onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.leave_app_item, viewGroup, false);

                return new LPndHodViewHolderSL(view);
            }
        };
        recyclerViewPendingSL.setAdapter(recyclerAdapterLPndSL);


        return view;
    }

    @Override
    public void onStart() {
        recyclerAdapterLPrSL.startListening();
        recyclerAdapterLPndSL.startListening();
        super.onStart();
    }
    public static class LPrHodViewHolderSL extends RecyclerView.ViewHolder {
        View view;
        TextView tvApplicantNameLPr;
        TextView tvLeaveApplyingTimeLPr;
        TextView tvApplicantDomainLPr;
        TextView tvLeaveTypeLPr;
        TextView tvApplicantDesignationLPr;
        TextView tvHodApprovalLPr;
        CircleImageView civApplicantPicLPr;

        public LPrHodViewHolderSL(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvApplicantNameLPr = itemView.findViewById(R.id.txtI_name_pr_hod);
            tvApplicantDesignationLPr = itemView.findViewById(R.id.txtI_designation_pr_hod);
            tvLeaveApplyingTimeLPr = itemView.findViewById(R.id.txtI_app_time_pr_hod);
            tvApplicantDomainLPr = itemView.findViewById(R.id.txtI_domain_pr_hod);
            tvHodApprovalLPr = itemView.findViewById(R.id.txtI_pr_hod);
            tvLeaveTypeLPr = itemView.findViewById(R.id.txt_leaveTyp_pr_hod);
            civApplicantPicLPr = itemView.findViewById(R.id.iv_person_pic_fl_pr_hod);
        }
    }

    public static class LPndHodViewHolderSL extends RecyclerView.ViewHolder {
        View view;
        TextView tvApplicantNameLPnd;
        TextView tvLeaveApplyingTimeLPnd;
        TextView tvApplicantDomainLPnd;
        TextView tvLeaveTypeLPnd;
        TextView tvApplicantDesignationLPnd;
        CircleImageView civApplicantPicLPnd;

        public LPndHodViewHolderSL (@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvApplicantNameLPnd = itemView.findViewById(R.id.txtI_name);
            tvApplicantDesignationLPnd = itemView.findViewById(R.id.txtI_designation);
            tvLeaveApplyingTimeLPnd = itemView.findViewById(R.id.txtI_app_time);
            tvApplicantDomainLPnd = itemView.findViewById(R.id.txtI_domain);
            tvLeaveTypeLPnd = itemView.findViewById(R.id.txt_leaveTyp);
            civApplicantPicLPnd = itemView.findViewById(R.id.iv_person_pic_fl);
        }
    }

    @Override
    public void onBackButtonPressed() {
        sharedPreferences = getContext().getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.clear();
        editor.commit();
        strFaculty = null;
        strDepartment = null;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(AdminSLFragment.this).commit();
        ((AdminMainActivity)getActivity()).clearBackstack();

    }
    public void viewNoLeaves() {
        tvNoLeaves.setVisibility(View.VISIBLE);
    }

    public void  hideNoLeaves() {
        tvNoLeaves.setVisibility(View.INVISIBLE);
    }

}
