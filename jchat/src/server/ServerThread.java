package server;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;
public class ServerThread extends Thread {
	Socket socket;
	HashMap<String,String> nickPsw;
	HashMap<String,InetAddress> nickIP;
	Semaphore semNickPsw;
	Semaphore semNickIP;
	public ServerThread(Socket socket, HashMap<String, String> nickPsw, HashMap<String, InetAddress> nickIP,
			Semaphore semNickPsw, Semaphore semNickIP) {
		super();
		this.socket = socket;
		this.nickPsw = nickPsw;
		this.nickIP = nickIP;
		this.semNickPsw = semNickPsw;
		this.semNickIP = semNickIP;
	}

	public void run() {
		try {
			BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String commandLine="";
			
			commandLine=in.readLine();
			
			/*
			while(more) {
				String line=in.readLine();
				if(line==null) more=false;
				else commandLine+=line;
			}
			*/
			String[] parse=commandLine.split(" ");
			if(parse[0].equals("CONNECT")) 
				connect(parse[1],parse[2]);
			else if(parse[0].equals("SIGNUP"))
				signup(parse[1],parse[2]);
			else if(parse[0].equals("GETIP"))
				getip(parse[1]);
			else System.out.println("Inputstream: "+commandLine);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void getip(String nickname) {
		System.out.println("Getip command received: GETIP "+nickname+".");
		try {
			PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
			semNickIP.acquire();
			InetAddress ip=nickIP.get(nickname);
			semNickIP.release();
			if(ip==null) {
				out.println("SERVER 21 NicknameNotFound");
				System.out.println("Getip request failed.");
			}
			else {
				out.println("SERVER 20 "+ip.getHostAddress());
				System.out.println("Getip request succeded.");
			}
				
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void signup(String nickname, String password) {
		System.out.println("Signup command received: SIGNUP "+nickname+" "+password+".");
		try {
			PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
			semNickPsw.acquire();
			if(!nickPsw.containsKey(nickname)) {
				nickPsw.put(nickname, password);
				semNickPsw.release();
				out.println("SERVER 31 SignupSuccess");
				System.out.println("Signup request succeded.");
			}
			else {
				semNickPsw.release();
				out.println("SERVER 33 NicknameAlreadyExists");
				System.out.println("Signup request failed with error 33.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void connect(String nickname, String password) {
		System.out.println("Connect command received: CONNECT "+nickname+" "+password+".");
		try {
			PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
			semNickPsw.acquire();
			if(nickPsw.containsKey(nickname)) {
				boolean isPswOK=nickPsw.get(nickname).equals(password);
				semNickPsw.release();
				if(isPswOK) {
					InetAddress ip=socket.getInetAddress();
					updateNickIP(nickname,ip);
					out.println("SERVER 30 SigninSuccess");
					System.out.println("Connect request succeded.");
				}
				else {
					out.println("SERVER 34 WrongPassword");
					System.out.println("Connect request failed with error 34.");
				}
			}
			else {
				semNickPsw.release();
				out.println("SERVER 32 NicknameDoesNotExists");
				System.out.println("Connect request failed with error 32.");
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean updateNickIP(String nickname,InetAddress ip) {
		boolean added=false;
		try {
			semNickIP.acquire();
			nickIP.put(nickname, ip);
			semNickIP.release();
			added=true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Nickname-IP Updated. New map: "+nickIP);
		return added;
	}
	
	public String toString() {
		StringBuilder sb=new StringBuilder(200);
		sb.append("ServerThread: socket "+socket.toString());
		return sb.toString();
	}
	
}
