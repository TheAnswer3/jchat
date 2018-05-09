package server;

public interface UsersTable {
	boolean exists(String nickname);
	boolean login(String nickname,String password);
	boolean addUser(String nickname,String password);
}
