package com.farhakhan.majulams.model_classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class HLAftrAdminResponse {
    public HLAftrAdminResponse() {
    }

    public String LeaveType;
    public String LeaveDate;
    public String LeaveBeginningTime;
    public String LeaveEndingTime;
    public String HowToCover;
    public String CommentsOpt;
    public String LeaveApplyingDate;
    public String LeaveApplyingTime;
    public String LeaveApprovalStatus;
    public String AdminApproval;

    public HLAftrAdminResponse(String leaveType, String leaveDate, String leaveBeginningTime,
                               String leaveEndingTime, String howToCover, String commentsOpt,
                               String leaveApplyingDate, String leaveApplyingTime, String leaveApprovalStatus,
                               String adminApproval) {
        LeaveType = leaveType;
        LeaveDate = leaveDate;
        LeaveBeginningTime = leaveBeginningTime;
        LeaveEndingTime = leaveEndingTime;
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
        result.put("AdminApproval", AdminApproval);
        result.put("LeaveType", LeaveType);
        result.put("LeaveDate", LeaveDate);
        result.put("LeaveBeginningTime", LeaveBeginningTime);
        result.put("LeaveEndingTime", LeaveEndingTime);
        result.put("HowToCover",HowToCover);
        result.put("CommentsOpt",CommentsOpt);
        result.put("LeaveApplyingDate",LeaveApplyingDate);
        result.put("LeaveApplyingTime",LeaveApplyingTime);
        result.put("LeaveApprovalStatus",LeaveApprovalStatus);
        return result;
    }
}
