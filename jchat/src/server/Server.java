package server;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import utils.*;
/* TODO
 * Porta default: 9876
 * Mappa nick-password, permanente.
 * Mappa nick-IP, non permanente.
 * 
 */
public class Server extends Thread{
	ServerSocket welcome;
	/*
	HashMap<String,String> nickPsw;
	HashMap<String,InetAddress> nickIP;
	Semaphore semNickPsw;
	Semaphore semNickIP;
	*/
	StatusTable status;
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
				Socket incoming=welcome.accept();
				System.out.println("Accepting connection from "+incoming.getInetAddress()+"...");
				//new ServerThread(incoming, nickPsw, nickIP, semNickIP, semNickIP).start();
				new ServerThread(incoming,status,users);
				System.out.println("Connection accepted.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
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
