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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.farhakhan.majulams.model_classes.HLforAdminApprovl;
import com.farhakhan.majulams.model_classes.HLforHodApprovl;
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
public class AdminHLFragment extends BackableFragment {


    public AdminHLFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerViewPending;
    RecyclerView recyclerViewProcessed;
    LinearLayoutManager linearLayoutManagerPending;
    LinearLayoutManager linearLayoutManagerProcessed;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    FirebaseRecyclerAdapter<HLforAdminApprovl,LPrHodViewHolder> recyclerAdapterLPr ;
    FirebaseRecyclerAdapter<HLforHodApprovl, LPndHodViewHolder> recyclerAdapterLPnd;
    SharedPreferences sharedPreferences;
    public static final String MY_SHARED_PREFERENCES = "MySharedPrefs";
    public static final String Faculty = "FacKey";
    public static final String Department = "DepKey";
    public static final String CurrYear = "CurrYearKey";
    String strFaculty, strDepartment,  strAppDateTimeOutHL, strAppDateTimeInHL, currentYear;
    Date ApplyingDateTimeHL;
    private TextView tvNoLeaves;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_admin_hl, container, false);

        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();

        sharedPreferences = getContext().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        strFaculty = sharedPreferences.getString(Faculty, null);
        strDepartment = sharedPreferences.getString(Department, null);
        currentYear = sharedPreferences.getString(CurrYear, null);

        tvNoLeaves = view.findViewById(R.id.tv_no_leave_admin);

        final Bundle bundle = new Bundle();
        bundle.putString("strFaculty",strFaculty);
        bundle.putString("strDepartment",strDepartment);
        bundle.putString("currentYear", currentYear);

        final Query UserInfo = mDbReference.child("Users").child("Faculty");

        final SimpleDateFormat AppDateTimeOutFormat = new SimpleDateFormat("dd MMM, yyyy | EEEE | h:mm a");
        final SimpleDateFormat AppDateTimeInFormat = new SimpleDateFormat("yyyy-MM-dd, h:mm");


        recyclerViewProcessed = view.findViewById(R.id.rv_processedHL);
        linearLayoutManagerProcessed = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerViewProcessed.setLayoutManager(linearLayoutManagerProcessed);
        recyclerViewProcessed.addItemDecoration(new HeaderDecoration(getContext(), recyclerViewProcessed, R.layout.header_item_pr_hod));
        final Query queryProcessedHLbyHod = mDbReference.child(strFaculty).child(strDepartment)
                .child("EmployeeLeaves").child("HalfLeaves").orderByChild("LeaveApprovalStatus")
                .equalTo("Processed By HoD");

        FirebaseRecyclerOptions<HLforAdminApprovl> optionsHLPrAdmin =
                new FirebaseRecyclerOptions.Builder<HLforAdminApprovl>()
                        .setQuery(queryProcessedHLbyHod, HLforAdminApprovl.class)
                        .build();

        recyclerAdapterLPr = new FirebaseRecyclerAdapter<HLforAdminApprovl, LPrHodViewHolder>(optionsHLPrAdmin) {
            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
            @Override
            protected void onBindViewHolder(@NonNull final LPrHodViewHolder holder, final int position, @NonNull final HLforAdminApprovl model) {
                if(getItemCount() == 0)
                    viewNoLeaves();
                else
                    hideNoLeaves();
                holder.tvLeaveTypeLPr.setText("Half Leave ("+model.LeaveType+" )");
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
                                    if (dataSnapshot3!=null) {
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

                strAppDateTimeInHL = model.LeaveApplyingDate+ ", "+model.LeaveApplyingTime;
                try {
                    ApplyingDateTimeHL = AppDateTimeInFormat.parse(strAppDateTimeInHL);
                    if(ApplyingDateTimeHL !=null)
                        strAppDateTimeOutHL = AppDateTimeOutFormat.format(ApplyingDateTimeHL);
                } catch (ParseException e) { e.printStackTrace(); }

                holder.tvLeaveApplyingTimeLPr.setText("Applied: "+" " + strAppDateTimeOutHL);

                holder.tvHodApprovalLPr.setText("HoD Approval:  "+ model.HoDApproval);

                final String HLKey = this.getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putString( "HLKey", HLKey);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                    }});
            }

            @NonNull
            @Override
            public LPrHodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.leave_app_processedbyhod_item, viewGroup, false);

                return new LPrHodViewHolder(view);
            }
        };
        recyclerViewProcessed.setAdapter(recyclerAdapterLPr);

        recyclerViewPending = view.findViewById(R.id.rv_pendingHL);
        linearLayoutManagerPending = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerViewPending.setLayoutManager(linearLayoutManagerPending);
        recyclerViewPending.addItemDecoration(new HeaderDecoration(getContext(), recyclerViewPending, R.layout.header_item_pnd_hod));
        final Query queryPendingHLbyHod = mDbReference.child(strFaculty).child(strDepartment)
                .child("EmployeeLeaves").child("HalfLeaves").orderByChild("LeaveApprovalStatus")
                .equalTo("Pending");

        FirebaseRecyclerOptions<HLforHodApprovl> optionsHLPndAdmin =
                new FirebaseRecyclerOptions.Builder<HLforHodApprovl>()
                        .setQuery(queryPendingHLbyHod, HLforHodApprovl.class)
                        .build();

        recyclerAdapterLPnd = new FirebaseRecyclerAdapter<HLforHodApprovl, LPndHodViewHolder>(optionsHLPndAdmin) {
            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            protected void onBindViewHolder(@NonNull final LPndHodViewHolder holder, int position, @NonNull final HLforHodApprovl model) {
                if(getItemCount() == 0)
                    viewNoLeaves();
                else
                    hideNoLeaves();
                holder.tvLeaveTypeLPnd.setText("Half Leave ("+model.LeaveType+" )");
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
                                    if (dataSnapshot3!=null) {
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

                strAppDateTimeInHL = model.LeaveApplyingDate+ ", "+model.LeaveApplyingTime;
                try {
                    ApplyingDateTimeHL = AppDateTimeInFormat.parse(strAppDateTimeInHL);
                    if(ApplyingDateTimeHL !=null)
                        strAppDateTimeOutHL = AppDateTimeOutFormat.format(ApplyingDateTimeHL);
                } catch (ParseException e) { e.printStackTrace(); }

                holder.tvLeaveApplyingTimeLPnd.setText("Applied: "+" " + strAppDateTimeOutHL);

                final String HLPndKey = this.getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putString("HLPndKey", HLPndKey);
                        AdminPendingHL adminPendingHL = new AdminPendingHL();
                        adminPendingHL.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.admin_hl_frame, adminPendingHL)
                                .addToBackStack(null).commit();
                    }
                });
            }

            @NonNull
            @Override
            public LPndHodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.leave_app_item, viewGroup, false);

                return new LPndHodViewHolder(view);
            }
        };
        recyclerViewPending.setAdapter(recyclerAdapterLPnd);

        return view;
    }

    @Override
    public void onStart() {
       recyclerAdapterLPr.startListening();
       recyclerAdapterLPnd.startListening();
        super.onStart();
    }

    public static class LPrHodViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvApplicantNameLPr;
        TextView tvLeaveApplyingTimeLPr;
        TextView tvApplicantDomainLPr;
        TextView tvLeaveTypeLPr;
        TextView tvApplicantDesignationLPr;
        TextView tvHodApprovalLPr;
        CircleImageView civApplicantPicLPr;

        public LPrHodViewHolder(@NonNull View itemView) {
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

    public static class LPndHodViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvApplicantNameLPnd;
        TextView tvLeaveApplyingTimeLPnd;
        TextView tvApplicantDomainLPnd;
        TextView tvLeaveTypeLPnd;
        TextView tvApplicantDesignationLPnd;
        CircleImageView civApplicantPicLPnd;

        public LPndHodViewHolder(@NonNull View itemView) {
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
        fragmentTransaction.remove(AdminHLFragment.this).commit();
        ((AdminMainActivity)getActivity()).clearBackstack();
    }


    public void viewNoLeaves() {
        tvNoLeaves.setVisibility(View.VISIBLE);
    }

    public void  hideNoLeaves() {
        tvNoLeaves.setVisibility(View.INVISIBLE);
    }
}
