package com.example.employeedirectoryapplication.network;

import com.example.employeedirectoryapplication.model.Employee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONDecode  {

    static   List<Employee> convertJSONtoList(String jsonString) throws JSONException {
    List<Employee> list = new ArrayList<>();
    JSONObject jsonObject = new JSONObject(jsonString);
    JSONArray jsonArray = jsonObject.getJSONArray("data");
    int count = jsonArray.length();
    if (count > 0) {
        for (int i = 0; i < count; i++) {
            JSONObject jsonEmployee = jsonArray.getJSONObject(i);
            Employee employee = new Employee();
            employee.setId(jsonEmployee.getInt("id"));
            employee.setName(jsonEmployee.getString("name"));
            employee.setWebsite(jsonEmployee.getString("website"));
            employee.setProfile_image(nullCheck(jsonEmployee.getString("profile_image")));
            list.add(employee);
        }
    }
    return list;
}

        private static String nullCheck(String text) {
            if (text.isEmpty()) {
                return null;
            } else {
                return text;
            }
        }
}
