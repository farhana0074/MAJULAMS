package com.farhakhan.majulams.model_classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class EmpNotifDetailsHL {

    public String LeaveType;
    public String LeaveDate;
    public String LeaveBeginningTime;
    public String LeaveEndingTime;
    public String HowToCover;
    public String CommentsOpt;
    public String LeaveApplyingDate;
    public String AdminApproval;
    public String LeaveApplyingTime;
    public String LeaveApprovalStatus;

    public EmpNotifDetailsHL(String leaveType, String leaveDate,
                             String leaveBeginningTime, String leaveEndingTime, String howToCover,
                             String commentsOpt, String leaveApplyingDate, String adminApproval,
                             String leaveApplyingTime, String leaveApprovalStatus) {

        LeaveType = leaveType;
        LeaveDate = leaveDate;
        LeaveBeginningTime = leaveBeginningTime;
        LeaveEndingTime = leaveEndingTime;
        HowToCover = howToCover;
        CommentsOpt = commentsOpt;
        LeaveApplyingDate = leaveApplyingDate;
        AdminApproval = adminApproval;
        LeaveApplyingTime = leaveApplyingTime;
        LeaveApprovalStatus = leaveApprovalStatus;
    }

    public EmpNotifDetailsHL() {
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("LeaveType", LeaveType);
        result.put("LeaveDate", LeaveDate);
        result.put("LeaveBeginningTime", LeaveBeginningTime);
        result.put("LeaveEndingTime", LeaveEndingTime);
        result.put("HowToCover",HowToCover);
        result.put("CommentsOpt",CommentsOpt);
        result.put("LeaveApplyingDate",LeaveApplyingDate);
        result.put("LeaveApplyingTime",LeaveApplyingTime);
        result.put("LeaveApprovalStatus",LeaveApprovalStatus);
        result.put("AdminApproval", AdminApproval);
        return result;
    }
}
