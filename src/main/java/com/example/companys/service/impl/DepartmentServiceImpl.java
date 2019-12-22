package com.example.companys.service.impl;

import com.example.companys.dao.DepartmentDao;
import com.example.companys.entity.Department;
import com.example.companys.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentDao dao;
    //添加部门
    @Override
    public Department addDepartment(Department department) {
        Department department1=dao.save(department);
        return department1;
    }
    //删除部门
    @Override
    public String deleteDepartment(Integer dep_id) {
        dao.deleteById(dep_id);
        return "";
    }
    //修改部门
    @Override
    public String updateDepartment(Department department) {
        dao.save(department);
        return "";
    }
    //查询全部信息
    @Override
    public List<Department> queryAll() {
        List<Department> list=dao.findAll();
        return list;
    }
}
