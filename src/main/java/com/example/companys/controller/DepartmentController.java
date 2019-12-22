package com.example.companys.controller;

import com.example.companys.entity.Department;
import com.example.companys.service.WeiXinDepartmentService;
import com.example.companys.service.impl.DepartmentServiceImpl;
import com.example.companys.service.weixin.WeiXinParamesUtil;
import com.example.companys.service.weixin.WeiXinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class DepartmentController {
    @Autowired
    private DepartmentServiceImpl service;
    //1.添加部门入口    OK
    @PostMapping("/addDepartment")
    public String addDepartment(Department department){
        //1.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
        String accessToken= WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.contactsSecret).getToken();
        System.out.println("2:accessToken:"+accessToken);
        //2.创建部门
        WeiXinDepartmentService weiXinDepartmentService= new WeiXinDepartmentService();
        String result= weiXinDepartmentService.createDepartment(accessToken,department);
        //3.数据库操作
        if (result=="添加成功") {
            service.addDepartment(department);
        }
        return result;
    }

    //2.更新部门入口  OK
    @PostMapping("/updateDepartment")
    public String updateDepartment(Department department) {
        //1.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
        String accessToken= WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.contactsSecret).getToken();
        System.out.println("accessToken:"+accessToken);

        //2.更新部门
        WeiXinDepartmentService weiXinDepartmentService= new WeiXinDepartmentService();
        String result= weiXinDepartmentService.updateDepartment(accessToken, department);
        if (result=="更新成功"){
            service.updateDepartment(department);
        }
        return result;
    }

    //3.删除部门入口  OK
    @GetMapping("/deleteDepartment/{departmentId}")
    public String deleteDepartment(@PathVariable Integer departmentId) {

        //1..获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
        String accessToken= WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.contactsSecret).getToken();
        System.out.println("accessToken:"+accessToken);

        //2.删除部门
        WeiXinDepartmentService weiXinDepartmentService= new WeiXinDepartmentService();
        String result= weiXinDepartmentService.deleteDepartment(accessToken, departmentId);
        System.out.println(result);
        if (result=="删除成功"){
            service.deleteDepartment(departmentId);
        }
        return result;
    }


    /*
     * 4.获取部门列表    OK
     * 参数                  必须    说明
     * access_token    是    调用接口凭证
     * id                    否     部门id。获取指定部门及其下的子部门。 如果不填，默认获取全量组织架构
     */
    @GetMapping("/getDepartment")
    public List<Department> getDepartmentList(){
        //1.获取departmentId
        String departmentId="0";

        //2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
        String accessToken= WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.contactsSecret).getToken();
        System.out.println("accessToken:"+accessToken);

        //3.调用接口，发送请求，获取部门
        WeiXinDepartmentService weiXinDepartmentService= new WeiXinDepartmentService();
        List<Department> result= weiXinDepartmentService.getDepartmentList(accessToken, departmentId);
        System.out.println(result);
        return result;
    }
}
