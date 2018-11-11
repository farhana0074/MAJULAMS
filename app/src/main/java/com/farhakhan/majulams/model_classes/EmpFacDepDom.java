package com.farhakhan.majulams.model_classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class EmpFacDepDom {

    public String Faculty;
    public String Department;
    public String Domain;

    public EmpFacDepDom(String empFac, String empDep, String empDom) {
        this.Faculty = empFac;
        this.Department = empDep;
        this.Domain = empDom;
    }

    public EmpFacDepDom() { }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Department", Department);
        result.put("Domain", Domain);
        result.put("Faculty", Faculty);
        return result;
    }

}
