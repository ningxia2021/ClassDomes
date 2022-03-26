package java6;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseReview {
    /**
     * 定义属性
     */
    private String version;
    private int status;
    private String messages;
    private Map<String,String> headers = new HashMap<>();
    private StringBuilder body = new StringBuilder();
    OutputStream outputStreams = null;

    public static HttpResponseReview build(OutputStream outputStream){
        HttpResponseReview ResponseReview = new HttpResponseReview();
        ResponseReview.outputStreams = outputStream;
        return ResponseReview;
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

    public void setHeaders( String key , String value) {
        headers.put(key,value);
    }

    public void writeBody(String content) {
        body.append(content);
    }

    public void flush() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStreams));
        /**
         * 展示首行
         */
        bufferedWriter.write(version+" "+status+" "+messages+"\n");
        /**
         * 展示所有headers；这个打印哈希表的手法要记住
         */
        headers.put("Content-Type","text/html; charset=utf-8");
        for (Map.Entry<String,String> entry:headers.entrySet()){
            bufferedWriter.write(entry.getKey()+": "+entry.getValue()+"\n");
        }
        bufferedWriter.write("\n");
        bufferedWriter.write(body.toString());
        /**
         * 目前的操作都是在内存上的，还没有提交给socket，一定要记得flush
         */
        bufferedWriter.flush();
    }
}









