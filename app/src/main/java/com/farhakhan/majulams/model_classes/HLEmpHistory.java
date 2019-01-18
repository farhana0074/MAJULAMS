package com.farhakhan.majulams.model_classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class HLEmpHistory {


    public String LeaveType;
    public String LeaveDate;
    public String LeaveBeginningTime;
    public String LeaveEndingTime;
    public String HowToCover;
    public String CommentsOpt;
    public String LeaveApplyingDate;
    public String LeaveApplyingTime;
    public String LeaveApprovalStatus;

    public HLEmpHistory() {}

    public HLEmpHistory(String leaveType, String leaveDate, String leaveBegnTime, String leaveEndnTime,
                        String howToCover, String commentsOpt, String leaveApplyingDate,
                        String leaveApplyingTime, String leaveApprovalStatus) {

        LeaveType = leaveType;
        LeaveDate = leaveDate;
        LeaveBeginningTime = leaveBegnTime;
        LeaveEndingTime = leaveEndnTime;
        HowToCover = howToCover;
        CommentsOpt = commentsOpt;
        LeaveApplyingDate = leaveApplyingDate;
        LeaveApplyingTime = leaveApplyingTime;
        LeaveApprovalStatus = leaveApprovalStatus;
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
        return result;
    }
}
