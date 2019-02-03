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

import com.farhakhan.majulams.model_classes.FLAftrAdminResponse;
import com.farhakhan.majulams.model_classes.HLAftrAdminResponse;
import com.farhakhan.majulams.model_classes.SLnLWAftrAdminResponse;
import com.farhakhan.majulams.model_classes.SLnLWPforAdminApprovl;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class FacultyHistoryDetails extends BackableFragment {


    public FacultyHistoryDetails() {
        // Required empty public constructor
    }
    String person_email, empFaculty, empDepartment, empDomain;
    String TAG, LeaveName;
    public String strAppDateTimeOutHL, strAppDateTimeInHL, strAppDateTimeInFL,
            strAppDateTimeOutFL, strAppDateTimeInLWP, strAppDateTimeOutLWP, strAppDateTimeInSL,
            strAppDateTimeOutSL, strBInTime, strBOutTime, strEInTime, strEOutTime, strLeaveInDate,
            strLeaveOutDate, strBInDate, strBOutDate, strEInDate, strEOutDate;
    RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    LinearLayoutManager mLinearLayoutManager;
    FirebaseRecyclerAdapter <HLAftrAdminResponse, ViewHolderHL> mFirebaseAdapterHL;
    FirebaseRecyclerAdapter <FLAftrAdminResponse, ViewHolderOthers> mFirebaseAdapterFL;
    FirebaseRecyclerAdapter<SLnLWAftrAdminResponse, ViewHolderOthers> mFirebaseAdapterLWP;
    FirebaseRecyclerAdapter<SLnLWAftrAdminResponse, ViewHolderOthers> mFirebaseAdapterSL;
    Date ApplyingDateTimeHL, ApplyingDateTimeFL, ApplyingDateTimeLWP, ApplyingDateTimeSL,  BTime, ETime,
            LeaveDate, BDate, EDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faculty_history_details, container, false);

        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();
        Bundle bundle = getArguments();
        if(bundle!= null)
        {
            person_email = bundle.getString("person_email");
            empFaculty = bundle.getString("empFaculty");
            empDepartment = bundle.getString("empDepartment");
            empDomain = bundle.getString("empDomain");
            TAG = bundle.getString("TAG");

            if(TAG == "HL")
                LeaveName = "Half Leaves";
            else if(TAG == "FL")
                LeaveName = "Full Leaves";
            else if (TAG == "LWP")
                LeaveName = "Leaves Without Pay";
            else if (TAG == "SL")
                LeaveName = "Summer Leaves";

            final SimpleDateFormat AppDateTimeOutFormat = new SimpleDateFormat("dd MMM, yyyy | EEEE | h:mm a");
            final SimpleDateFormat AppDateTimeInFormat = new SimpleDateFormat("yyyy-MM-dd, h:mm");
            final SimpleDateFormat TimeInFormat = new SimpleDateFormat("HH:mm");
            final SimpleDateFormat TimeOutFormat = new SimpleDateFormat("h:mm a");
            final SimpleDateFormat DateOutFormat = new SimpleDateFormat("dd MMM, yyyy, EEEE");
            final SimpleDateFormat DateInFormat = new SimpleDateFormat("yyyy-MM-dd");

            TextView tvLeavetypname = view.findViewById(R.id.tvLeaveTypeName);

            Query queryLeavesMain= mDbReference.child(empFaculty).child(empDepartment).child(empDomain)
                    .child(person_email).child("LeavesHistory");

            mRecyclerView = view.findViewById(R.id.rv_HistoryDetails);
            mLinearLayoutManager =  new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            if(TAG == "HL")
            {
                LeaveName = "Half Leaves";
                tvLeavetypname.setText(LeaveName);
                Query queryHL = ((DatabaseReference) queryLeavesMain).child("HalfLeaves");
                FirebaseRecyclerOptions<HLAftrAdminResponse> optionsHL = new FirebaseRecyclerOptions.Builder<HLAftrAdminResponse>()
                        .setQuery(queryHL, HLAftrAdminResponse.class)
                        .build();
                mFirebaseAdapterHL = new FirebaseRecyclerAdapter<HLAftrAdminResponse, ViewHolderHL>(optionsHL) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderHL holder, int position, @NonNull HLAftrAdminResponse model) {
                        holder.tvLeaveType.setText("Half Leave ("+model.LeaveType+")");
                        holder.tvLeaveStatus.setText(model.LeaveApprovalStatus);
                        holder.tvLeaveApprovalStatus.setText(model.AdminApproval);
                        holder.tvLeaveComments.setText(model.CommentsOpt);
                        holder.tvManagingMethod.setText(model.HowToCover);
                        strBInTime = model.LeaveBeginningTime;
                        try {
                            BTime = TimeInFormat.parse(strBInTime);

                            if(BTime != null)
                                strBOutTime = TimeOutFormat.format(BTime);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        holder.tvBTime.setText(strBOutTime);

                        strLeaveInDate = model.LeaveDate;
                        try {
                            LeaveDate = DateInFormat.parse(strLeaveInDate);

                            if (LeaveDate!= null)
                                strLeaveOutDate = DateOutFormat.format(LeaveDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        holder.tvLeaveDate.setText(strLeaveOutDate);

                        strEInTime = model.LeaveEndingTime;
                        if(strEInTime.equals("Nil"))
                            holder.tvETime.setText(strEInTime);
                        else {
                            try {
                                ETime = TimeInFormat.parse(strEInTime);
                                if (ETime != null)
                                    strEOutTime = TimeOutFormat.format(ETime);
                                else
                                    strEOutTime = strEInTime;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            holder.tvETime.setText(strEOutTime);
                        }

                        strAppDateTimeInHL = model.LeaveApplyingDate+ ", "+model.LeaveApplyingTime;
                        try {
                            ApplyingDateTimeHL = AppDateTimeInFormat.parse(strAppDateTimeInHL);
                            if(ApplyingDateTimeHL !=null)
                                strAppDateTimeOutHL = AppDateTimeOutFormat.format(ApplyingDateTimeHL);
                        } catch (ParseException e) { e.printStackTrace(); }

                        holder.ApplyingDateTime.setText("Applied: "+" " + strAppDateTimeOutHL);
                    }

                    @NonNull
                    @Override
                    public ViewHolderHL onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view1 = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.faculty_hl_history_item, viewGroup, false);
                        return new ViewHolderHL(view1);
                    }
                };
                mRecyclerView.setAdapter(mFirebaseAdapterHL);

            }
            else if (TAG== "FL")
            {
                LeaveName = "Full Leaves";
                tvLeavetypname.setText(LeaveName);
                Query queryFL = ((DatabaseReference) queryLeavesMain).child("FullLeaves");
                FirebaseRecyclerOptions<FLAftrAdminResponse> optionsFL = new FirebaseRecyclerOptions.Builder<FLAftrAdminResponse>()
                        .setQuery(queryFL, FLAftrAdminResponse.class)
                        .build();
                mFirebaseAdapterFL = new FirebaseRecyclerAdapter<FLAftrAdminResponse, ViewHolderOthers>(optionsFL) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderOthers holder, int position, @NonNull FLAftrAdminResponse model) {
                        holder.tvLeaveType.setText("Full Leave ("+model.LeaveType+")");
                        holder.tvLeaveStatus.setText(model.LeaveApprovalStatus);
                        holder.tvLeaveApprovalStatus.setText(model.AdminApproval);
                        holder.tvLeaveComments.setText(model.CommentsOpt);
                        holder.tvManagingMethod.setText(model.HowToCover);
                        strBInDate = model.LeaveBeginningDate;
                        try {
                            BDate = DateInFormat.parse(strBInDate);

                            if(BDate != null)
                                strBOutDate = DateOutFormat.format(BDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        holder.tvBDate.setText(strBOutDate);

                        strEInDate= model.LeaveEndingDate;
                        try {
                            EDate = DateInFormat.parse(strEInDate);

                            if (LeaveDate!= null)
                                strEOutDate = DateOutFormat.format(EDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        holder.tvEDate.setText(strBOutDate);

                        strAppDateTimeInFL = model.LeaveApplyingDate+ ", "+model.LeaveApplyingTime;
                        try {
                            ApplyingDateTimeFL = AppDateTimeInFormat.parse(strAppDateTimeInFL);
                            if(ApplyingDateTimeFL !=null)
                                strAppDateTimeOutFL = AppDateTimeOutFormat.format(ApplyingDateTimeFL);
                        } catch (ParseException e) { e.printStackTrace(); }

                        holder.ApplyingDateTime.setText("Applied: "+" " + strAppDateTimeOutFL);
                    }

                    @NonNull
                    @Override
                    public ViewHolderOthers onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view1 = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.faculty_fl_lwp_sl_history_item, viewGroup, false);
                        return new ViewHolderOthers(view1);
                    }
                };
                mRecyclerView.setAdapter(mFirebaseAdapterFL);
            }

            else if(TAG == "LWP")
            {
                LeaveName = "Leaves Without Pay";
                tvLeavetypname.setText(LeaveName);
                Query queryLWP = ((DatabaseReference) queryLeavesMain).child("LeavesWithoutPay");
                FirebaseRecyclerOptions<SLnLWAftrAdminResponse> optionsLWP = new FirebaseRecyclerOptions.Builder<SLnLWAftrAdminResponse>()
                        .setQuery(queryLWP, SLnLWAftrAdminResponse.class)
                        .build();
                mFirebaseAdapterLWP = new FirebaseRecyclerAdapter<SLnLWAftrAdminResponse, ViewHolderOthers>(optionsLWP) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderOthers holder, int position, @NonNull SLnLWAftrAdminResponse model) {
                        holder.tvLeaveType.setText("Leave Without Pay");
                        holder.tvLeaveStatus.setText(model.LeaveApprovalStatus);
                        holder.tvLeaveApprovalStatus.setText(model.AdminApproval);
                        holder.tvLeaveComments.setText(model.CommentsOpt);
                        holder.tvManagingMethod.setText(model.HowToCover);
                        strBInDate = model.LeaveBeginningDate;
                        try {
                            BDate = DateInFormat.parse(strBInDate);

                            if(BDate != null)
                                strBOutDate = DateOutFormat.format(BDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        holder.tvBDate.setText(strBOutDate);

                        strEInDate= model.LeaveEndingDate;
                        try {
                            EDate = DateInFormat.parse(strEInDate);

                            if (LeaveDate!= null)
                                strEOutDate = DateOutFormat.format(EDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        holder.tvEDate.setText(strBOutDate);

                        strAppDateTimeInLWP = model.LeaveApplyingDate+ ", "+model.LeaveApplyingTime;
                        try {
                            ApplyingDateTimeLWP = AppDateTimeInFormat.parse(strAppDateTimeInLWP);
                            if(ApplyingDateTimeLWP !=null)
                                strAppDateTimeOutLWP = AppDateTimeOutFormat.format(ApplyingDateTimeLWP);
                        } catch (ParseException e) { e.printStackTrace(); }

                        holder.ApplyingDateTime.setText("Applied: "+" " + strAppDateTimeOutLWP);
                    }

                    @NonNull
                    @Override
                    public ViewHolderOthers onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view1 = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.faculty_fl_lwp_sl_history_item, viewGroup, false);
                        return new ViewHolderOthers(view1);
                    }
                };
                mRecyclerView.setAdapter(mFirebaseAdapterLWP);
            }

            else if(TAG == "SL")
            {
                LeaveName = "Summer Leaves";
                tvLeavetypname.setText(LeaveName);
                Query querySL = ((DatabaseReference) queryLeavesMain).child("SummerLeaves");
                FirebaseRecyclerOptions<SLnLWAftrAdminResponse> optionsSL = new FirebaseRecyclerOptions.Builder<SLnLWAftrAdminResponse>()
                        .setQuery(querySL, SLnLWAftrAdminResponse.class)
                        .build();
                mFirebaseAdapterSL = new FirebaseRecyclerAdapter<SLnLWAftrAdminResponse, ViewHolderOthers>(optionsSL) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderOthers holder, int position, @NonNull SLnLWAftrAdminResponse model) {
                        holder.tvLeaveType.setText("Summer Leave");
                        holder.tvLeaveStatus.setText(model.LeaveApprovalStatus);
                        holder.tvLeaveApprovalStatus.setText(model.AdminApproval);
                        holder.tvLeaveComments.setText(model.CommentsOpt);
                        holder.tvManagingMethod.setText(model.HowToCover);
                        strBInDate = model.LeaveBeginningDate;
                        try {
                            BDate = DateInFormat.parse(strBInDate);

                            if(BDate != null)
                                strBOutDate = DateOutFormat.format(BDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        holder.tvBDate.setText(strBOutDate);

                        strEInDate= model.LeaveEndingDate;
                        try {
                            EDate = DateInFormat.parse(strEInDate);

                            if (LeaveDate!= null)
                                strEOutDate = DateOutFormat.format(EDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        holder.tvEDate.setText(strBOutDate);

                        strAppDateTimeInSL= model.LeaveApplyingDate+ ", "+model.LeaveApplyingTime;
                        try {
                            ApplyingDateTimeSL = AppDateTimeInFormat.parse(strAppDateTimeInSL);
                            if(ApplyingDateTimeSL !=null)
                                strAppDateTimeOutSL = AppDateTimeOutFormat.format(ApplyingDateTimeSL);
                        } catch (ParseException e) { e.printStackTrace(); }

                        holder.ApplyingDateTime.setText("Applied: "+" " + strAppDateTimeOutSL);
                    }

                    @NonNull
                    @Override
                    public ViewHolderOthers onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view1 = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.faculty_fl_lwp_sl_history_item, viewGroup, false);
                        return new ViewHolderOthers(view1);
                    }
                };
                mRecyclerView.setAdapter(mFirebaseAdapterSL);
            }

        }

        return view;
    }

    @Override
    public void onStart() {
        if(TAG=="HL")
        mFirebaseAdapterHL.startListening();
       else if (TAG == "FL")
        mFirebaseAdapterFL.startListening();
       else if (TAG == "LWP")
           mFirebaseAdapterLWP.startListening();
       else if(TAG == "SL")
           mFirebaseAdapterSL.startListening();
        super.onStart();
    }

    public static class ViewHolderHL extends RecyclerView.ViewHolder {
        View view;
        TextView tvLeaveDate;
        TextView tvBTime;
        TextView tvETime;
        TextView tvManagingMethod;
        TextView tvLeaveType;
        TextView tvLeaveStatus;
        TextView tvLeaveApprovalStatus;
        TextView tvLeaveComments;
        TextView ApplyingDateTime;

        public ViewHolderHL(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvLeaveDate = itemView.findViewById(R.id.tvLeave_DateIHL);
            tvBTime = itemView.findViewById(R.id.tvLeave_BTimeIHL);
            tvETime = itemView.findViewById(R.id.tvLeave_ETimeIHL);
            tvManagingMethod = itemView.findViewById(R.id.tvLeave_C_OptIHL);
            tvLeaveType = itemView.findViewById(R.id.txt_leaveTypIHL);
            tvLeaveStatus = itemView.findViewById(R.id.txtI_Leave_StatusIHL);
            tvLeaveComments = itemView.findViewById(R.id.tvCommentsIHL);
            tvLeaveApprovalStatus = itemView.findViewById(R.id.txtI_Leave_ApprovalStatusIHL);
            ApplyingDateTime = itemView.findViewById(R.id.txtI_app_timeIHL);

        }
    }

    public static class ViewHolderOthers extends RecyclerView.ViewHolder {
        View view;
        TextView tvBDate;
        TextView tvEDate;
        TextView tvManagingMethod;
        TextView tvLeaveType;
        TextView tvLeaveStatus;
        TextView tvLeaveApprovalStatus;
        TextView tvLeaveComments;
        TextView ApplyingDateTime;

        public ViewHolderOthers(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvBDate = itemView.findViewById(R.id.tvLeave_BDateOthers);
            tvEDate = itemView.findViewById(R.id.tvLeave_EDateOthers);
            tvManagingMethod = itemView.findViewById(R.id.tvLeave_C_OptOthers);
            tvLeaveType = itemView.findViewById(R.id.txt_leaveTypIHLOthers);
            tvLeaveStatus = itemView.findViewById(R.id.txtI_Leave_StatusOthers);
            tvLeaveComments = itemView.findViewById(R.id.tvCommentsOthers);
            tvLeaveApprovalStatus = itemView.findViewById(R.id.txtI_Leave_ApprovalStatusIOthers);
            ApplyingDateTime = itemView.findViewById(R.id.txt_appTimeOthers);
        }
    }

    @Override
    public void onBackButtonPressed() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(FacultyHistoryDetails.this).commit();

    }
}
