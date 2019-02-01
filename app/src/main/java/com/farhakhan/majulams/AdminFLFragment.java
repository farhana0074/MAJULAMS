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
import com.farhakhan.majulams.model_classes.FLforAdminApprovl;
import com.farhakhan.majulams.model_classes.FLforHodApprovl;
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

public class AdminFLFragment extends BackableFragment {

    public AdminFLFragment() { }

    RecyclerView recyclerViewPendingFL;
    RecyclerView recyclerViewProcessedFL;
    LinearLayoutManager lLMgrPendingFL;
    LinearLayoutManager lLMgrProcessedFL;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    FirebaseRecyclerAdapter<FLforAdminApprovl,LPrHodViewHolderFL> recyclerAdapterLPrFL ;
    FirebaseRecyclerAdapter<FLforHodApprovl, LPndHodViewHolderFL> recyclerAdapterLPndFL;
    SharedPreferences sharedPreferences;
    public static final String MY_SHARED_PREFERENCES = "MySharedPrefs";
    public static final String Faculty = "FacKey";
    public static final String Department = "DepKey";
    String strFaculty, strDepartment,  strAppDateTimeOutFL, strAppDateTimeInFL;
    Date ApplyingDateTimeFL;
    private TextView tvNoLeaves;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_admin_fl, container, false);

        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();

        sharedPreferences = getContext().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        strFaculty = sharedPreferences.getString(Faculty, null);
        strDepartment = sharedPreferences.getString(Department, null);

        tvNoLeaves = view.findViewById(R.id.tv_no_leave_adminFL);
        final Query UserInfo = mDbReference.child("Users").child("Faculty");

        final SimpleDateFormat AppDateTimeOutFormat = new SimpleDateFormat("dd MMM, yyyy | EEEE | h:mm a");
        final SimpleDateFormat AppDateTimeInFormat = new SimpleDateFormat("yyyy-MM-dd, h:mm");


        recyclerViewProcessedFL = view.findViewById(R.id.rv_processedFL);
        lLMgrProcessedFL = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerViewProcessedFL.setLayoutManager(lLMgrProcessedFL);
        recyclerViewProcessedFL.addItemDecoration(new HeaderDecoration(getContext(), recyclerViewProcessedFL, R.layout.header_item_pr_hod));
        final Query queryProcessedFLbyHod = mDbReference.child(strFaculty).child(strDepartment)
                .child("EmployeeLeaves").child("FullLeaves").orderByChild("LeaveApprovalStatus")
                .equalTo("Processed By HoD");

        FirebaseRecyclerOptions<FLforAdminApprovl> optionsFLPrAdmin =
                new FirebaseRecyclerOptions.Builder<FLforAdminApprovl>()
                        .setQuery(queryProcessedFLbyHod, FLforAdminApprovl.class)
                        .build();

        recyclerAdapterLPrFL = new FirebaseRecyclerAdapter<FLforAdminApprovl, LPrHodViewHolderFL>(optionsFLPrAdmin) {
            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            protected void onBindViewHolder(@NonNull final LPrHodViewHolderFL holder, int position, @NonNull final FLforAdminApprovl model) {
                if(getItemCount() == 0)
                    viewNoLeaves();
                else
                    hideNoLeaves();
                holder.tvLeaveTypeLPr.setText("Full Leave ("+model.LeaveType+" )");
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
                                        Glide.with(
                                                getContext())
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

                strAppDateTimeInFL = model.LeaveApplyingDate+", "+model.LeaveApplyingTime;
                try {
                    ApplyingDateTimeFL = AppDateTimeInFormat.parse(strAppDateTimeInFL);
                    if(ApplyingDateTimeFL !=null)
                        strAppDateTimeOutFL = AppDateTimeOutFormat.format(ApplyingDateTimeFL);
                } catch (ParseException e) { e.printStackTrace(); }

                holder.tvLeaveApplyingTimeLPr.setText("Applied: "+" " + strAppDateTimeOutFL);

                holder.tvHodApprovalLPr.setText("HoD Approval:  "+ model.HoDApproval);

                final String FLKey = this.getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            @NonNull
            @Override
            public LPrHodViewHolderFL onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.leave_app_processedbyhod_item, viewGroup, false);

                return new LPrHodViewHolderFL(view);
            }
        };
        recyclerViewProcessedFL.setAdapter(recyclerAdapterLPrFL);

        recyclerViewPendingFL = view.findViewById(R.id.rv_pendingFL);
        lLMgrPendingFL = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerViewPendingFL.setLayoutManager(lLMgrPendingFL);
        recyclerViewPendingFL.addItemDecoration(new HeaderDecoration(getContext(), recyclerViewPendingFL, R.layout.header_item_pnd_hod));
        final Query queryPendingFLbyHod = mDbReference.child(strFaculty).child(strDepartment)
                .child("EmployeeLeaves").child("FullLeaves").orderByChild("LeaveApprovalStatus")
                .equalTo("Pending");

        FirebaseRecyclerOptions<FLforHodApprovl> optionsFLPndAdmin =
                new FirebaseRecyclerOptions.Builder<FLforHodApprovl>()
                        .setQuery(queryPendingFLbyHod, FLforHodApprovl.class)
                        .build();

        recyclerAdapterLPndFL = new FirebaseRecyclerAdapter<FLforHodApprovl, LPndHodViewHolderFL>(optionsFLPndAdmin) {
            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            protected void onBindViewHolder(@NonNull final LPndHodViewHolderFL holder, int position, @NonNull final FLforHodApprovl model) {
                if(getItemCount() == 0)
                    viewNoLeaves();
                else
                    hideNoLeaves();
                holder.tvLeaveTypeLPnd.setText("Full Leave ("+model.LeaveType+" )");
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
                                        Glide.with(
                                                getContext())
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

                strAppDateTimeInFL = model.LeaveApplyingDate+", "+model.LeaveApplyingTime;
                try {
                    ApplyingDateTimeFL = AppDateTimeInFormat.parse(strAppDateTimeInFL);
                    if(ApplyingDateTimeFL !=null)
                        strAppDateTimeOutFL = AppDateTimeOutFormat.format(ApplyingDateTimeFL);
                } catch (ParseException e) { e.printStackTrace(); }

                holder.tvLeaveApplyingTimeLPnd.setText("Applied: "+" " + strAppDateTimeOutFL);

                final String FLKey = this.getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            @NonNull
            @Override
            public LPndHodViewHolderFL onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.leave_app_item, viewGroup, false);

                return new LPndHodViewHolderFL(view);
            }
        };
        recyclerViewPendingFL.setAdapter(recyclerAdapterLPndFL);



        return view;
    }

    @Override
    public void onStart() {
        recyclerAdapterLPrFL.startListening();
        recyclerAdapterLPndFL.startListening();
        super.onStart();
    }
    public static class LPrHodViewHolderFL extends RecyclerView.ViewHolder {
        View view;
        TextView tvApplicantNameLPr;
        TextView tvLeaveApplyingTimeLPr;
        TextView tvApplicantDomainLPr;
        TextView tvLeaveTypeLPr;
        TextView tvApplicantDesignationLPr;
        TextView tvHodApprovalLPr;
        CircleImageView civApplicantPicLPr;

        public LPrHodViewHolderFL(@NonNull View itemView) {
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

    public static class LPndHodViewHolderFL extends RecyclerView.ViewHolder {
        View view;
        TextView tvApplicantNameLPnd;
        TextView tvLeaveApplyingTimeLPnd;
        TextView tvApplicantDomainLPnd;
        TextView tvLeaveTypeLPnd;
        TextView tvApplicantDesignationLPnd;
        CircleImageView civApplicantPicLPnd;

        public LPndHodViewHolderFL(@NonNull View itemView) {
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
        fragmentTransaction.remove(AdminFLFragment.this).commit();
         ((AdminMainActivity)getActivity()).clearBackstack();

    }
    public void viewNoLeaves() {
        tvNoLeaves.setVisibility(View.VISIBLE);
    }

    public void  hideNoLeaves() {
        tvNoLeaves.setVisibility(View.INVISIBLE);
    }

}
