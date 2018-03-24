import java.io.*;
import java.net.*;
import java.util.*;  


class Store{
   public String message="";
   public String address="";

   Store(String str){
      address=str;
   }
   public String getAddress(){
      return address;
   }

   public String getMessage(){
      
      String temp= new String(message);
      message="";
      return temp;
   }
   public void addMessage(String msg){
      message=message+"\n"+msg;
   }
}

public class Server {
   static ArrayList<Store> al=new ArrayList<Store>();  
   public static void main(String[] args) throws IOException{
      ServerSocket serverSocket= new ServerSocket(6000);
      while(true) {
         try {
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
            Socket clientSocket = serverSocket.accept();
            new ClientHandler(clientSocket).start();
            Store data=new Store(clientSocket.getRemoteSocketAddress().toString());
            al.add(data);   
         }
         catch (IOException e) {
            e.printStackTrace();
            break;
         }
      }
   }
}

class ClientHandler extends Thread{
   protected Socket socket;
   ClientHandler(Socket socket)throws IOException {
      this.socket=socket;
      // this.socket.setSoTimeout(10000);
   }

public void run() {
   String receivedFromOthers="";
   while(true) {
      try {
         System.out.println("Just connected to " + socket.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(socket.getInputStream());   //created a DataInputStream object 
            
            String received=in.readUTF();
            System.out.println(received); // Getting response using in.readUTF() and displaying client response
            if(received.equalsIgnoreCase("Quits")){
               socket.close();   
            }
            // Iterator itr=Server.al.iterator();  
            for(Store data: Server.al){
               String add=data.address;
               // System.out.println("Address:"+add);
               if(!add.equals(socket.getRemoteSocketAddress().toString())){
                  data.addMessage(received);
                  // System.out.println("unequal");
               }
               if(add.equals(socket.getRemoteSocketAddress().toString())){
                  receivedFromOthers = new String(data.getMessage());
                  // System.out.println("equal");
               }

            } 

            DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //created a DataOutputStream object
            // String str="Thank you for connecting to " + socket.getLocalSocketAddress();
            System.out.println(receivedFromOthers);
            out.writeUTF(receivedFromOthers);
         } 
         // catch (SocketTimeoutException s) {
         //    System.out.println("Socket timed out!");
         //    break;
         // } 
         catch (IOException e) {
            //e.printStackTrace();
            break;
         }
      }
   }

}
