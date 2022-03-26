package java6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 复现HttpServerV4版本
 * 目的：更加理解和熟练这一块的代码
 */
public class HttpRequestReview {
    /**
     * 这一块要定义属性
     * HTTP请求包含哪些内容以及这些内容需要怎样保存与提取
     * 1.首行：方法，url，version
     * 2.headers：键值对
     * 3.body
     */
    private String method;
    private String url;
    private String version;
    private String body;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> parameters = new HashMap<>();
    private Map<String, String> cookies = new HashMap<>();

    /**
     * 创建方法，接收字符流，处理请求逻辑
     */
    public static HttpRequestReview build(InputStream inputStream) throws IOException {
        HttpRequestReview httpRequestReview = new HttpRequestReview();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        /**
         * 1.解析首行
         */
        String firstLine = bufferedReader.readLine();
        String[] fl_Split = firstLine.split(" ");
        httpRequestReview.method = fl_Split[0];
        httpRequestReview.url = fl_Split[1];
        httpRequestReview.version = fl_Split[2];

//        System.out.println("method = " + httpRequestReview.method + " url = " + httpRequestReview.url + " version = " + httpRequestReview.version);

        /**
         * 2.解析header
         */
        String line = "";
        while ((line = bufferedReader.readLine()) != null && line.length() != 0) {
            String[] line_split = line.split(": ");
            httpRequestReview.headers.put(line_split[0], line_split[1]);
        }
//        System.out.println(httpRequestReview.headers);

        /**
         * 3.解析URL中的参数
         */
        int pos = httpRequestReview.url.indexOf("?");
        if (pos != -1) {
            String substring = httpRequestReview.url.substring(pos + 1);
            parseUrl(substring, httpRequestReview.parameters);
        }

        /**
         * 4.解析Cookie
         */
        String cookie = httpRequestReview.cookies.get("Cookie");
        if (cookie != null) {
            parseCookies(cookie, httpRequestReview.cookies);
        }

        /**
         * 5.解析body
         * GET方法是将参数显示再URL中
         * POST PUT方法是将参数显示再body中
         */
        if (httpRequestReview.method.equalsIgnoreCase("POST") ||
                httpRequestReview.method.equalsIgnoreCase("PUT")) {
            /**
             * 首先确定body的长度 比如body=“abcdef”  那么ContentLength就是6
             * 开启空间
             * bufferedReader.read(buffer) 从socket中读取数据,经过上面一系列按行读取，剩下的都输出至buffer。
             */

            int ContentLength = Integer.parseInt(httpRequestReview.headers.get("Content-Length"));
            char[] buffer = new char[ContentLength];
            int len = bufferedReader.read(buffer);

            httpRequestReview.body = new String(buffer, 0, len);
//            System.out.println(httpRequestReview.body);
            parseBody(httpRequestReview.body, httpRequestReview.parameters);
//            System.out.println("request 中的 "+httpRequestReview.parameters);
        }
//        System.out.println(httpRequestReview.body);
        return httpRequestReview;
    }

    // body 中的格式形如: username=tanglaoshi&password=123
    private static void parseBody(String body, Map<String, String> parameters) {
        String[] split = body.split("&");
        for (String bs : split) {
            String[] split1 = bs.split("=");
            parameters.put(split1[0],split1[1]);
        }
    }

    private static void parseCookies(String cookie, Map<String, String> cookies) {
        //cookie 每一个键值对用 分号空格 分割
        String[] split = cookie.split("; ");
        for (String cs : split) {
            String[] split1 = cs.split("=");
            cookies.put(split1[0], split1[1]);
        }
    }

    private static void parseUrl(String substring, Map<String, String> parameters) {
        String[] sub_split = substring.split("&");
        for (String sub : sub_split) {
            String[] split = sub.split("=");
            parameters.put(split[0], split[1]);
        }

    }

    /**
     * 添加get方法
     */
    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public String getBody() {
        return body;
    }

    public String getHeaders(String key) {
        return headers.get(key);
    }

    public String getParameters(String key) {
        return parameters.get(key);
    }

    public String getCookies(String key) {
        return cookies.get(key);
    }

    @Override
    public String toString() {
        return "HttpRequestReview{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                ", body='" + body + '\'' +
                ", headers=" + headers +
                ", parameters=" + parameters +
                ", cookies=" + cookies +
                '}';
    }
}













