package com.farhakhan.majulams.model_classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class HLforAdminApprovl {

    public String EmployeeEmail;
    public String LeaveType;
    public String LeaveDate;
    public String LeaveBeginningTime;
    public String LeaveEndingTime;
    public String HowToCover;
    public String CommentsOpt;
    public String LeaveApplyingDate;
    public String LeaveApplyingTime;
    public String LeaveApprovalStatus;
    public String HoDApproval;
    public String HodComments;

    public HLforAdminApprovl() {
    }

    public HLforAdminApprovl(String employeeEmail, String leaveType, String leaveDate,
                             String leaveBeginningTime, String leaveEndingTime, String howToCover,
                             String commentsOpt, String leaveApplyingDate, String leaveApplyingTime,
                             String leaveApprovalStatus, String hoDApproval, String hodComments) {
        EmployeeEmail = employeeEmail;
        LeaveType = leaveType;
        LeaveDate = leaveDate;
        LeaveBeginningTime = leaveBeginningTime;
        LeaveEndingTime = leaveEndingTime;
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
        result.put("LeaveDate", LeaveDate);
        result.put("LeaveBeginningTime", LeaveBeginningTime);
        result.put("LeaveEndingTime", LeaveEndingTime);
        result.put("HowToCover",HowToCover);
        result.put("CommentsOpt",CommentsOpt);
        result.put("LeaveApplyingDate",LeaveApplyingDate);
        result.put("LeaveApplyingTime",LeaveApplyingTime);
        result.put("LeaveApprovalStatus",LeaveApprovalStatus);
        result.put("HoDApproval",HoDApproval);
        result.put("HodComments",HodComments);
        return result;
    }

}
