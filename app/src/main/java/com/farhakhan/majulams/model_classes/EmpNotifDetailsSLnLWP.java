package com.farhakhan.majulams.model_classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class EmpNotifDetailsSLnLWP {
    public String LeaveBeginningDate;
    public String LeaveEndingDate;
    public String HowToCover;
    public String CommentsOpt;
    public String LeaveApplyingDate;
    public String LeaveApplyingTime;
    public String LeaveApprovalStatus;
    public String AdminApproval;

    public EmpNotifDetailsSLnLWP() {
    }

    public EmpNotifDetailsSLnLWP(String leaveBeginningDate, String leaveEndingDate, String howToCover,
                                 String commentsOpt, String leaveApplyingDate, String leaveApplyingTime,
                                 String leaveApprovalStatus, String adminApproval) {
        LeaveBeginningDate = leaveBeginningDate;
        LeaveEndingDate = leaveEndingDate;
        HowToCover = howToCover;
        CommentsOpt = commentsOpt;
        LeaveApplyingDate = leaveApplyingDate;
        LeaveApplyingTime = leaveApplyingTime;
        LeaveApprovalStatus = leaveApprovalStatus;
        AdminApproval = adminApproval;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("LeaveBeginningDate", LeaveBeginningDate);
        result.put("LeaveEndingDate", LeaveEndingDate);
        result.put("HowToCover",HowToCover);
        result.put("CommentsOpt",CommentsOpt);
        result.put("LeaveApplyingDate",LeaveApplyingDate);
        result.put("LeaveApplyingTime",LeaveApplyingTime);
        result.put("LeaveApprovalStatus",LeaveApprovalStatus);
        result.put("AdminApproval",AdminApproval);
        return result;
    }
}
