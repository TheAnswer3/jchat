package server;
import java.util.*;
import java.util.concurrent.*;
import java.net.*;

public class StatusTableHashMap implements StatusTable{
	private ConcurrentHashMap<String,InetAddress> table;
	public StatusTableHashMap(Map<String,InetAddress> m) {
		table=new ConcurrentHashMap<>(m);
	}
	
	public boolean isOnline(String nickname) {
		return table.containsKey(nickname);
	}
	
	public InetAddress getIP(String nickname) {
		return table.get(nickname);
	}
	
	public boolean login(String nickname,InetAddress ip) {
		table.put(nickname, ip);
		return true;
	}
}
