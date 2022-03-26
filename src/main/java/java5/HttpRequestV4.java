package java5;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestV4 {
    private String METHOD,VERSION,URL,BODY;
    private Map<String,String> Headers = new HashMap<>();
    private Map<String,String> Parameters = new HashMap<>();
    private Map<String,String> Cookies = new HashMap<>();

    public static HttpRequestV4 bulid(InputStream inputStream) throws IOException {

        HttpRequestV4 request_V4 = new HttpRequestV4();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        //1.解析首行
        String FirstLine = bufferedReader.readLine();
        String[] splitFisrtLine = FirstLine.split(" ");
        request_V4.METHOD = splitFisrtLine[0];
        request_V4.URL = splitFisrtLine[1];
        request_V4.VERSION = splitFisrtLine[2];

        //2.解析URL中的参数
        int pos = request_V4.URL.indexOf("?");
        if (pos != -1){
            //username=gh&password=123
            String substring = request_V4.URL.substring(pos + 1);
            //将参数解析为键值对的格式保存在paramets哈希表里
            parsrKV(substring,request_V4.Parameters);
        }

        //3.解析Header
        String line = "";
        while((line = bufferedReader.readLine())!= null && line.length()!=0){
            String[] spliLine = line.split(": ");
            request_V4.Headers.put(spliLine[0],spliLine[1]);
        }

        //4.解析cookie
        //获取cookie的值
        String cookie = request_V4.Headers.get("Cookie");
        if (cookie != null){
            //cookie如果存在  接下来就将Cookie存的值拆分保存
            parseCookie(cookie,request_V4.Cookies);
        }

        //5.解析body
        //如果是post、put方法 则参数在body中；如果是get方法，则body'中没有参数
        if (request_V4.METHOD.equalsIgnoreCase("POST") ||
                request_V4.METHOD.equalsIgnoreCase("PUT")){
            //只有这两个方法需要处理body，所以这里做一下判断
            //需要读取出body，要知道body长度
            int body_ContentLength = Integer.parseInt(request_V4.Headers.get("Content-Length"));
            char[] buffer = new char[body_ContentLength];
            int bufffer_len = bufferedReader.read(buffer);
            request_V4.BODY = new String(buffer,0,bufffer_len);
            // body 中的格式形如: username=tanglaoshi&password=123   需要对其进行处理 并保存在parameters哈希表中
            parseBody(request_V4.BODY,request_V4.Parameters);
        }
        return request_V4;
    }

    private static void parseBody(String body, Map<String, String> parameters) {
        //按照&分割
        String[] splitSubstring = body.split("&");
        //将分割出来的参数按照=分割，保存在parameters的键值对里
        for (String kv : splitSubstring){
            //分一个
            String[] splitKv = kv.split("=");
            //保存一个
            parameters.put(splitKv[0],splitKv[1]);
        }
    }

    private static void parseCookie(String cookie, Map<String, String> cookies) {
        //按照 分号空格 分割
        String[] splitCookie = cookie.split("; ");
        //按照 等号 保存至cookies 哈希表
        for (String ck : splitCookie){
            String[] splitCk = ck.split("=");
            cookies.put(splitCk[0],splitCk[1]);
        }
    }

    private static void parsrKV(String substring, Map<String, String> parameters) {
        //按照&分割
        String[] splitSubstring = substring.split("&");
        //将分割出来的参数按照=分割，保存在parameters的键值对里
        for (String kv : splitSubstring){
            //分一个
            String[] splitKv = kv.split("=");
            //保存一个
            parameters.put(splitKv[0],splitKv[1]);
        }
    }

    public String getMETHOD() {
        return METHOD;
    }

    public String getVERSION() {
        return VERSION;
    }

    public String getURL() {
        return URL;
    }

    public String getBODY() {
        return BODY;
    }

    public String getHeaders(String key) {
        return Headers.get(key);
    }

    public String getParameters(String key) {
        return Parameters.get(key);
    }

    public String getCookies(String key) {
        return Cookies.get(key);
    }

    @Override
    public String toString() {
        return "HttpRequestV4{" +
                "METHOD='" + METHOD + '\'' +
                ", VERSION='" + VERSION + '\'' +
                ", URL='" + URL + '\'' +
                ", BODY='" + BODY + '\'' +
                ", Headers=" + Headers +
                ", Parameters=" + Parameters +
                ", Cookies=" + Cookies +
                '}';
    }
}
