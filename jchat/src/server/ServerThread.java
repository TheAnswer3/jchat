package server;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;
public class ServerThread extends Thread {
	//Incoming socket
	Socket socket;
	
	//Data structures
	StatusTable status;
	UsersTable users;
	
	final String NAME_STRING;
	
	//Full constructor
	public ServerThread(Socket socket,StatusTable status,UsersTable users) {
		this.socket=socket;
		this.status=status;
		this.users=users;
		NAME_STRING=this.getName();
	}
	
	public void run() {
		try {
			//Stream from incoming socket
			BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String commandLine="";
			commandLine=in.readLine();
			//Parsing the string by token " ". First element is the command received.
			String[] parse=commandLine.split(" ");
			if(parse[0].equals("CONNECT")) 
				connect(parse[1],parse[2]);
			else if(parse[0].equals("SIGNUP"))
				signup(parse[1],parse[2]);
			else if(parse[0].equals("GETIP"))
				getip(parse[1]);
			else System.out.println("Unexpected inputstream: "+commandLine);
			
		} catch (IOException e) {
			System.out.println("Problems with socket stream.");
		}
	}
	
	private void getip(String nickname) {
		System.out.println("Getip command received: GETIP "+nickname+".");
		try {
			//Output stream
			PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
			//Check if the user is online.
			if(status.isOnline(nickname)) {
				//User is online
				out.println("SERVER 20 "+status.getIP(nickname).getHostAddress());
				printStatus(20);
			}
			else {
				//User is offline
				out.println("SERVER 21 NicknameNotFound");
				printStatus(21);
			}

		}catch (IOException e) {
			// TODO
			e.printStackTrace();
		} 
	}

	private void signup(String nickname, String password) {
		System.out.println("Signup command received: SIGNUP "+nickname+" "+password+".");
		try {
			//Output stream
			PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
			if(users.exists(nickname)) {
				//User already exists
				out.println("SERVER 33 NicknameAlreadyExists");
				printStatus(33);
			}
			else {
				//User does not exists
				users.addUser(nickname, password);
				out.println("SERVER 31 SignupSuccess");
				printStatus(31);
			}
		} catch (IOException e) {
			// TODO 
			e.printStackTrace();
		}
	}

	private void connect(String nickname, String password) {
		System.out.println("Connect command received: CONNECT "+nickname+" "+password+".");
		try {
			//Output stream
			PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
			if(users.exists(nickname)) {
				//User registered
				boolean validLogin=users.login(nickname, password);
				if(validLogin) {
					status.login(nickname, socket.getInetAddress());
					out.println("SERVER 30 SigninSuccess");
					printStatus(30);
				}
				else {
					//Wrong password 
					out.println("SERVER 34 WrongPassword");
					printStatus(34);
				}
			}
			else {
				//User is not registered
				out.println("SERVER 32 NicknameDoesNotExists");
				printStatus(32);
			}
			
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
	}
	
	//Prints the errors, receives the error code.
	private void printStatus(int code) {
		String toPrint="["+NAME_STRING+"]: ";
		switch(code) {
		case(20):
			toPrint+="Getip request succeded.";
			break;
		case(21):
			toPrint+="Getip request failed with error 21.";
			break;
		case(30):
			toPrint+="Connect request succeded";
			break;
		case(31):
			toPrint+="Signup request succeded.";
			break;
		case(32):
			toPrint+="Connect request failed with error 32.";
			break;
		case(33):
			toPrint+="Signup request failed with error 33.";
			break;
		case(34):
			toPrint+="Connect request failed with error 34.";
			break;
			
		}
		System.out.println(toPrint);
	}
	public String toString() {
		StringBuilder sb=new StringBuilder(200);
		sb.append("ServerThread "+NAME_STRING+" : socket "+socket.toString());
		return sb.toString();
	}
	
}
