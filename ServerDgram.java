import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ServerDgram{
    private InetAddress diaChiServer;
    private int portServer;

    private InetAddress diaChiClient;
    private int portClient;

    private DatagramSocket serverDgramSocket;
    
    private Thread threadGuiTinNhan;
    private Thread threadNhanTinNhan;

    public ServerDgram(InetAddress diaChiServer, int portServer, InetAddress diaChiClient, int portClient) throws SocketException {
        this.diaChiServer = diaChiServer;
        this.portServer = portServer;
        this.diaChiClient = diaChiClient;
        this.portClient = portClient;
        this.serverDgramSocket = new DatagramSocket(portServer);
        this.threadGuiTinNhan= new Thread( new RunnableGuiTinNhan());
        this.threadNhanTinNhan= new Thread( new RunnableNhanTinNhan());
    }

    public void guiTinNhan() {
        Scanner sc1= new Scanner(System.in);
        String str1= sc1.nextLine();

        byte[] byteGui= str1.getBytes();
        DatagramPacket dgramGui= new DatagramPacket(byteGui, byteGui.length, diaChiClient, portClient);
        
        try {
            serverDgramSocket.send(dgramGui);
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("IOException : "+ e.getMessage());
        }
        
        System.out.println("ban da gui : "+ str1);
    }

    public void nhanTinNhan(){
        byte[] byteNhan = new byte[1024];
        DatagramPacket dgramNhan = new DatagramPacket(byteNhan, byteNhan.length);

        try {
            serverDgramSocket.receive(dgramNhan);
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("IOException : "+ e.getMessage());
        }

        String str2 = new String(dgramNhan.getData());
        System.out.println("Client da gui : "+ str2);

        this.diaChiClient = dgramNhan.getAddress();
        this.portClient = dgramNhan.getPort();
    }

    public class RunnableGuiTinNhan implements Runnable{

        @Override
        public void run() {
            while(true){
                guiTinNhan();
            }
        }
    }

    public class RunnableNhanTinNhan implements Runnable{

        @Override
        public void run(){
            while(true){
                nhanTinNhan();
            }
        }
    }

    public void chayChuongTrinh(){
        while(true){
            threadNhanTinNhan.start();
            threadGuiTinNhan.start();
        }
    }

    /*bat dau*/
    public InetAddress getDiaChiServer() {
        return diaChiServer;
    }

    public void setDiaChiServer(InetAddress diaChiServer) {
        this.diaChiServer = diaChiServer;
    }

    public int getPortServer() {
        return portServer;
    }

    public void setPortServer(int portServer) {
        this.portServer = portServer;
    }

    public InetAddress getDiaChiClient() {
        return diaChiClient;
    }

    public void setDiaChiClient(InetAddress diaChiClient) {
        this.diaChiClient = diaChiClient;
    }

    public int getPortClient() {
        return portClient;
    }

    public void setPortClient(int portClient) {
        this.portClient = portClient;
    }

    public DatagramSocket getServerDgramSocket() {
        return serverDgramSocket;
    }

    public void setServerDgramSocket(DatagramSocket serverDgramSocket) {
        this.serverDgramSocket = serverDgramSocket;
    }
    /*ket thuc */

    public static void main(String[] args) throws SocketException, UnknownHostException{
        InetAddress diaChiServer= InetAddress.getByName("localhost");
        int portServer= 1234;

        InetAddress diaChiClient = InetAddress.getByName("localhost");
        int portClient= 5678;

        ServerDgram serverDgram1 = new ServerDgram(diaChiServer, portServer, diaChiClient, portClient);
        serverDgram1.chayChuongTrinh();
    }
}