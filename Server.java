import java.io.*;
import java.net.*;
import java.util.*;  


class Store extends Thread{
   public String message="";
   public String address="";
   protected Socket socket;
   Store(String address,Socket socket)throws IOException{
      this.address=address;
      this.socket=socket;
      System.out.println("address"+address);
   }
   public String getAddress(){
      return address;
   }

   public String getMessage(){
      return message;
   }
   public void addMessage(String msg){
      // System.out.println("Message to:"+getAddress()+msg);
      if(!message.equals(""))
         message=message+"\n"+msg;
      else
         message=message+msg;
   }

   public void run(){
      while(true) {
         try {
            String receivedFromOthers=new String(getMessage());
            if(!receivedFromOthers.equals("")) {
               // System.out.println("receivedFromOthers:"+receivedFromOthers);
               DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //created a DataOutputStream object
               System.out.println(receivedFromOthers);
               out.writeUTF(receivedFromOthers);
               message="";
            }
            
         } 
         catch (IOException e) {
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
               if(!add.equals(socket.getRemoteSocketAddress().toString())){
                  // System.out.println("unequal:"+data.getAddress());
                  data.addMessage(received);
               }
               // System.out.println("unequal:"+data.getMessage());
            } 
         } 
         catch (IOException e) {
            break;
         }
      }
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
            Store data=new Store(clientSocket.getRemoteSocketAddress().toString(),clientSocket);
            data.start();
            al.add(data);   
         }
         catch (IOException e) {
            e.printStackTrace();
            break;
         }
      }
   }
}

