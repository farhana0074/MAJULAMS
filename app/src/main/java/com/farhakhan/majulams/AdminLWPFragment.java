package com.farhakhan.majulams;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class AdminLWPFragment extends BackableFragment {


    public AdminLWPFragment() { }

    RecyclerView recyclerViewPendingLWP;
    RecyclerView recyclerViewProcessedLWP;
    LinearLayoutManager lLMgrPendingLWP;
    LinearLayoutManager lLMgrProcessedLWP;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    FirebaseRecyclerAdapter<SLnLWPforAdminApprovl,LPrHodViewHolderLWP> recyclerAdapterLPrLWP ;
    FirebaseRecyclerAdapter<SLnLWPforHodApprovl, LPndHodViewHolderLWP> recyclerAdapterLPndLWP;
    SharedPreferences sharedPreferences;
    public static final String MY_SHARED_PREFERENCES = "MySharedPrefs";
    public static final String Faculty = "FacKey";
    public static final String Department = "DepKey";
    String strFaculty, strDepartment,  strAppDateTimeOutLWP, strAppDateTimeInLWP;
    Date ApplyingDateTimeLWP;
    private TextView tvNoLeaves;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_lwp_n_sl, container, false);

        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();

        sharedPreferences = getContext().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        strFaculty = sharedPreferences.getString(Faculty, null);
        strDepartment = sharedPreferences.getString(Department, null);

        tvNoLeaves = view.findViewById(R.id.tv_no_leave_adminLWP);
        final Query UserInfo = mDbReference.child("Users").child("Faculty");

        final SimpleDateFormat AppDateTimeOutFormat = new SimpleDateFormat("dd MMM, yyyy | EEEE | h:mm a");
        final SimpleDateFormat AppDateTimeInFormat = new SimpleDateFormat("yyyy-MM-dd, h:mm");

        recyclerViewProcessedLWP = view.findViewById(R.id.rv_processedLWP);
        lLMgrProcessedLWP =new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false );
        recyclerViewProcessedLWP.setLayoutManager(lLMgrProcessedLWP);
        recyclerViewProcessedLWP.addItemDecoration(new HeaderDecoration(getContext(), recyclerViewProcessedLWP, R.layout.header_item_pr_hod));
        Query queryProcessedLWPbyHod = mDbReference.child(strFaculty).child(strDepartment).child("EmployeeLeaves")
                .child("LeavesWithoutPay").orderByChild("LeaveApprovalStatus").equalTo("Processed By HoD");

        FirebaseRecyclerOptions<SLnLWPforAdminApprovl> optionsLWPPrAdmin =
                new FirebaseRecyclerOptions.Builder<SLnLWPforAdminApprovl>()
                        .setQuery(queryProcessedLWPbyHod, SLnLWPforAdminApprovl.class)
                        .build();
        recyclerAdapterLPrLWP = new FirebaseRecyclerAdapter<SLnLWPforAdminApprovl, LPrHodViewHolderLWP >(optionsLWPPrAdmin) {

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            protected void onBindViewHolder(@NonNull final LPrHodViewHolderLWP holder, int position, @NonNull final SLnLWPforAdminApprovl model) {
                if(getItemCount() == 0)
                    viewNoLeaves();
                else
                    hideNoLeaves();
                holder.tvLeaveTypeLPr.setText("Leave Without Pay");
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

                strAppDateTimeInLWP = model.LeaveApplyingDate+", "+model.LeaveApplyingTime;
                try {
                    ApplyingDateTimeLWP = AppDateTimeInFormat.parse(strAppDateTimeInLWP);
                    if(ApplyingDateTimeLWP !=null)
                        strAppDateTimeOutLWP = AppDateTimeOutFormat.format(ApplyingDateTimeLWP);
                } catch (ParseException e) { e.printStackTrace(); }

                holder.tvLeaveApplyingTimeLPr.setText("Applied: "+" " + strAppDateTimeOutLWP);

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
            public LPrHodViewHolderLWP onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.leave_app_processedbyhod_item, viewGroup, false);

                return new LPrHodViewHolderLWP(view);
            }
        };
        recyclerViewProcessedLWP.setAdapter(recyclerAdapterLPrLWP);

        recyclerViewPendingLWP = view.findViewById(R.id.rv_pendingLWP);
        lLMgrPendingLWP =new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false );
        recyclerViewPendingLWP.setLayoutManager(lLMgrPendingLWP);
        recyclerViewPendingLWP.addItemDecoration(new HeaderDecoration(getContext(), recyclerViewPendingLWP, R.layout.header_item_pnd_hod));
        Query queryPendingLWPbyHod = mDbReference.child(strFaculty).child(strDepartment).child("EmployeeLeaves")
                .child("LeavesWithoutPay").orderByChild("LeaveApprovalStatus").equalTo("Pending");

        FirebaseRecyclerOptions<SLnLWPforHodApprovl> optionsLWPPndAdmin =
                new FirebaseRecyclerOptions.Builder<SLnLWPforHodApprovl>()
                        .setQuery(queryPendingLWPbyHod, SLnLWPforHodApprovl.class)
                        .build();
        recyclerAdapterLPndLWP = new FirebaseRecyclerAdapter<SLnLWPforHodApprovl, LPndHodViewHolderLWP >(optionsLWPPndAdmin) {

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            protected void onBindViewHolder(@NonNull final LPndHodViewHolderLWP holder, int position, @NonNull final SLnLWPforHodApprovl model) {
                if(getItemCount() == 0)
                    viewNoLeaves();
                else
                    hideNoLeaves();
                holder.tvLeaveTypeLPnd.setText("Leave Without Pay");
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

                strAppDateTimeInLWP = model.LeaveApplyingDate+", "+model.LeaveApplyingTime;
                try {
                    ApplyingDateTimeLWP = AppDateTimeInFormat.parse(strAppDateTimeInLWP);
                    if(ApplyingDateTimeLWP !=null)
                        strAppDateTimeOutLWP = AppDateTimeOutFormat.format(ApplyingDateTimeLWP);
                } catch (ParseException e) { e.printStackTrace(); }

                holder.tvLeaveApplyingTimeLPnd.setText("Applied: "+" " + strAppDateTimeOutLWP);
                final String LWPKey = this.getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }

            @NonNull
            @Override
            public LPndHodViewHolderLWP onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.leave_app_item, viewGroup, false);

                return new LPndHodViewHolderLWP(view);
            }
        };
        recyclerViewPendingLWP.setAdapter(recyclerAdapterLPndLWP);


        return view;
    }

    @Override
    public void onStart() {
        recyclerAdapterLPrLWP.startListening();
        recyclerAdapterLPndLWP.startListening();
        super.onStart();
    }
    public static class LPrHodViewHolderLWP extends RecyclerView.ViewHolder {
        View view;
        TextView tvApplicantNameLPr;
        TextView tvLeaveApplyingTimeLPr;
        TextView tvApplicantDomainLPr;
        TextView tvLeaveTypeLPr;
        TextView tvApplicantDesignationLPr;
        TextView tvHodApprovalLPr;
        CircleImageView civApplicantPicLPr;

        public LPrHodViewHolderLWP(@NonNull View itemView) {
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

    public static class LPndHodViewHolderLWP extends RecyclerView.ViewHolder {
        View view;
        TextView tvApplicantNameLPnd;
        TextView tvLeaveApplyingTimeLPnd;
        TextView tvApplicantDomainLPnd;
        TextView tvLeaveTypeLPnd;
        TextView tvApplicantDesignationLPnd;
        CircleImageView civApplicantPicLPnd;

        public LPndHodViewHolderLWP (@NonNull View itemView) {
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
        fragmentTransaction.remove(AdminLWPFragment.this).commit();
        ((AdminMainActivity)getActivity()).clearBackstack();

    }
    public void viewNoLeaves() {
        tvNoLeaves.setVisibility(View.VISIBLE);
    }

    public void  hideNoLeaves() {
        tvNoLeaves.setVisibility(View.INVISIBLE);
    }

}
