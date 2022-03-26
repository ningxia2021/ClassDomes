package Java1;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UdpEchoClient2 {
    private String server_ip;
    private int server_port;
    DatagramSocket socket = null;

    public UdpEchoClient2(String server_ip, int server_port) throws SocketException {
        this.server_ip = server_ip;
        this.server_port = server_port;
        socket = new DatagramSocket();
    }

    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("->");
            String request = scanner.nextLine();
            //构造请求发送给服务端
            DatagramPacket requestPacket = new DatagramPacket(request.getBytes(),request.getBytes().length,
                    InetAddress.getByName(server_ip),server_port);
            socket.send(requestPacket);
            //接收服务端返回的响应
            DatagramPacket responsePacket = new DatagramPacket(new byte[4096],4096);
            socket.receive(responsePacket);
            String response = new String(responsePacket.getData(),0,responsePacket.getLength()).trim();
            System.out.println(response);

        }
    }

    public static void main(String[] args) throws IOException {
        UdpEchoClient2 client2 = new UdpEchoClient2("127.0.0.1",9090);
        client2.start();
    }
}
