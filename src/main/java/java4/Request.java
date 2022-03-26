package java4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private String method,url,version;
    private Map<String,String> header = new HashMap<>();
    private Map<String,String> parameter = new HashMap<>();

    //请求构造方法，参数为socket建立连接后传入的字节流
    public static Request bulid(InputStream inputStream) throws IOException {
        Request request = new Request();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        //读取并解析首行
        String firstLine = bufferedReader.readLine();
        String[] splitFirstLine = firstLine.split(" ");
        request.method = splitFirstLine[0];
        request.url = splitFirstLine[1];
        request.version = splitFirstLine[2];
        //解析header
        String line = "";
        while((line = bufferedReader.readLine())!=null && line.length() != 0){
            String[] splitheader = line.split(": ");
            request.header.put(splitheader[0],splitheader[1]);
        }
        //解析parameter
        int pos = request.url.indexOf("?");
        if (pos != -1){
            String parameters = request.url.substring(pos+1);
            parseKv(parameters,request.parameter);
        }
        return request;
    }

    //例如 url = "http://www.baidu.com/book/novel?
    // content=c_app&
    // title=斗破苍穹&
    // createtime=201604262247&
    // type=玄幻&
    // docid=2698548";
    private static void parseKv(String parameters, Map<String, String> parameter) {
        //此处是区分有多少对参数
        String[] splitparameter = parameters.split("&");
        //此处为将每一对的键值对拆分出来
        //for(数组中的元素类型：数组名）
        for (String kv : splitparameter){
            String[] splitKv = kv.split("=");
            parameter.put(splitKv[0],splitKv[1]);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public String getHeader(String KEY) {
        return header.get(KEY);
    }

    public String getParameter(String key) {
        return parameter.get(key);
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                ", header=" + header +
                ", parameter=" + parameter +
                '}';
    }
}
