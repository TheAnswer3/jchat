package server;
import java.net.*;
public interface StatusTable {
	boolean isOnline(String nickname);
	InetAddress getIP(String nickname);
	boolean login(String nickname,InetAddress ip);
}
