package java4;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private String version;
    private int status;
    private String messages;
    private Map<String ,String > header = new HashMap<>();
    private StringBuilder body = new StringBuilder();
    OutputStream outputStream = null;

    public static HttpResponse bulid(OutputStream outputStream){
        HttpResponse response = new HttpResponse();
        response.outputStream = outputStream;
        return response;
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

    public void setHeader(String key,String value) {
        this.header.put(key,value);
    }

    public void writeBody(String body) {
        this.body.append(body);
    }

    public void flush() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        bufferedWriter.write(version+" "+status+""+messages+"\n");
        header.put("Content-Lenth",body.toString().getBytes().length+"");
        for (Map.Entry<String, String> entry :header.entrySet()){
            bufferedWriter.write(entry.getKey()+":"+entry.getValue()+"\n");
        }
        bufferedWriter.write("\n");
        bufferedWriter.write(body.toString());
        bufferedWriter.flush();
    }
}
