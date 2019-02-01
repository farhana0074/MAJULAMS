package com.farhakhan.majulams.model_classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class FLforAdminApprovl {
    public String EmployeeEmail;
    public String LeaveType;
    public String LeaveBeginningDate;
    public String LeaveEndingDate;
    public String HowToCover;
    public String CommentsOpt;
    public String LeaveApplyingDate;
    public String LeaveApplyingTime;
    public String LeaveApprovalStatus;
    public String HoDApproval;
    public String HodComments;

    public FLforAdminApprovl() { }

    public FLforAdminApprovl(String employeeEmail, String leaveType, String leaveBeginningDate,
                             String leaveEndingDate, String howToCover, String commentsOpt,
                             String leaveApplyingDate, String leaveApplyingTime,
                             String leaveApprovalStatus, String hoDApproval, String hodComments) {
        EmployeeEmail = employeeEmail;
        LeaveType = leaveType;
        LeaveBeginningDate = leaveBeginningDate;
        LeaveEndingDate = leaveEndingDate;
        HowToCover = howToCover;
        CommentsOpt = commentsOpt;
        LeaveApplyingDate = leaveApplyingDate;
        LeaveApplyingTime = leaveApplyingTime;
        LeaveApprovalStatus = leaveApprovalStatus;
        HoDApproval = hoDApproval;
        HodComments = hodComments;
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
        result.put("HoDApproval",HoDApproval);
        result.put("HodComments", HodComments);
        return result;
    }
}
