package java5;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseV4 {
    //Http响应包含 1.版本 2.状态码 3.信息 4.头键值对 5.body 6.输出流
    private String version ;
    private int status ;
    private String messages;
    private StringBuilder body = new StringBuilder();
    private Map<String,String> headers = new HashMap<>();
    private OutputStream outputStream = null;

    //初始化方法
    public static HttpResponseV4 build(OutputStream outputStream){
        //new对象 可以获取属性
        HttpResponseV4 responseV4 = new HttpResponseV4();
        //绑定输出流，具备输出功能
        responseV4.outputStream = outputStream;
        return responseV4;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }


    public void setHeaders(String key, String value) {
        headers.put(key,value);
    }

    public void Writebody(String content){
        body.append(content);
    }

    public void flush() throws IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        bufferedWriter.write(version+" "+status+" "+messages+"\n");
        headers.put("Content-Length", body.toString().getBytes().length + "");
        for (Map.Entry<String,String> entry:headers.entrySet()){
            bufferedWriter.write(entry.getKey()+":"+entry.getValue()+"\n");
        }
        bufferedWriter.write("\n");
        bufferedWriter.write(body.toString());
        bufferedWriter.flush();
    }
}
