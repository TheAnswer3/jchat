package server;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import utils.*;

public class Server extends Thread{
	ServerSocket welcome;
	/*
	HashMap<String,String> nickPsw;
	HashMap<String,InetAddress> nickIP;
	Semaphore semNickPsw;
	Semaphore semNickIP;
	*/
	//Data structure for users' connection status
	StatusTable status;
	//Data structure for registered users
	UsersTable users;
	boolean isStarted=false;
	
	public Server() {
		/*
		nickPsw=new HashMap<>();
		nickIP=new HashMap<>();
		semNickPsw=new Semaphore(1);
		semNickIP=new Semaphore(1);
		*/
		status=new StatusTableHashMap(new HashMap<>());
		users=new UsersTableHashMap(new HashMap<>());
		try {
			//Welcome socket
			welcome=new ServerSocket(Connectivity.PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		isStarted=true;
		while(true) {
			try {
				//Accepting new sockets
				Socket incoming=welcome.accept();
				System.out.println("Accepting connection from "+incoming.getInetAddress()+"...");
				//Start server thread
				new ServerThread(incoming,status,users);
				System.out.println("Connection accepted.");
			} catch (IOException e) {
				// TODO 
				e.printStackTrace();
			}
		}
	}
	
	//Add users from Server class
	public boolean addUser(String nickname,String password) {
		users.addUser(nickname, password);
		return true;
	}
	
	public static void main(String...args) {
		Server server=new Server();
		server.start();
		System.out.println("Server started.");
		server.addUser("guest", "psw");
	}
}
