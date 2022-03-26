package java7;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private String Version;
    private int Status;
    private String Messages;
    private Map<String,String> headers = new HashMap<>();
    private StringBuilder body = new StringBuilder();

    public OutputStream outputStream = null;

    public static HttpResponse build(OutputStream outputStream) {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.outputStream = outputStream;
        return httpResponse;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public void setMessages(String messages) {
        Messages = messages;
    }

    public void setHeaders(String key, String value) {
        headers.put(key,value);
    }

    public void writeBody(String Content) {
        this.body.append(Content);
    }

    public void flush() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        //响应首行
        bufferedWriter.write(Version+" "+Status+""+Messages+"\n");
        headers.put("Content-Length",body.toString().getBytes().length+"");
        //反馈响应头回去;哈希表怎样打印？
        for (Map.Entry<String,String> entry : headers.entrySet()){
            bufferedWriter.write(entry.getKey()+":"+entry.getValue());
        }
        //响应body
        bufferedWriter.write(body.toString());
    }
}