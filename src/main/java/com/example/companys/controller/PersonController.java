package com.example.companys.controller;

import com.example.companys.entity.Person;
import com.example.companys.service.WeiXinPersonService;
import com.example.companys.service.impl.PersonServiceImpl;
import com.example.companys.service.weixin.WeiXinParamesUtil;
import com.example.companys.service.weixin.WeiXinUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;
@RestController
public class PersonController {
    @Autowired
    private PersonServiceImpl service;

    //1.添加人员入口   OK
    @PostMapping("/addPerson")
    public String addPerson(Person person){
        //1.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
        String accessToken= WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.contactsSecret).getToken();
        System.out.println("accessToken:"+accessToken);
        //2.创建成员
        if (person.getGender().equals("")||person.getGender().equals("男")){
            person.setGender("1");
        }else if (person.getGender().equals("女")){
            person.setGender("2");
        }else {
            return "性别填写不符合规范";
        }
        WeiXinPersonService weiXinPersonService =new WeiXinPersonService();
        String result= weiXinPersonService.createPerson(accessToken,person);
        System.out.println(person.getGender());

        //3.数据库操作
        if (result=="添加成功"){
            service.addPerson(person);
        }
        return result;
    }

    //2.获取成员信息入口 OK
    @GetMapping("/get/{userid}")
    public  JSONObject getPerson(@PathVariable String userid){
        //1.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
        String accessToken= WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.contactsSecret).getToken();
        System.out.println("accessToken:"+accessToken);

        //2.获取成员
        WeiXinPersonService weiXinPersonService =new WeiXinPersonService();
        JSONObject jo= weiXinPersonService.getPerson(accessToken,userid);
        if (jo.getString("gender").equals("1")){
            jo.put("gender","男");
        }else{
            jo.put("gender","女");
        }
        return jo;
    }

    //3.更新成员信息入口  OK
    @PostMapping("/updatePerson")
    public String UpdatePerson(Person person) {
        //1.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
        String accessToken= WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.contactsSecret).getToken();
        //String accessToken="-GHvJUSwn8IOhjcloYTohW5G41oniJ6cspyikVhyHOc53t6T4PhXUb2ATmKRJC0uBkiby_Rb5hERg21xP8IcrGFZQU_eIJY-eTwhEwMxrPhTYTaGaXs7H4kuGzWj7SW-pUF5qx50vNwcZmMEKyq_GEa3vSDCmQudB9XaGVY-KetX870PT7xlxa81q0VD7uJLYCAUQXeaCheDiHpA_EWlpw" ;
        System.out.println("accessToken:"+accessToken);

        //2.更改成员
        WeiXinPersonService weiXinPersonService =new WeiXinPersonService();
        String result = weiXinPersonService.updatePerson( accessToken,person);
        //3.数据库操作
        if (result=="更新成功"){
            service.updatePerson(person);
        }
        return result;
    }

    //4.删除成员入口  OK
    @GetMapping("/deletePerson/{userid}")
    public String DeletePerson(@PathVariable String userid){
        //1.获取userId
        String userId=userid;

        //2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
        String accessToken= WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.contactsSecret).getToken();
        System.out.println("accessToken:"+accessToken);

        //3.删除成员
        WeiXinPersonService weiXinPersonService =new WeiXinPersonService();
        String result= weiXinPersonService.deletePerson(accessToken,userId);
        //数据库操作
        if (result=="删除成功"){
            service.deletePerson(userId);
        }
        return result;
    }

    //5.批量删除成员入口
    public void testbatchdeleteUser() {
        //1.获取userIdList
        String userId1="5";
        String userId2="4";
        List<String> userIdList = Arrays.asList(userId1, userId2);  //此时将userIdList转json,则结果为：["3","4"],会报错：{"errcode":40063,"errmsg":"some parameters are empty"}

        //2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
        String accessToken= WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.contactsSecret).getToken();
        System.out.println("accessToken:"+accessToken);

        //3.批量删除成员
        WeiXinPersonService weiXinPersonService =new WeiXinPersonService();
        weiXinPersonService.batchdeleteUser(accessToken,userIdList);
    }

    //6.获取部门成员入口    OK
    @GetMapping("/getDepartmentPersonShort/{departmentId}")
    public List<Person> getDepartmentPerson(@PathVariable String departmentId) {
        //1.获取部门ID以及是否获取子部门成员
        String fetchChild="0";

        //2.获取accessToken:根据企业id和通讯录密钥获取access_token,并拼接请求url
        String accessToken= WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.contactsSecret).getToken();
        System.out.println("accessToken:"+accessToken);

        //3.获取部门成员
        WeiXinPersonService weiXinPersonService =new WeiXinPersonService();
        List<Person> list= weiXinPersonService.getDepartmentPerson(accessToken, departmentId, fetchChild);
        return list;
    }

    //7.获取部门成员详情入口 OK
    @GetMapping("/getDepartmentPerson/{departmentId}")
    public List<Person> getDepartmentUserDetails(@PathVariable String departmentId ) {
        //1.获取部门ID以及是否获取子部门成员
        String fetchChild="1";

        //2.获取accessToken:根据企业id和通讯录密钥获取access_token,并拼接请求url
        String accessToken= WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.contactsSecret).getToken();
        System.out.println("accessToken:"+accessToken);

        //3.获取部门成员
        WeiXinPersonService weiXinPersonService =new WeiXinPersonService();
        List<Person> list=  weiXinPersonService.getDepartmentUserDetails(accessToken, departmentId, fetchChild);
        return list;
    }
}
