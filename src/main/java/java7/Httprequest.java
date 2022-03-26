package java7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
/**
 * Http请求主要包含：
 * 1.首行：按照空格分割；包含请求方法，URL，版本
 * 2.请求头：按照 冒号+空格分割；
 * 3.空行；
 * 4.body：POST/PUT方法的参数保存在body中；GET方法的参数保存在url中  以http://localhost:9090/index.html?gaoh&123
 */
public class Httprequest {
    /**
     * 定义属性
     */
    private String METHOD, URL, VERSION, BODY;
    private Map<String, String> HEADERS = new HashMap<>();//用于保存请求头的键值对；
    private Map<String, String> Cookies = new HashMap<>();
    private Map<String, String> PARAMETERS = new HashMap<>();
    /**
     * 构造方法
     */
    public static Httprequest build(InputStream inputStream) throws IOException {
        //为了调用属性变量，需要new 实例
        Httprequest httprequest = new Httprequest();
        //创建字符流对象 接收形参inputstream从Socket.accept.getinputstream传入的字符流信息
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        //解析请求内容
        //a)首行
        String FIRSTLINE = bufferedReader.readLine();
        String[] FirstLineSplit = FIRSTLINE.split(" ");
        httprequest.METHOD = FirstLineSplit[0];
        httprequest.URL = FirstLineSplit[1];
        httprequest.VERSION = FirstLineSplit[2];
        //b)解析URL中的参数   形式形如：http://localhost:9090/index.html?username=gaoh&password=123
        int pos = httprequest.URL.indexOf("?");
        if (pos != -1) {
            String substring = httprequest.URL.substring(pos + 1);
            parseURL(substring,httprequest.HEADERS);
        }
        //c)解析headers
        String line = "";
        while ((line = bufferedReader.readLine()) != null && line.length() != 0) {
            //对每一行请求头按行读取，每一行都进行切分，并存入HEADERS的哈希表中
            String[] split = line.split(": ");
            httprequest.HEADERS.put(split[0], split[1]);
        }

        //e)解析Cookies
        //未来响应给的SessionId是要放在咱们自己设定Cooikes哈希表中
        //TODO
        String cookie = httprequest.Cookies.get("Cookie");
        if (cookie!=null){
            //Set-Cookie: SessionId=asddasaafafa-asdas-sadas; 这里要解析这个参数
            parseCookie(cookie,httprequest.Cookies);
        }

        //d)解析body
        if (httprequest.METHOD.equalsIgnoreCase("POST")||
                httprequest.METHOD.equalsIgnoreCase("GET")){
            //获取body有多少字节
            int ContentLength = Integer.parseInt(httprequest.HEADERS.get("Content-Length"));
            System.out.println("ContentLength:"+ContentLength);
            //开辟缓冲区
            char[] buffer = new char[ContentLength];
            //将bufferedRead内还剩下的内容（body）读进buffer缓冲区内，计算占据缓冲区的长度
            int len = bufferedReader.read(buffer);
            //将buffer内的字符转字符串 保存在BODY内
            httprequest.BODY = new String(buffer, 0, len);
            //处理参数
            parseBODY(httprequest.BODY, httprequest.PARAMETERS);
        }
        return httprequest;
    }

    private static void parseCookie(String cookie, Map<String, String> Cookies) {
        String[] split = cookie.split("; ");
        for (String s : split){
            String[] split1 = s.split("=");
            Cookies.put(split1[0],split1[1]);
        }
    }

    private static void parseURL(String substring, Map<String, String> headers) {
        String[] SubSplit = substring.split("&");
        for (String sub : SubSplit) {
            String[] split = sub.split("=");
            headers.put(split[0], split[1]);
        }
    }

    private static void parseBODY(String body, Map<String, String> parameters) {
        //BODY内的参数 形如url
        String[] bodySplit = body.split("&");
        for (String b : bodySplit){
            String[] split = b.split("=");
            parameters.put(split[0],split[1]);
        }
    }

    public String getMETHOD() {
        return METHOD;
    }

    public String getURL() {
        return URL;
    }

    public String getVERSION() {
        return VERSION;
    }

    public String getBODY() {
        return BODY;
    }

    public String getHEADERS(String key) {
        return HEADERS.get(key);
    }

    public String getCookies(String key) {
        return Cookies.get(key);
    }

    public String getPARAMETERS(String key) {
        return PARAMETERS.get(key);
    }
}
