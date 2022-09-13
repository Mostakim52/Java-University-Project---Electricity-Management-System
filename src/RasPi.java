import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Platform;

public class RasPi{
	double watts;
	public static boolean isConnected;
	public RasPi(){
		watts = 0;
		Thread t = new Thread() {
			public void run() {
				try {
					Server(6666);
				} catch (Exception i) {
					watts = 0;
				}
			}
		};
		t.start();
	}
	
	
	// constructor with port
	public void Server(int port) throws Exception{

		//initialize socket and input stream
		Socket socket = null;
		ServerSocket server = null;
		DataInputStream in = null;
		
		while(true) {
					try {
						try{
				            InetAddress address = InetAddress.getByName("192.168.137.55");
				            boolean reachable = address.isReachable(1000);
				            if (!reachable) {
				            	//System.out.println("nope");
				            	continue;
				            }
				            } 
						catch (Exception e){
				            e.printStackTrace();
				        	}
						server = new ServerSocket(port);
						server.setSoTimeout(1000);
						socket = server.accept();
						// takes input from the client socket
						in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
						// reads message from client until exeption happens
						while (true){
							try{
								watts = Double.parseDouble(in.readUTF());
								isConnected = true;
							}
							catch(IOException i){
								isConnected = false;
								watts = 0;
								socket.close();
								in.close();
								throw i;
								}
							}
					}
					catch(Exception e){
						server.close();
						if (socket != null) socket.close();
					}
					Thread.sleep(2000);
		}
	}
	
	public static void controldownPi(boolean onoff) {
		boolean done = false;
		while(!done) {
			try{      
				Socket s=new Socket("192.168.137.55",6667);  
				DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
				dout.writeUTF(String.valueOf(onoff));
				done = true;
				dout.flush();  
				dout.close();  
				s.close();  
			}catch(Exception e){done = false;}  
		}
	}
	
	public static void runRaspberryPi(Building b1[],int buildingnum,int apartmentnum) {
		
		int k = buildingnum;
		int l= apartmentnum;
		RasPi r = new RasPi();
		Thread t = new Thread() {
			boolean connected = false;
			public void run() {
				while (true) {
					try {
						if (r.watts > 0) connected = true;
						b1[k].a[l].setElectricityrate(r.watts/1000);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (connected == true && r.isConnected == false && r.watts == 0) {
									connected = false;
									AlertBox.display("Attention!", "Apartment Raspberry PI has been disconnected!");
								}
							}
							   
							});
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}
}
