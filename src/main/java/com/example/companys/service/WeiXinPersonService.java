package com.example.companys.service;

import com.example.companys.controller.PersonController;
import com.example.companys.entity.Person;
import com.example.companys.service.weixin.WeiXinUtil;
import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WeiXinPersonService {
    private static Logger log = LoggerFactory.getLogger(PersonController.class);

    private  String createUser_url="https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=ACCESS_TOKEN";
    private  String getUser_url="https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID";
    private  String updateUser_url="https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=ACCESS_TOKEN";
    private  String batchdeleteUser_url="https://qyapi.weixin.qq.com/cgi-bin/user/batchdelete?access_token=ACCESS_TOKEN";
    private  String getDepartmentUser_url="https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD";
    private  String getDepartmentUserDetails_url="https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD";
    private  String deleteUser_url="https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token={ACCESS_TOKEN}&userid={USERID}" ;

    //1.创建成员
    public String createPerson(String accessToken, Person person) {

        //1.获取json字符串：将user对象转换为json字符串
        //Gson gson = new Gson();
        System.out.println("jsonU2:"+ person);
        String jsonU1 =person.toJson();      //使用gson.toJson(user)即可将user对象顺序转成json
        System.out.println("jsonU1:"+jsonU1);


        //2.获取请求的url
        createUser_url=createUser_url.replace("ACCESS_TOKEN", accessToken);

        //3.调用接口，发送请求，创建成员
        JSONObject jsonObject = WeiXinUtil.httpRequest(createUser_url, "POST", jsonU1);
         System.out.println("jsonObject:"+jsonObject.toString());

        //4.根据微信返回的错误代码做信息校验
        String error="添加失败";
        if (null != jsonObject) {
            switch (jsonObject.getInt("errcode")) {
                case 0:
                    error= "添加成功";
                    log.error("添加成功");
                    break;
                case 40003:
                    error="无效的UserID";
                    break;
                case 40066:
                    error="不合法的部门列表";
                    break;
                case 60003:
                    error="部门不存在";
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
                case 60102:
                    error="UserID已存在";
                    break;
                case 60103:
                    error="手机号码不合法";
                    break;
                case 60104:
                    error="手机号已存在";
                    break;
                case 60105:
                    error="邮箱不合法";
                    break;
                case 60106:
                    error="邮箱已存在";
                    break;
                case 60123:
                    error="无效的部门id";
                    log.error(error);
                    break;
                default:
                    error="联系管理员";
                    break;
            }
        }
        return error;
    }

    //2.获取成员
    public JSONObject getPerson(String accessToken, String userId) {

        //1.获取请求的url
        getUser_url=getUser_url.replace("ACCESS_TOKEN", accessToken)
                .replace("USERID", userId);

        //2.调用接口，发送请求，获取成员
        JSONObject jsonObject = WeiXinUtil.httpRequest(getUser_url, "GET", null);
        System.out.println("jsonObject:"+jsonObject.toString());

        //3.错误消息处理
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                log.error("获取成员失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return jsonObject;
    }

    //3.更新成员
    public String updatePerson(String accessToken, Person person) {

        //1.获取json字符串：将user对象转换为json字符串
        //Gson gson = new Gson();
        String jsonU1 =person.toJson();      //使用gson.toJson(user)即可将user对象顺序转成json
        System.out.println("jsonU1:"+jsonU1);

        //2.获取请求的url
        updateUser_url=updateUser_url.replace("ACCESS_TOKEN", accessToken);

        //3.调用接口，发送请求，创建成员
        JSONObject jsonObject = WeiXinUtil.httpRequest(updateUser_url, "POST", jsonU1);
        System.out.println("jsonObject:"+jsonObject.toString());

        //4.根据微信返回的错误代码做信息校验
        String error="更新失败";
        if (null != jsonObject) {
            switch (jsonObject.getInt("errcode")) {
                case 0:
                    error= "更新成功";
                    log.error(error);
                    break;
                case 40058:
                    error="不合法的参数";
                    log.error(error);
                    break;
                case 40066:
                    error="不合法的部门列表";
                    log.error(error);
                    break;
                case 60003:
                    error="部门不存在";
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
                case 60103:
                    error="手机号码不合法";
                    break;
                case 60104:
                    error="手机号码已存在";
                    break;
                case 60105:
                    error="邮箱不合法";
                    break;
                case 60106:
                    error="邮箱已存在";
                    break;
                case 60111:
                    error="无效的部门id";
                    log.error(error);
                    break;
                case 60129:
                    error="成员手机和邮箱都为空";
                    log.error(error);
                    break;
            }
        }
        return error;
    }

    //4.删除成员
    public String deletePerson(String accessToken,String userId) {

        //1.获取请求的url
        deleteUser_url=deleteUser_url.replace("{ACCESS_TOKEN}", accessToken).replace("{USERID}", userId);
        System.out.println(deleteUser_url);
        //2.调用接口，发送请求，删除成员
        JSONObject jsonObject = WeiXinUtil.httpRequest(deleteUser_url, "GET", null);
        System.out.println("jsonObject:"+jsonObject.toString());

        //3.根据微信返回的错误代码做信息校验
        String error="删除失败";
        if (null != jsonObject) {
            switch (jsonObject.getInt("errcode")) {
                case 0:
                    error= "删除成功";
                    log.error(error);
                    break;
                case 60111:
                    error="UserID不存在";
                    log.error(error);
                    break;
                default:
                    error="请联系管理员";
                    break;
            }
        }
        return error;
    }

    //5.批量删除成员
    public void batchdeleteUser(String accessToken, List<String> userIdList){
        //1.获取json字符串：将user对象转换为json字符串
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("useridlist", userIdList);

        Gson gson=new Gson();
        String useridlist=gson.toJson(content);
        System.out.println(useridlist);

        //2.获取请求的url
        batchdeleteUser_url=batchdeleteUser_url.replace("ACCESS_TOKEN", accessToken);

        //3.调用接口，发送请求，创建成员
        JSONObject jsonObject = WeiXinUtil.httpRequest(batchdeleteUser_url, "POST", useridlist);
        System.out.println("jsonObject:"+jsonObject.toString());

        //4.错误消息处理
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                log.error("批量删除成员失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
    }


    //6.获取部门成员
    public List<Person> getDepartmentPerson(String accessToken,String departmentId,String fetchChild) {

        //1.获取请求的url
        getDepartmentUser_url=getDepartmentUser_url.replace("ACCESS_TOKEN", accessToken)
                .replace("DEPARTMENT_ID", departmentId)
                .replace("FETCH_CHILD", fetchChild);

        //2.调用接口，发送请求，获取部门成员
        JSONObject jsonObject = WeiXinUtil.httpRequest(getDepartmentUser_url, "GET", null);
        System.out.println("jsonObject:"+jsonObject.toString());

        //3.错误消息处理
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                log.error("获取部门成员失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
            List<Person> list=new LinkedList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("userlist");//返回用户列表
            for(int i=0; i<jsonArray.size();i++)//遍历信息数组
            {
                JSONObject jo = jsonArray.getJSONObject(i);//信息分组存入数组
                Person p=new Person();
                p.setUser_id(jo.getString("userid"));
                p.setName(jo.getString("name"));
                JSONArray array;
                array=jo.getJSONArray("department");
                p.setDep_id(array.optInt(0));
                list.add(p);
            }
            return list;
        }
        return null;
    }


    //7.获取部门成员详情
    public List<Person> getDepartmentUserDetails(String accessToken,String departmentId,String fetchChild) {
        //1.获取请求的url
        getDepartmentUserDetails_url=getDepartmentUserDetails_url.replace("ACCESS_TOKEN", accessToken)
                .replace("DEPARTMENT_ID", departmentId)
                .replace("FETCH_CHILD", fetchChild);

        //2.调用接口，发送请求，获取部门成员
        JSONObject jsonObject = WeiXinUtil.httpRequest(getDepartmentUserDetails_url, "GET", null);
        System.out.println("jsonObject:"+jsonObject.toString());

        //3.错误消息处理
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                log.error("获取部门成员详情失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
            List<Person> list=new LinkedList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("userlist");//返回用户列表
            for(int i=0; i<jsonArray.size();i++)//遍历信息数组
            {
                JSONObject jo = jsonArray.getJSONObject(i);//信息分组存入数组
                Person p=new Person();
                p.setUser_id(jo.getString("userid"));
                p.setName(jo.getString("name"));
                JSONArray array;
                array=jo.getJSONArray("department");
                p.setDep_id(array.optInt(0));
                p.setPosition(jo.getString("position"));
                if (jo.getString("gender").equals("1")){
                    p.setGender("男");
                }else{
                    p.setGender("女");
                }
                p.setEmail(jo.getString("email"));
                p.setTel(jo.getString("mobile"));
                list.add(p);
            }
            return list;
        }
        return null;
    }
}
