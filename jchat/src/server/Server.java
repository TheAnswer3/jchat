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
	HashMap<String,String> nickPsw;
	HashMap<String,InetAddress> nickIP;
	Semaphore semNickPsw;
	Semaphore semNickIP;
	public Server() {
		nickPsw=new HashMap<>();
		nickIP=new HashMap<>();
		semNickPsw=new Semaphore(1);
		semNickIP=new Semaphore(1);
		try {
			welcome=new ServerSocket(Connectivity.PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(true) {
			try {
				Socket incoming=welcome.accept();
				System.out.println("Accepting connection from "+incoming.getInetAddress()+"...");
				new ServerThread(incoming, nickPsw, nickIP, semNickIP, semNickIP).start();
				System.out.println("Connection accepted.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String...args) {
		Server server=new Server();
		server.nickPsw.put("Aleandro", "ppp");
		server.start();
		System.out.println("Server started.");
	}
}
