package com.example.companys.service;

import com.example.companys.entity.Department;

import java.util.List;

public interface DepartmentService {
    //添加数据
    Department addDepartment(Department department);
    //通过 deo_id 删除部门
    String deleteDepartment(Integer dep_id);
    //通过 dep_id 修改部门
    String updateDepartment(Department department);
    //查询全部信息
    List<Department> queryAll();
}
