package com.farhakhan.majulams.model_classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class AdminDepLeaves {

    public String Department;

    public AdminDepLeaves(String department) {
        Department = department;
    }

    public AdminDepLeaves() { }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Department", Department);
        return result;
    }
}
