import java.io.*;
import java.net.*;

public class Client extends Thread{

	private static Socket client;

	public Client(String serverName,int port) throws IOException{
		client = new Socket(serverName, port);	
	}
	
	public static void main(String[] args) {
		String serverName = "localhost";
		int port = Integer.parseInt("6000");
		BufferedReader brinp = null;
		try {
			Thread t = new Client(serverName,port);
			t.start();
			System.out.println("Connecting to " + serverName + " on port " + port);
		            //client  requesting to connect with the server
			System.out.println("Just connected to " + client.getRemoteSocketAddress());
			OutputStream outToServer = client.getOutputStream();  
			DataOutputStream out = new DataOutputStream(outToServer);
			brinp = new BufferedReader(new InputStreamReader(System.in));
			String str;
			while(true){
				System.out.print("");
				str=brinp.readLine();
				out.writeUTF(str);
				if(str.equalsIgnoreCase("Quits")){
					client.close();	
				}

			}

		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void run(){
		while(true) {
			try {
				InputStream inFromServer = client.getInputStream();
				DataInputStream in = new DataInputStream(inFromServer);	
				String response = in.readUTF();
				if(!response.equals(""))
					System.out.println("\n" + response);
			}
			catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		
	}
}
