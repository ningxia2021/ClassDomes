package Java1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpEchoServer {
    // 1.
    DatagramSocket socket = null;

    public UdpEchoServer(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    //2.
    public void start() throws IOException {
        System.out.println("服务器启动");
        while (true){
            //读取请求并解析
            DatagramPacket requestPacket = new DatagramPacket(new byte[4096],4096);
            //receive方法就会读网卡, 然后把读到的数据放到 requestPacket 变量持有的缓冲区里，如果没有相关请求，就会阻塞等待。
            socket.receive(requestPacket);
            String request = new String(requestPacket.getData(),0,requestPacket.getLength()).trim();
            //根据请求计算响应
            String response = process(request);
            //把响应写回给客户端，响应数据就是response，需要包装成Packet对象
            DatagramPacket responsePacket = new DatagramPacket(response.getBytes(),response.getBytes().length,
                    requestPacket.getSocketAddress());
            socket.send(responsePacket);
            System.out.println("["+requestPacket.getSocketAddress()+"] "+"req:"+ request+"; "+"resp:"+response);
        }
    }

    //3.
    private String process(String request) {
        return request;
    }

    //4.
    public static void main(String[] args) throws IOException {
        UdpEchoServer server = new UdpEchoServer(9090);
        server.start();
    }
}


