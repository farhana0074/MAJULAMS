package com.farhakhan.majulams;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.farhakhan.majulams.model_classes.SLnLWPforAdminApprovl;
import com.farhakhan.majulams.model_classes.SLnLWPforHodApprovl;
import com.farhakhan.majulams.model_classes.UserNamePic;
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
public class AdminProcessedSL extends BackableFragment {


    public AdminProcessedSL() {
        // Required empty public constructor
    }
    String person_email, currentYear, ApplicantEmail, ApplicantDesignation, ApplicantName, ApplicantPic, SLKey,
            ApplicantDomain, empDepartment, empFaculty, strComment;
    String  strAppInDate, strAppOutDate,strAppInTime, strAppOutTime,
            strBInDate, strBOutDate, strEInDate, strEOutDate, LeaveManage, LeaveComments, HodComments, HodApprovl;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    Date AppDate, AppTime, BDate, EDate;
    private long totalSL, updatedSL, diff, diffDates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_admin_processed_lwp_n_sl, container, false);
        Bundle bundle = getArguments();
        if (bundle!= null)
        {
            empFaculty = bundle.getString("strFaculty");
            empDepartment = bundle.getString("strDepartment");
            currentYear = bundle.getString("currentYear");
            SLKey = bundle.getString("SLPrKey");
        }
        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();

        TextView LeaveType = view.findViewById(R.id.textView2_admin_pr_lwpNsl);
        LeaveType.setText("Summer Leave");

        final SimpleDateFormat DateOutFormat = new SimpleDateFormat("dd MMM, yyyy, EEEE");
        final SimpleDateFormat DateInFormat = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat TimeInFormat = new SimpleDateFormat("HH:mm");
        final SimpleDateFormat TimeOutFormat = new SimpleDateFormat("h:mm a");

        final Query querySLNode = mDbReference.child(empFaculty).child(empDepartment).child("EmployeeLeaves").child("SummerLeaves")
                .child(SLKey);

        querySLNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final SLnLWPforAdminApprovl slforAdminApprovl = dataSnapshot.getValue(SLnLWPforAdminApprovl.class);
                ApplicantEmail = slforAdminApprovl.EmployeeEmail;
                mDbReference.child("Users").child("Faculty").child(ApplicantEmail).child("Domain")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                ApplicantDomain = dataSnapshot2.getValue().toString();
                                TextView tvApplicantDomainSL = view.findViewById(R.id.tvApplicant_domain_admin_pr_lwpNsl);
                                tvApplicantDomainSL.setText(ApplicantDomain);

                                Query queryInfo = mDbReference.child(empFaculty).child(empDepartment).child(ApplicantDomain)
                                        .child(ApplicantEmail);
                                ((DatabaseReference) queryInfo).child("Info").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                        UserNamePic userNamePic =dataSnapshot3.getValue(UserNamePic.class);
                                        ApplicantName = userNamePic.Name;

                                        TextView tvApplicantNameSL = view.findViewById(R.id.tvApplicant_name_admin_pr_lwpNsl);
                                        tvApplicantNameSL.setText(ApplicantName);

                                        ApplicantPic = userNamePic.PicUrl;
                                        CircleImageView civImage = view.findViewById(R.id.iv_person_pic_admin_pr_lwpNsl);
                                        Glide.with(getContext())
                                                .load(ApplicantPic)
                                                .apply(new RequestOptions().override(100, 100))
                                                .apply(new RequestOptions().centerCrop())
                                                .into(civImage);

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }});

                                ((DatabaseReference) queryInfo).child("Designation").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot4) {
                                        ApplicantDesignation =  dataSnapshot4.getValue().toString();
                                        TextView tvApplicantDesigSL = view.findViewById(R.id.tvApplicant_desig_admin_pr_lwpNsl);
                                        tvApplicantDesigSL.setText(ApplicantDesignation);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }});
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }});


                strAppInTime = slforAdminApprovl.LeaveApplyingTime;
                try {
                    AppTime = TimeInFormat.parse(strAppInTime);
                    if(AppTime != null)
                        strAppOutTime = TimeOutFormat.format(AppTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                TextView tvAppTime = view.findViewById(R.id.tvAppTime_admin_pr_lwpNsl);
                tvAppTime.setText(strAppOutTime);

                strAppInDate = slforAdminApprovl.LeaveApplyingDate;
                try {
                    AppDate = DateInFormat.parse(strAppInDate);
                    if(AppDate!= null)
                        strAppOutDate = DateOutFormat.format(AppDate);
                } catch (ParseException e) {
                    e.printStackTrace(); }

                TextView tvAppDate = view.findViewById(R.id.tvAppDate_admin_pr_lwpNsl);
                tvAppDate.setText(strAppOutDate);

                strBInDate = slforAdminApprovl.LeaveBeginningDate;
                try {
                    BDate = DateInFormat.parse(strBInDate);
                    if(BDate!= null)
                        strBOutDate = DateOutFormat.format(BDate);
                } catch (ParseException e) {
                    e.printStackTrace(); }

                TextView tvBDate = view.findViewById(R.id.tvLeave_BDate_admin_pr_lwpNsl);
                tvBDate.setText(strBOutDate);

                strEInDate = slforAdminApprovl.LeaveEndingDate;
                try {
                    EDate = DateInFormat.parse(strEInDate);
                    if(EDate !=null)
                        strEOutDate = DateOutFormat.format(EDate);
                } catch (ParseException e) {
                    e.printStackTrace(); }

                TextView tvEDate = view.findViewById(R.id.tvLeave_EDate_admin_pr_lwpNsl);
                tvEDate.setText(strEOutDate);

                diff = Math.abs(EDate.getTime() - BDate.getTime());
                diffDates = diff/(24*60*60*1000);
                if(diffDates == 0)
                    diffDates = 1;

                TextView tvTotalLeaveDays = view.findViewById(R.id.tv_TotalLvDays_admin_pr_lwpNsl);
                tvTotalLeaveDays.setText("Total Leave Days: " + diffDates );


                LeaveManage = slforAdminApprovl.HowToCover;
                TextView tvManage = view.findViewById(R.id.tvLeave_C_Opt_admin_pr_lwpNsl);
                tvManage.setText(LeaveManage);

                LeaveComments = slforAdminApprovl.CommentsOpt;
                TextView tvComments = view.findViewById(R.id.tvComments_admin_pr_lwpNsl);
                tvComments.setText(LeaveComments);

                HodComments = slforAdminApprovl.HodComments;
                TextView tvHodComments = view.findViewById(R.id.tvCommentsHod_admin_pr_lwpNsl);
                tvHodComments.setText(HodComments);

                HodApprovl = slforAdminApprovl.HoDApproval;
                TextView tvHodApprovl = view.findViewById(R.id.textView2_admin_pr_approval_lwpNsl);
                tvHodApprovl.setText(HodApprovl+" by HoD");

                BottomNavigationView bnvHodSummersLeave = view.findViewById(R.id.bnv_admin_pr_lwpNsl);
                bnvHodSummersLeave.getMenu().getItem(0).setCheckable(false);
                bnvHodSummersLeave.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if(id == R.id.menu_item_profile)
                        {
                            menuItem.setCheckable(true);
                            return true;
                        }
                        else if(id == R.id.menu_item_approve)
                        {
                            menuItem.setCheckable(true);
                            final Dialog dialog = new Dialog(getContext(),R.style.MyAlertDialogTheme);
                            dialog.setTitle("Approve");
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setContentView(R.layout.hod_approve_disapprove_dialog);
                            Button btnCancel = dialog.findViewById(R.id.btn_cancel);
                            Button btnApprove = dialog.findViewById(R.id.btn_approve);
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    menuItem.setCheckable(false);
                                }
                            });

                            btnApprove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditText etComments =dialog.findViewById(R.id.et_comments);
                                    if(!isEmpty(etComments)) {
                                        strComment = etComments.getText().toString().trim();
                                        ((DatabaseReference) querySLNode).child("AdminComments")
                                                .setValue(strComment);
                                        ((DatabaseReference) querySLNode).child("LeaveApprovalStatus")
                                                .setValue("Processed By Admin");
                                        ((DatabaseReference) querySLNode).child("AdminApproval").setValue("Approved");

                                        Query queryEmp = mDbReference.child(empFaculty).child(empDepartment).child(ApplicantDomain)
                                                .child(ApplicantEmail).child("LeavesHistory").child("LeavesWithoutPay")
                                                .child(SLKey);

                                        ((DatabaseReference) queryEmp).child("LeaveApprovalStatus")
                                                .setValue("Processed");
                                        ((DatabaseReference) queryEmp).child("AdminApproval").setValue("Approved");
                                        ((DatabaseReference) queryEmp).child("Seen").setValue("No");


                                        final Query querySLStatus = mDbReference.child(empFaculty).child(empDepartment)
                                                .child(ApplicantDomain).child(ApplicantEmail).child("LeavesStatus")
                                                .child("SummerLeaves");

                                        querySLStatus.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshotStatus) {
                                                if(dataSnapshotStatus!=null)
                                                {
                                                    totalSL = ((long) dataSnapshotStatus.getValue());
                                                    updatedSL = totalSL + diffDates;
                                                    ((DatabaseReference) querySLStatus).setValue(updatedSL);
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) { }});


                                        Toast.makeText(getContext(), "Approved", Toast.LENGTH_LONG).show();
                                        onBackButtonPressed();
                                        dialog.dismiss();
                                    }

                                    else
                                        Toast.makeText(getContext(), "Comments are Mandatory", Toast.LENGTH_LONG).show();
                                }
                            });
                            dialog.show();
                            return true;
                        }
                        else if(id == R.id.menu_item_disapprove)
                        {
                            menuItem.setCheckable(true);
                            final Dialog dialog = new Dialog(getContext(),R.style.MyAlertDialogTheme);
                            dialog.setTitle("Disapprove");
                            dialog.setContentView(R.layout.hod_approve_disapprove_dialog);
                            dialog.setCanceledOnTouchOutside(false);
                            Button btnCancel = dialog.findViewById(R.id.btn_cancel);
                            Button btnApprove = dialog.findViewById(R.id.btn_approve);
                            btnApprove.setText("Disapprove");
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    menuItem.setCheckable(false);
                                }
                            });

                            btnApprove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditText etComments =dialog.findViewById(R.id.et_comments);
                                    if(!isEmpty(etComments)) {
                                        strComment = etComments.getText().toString().trim();
                                        ((DatabaseReference) querySLNode).child("HodComments")
                                                .setValue(strComment);
                                        ((DatabaseReference) querySLNode).child("LeaveApprovalStatus")
                                                .setValue("Processed By HoD");
                                        ((DatabaseReference) querySLNode).child("HoDApproval").setValue("Disapproved");

                                        Query queryEmp = mDbReference.child(empFaculty).child(empDepartment).child(ApplicantDomain)
                                                .child(ApplicantEmail).child("LeavesHistory").child("SummerLeaves")
                                                .child(SLKey);

                                        ((DatabaseReference) queryEmp).child("LeaveApprovalStatus")
                                                .setValue("Processed");
                                        ((DatabaseReference) queryEmp).child("AdminApproval").setValue("Disapproved");
                                        ((DatabaseReference) queryEmp).child("Seen").setValue("No");


                                        Toast.makeText(getContext(), "Disapproved", Toast.LENGTH_LONG).show();
                                        onBackButtonPressed();
                                        dialog.dismiss();
                                    }
                                }
                            });
                            dialog.show();
                            return true;
                        }
                        else
                            return false;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }


    private boolean isEmpty(EditText textInputEditText) {
        if (textInputEditText.getText().toString().trim().length() > 0)
            return false;
        return true;
    }
    @Override
    public void onBackButtonPressed() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(AdminProcessedSL.this).commit();

    }
}
