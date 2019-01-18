package com.farhakhan.majulams.model_classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class TotalAnnualLeaves {


    public Long FullLeaves;
    public Long HalfLeaves;
    public Long LeavesWithoutPay;
    public Long SummerLeaves;

    public TotalAnnualLeaves(Long fullLeaves, Long halfLeaves, Long leavesWithoutPay,
                             Long summerLeaves) {
        this.FullLeaves = fullLeaves;
        this.HalfLeaves = halfLeaves;
        this.LeavesWithoutPay = leavesWithoutPay;
        this.SummerLeaves = summerLeaves;
    }

    public TotalAnnualLeaves() {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("FullLeaves", FullLeaves);
        result.put("HalfLeaves", HalfLeaves);
        result.put("LeavesWithoutPay", LeavesWithoutPay);
        result.put("SummerLeaves", SummerLeaves);
        return result;
    }
}
