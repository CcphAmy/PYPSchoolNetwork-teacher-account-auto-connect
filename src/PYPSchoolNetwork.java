import java.net.URLDecoder;
import java.net.URLEncoder;

import java.io.*;
import java.net.URL;
import java.net.InetAddress;
import java.net.URLConnection;
import java.net.HttpURLConnection;

import java.text.SimpleDateFormat;  

import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;  
import java.util.List;  
import java.util.regex.Matcher;  
import java.util.regex.Pattern;

public class PYPSchoolNetwork{
    /**
     * 
     * @param url
     * @param param
     * @return
     */
    public static String HttpPost(String url, String param) {
        String result="";
        PrintWriter output=null;
        BufferedReader input=null;
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
									"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.3538.400 QQBrowser/9.6.12501.400");

            conn.setDoOutput(true);
            conn.setDoInput(true);

            output = new PrintWriter(conn.getOutputStream());

            output.print(param);
            output.flush();

            input = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("print error ,Exception!"+e);
            e.printStackTrace();
        }
        //finally close 
        finally{
            try{
                if(output!=null){
                    output.close();
                }
                if(input!=null){
                    input.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    /**
     * GET
     * @param url
     * @param param
     * @return
     */
    public static String HttpGet(String url,String params)
    {
        String result="";
        //System.out.println("into");
        BufferedReader in=null;
        try {
            String urlName=params.equals("")?url:url+"?"+params;
            URL realUrl;
            realUrl = new URL(urlName);
            URLConnection conn=realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.3538.400 QQBrowser/9.6.12501.400");
            conn.connect();
            Map<String,List<String>> map=conn.getHeaderFields();
            for(String key:map.keySet())
            {
                System.out.println(key+"--->"+map.get(key));
            }
            in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while((line=in.readLine())!=null)
            {
                result+="\n"+line;
            }
        } 
        catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("GET error"+e);
            e.printStackTrace();
        }
        finally
        {
            if(in!=null)
            {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return result;
        
    }

 
    public static String getLocation(String url) {
        try {
            URL serverUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            String location = conn.getHeaderField("Location");
            return location;
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /** 
     * write file stream
     * @param
     * @param
     * @return 
     * from web...
     */  
	public static void writeByFileOutputStream(String content,String filePath) {  

        FileOutputStream fop = null;  
        File file;
        try {  
            file = new File(filePath);  
            fop = new FileOutputStream(file);  
            // if file doesnt exists, then create it  
            if (!file.exists()) {  
                file.createNewFile();  
            }  
            // get the content in bytes  
            byte[] contentInBytes = content.getBytes();  

            fop.write(contentInBytes);  
            fop.flush();  
            fop.close();  

        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (fop != null) {  
                    fop.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  

    /** 
     * return one string 
     * @param
     * @param
     * @return 
     */  
    public static String getSubUtilSimple(String soap,String rgex){  
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(soap);  
        while(m.find()){  
            return m.group(1);  
        }  
        return "";  
    }  

    public static boolean netWorkConnct(){
        String reTest  = HttpGet("https://www.sogou.com/","");

        if (reTest.indexOf("soso.com") > -1) {
            return true;
        }
        return false;
    }

    public static void main(String[] args)  {

        String userId   = ""; //账号
        String password = ""; //密码
        // 只支持登录教职员工账号
        String service  = "teacher";

        try{

            String reTest  = HttpGet("http://192.168.200.84","");

            if (reTest.indexOf("您已成功连接校园网") == -1) {

                if(reTest.indexOf("top.self.location.href")>-1){

                    String postDataT  = new String(getSubUtilSimple(reTest,"jsp\\?(.*?)\'</scr"));
                    postDataT         = postDataT.replace("=","%253D"); postDataT = postDataT.replace("&","%2526");
                                        //%253D = ；%2526 &
                    String postParams = "userId=" + userId + "&password=" + password + "&service=" + service + "&queryString=" +
                                        postDataT + "&operatorPwd=&operatorUserId=&validcode=";

                    String reOnePost  = HttpPost("http://192.168.200.84/eportal/InterFace.do?method=login",postParams);

                    byte[] bs = reOnePost.getBytes("GBK");//utf-8
                    reOnePost = new String(bs,"UTF-8");

                    String result  = new String(getSubUtilSimple(reOnePost,"result\":\"(.*?)\""));
                    String message = new String(getSubUtilSimple(reOnePost,"message\":\"(.*?)\""));

                    System.out.println("result--->["+result+"]");
                    System.out.println("message--->["+message+"]");

                    if (result.indexOf("success")>-1) {
                        System.out.println("message--->[连接成功,定向访问检测...]");
                        if (!netWorkConnct()) {

                            reTest           = getLocation("http://192.168.200.84/eportal/gologout.jsp");
                            String userIndex = new String(getSubUtilSimple(reTest + "&&","userIndex=(.*?)&&"));

                            System.out.println("userIndex--->["+userIndex+"]");

                            if (userIndex.length()>10) {
                                reOnePost  = HttpPost("http://192.168.200.84/eportal/InterFace.do?method=logout","userIndex=" + userIndex);
                                System.out.println("message--->[登出,重新登录]");
                                String[] str = new String[0];
                                main(str);
                            }else{
                                System.out.println("message--->[网络鉴定正常!]");
                            }
                        }
                        
                    }
                }

            }else{
                    if (!netWorkConnct()) {
                        System.out.println("message--->[异常接口:192.168.200.84]");
                    }else{
                        System.out.println("message--->[网络鉴定正常!]");
                    }
                    
            }

        }catch (Exception ea) {
            System.out.println("Exception--->error 1001");
            //ea.printStackTrace();
        }
	}
}