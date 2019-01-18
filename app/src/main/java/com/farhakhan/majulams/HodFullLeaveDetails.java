package com.farhakhan.majulams;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import com.farhakhan.majulams.model_classes.FLforHodApprovl;
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

public class HodFullLeaveDetails extends BackableFragment {


    public HodFullLeaveDetails() { }
    String person_email, currentYear, ApplicantEmail, ApplicantDesignation, ApplicantName, ApplicantPic, FLKey,
            ApplicantDomain, empDepartment, empFaculty, strComment;
    String LeaveType, strAppInDate, strAppOutDate,strAppInTime, strAppOutTime,
             strBInDate, strBOutDate, strEInDate, strEOutDate, LeaveManage, LeaveComments;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    Date AppDate, AppTime, BDate, EDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_hod_full_leaves, container, false);

        Bundle bundle = getArguments();
        if (bundle!= null)
        {
            person_email = bundle.getString("person_email");
            empFaculty = bundle.getString("empFaculty");
            empDepartment = bundle.getString("empDepartment");
            currentYear = bundle.getString("currentYear");
            FLKey = bundle.getString("FLKey");
        }

        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();

        final SimpleDateFormat DateOutFormat = new SimpleDateFormat("dd MMM, yyyy, EEEE");
        final SimpleDateFormat DateInFormat = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat TimeInFormat = new SimpleDateFormat("HH:mm");
        final SimpleDateFormat TimeOutFormat = new SimpleDateFormat("h:mm a");

        final Query queryFLNode = mDbReference.child(empFaculty).child(empDepartment).child("EmployeeLeaves").child("FullLeaves")
                .child(FLKey);

        queryFLNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              final FLforHodApprovl fLforHodApprovl = dataSnapshot.getValue(FLforHodApprovl.class);
              ApplicantEmail = fLforHodApprovl.EmployeeEmail;
                mDbReference.child("Users").child("Faculty").child(ApplicantEmail).child("Domain")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                ApplicantDomain = dataSnapshot2.getValue().toString();
                                TextView tvApplicantDomainFL = view.findViewById(R.id.tvApplicant_domain_fl);
                                tvApplicantDomainFL.setText(ApplicantDomain);

                                Query queryInfo = mDbReference.child(empFaculty).child(empDepartment).child(ApplicantDomain)
                                        .child(ApplicantEmail);
                                ((DatabaseReference) queryInfo).child("Info").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                        UserNamePic userNamePic =dataSnapshot3.getValue(UserNamePic.class);
                                        ApplicantName = userNamePic.Name;

                                        TextView tvApplicantNameFL = view.findViewById(R.id.tvApplicant_name_fl);
                                        tvApplicantNameFL.setText(ApplicantName);

                                        ApplicantPic = userNamePic.PicUrl;
                                        CircleImageView civImage = view.findViewById(R.id.iv_person_pic_fl);
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
                                        TextView tvApplicantDesigFL = view.findViewById(R.id.tvApplicant_desig_fl);
                                        tvApplicantDesigFL.setText(ApplicantDesignation);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }});
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }});

                LeaveType = fLforHodApprovl.LeaveType;
                TextView tvLeaveType = view.findViewById(R.id.tvLeave_Type_fl);
                tvLeaveType.setText(LeaveType);

                strAppInTime = fLforHodApprovl.LeaveApplyingTime;
                try {
                    AppTime = TimeInFormat.parse(strAppInTime);
                    if(AppTime != null)
                        strAppOutTime = TimeOutFormat.format(AppTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                TextView tvAppTime = view.findViewById(R.id.tvAppTime_fl);
                tvAppTime.setText(strAppOutTime);

                strAppInDate = fLforHodApprovl.LeaveApplyingDate;
                try {
                    AppDate = DateInFormat.parse(strAppInDate);
                    if(AppDate!= null)
                        strAppOutDate = DateOutFormat.format(AppDate);
                } catch (ParseException e) {
                    e.printStackTrace(); }

                TextView tvAppDate = view.findViewById(R.id.tvAppDate_fl);
                tvAppDate.setText(strAppOutDate);

                strBInDate = fLforHodApprovl.LeaveBeginningDate;
                try {
                    BDate = DateInFormat.parse(strBInDate);
                    if(BDate!= null)
                        strBOutDate = DateOutFormat.format(BDate);
                } catch (ParseException e) {
                    e.printStackTrace(); }

                TextView tvBDate = view.findViewById(R.id.tvLeave_BDate);
                tvBDate.setText(strBOutDate);

                strEInDate = fLforHodApprovl.LeaveEndingDate;
                try {
                    EDate = DateInFormat.parse(strEInDate);
                    if(EDate !=null)
                        strEOutDate = DateOutFormat.format(EDate);
                } catch (ParseException e) {
                    e.printStackTrace(); }

                    TextView tvEDate = view.findViewById(R.id.tvLeave_EDate);
                tvEDate.setText(strEOutDate);

                LeaveManage = fLforHodApprovl.HowToCover;
                TextView tvManage = view.findViewById(R.id.tvLeave_C_Opt_fl);
                tvManage.setText(LeaveManage);

                LeaveComments = fLforHodApprovl.CommentsOpt;
                TextView tvComments = view.findViewById(R.id.tvComments_fl);
                tvComments.setText(LeaveComments);

                BottomNavigationView bnvHodFullLeave = view.findViewById(R.id.bnv_hod_FL);
                bnvHodFullLeave.getMenu().getItem(0).setCheckable(false);
                bnvHodFullLeave.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                                        ((DatabaseReference) queryFLNode).child("HodComments")
                                                .setValue(strComment);
                                        ((DatabaseReference) queryFLNode).child("LeaveApprovalStatus")
                                                .setValue("Processed By HoD");
                                        ((DatabaseReference) queryFLNode).child("HoDApproval").setValue("Approved");
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
                                        ((DatabaseReference) queryFLNode).child("HodComments")
                                                .setValue(strComment);
                                        ((DatabaseReference) queryFLNode).child("LeaveApprovalStatus")
                                                .setValue("Processed By HoD");
                                        ((DatabaseReference) queryFLNode).child("HoDApproval").setValue("Disapproved");
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
        fragmentTransaction.remove(HodFullLeaveDetails.this).commit();
        ((HodMainActivity)getActivity()).onStart();
    }
}
