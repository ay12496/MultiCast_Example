import java.io.*;
import java.net.*;

public class Client {
	public static void main(String[] args) {
		String serverName = "localhost";
		int port = Integer.parseInt("6000");
        BufferedReader brinp = null;
		try {
				System.out.println("Connecting to " + serverName + " on port " + port);
		         Socket client = new Socket(serverName, port);   //client  requesting to connect with the server
		         System.out.println("Just connected to " + client.getRemoteSocketAddress());
		         OutputStream outToServer = client.getOutputStream();  
		         DataOutputStream out = new DataOutputStream(outToServer);
		         brinp = new BufferedReader(new InputStreamReader(System.in));
		         String str;
		         while(true){
		         	System.out.print(">>");
		         	str=brinp.readLine();
		         	out.writeUTF(str);
		         	if(str.equalsIgnoreCase("Quits")){
		         		client.close();	
		         	}
		         	InputStream inFromServer = client.getInputStream();
		         	DataInputStream in = new DataInputStream(inFromServer);	
		         	String response = in.readUTF();
		         	if(!response.equals(""))
		         		System.out.println("<<" + response);
		         }
		         
		} 
		catch (IOException e) {
		   	e.printStackTrace();
		}
		
	}
}
