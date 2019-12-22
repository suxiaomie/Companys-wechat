package com.example.companys.service.impl;

import com.example.companys.entity.Department;
import com.example.companys.entity.Person;
import com.example.companys.service.weixin.WeiXinParamesUtil;
import com.example.companys.service.weixin.WeiXinUtil;
import net.sf.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SqlImpl {

    //发送数据库数据到微信服务器  Department
    public String DepsendToWeChat(List<Department> list) {
        //1.获取access_token:定义数据缓冲区
        StringBuilder sb = new StringBuilder();
        String accessToken= WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.contactsSecret).getToken();
        //2.添加数据库信息至缓冲区
        sb.append("部门名称,部门ID,父部门ID,\n");
        for(Department department:list){
            sb.append(department.toStrings() + "\n");
        }
        //3.发送部门信息，直接以bytes形式发送文件并接受微信返回信息
        String res = this.sendFileByByte(sb.toString().getBytes(),"https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token="
                + accessToken + "&type=" + "file");
        //4.分析返回结果，取出 media_id
        JSONObject jsonObject = JSONObject.fromObject(res);
        res = jsonObject.get("media_id").toString();
        //5.覆盖部门信息并接受返回结果
        String result= updateDepartment(res,accessToken);
        return result;
    }

    //发送数据库数据到微信服务器  Person
    public String PersendToWeChat(List<Person> list) {
        //1.获取access_token:定义数据缓冲区
        StringBuilder sb = new StringBuilder();
        String accessToken= WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.contactsSecret).getToken();
        sb.append("姓名,账号,手机号,邮箱,所在部门,职位,性别\n");
            for(Person person:list){
            sb.append(person.toStrings() + "\n");
        }
        //发送部门信息，直接以bytes形式发送文件并接受微信返回信息
        String res = this.sendFileByByte(sb.toString().getBytes(),"https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token="
                + accessToken + "&type=" + "file");
        System.out.println("文件发送结果："+res);
        //发送后的返回结果
        JSONObject jsonObject = JSONObject.fromObject(res);
        //获取文件id
        res = jsonObject.get("media_id").toString();
        //覆盖人员信息
        String result= updatePerson(res,accessToken);
        return result;
    }

    //覆盖部门信息
    public String updateDepartment(String media_id ,String accessToken){
        //1.定义发送包体及请求 URL
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("media_id", media_id);
        String url="https://qyapi.weixin.qq.com/cgi-bin/batch/replaceparty?access_token="+accessToken;
        //2.根据文件 id 进行部门覆盖操作，接受返回信息
        JSONObject get_data= WeiXinUtil.httpRequest(url, "POST", jsonObject.toString());
        //3.从返回信息中提取 异步任务id
        String jobid=get_data.getString("jobid");
        //4.获取任务的执行结果
        while(true){
            JSONObject result= WeiXinUtil.httpRequest("https://qyapi.weixin.qq.com/cgi-bin/batch/getresult?access_token=" + accessToken
                    + "&jobid=" +jobid, "GET", null);
            if(result.getString("status").equals("3")&&result.getString("errcode").equals("0")){
                return "部门覆盖完毕";
            }else {
                return "部门覆盖失败";
            }
        }
     }

    //覆盖人员信息
    public String updatePerson(String media_id ,String accessToken){

        //1.定义发送包体及请求 URL
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("media_id", media_id);
        String url="https://qyapi.weixin.qq.com/cgi-bin/batch/replaceuser?access_token="+accessToken;
        //2.根据文件 id 进行部门覆盖操作，接受返回信息
        JSONObject get_data= WeiXinUtil.httpRequest(url, "POST", jsonObject.toString());
        //3.从返回信息中提取 异步任务id
        String jobid=get_data.getString("jobid");
        //4.获取任务的执行结果
        while(true){
            JSONObject result= WeiXinUtil.httpRequest("https://qyapi.weixin.qq.com/cgi-bin/batch/getresult?access_token=" + accessToken
                    + "&jobid=" +jobid, "GET", null);
            System.out.println("任务处理结果："+result);
            if(result.getString("status").equals("3")&&result.getString("errcode").equals("0")){
                return "人员信息覆盖完毕";
            }else if (result.getString("status").equals("2")&&result.getString("errcode").equals("0")){

            }else if (result.getString("status").equals("1")&&result.getString("errcode").equals("0")){

            }else {
                return "人员信息覆盖失败";
            }
        }
    }
/*
    一个支持HTTP特定功能的URLConnection，使用这个类遵循以下模式：

　　1.通过调用URL.openConnection()来获得一个新的HttpURLConnection对象，并且将其结果强制转换为HttpURLConnection.

　　2.准备请求。一个请求主要的参数是它的URI。请求头可能也包含元数据，例如证书，首选数据类型和会话cookies.

　　3.可以选择性的上传一个请求体。HttpURLConnection实例必须设置setDoOutput(true)，如果它包含一个请求体。通过将数据写入一个由getOutStream()返回的输出流来传输数据。

　　4.读取响应。响应头通常包含元数据例如响应体的内容类型和长度，修改日期和会话cookies。响应体可以被由getInputStream返回的输入流读取。如果响应没有响应体，则该方法会返回一个空的流。

　　5.关闭连接。一旦一个响应体已经被阅读后，HttpURLConnection 对象应该通过调用disconnect()关闭。断开连接会释放被一个connection占有的资源，这样它们就能被关闭或再次使用。
*/
    //发送文件至微信服务器
    private String sendFileByByte(byte[] bytes, String urlStr) {
        try {
            // 换行符
            final String newLine = "\r\n";
            // 服务器的上传地址
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置请求方式
            conn.setRequestMethod("POST");
            // 设置连接参数
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=-------------------------acebdf13572468");
            //建立连接发送请求
            OutputStream out = conn.getOutputStream();

            // 上传文件
            StringBuilder sb = new StringBuilder();
            //换行
            sb.append(newLine);
            sb.append(newLine);
            // 文件参数
            sb.append("---------------------------acebdf13572468\r\n" +
                    "Content-Disposition: form-data; name=\"media\"; filename=\"" + "date.csv" + "\";filelength="+bytes.length+"\r\n");
            sb.append("Content-Type:application/octet-stream");
            // 换行接数据库数据
            sb.append(newLine);
            sb.append(newLine);
            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());
            //写入数据库数据
            out.write(bytes, 0, bytes.length);

            //换行
            out.write(newLine.getBytes());
            // 定义最后数据分隔线
            byte[] end_data = "---------------------------acebdf13572468--".getBytes();
            // 写上结尾标识，添加最后数据分隔线
            out.write(end_data);
            out.write(newLine.getBytes());
            //关闭传输连接
            out.close();

            // 定义BufferedReader输入流来读取URL的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //将接收到的数据存入缓存
                buffer.append(line);
            }
            return buffer.toString();
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        return null;
    }
}
