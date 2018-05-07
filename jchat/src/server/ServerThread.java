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
			String commandLine=in.readLine();
			String[] parse=commandLine.split(" ");
			switch(parse[0]) {
			case("CONNECT"):
				connect(parse[1],parse[2]);
				break;
			case("SIGNUP"):
				break;
			case("GETIP"):
				break;
			default:
				break;
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void connect(String nickname, String password) {
		try {
			PrintWriter out=new PrintWriter(socket.getOutputStream());
			semNickPsw.acquire();
			if(nickPsw.containsKey(nickname)) {
				boolean isPswOK=nickPsw.get(nickname).equals(password);
				semNickPsw.release();
				if(isPswOK) {
					InetAddress ip=socket.getInetAddress();
					semNickIP.acquire();
					nickIP.put(nickname, ip);
					semNickIP.release();
				}
				else {
					out.println("SERVER 34 WrongPassword");
				}
			}
			else {
				semNickPsw.release();
				out.println("SERVER 32 NicknameDoesNotExists");
				System.out.println("32");
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
