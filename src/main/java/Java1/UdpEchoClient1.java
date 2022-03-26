package Java1;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UdpEchoClient1 {

    private String serverip;
    private int serverport;
    DatagramSocket socket = null;

    public UdpEchoClient1(String serverip, int port) throws SocketException {
        this.serverip = serverip;
        this.serverport = port;
        socket = new DatagramSocket();
    }

    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while(true){
            //读取用户数据
            System.out.println("-> ");
            String request = scanner.nextLine();
            if (request.equals("exit")){
                break;
            }
            //构造请求发送给服务器
            DatagramPacket requestPacket = new DatagramPacket(request.getBytes(),request.getBytes().length,
                    InetAddress.getByName(serverip),serverport);
            socket.send(requestPacket);
            //从服务器读取响应
            DatagramPacket responsePacket = new DatagramPacket(new byte[4096],4096);
            socket.receive(responsePacket);
            String response = new String(responsePacket.getData(),0,responsePacket.getLength()).trim();
            //显示数据
            System.out.println(response);
        }
    }

    public static void main(String[] args) throws IOException {
        UdpEchoClient1 client = new UdpEchoClient1("127.0.0.1",9090);

        client.start();
    }

}
