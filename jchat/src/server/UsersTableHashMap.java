package server;
import java.util.*;
import java.util.concurrent.*;

public class UsersTableHashMap implements UsersTable{
	private ConcurrentHashMap<String,String> table;
	public UsersTableHashMap(Map<String,String> m) {
		table=new ConcurrentHashMap<>(m);
	}
	
	public boolean exists(String nickname) {
		return table.containsKey(nickname);
	}
	
	public boolean login(String nickname,String password) {
		if(!exists(nickname))return false;
		String psw=table.get(nickname);
		return password.equals(psw);
	}
	
	public boolean addUser(String nickname,String password) {
		table.put(nickname, password);
		return true;
	}
}
