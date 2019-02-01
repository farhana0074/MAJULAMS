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
import com.farhakhan.majulams.model_classes.HLforHodApprovl;
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
public class AdminPendingHL extends BackableFragment {

    public AdminPendingHL() {
    }


    String person_email, currentYear, ApplicantEmail, ApplicantDesignation, ApplicantName, ApplicantPic, HLKey,
            ApplicantDomain, empDepartment, empFaculty, strComment;
    String LeaveType,strBInTime, strBOutTime, strEInTime, strEOutTime, strAppInDate, strAppOutDate,
            strAppInTime, strAppOutTime, strLeaveInDate, strLeaveOutDate, LeaveManage, LeaveComments;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    Date AppDate, AppTime,LeaveDate, BTime, ETime;
    private long totalHL, updatedHL;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      final View view= inflater.inflate(R.layout.fragment_admin_pending_hl, container, false);
        Bundle bundle = getArguments();
        if (bundle!= null)
        {
            empFaculty = bundle.getString("strFaculty");
            empDepartment = bundle.getString("strDepartment");
            currentYear = bundle.getString("currentYear");
            HLKey = bundle.getString("HLPndKey");
        }

        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();

        final SimpleDateFormat DateOutFormat = new SimpleDateFormat("dd MMM, yyyy, EEEE");
        final SimpleDateFormat DateInFormat = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat TimeInFormat = new SimpleDateFormat("HH:mm");
        final SimpleDateFormat TimeOutFormat = new SimpleDateFormat("h:mm a");

        final Query queryHLNode = mDbReference.child(empFaculty).child(empDepartment).child("EmployeeLeaves").child("HalfLeaves")
                .child(HLKey);
        queryHLNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final HLforHodApprovl hLforHodApprovl = dataSnapshot.getValue(HLforHodApprovl.class);

                ApplicantEmail = hLforHodApprovl.EmployeeEmail;
                mDbReference.child("Users").child("Faculty").child(ApplicantEmail).child("Domain")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                ApplicantDomain = dataSnapshot2.getValue().toString();
                                TextView tvDomain = view.findViewById(R.id.tvApplicant_domain_admin_pnd_hl);
                                tvDomain.setText(ApplicantDomain);

                                Query queryInfo = mDbReference.child(empFaculty).child(empDepartment).child(ApplicantDomain)
                                        .child(ApplicantEmail);
                                ((DatabaseReference) queryInfo).child("Info").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                        if (dataSnapshot3 != null) {
                                            UserNamePic userNamePic =dataSnapshot3.getValue(UserNamePic.class);
                                            ApplicantName = userNamePic.Name;
                                            TextView tvName = view.findViewById(R.id.tvApplicant_name_admin_pnd_hl);
                                            tvName.setText(ApplicantName);

                                            ApplicantPic = userNamePic.PicUrl;
                                            CircleImageView civImage = view.findViewById(R.id.iv_person_pic_admin_pnd_hl);
                                            Glide.with(getContext())
                                                    .load(ApplicantPic)
                                                    .apply(new RequestOptions().override(100, 100))
                                                    .apply(new RequestOptions().centerCrop())
                                                    .into(civImage);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }});

                                ((DatabaseReference) queryInfo).child("Designation").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot4) {
                                        if (dataSnapshot4!= null)
                                        {
                                            ApplicantDesignation =  dataSnapshot4.getValue().toString();
                                            TextView tvDesignation = view.findViewById(R.id.tvApplicant_desig_admin_pnd_hl);
                                            tvDesignation.setText(ApplicantDesignation);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }});


                LeaveType = hLforHodApprovl.LeaveType;
                TextView tvLeaveType = view.findViewById(R.id.tvLeave_Type_admin_pnd_hl);
                tvLeaveType.setText(LeaveType);

                strBInTime = hLforHodApprovl.LeaveBeginningTime;
                try {
                    BTime = TimeInFormat.parse(strBInTime);

                    if(BTime != null)
                        strBOutTime = TimeOutFormat.format(BTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                TextView tvBTime = view.findViewById(R.id.tvLeave_BTime_admin_pnd_hl);
                tvBTime.setText(strBOutTime);

                strEInTime = hLforHodApprovl.LeaveEndingTime;
                TextView tvETime = view.findViewById(R.id.tvLeave_ETime_admin_pnd_hl);
                if(strEInTime.equals("Nil"))
                    tvETime.setText(strEInTime);
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
                    tvETime.setText(strEOutTime);
                }

                strAppInTime = hLforHodApprovl.LeaveApplyingTime;
                try {
                    AppTime = TimeInFormat.parse(strAppInTime);
                    if(AppTime != null)
                        strAppOutTime = TimeOutFormat.format(AppTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                TextView tvAppTime = view.findViewById(R.id.tvAppTime_admin_pnd_hl);
                tvAppTime.setText(strAppOutTime);

                strAppInDate = hLforHodApprovl.LeaveApplyingDate;
                strLeaveInDate = hLforHodApprovl.LeaveDate;
                try {
                    AppDate = DateInFormat.parse(strAppInDate);
                    LeaveDate = DateInFormat.parse(strLeaveInDate);

                    if(AppDate!= null)
                        strAppOutDate = DateOutFormat.format(AppDate);

                    if (LeaveDate!= null)
                        strLeaveOutDate = DateOutFormat.format(LeaveDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                TextView tvAppDate = view.findViewById(R.id.tvAppDate_admin_pnd_hl);
                tvAppDate.setText(strAppOutDate);

                TextView tvLeaveDate = view.findViewById(R.id.tvLeave_Date_admin_pnd_hl);
                tvLeaveDate.setText(strLeaveOutDate);

                LeaveManage = hLforHodApprovl.HowToCover;
                TextView tvManage = view.findViewById(R.id.tvLeave_C_Opt_admin_pnd_hl);
                tvManage.setText(LeaveManage);

                LeaveComments = hLforHodApprovl.CommentsOpt;
                TextView tvComments = view.findViewById(R.id.tvComments_admin_pnd_hl);
                tvComments.setText(LeaveComments);

                BottomNavigationView bnvHodHalfLeave = view.findViewById(R.id.bnv_admin_pnd_hl);
                bnvHodHalfLeave.getMenu().getItem(0).setCheckable(false);
                bnvHodHalfLeave.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                            dialog.setContentView(R.layout.hod_approve_disapprove_dialog);
                            dialog.setCanceledOnTouchOutside(false);
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
                                        ((DatabaseReference) queryHLNode).child("AdminComments")
                                                .setValue(strComment);
                                        ((DatabaseReference) queryHLNode).child("LeaveApprovalStatus")
                                                .setValue("Processed By Admin");
                                        ((DatabaseReference) queryHLNode).child("AdminApproval").setValue("Approved");

                                        Query queryEmp = mDbReference.child(empFaculty).child(empDepartment).child(ApplicantDomain)
                                                .child(ApplicantEmail).child("LeavesHistory").child("HalfLeaves")
                                                .child(HLKey);

                                        ((DatabaseReference) queryEmp).child("LeaveApprovalStatus")
                                                .setValue("Processed");
                                        ((DatabaseReference) queryEmp).child("AdminComments")
                                                .setValue(strComment);
                                        ((DatabaseReference) queryEmp).child("AdminApproval").setValue("Approved");
                                        ((DatabaseReference) queryEmp).child("Seen").setValue("No");


                                       final Query queryHLStatus = mDbReference.child(empFaculty).child(empDepartment)
                                               .child(ApplicantDomain).child(ApplicantEmail).child("LeavesStatus")
                                               .child("HalfLeaves");

                                       queryHLStatus.addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot dataSnapshotStatus) {
                                               if(dataSnapshotStatus!=null)
                                                   totalHL = ((long) dataSnapshotStatus.getValue());
                                                   updatedHL = totalHL+1;
                                               ((DatabaseReference) queryHLStatus).setValue(updatedHL);
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
                                        ((DatabaseReference) queryHLNode).child("AdminComments")
                                                .setValue(strComment);
                                        ((DatabaseReference) queryHLNode).child("LeaveApprovalStatus")
                                                .setValue("Processed By Admin");
                                        ((DatabaseReference) queryHLNode).child("AdminApproval").setValue("Disapproved");

                                        Query queryEmp = mDbReference.child(empFaculty).child(empDepartment).child(ApplicantDomain)
                                                .child(ApplicantEmail).child("LeavesHistory").child("HalfLeaves")
                                                .child(HLKey);

                                        ((DatabaseReference) queryEmp).child("LeaveApprovalStatus")
                                                .setValue("Processed");
                                        ((DatabaseReference) queryEmp).child("AdminComments")
                                                .setValue(strComment);
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
                public void onCancelled(@NonNull DatabaseError databaseError) { }});

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
        fragmentTransaction.remove(AdminPendingHL.this).commit();
    }
}