import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientDgram{
    private InetAddress diaChiServer;
    private int portServer;

    private InetAddress diaChiClient;
    private int portClient;

    private DatagramSocket clientDgramSocket;

    private Thread threadGuiTinNhan;
    private Thread threadNhanTinNhan;

    public ClientDgram(InetAddress diaChiServer, int portServer, InetAddress diaChiClient, int portClient) {
        try {
            this.diaChiServer = diaChiServer;
            this.portServer = portServer;
            this.diaChiClient = diaChiClient;
            this.portClient = portClient;
            this.clientDgramSocket = new DatagramSocket(portClient);
            this.threadGuiTinNhan= new Thread(new RunnableGuiTinNhan());
            this.threadNhanTinNhan= new Thread(new RunnableNhanTinNhan());
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void guiTinNhan(){
        Scanner sc1= new Scanner(System.in);
        String str1= sc1.nextLine();

        byte[] byteGui= str1.getBytes();
        DatagramPacket dgramGui= new DatagramPacket(byteGui, byteGui.length, diaChiServer, portServer);

        try {
            clientDgramSocket.send(dgramGui);   
        } catch (IOException e) {
            System.out.println("IOException : "+ e.getMessage());
        }
        
        System.out.println("ban da gui : "+ str1);
    }
    
    public void nhanTinNhan() {
            byte[] byteNhan= new byte[1024];
            DatagramPacket dgramNhan= new DatagramPacket(byteNhan, byteNhan.length);
            
            try {
                clientDgramSocket.receive(dgramNhan);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            String str2= new String( dgramNhan.getData());

            System.out.println("server da gui : "+ str2);

            this.diaChiServer= dgramNhan.getAddress();
            this.portServer= dgramNhan.getPort();
        
    }

    public class RunnableGuiTinNhan implements Runnable{
        @Override
        public void run(){
            while(true){
                guiTinNhan();
            }
        }
    }

    public class RunnableNhanTinNhan implements Runnable{

        @Override
        public void run() {
            while(true){
                nhanTinNhan();
            }
        }
    }

    public void chayChuongTrinh(){
        
        while(true){
            threadGuiTinNhan.start();
            threadNhanTinNhan.start();
        }
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        InetAddress diaChiServer= InetAddress.getByName("localhost");
        int portServer= 1234;

        InetAddress diaChiClient = InetAddress.getByName("localhost");
        int portClient= 5678;

        ClientDgram clientDgram1 = new ClientDgram(diaChiServer, portServer, diaChiClient, portClient);
        clientDgram1.chayChuongTrinh();
    }
}
