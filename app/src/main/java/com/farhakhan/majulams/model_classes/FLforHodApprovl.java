package com.farhakhan.majulams.model_classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class FLforHodApprovl {

    public String EmployeeEmail;
    public String LeaveType;
    public String LeaveBeginningDate;
    public String LeaveEndingDate;
    public String HowToCover;
    public String CommentsOpt;
    public String LeaveApplyingDate;
    public String LeaveApplyingTime;
    public String LeaveApprovalStatus;

    public FLforHodApprovl() { }

    public FLforHodApprovl(String employeeEmail, String leaveType, String leaveBegnDate,
                           String leaveEndnDate, String howToCover, String commentsOpt,
                           String leaveApplyingDate, String leaveApplyingTime,
                           String leaveApprovalStatus) {
        EmployeeEmail = employeeEmail;
        LeaveType = leaveType;
        LeaveBeginningDate = leaveBegnDate;
        LeaveEndingDate = leaveEndnDate;
        HowToCover = howToCover;
        CommentsOpt = commentsOpt;
        LeaveApplyingDate = leaveApplyingDate;
        LeaveApplyingTime = leaveApplyingTime;
        LeaveApprovalStatus = leaveApprovalStatus;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("EmployeeEmail", EmployeeEmail);
        result.put("LeaveType", LeaveType);
        result.put("LeaveBeginningDate", LeaveBeginningDate);
        result.put("LeaveEndingDate", LeaveEndingDate);
        result.put("HowToCover",HowToCover);
        result.put("CommentsOpt",CommentsOpt);
        result.put("LeaveApplyingDate",LeaveApplyingDate);
        result.put("LeaveApplyingTime",LeaveApplyingTime);
        result.put("LeaveApprovalStatus",LeaveApprovalStatus);
        return result;
    }
}
