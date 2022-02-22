package com.example.employeedirectoryapplication.callback;

import com.example.employeedirectoryapplication.model.Employee;

import java.util.List;

public interface EmployeeListCallback{

    void onSuccess(List<Employee> list);

    void onFailure(String status);

}
