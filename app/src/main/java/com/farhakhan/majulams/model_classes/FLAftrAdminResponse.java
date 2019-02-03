package com.farhakhan.majulams.model_classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class FLAftrAdminResponse {
    public String LeaveType;
    public String LeaveBeginningDate;
    public String LeaveEndingDate;
    public String HowToCover;
    public String CommentsOpt;
    public String LeaveApplyingDate;
    public String LeaveApplyingTime;
    public String LeaveApprovalStatus;
    public String AdminApproval;

    public FLAftrAdminResponse(String leaveType, String leaveBeginningDate, String leaveEndingDate,
                               String howToCover, String commentsOpt, String leaveApplyingDate,
                               String leaveApplyingTime, String leaveApprovalStatus, String adminApproval) {
        LeaveType = leaveType;
        LeaveBeginningDate = leaveBeginningDate;
        LeaveEndingDate = leaveEndingDate;
        HowToCover = howToCover;
        CommentsOpt = commentsOpt;
        LeaveApplyingDate = leaveApplyingDate;
        LeaveApplyingTime = leaveApplyingTime;
        LeaveApprovalStatus = leaveApprovalStatus;
        AdminApproval = adminApproval;
    }

    public FLAftrAdminResponse() {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("LeaveType", LeaveType);
        result.put("LeaveBeginningTime", LeaveBeginningDate);
        result.put("LeaveEndingTime", LeaveEndingDate);
        result.put("HowToCover",HowToCover);
        result.put("CommentsOpt",CommentsOpt);
        result.put("LeaveApplyingDate",LeaveApplyingDate);
        result.put("LeaveApplyingTime",LeaveApplyingTime);
        result.put("LeaveApprovalStatus",LeaveApprovalStatus);
        result.put("AdminApproval", AdminApproval);
        return result;
    }
}
