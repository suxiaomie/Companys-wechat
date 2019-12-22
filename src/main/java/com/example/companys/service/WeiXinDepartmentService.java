package com.example.companys.service;

import com.example.companys.controller.DepartmentController;
import com.example.companys.entity.Department;
import com.example.companys.service.weixin.WeiXinUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

public class WeiXinDepartmentService {
    private static Logger log = LoggerFactory.getLogger(DepartmentController.class);

    private  String createDepartment_url="https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=ACCESS_TOKEN";
    private  String updateDepartment_url="https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token=ACCESS_TOKEN";
    private  String getDepartmentList_url="https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=ACCESS_TOKEN&id=ID";
    private  String deleteDepartment_url="https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token={ACCESS_TOKEN}&id={ID}";
    //1.创建部门
    public String createDepartment(String accessToken, Department department) {
        //1.获取json字符串：将Department对象转换为json字符串
        //Gson gson = new Gson();
        String jsonDepartment =department.toJson();      //使用gson.toJson(jsonDepartment)即可将jsonDepartment对象顺序转成json
        System.out.println("jsonDepartment:"+jsonDepartment);
        //2.拼接请求的url
        createDepartment_url=createDepartment_url.replace("ACCESS_TOKEN", accessToken);

        //3.调用接口，发送请求，创建部门
        JSONObject jsonObject = WeiXinUtil.httpRequest(createDepartment_url, "POST", jsonDepartment);
        System.out.println("jsonObjectss:"+jsonObject.toString());
        //根据微信返回的错误代码做信息校验
        String error="添加失败";
        if (null != jsonObject) {
            switch (jsonObject.getInt("errcode")) {
                case 0:
                    error= "添加成功";
                    log.error(error);
                break;
                case 60123:
                    error="无效部门Id";
                    log.error(error);
                break;
                case 60004:
                    error= "父部门不存在";
                    log.error(error);
                break;
                case 60008:
                    error="部门已存在";
                    log.error(error);
                break;
                default:
                    error="请联系管理员";
                    log.error(error);
            }
        }
        return error;
    }

    //2.更新部门
    public String updateDepartment(String accessToken, Department department) {

        //1.获取json字符串：将Department对象转换为json字符串
        //Gson gson = new Gson();
        String jsonDepartment =department.toJson();      //使用gson.toJson(jsonDepartment)即可将jsonDepartment对象顺序转成json
        System.out.println("jsonDepartment:"+jsonDepartment);
        //2.拼接请求的url
        updateDepartment_url=updateDepartment_url.replace("ACCESS_TOKEN", accessToken);

        //3.调用接口，发送请求，更新部门
        JSONObject jsonObject = WeiXinUtil.httpRequest(updateDepartment_url, "POST", jsonDepartment);
        System.out.println("jsonObject:"+jsonObject.toString());
        //根据微信返回的错误代码做信息校验
        String error="更新失败";
        if (null != jsonObject) {
            switch (jsonObject.getInt("errcode")) {
                case 0:
                    error= "更新成功";
                    log.error(error);
                    break;
                case 60008:
                    error="部门已存在";
                    log.error(error);
                    break;
                case 60004:
                    error= "父部门不存在";
                    log.error(error);
                    break;
                case 60010:
                    error="父ID错误";
                    log.error(error);
                    break;
                case 60003:
                    error="部门不存在";
                    log.error(error);
                    break;
                default:
                    error="联系管理员";
                    log.error(error);
                    break;
            }
        }
        return error;
    }

    //3.删除部门
    public String deleteDepartment(String accessToken,Integer departmentId) {

        //1.获取请求的url
        deleteDepartment_url=deleteDepartment_url.replace("{ACCESS_TOKEN}", accessToken).replace("{ID}", (String)departmentId.toString());
        System.out.println(deleteDepartment_url);
        //2.调用接口，发送请求，删除部门
        JSONObject jsonObject = WeiXinUtil.httpRequest(deleteDepartment_url, "GET", null);
        System.out.println("jsonObject:"+jsonObject.toString());

        //根据微信返回的错误代码做信息校验
        String error="删除失败";
        if (null != jsonObject) {
            switch (jsonObject.getInt("errcode")) {
                case 0:
                    error= "删除成功";
                    log.error(error);
                    break;
                case 60123:
                    error="无效的部门ID";
                    log.error(error);
                    break;
                case 60006:
                    error="部门下存在子部门";
                    log.error(error);
                    break;
                default:
                    error="联系管理员";
                    log.error(error);
                    break;
            }
        }
        return error;
    }

    //4.获取部门列表
    public List<Department> getDepartmentList(String accessToken, String departmentId) {

        //1.获取请求的url
        getDepartmentList_url=getDepartmentList_url.replace("ACCESS_TOKEN", accessToken)
                .replace("ID", departmentId);

        //2.调用接口，发送请求，获取成员
        JSONObject jsonObject = WeiXinUtil.httpRequest(getDepartmentList_url, "GET", null);
        System.out.println("jsonObject:"+jsonObject.toString());
        JSONArray jsonArray = jsonObject.getJSONArray("department");//返回值部门信息部分

        //3.返回信息
        if (null != jsonObject) {
            List<Department> list=new LinkedList<Department>();
            for(int i=0; i<jsonArray.size();i++)//遍历部门信息
            {
                JSONObject jo = jsonArray.getJSONObject(i);//部门信息分组存入数组
                Department dp=new Department();
                dp.setDep_id(jo.getInt("id"));
                dp.setName(jo.getString("name"));
                dp.setParent_id(jo.getInt("parentid"));
                dp.setOrder(jo.getInt("order"));
                list.add(dp);
            }
            return list;
        }
        return null;
    }

}
