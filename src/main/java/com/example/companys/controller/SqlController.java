package com.example.companys.controller;

import com.example.companys.entity.Department;
import com.example.companys.entity.Person;
import com.example.companys.service.impl.DepartmentServiceImpl;
import com.example.companys.service.impl.PersonServiceImpl;
import com.example.companys.service.impl.SqlImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SqlController {
    @Autowired
    private PersonServiceImpl personService;
    @Autowired
    private DepartmentServiceImpl departmentService;

    //数据库读取人员信息
    @GetMapping("/covered")
    public String readPersonFromSql() {
        String result="操作失败";
       //1.部门覆盖
        List<Department> departmentList=departmentService.queryAll();
        SqlImpl sql=new SqlImpl();
        String dep= sql.DepsendToWeChat(departmentList);
        //2.人员覆盖
        if (dep.equals("部门覆盖完毕")){
            List<Person> personList=personService.queryAll();
            String per= sql.PersendToWeChat(personList);
            if (per.equals("人员信息覆盖完毕")){
                result="操作成功";
            }
        }
        System.out.println(result);
        return result;
    }
}
