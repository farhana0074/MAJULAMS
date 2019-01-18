package com.farhakhan.majulams.model_classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class HODFacDep {

    public String Faculty;
    public String Department;

    public HODFacDep() { }

    public HODFacDep(String faculty, String department) {
        Faculty = faculty;
        Department = department;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Department", Department);
        result.put("Faculty", Faculty);
        return result;
    }
}
