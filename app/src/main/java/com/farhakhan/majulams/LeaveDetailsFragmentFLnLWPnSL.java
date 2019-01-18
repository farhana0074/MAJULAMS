package com.farhakhan.majulams;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.farhakhan.majulams.model_classes.FLnLWPnSLEmpHistoty;
import com.farhakhan.majulams.model_classes.FLforHodApprovl;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LeaveDetailsFragmentFLnLWPnSL extends BackableFragment {

    public LeaveDetailsFragmentFLnLWPnSL(){}

    String person_email, empFaculty, empDepartment, empDomain, empDesignation,
            leaveType, strDateFrom, strDateTill, classCover;
    String TAG;
    RadioGroup radioGroup;
    EditText etComments;
    Button btnSubmit;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_leave_details, container, false);
        ((FacultyMainActivity) getActivity()).hideFloatingActionButton();
        Bundle bundle = getArguments();
        if (bundle!= null) {
            TAG = bundle.getString("TAG");
            person_email = bundle.getString("EmailID");
            empFaculty = bundle.getString("Faculty");
            empDepartment = bundle.getString("Department");
            empDomain = bundle.getString("Domain");
            empDesignation = bundle.getString("Designation");
            leaveType = bundle.getString("LeaveType");
            strDateFrom = bundle.getString("LeaveFromDate");
            strDateTill = bundle.getString("LeaveTillDate");
        }

        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();

        radioGroup = view.findViewById(R.id.rgCoverOptions);
        radioGroup.check(R.id.rBMakeup);
        RadioButton RbMakeup = view.findViewById(R.id.rBMakeup);
        classCover = RbMakeup.getText().toString();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = view.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.rBMakeup:
                        classCover = radioButton.getText().toString();
                        break;
                    case R.id.rbFixture:
                        classCover = radioButton.getText().toString();
                        break;
                    case R.id.rbNoClass:
                        classCover = radioButton.getText().toString();
                        break;
                }
            }
        });
        etComments = view.findViewById(R.id.etReason);
        btnSubmit = view.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle(" Confirmation")
                        .setMessage("Are you sure you want to submit the Leave Application?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat FormatApplyingDate = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat FormatApplyingTime = new SimpleDateFormat("HH:mm");
                                String ApplyingDate = FormatApplyingDate.format(calendar.getTime());
                                String ApplyingTime = FormatApplyingTime.format(calendar.getTime());
                                String ApprovalStatus = "Pending";
                                String strComment = "Nil";
                                if(!isEmpty(etComments))
                                    strComment = etComments.getText().toString().trim();

                                FLforHodApprovl fLforHodApprovl = new FLforHodApprovl();
                                fLforHodApprovl.EmployeeEmail = person_email;
                                fLforHodApprovl.LeaveType = leaveType;
                                fLforHodApprovl.LeaveBeginningDate = strDateFrom;
                                fLforHodApprovl.LeaveEndingDate = strDateTill;
                                fLforHodApprovl.HowToCover = classCover;
                                fLforHodApprovl.CommentsOpt = strComment;
                                fLforHodApprovl.LeaveApplyingDate = ApplyingDate;
                                fLforHodApprovl.LeaveApplyingTime = ApplyingTime;
                                fLforHodApprovl.LeaveApprovalStatus= ApprovalStatus;

                                String idKey = mDbReference.child(empFaculty).child(empDepartment)
                                        .child("EmployeeLeaves").child("PendingLeaves").child(TAG).push().getKey();
                                mDbReference.child(empFaculty).child(empDepartment).child("EmployeeLeaves")
                                        .child(TAG).child(idKey).setValue(fLforHodApprovl);

                                FLnLWPnSLEmpHistoty fLnLWPnSLEmpHistoty = new FLnLWPnSLEmpHistoty();
                                fLnLWPnSLEmpHistoty.LeaveType = leaveType;
                                fLnLWPnSLEmpHistoty.LeaveBeginningDate = strDateFrom;
                                fLnLWPnSLEmpHistoty.LeaveEndingDate = strDateTill;
                                fLnLWPnSLEmpHistoty.HowToCover = classCover;
                                fLnLWPnSLEmpHistoty.CommentsOpt = strComment;
                                fLnLWPnSLEmpHistoty.LeaveApplyingDate = ApplyingDate;
                                fLnLWPnSLEmpHistoty.LeaveApplyingTime = ApplyingTime;
                                fLnLWPnSLEmpHistoty.LeaveApprovalStatus = ApprovalStatus;

                                mDbReference.child(empFaculty).child(empDepartment).child(empDomain)
                                        .child(person_email).child("LeavesHistory").child(TAG)
                                        .child(idKey).setValue(fLnLWPnSLEmpHistoty);

                                Toast.makeText(getContext(),"Leave Application Submitted", Toast.LENGTH_LONG).show();
                                ((FacultyMainActivity)getActivity()).clearBackstack();
                            }
                        }).create().show();

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
        fragmentTransaction.detach(LeaveDetailsFragmentFLnLWPnSL.this).commit();
    }

}