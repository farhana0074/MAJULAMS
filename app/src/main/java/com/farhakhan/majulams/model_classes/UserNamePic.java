package com.farhakhan.majulams.model_classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserNamePic {

    public String Name;
    public String PicUrl;


    public UserNamePic() {
    }

    public UserNamePic( String name, String picUrl) {
        this.Name = name;
        this.PicUrl = picUrl;

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", Name);
        result.put("PicUrl", PicUrl);

        return result;
    }
}
