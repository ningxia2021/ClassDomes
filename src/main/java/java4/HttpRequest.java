package java4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    //定义请求中包含的元素
    private String Method;
    private String Url;
    private String Version;
    private Map<String,String> Header = new HashMap<>();
    private Map<String,String> Parameter = new HashMap<>();

    public static HttpRequest bulid(InputStream inputStream) throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
        //解析首行
        //BufferedReader.readLine 是调用一次 读取一行；
        String FirstLine = bufferedReader.readLine();
        String[] FirstLineSplit = FirstLine.split(" ");
        httpRequest.Method = FirstLineSplit[0];
        httpRequest.Url = FirstLineSplit[1];
        httpRequest.Version = FirstLineSplit[2];
        //解析header
        String line = "";
        //这一次调用就会读取第二行
        while((line = bufferedReader.readLine())!=null && line.length() != 0){
            String[] Headersplit = line.split(": ");
            httpRequest.Header.put(Headersplit[0],Headersplit[1]);
        }
        //解析url参数
            //indexOf() 方法可返回数组中某个指定的元素位置。如果在数组中没找到指定元素则返回 -1
        int pos = httpRequest.Url.indexOf("?");
        if (pos != -1){
            String Parameters = httpRequest.Url.substring(pos+1);
            parseKv(Parameters,httpRequest.Parameter);
        }
        return httpRequest;
    }

    private static void parseKv(String Parameters, Map<String, String> output) {
        //1.先按照&来切分
        String[] ParameterSplit = Parameters.split("&");
        //2.针对切分结果再分别按照=进行切分
        for (String kv : ParameterSplit) {
            String[] kvSplit = kv.split("=");
            output.put(kvSplit[0],kvSplit[1]);
        }
    }

    //添加 get 方法
    public String getMethod() {
        return Method;
    }

    public String getUrl() {
        return Url;
    }

    public String getVersion() {
        return Version;
    }

    public String getHeader(String key) {
        return Header.get(key);
    }

    public String getParameter(String key) {
        return Parameter.get(key);
    }

    @Override
    public String toString() {
        return "HttpRequestV4{" +
                "Method='" + Method + '\'' +
                ", Url='" + Url + '\'' +
                ", Version='" + Version + '\'' +
                ", Header=" + Header +
                ", Parameter=" + Parameter +
                '}';
    }
}
