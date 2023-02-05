package com.increff.pos.helper.dtoHelper;

import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;

import java.util.ArrayList;
import java.util.List;

public class UserDtoHelper {
    public static UserData convert(UserPojo p) {
        UserData d = new UserData();
        d.setUserId(p.getUserId());
        d.setEmail(p.getEmail());
        d.setPassword(p.getPassword());

        return d;
    }

    public static UserPojo convert(UserForm f) {
        UserPojo p = new UserPojo();
        p.setEmail(f.getEmail());
        p.setPassword(f.getPassword());
        return p;
    }

    public static void normalize(UserForm f) {
        f.setEmail(f.getEmail().toLowerCase().trim());
        f.setPassword(f.getPassword().trim());
    }

    public static List<UserData> getAllUserData(List<UserPojo> list){
        List<UserData> list2 = new ArrayList<UserData>();
        for (UserPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }


}
