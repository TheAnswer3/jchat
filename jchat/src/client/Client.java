package client;

import java.net.*;
import java.io.*;
import java.util.*;
import utils.*;

/* TODO Scegliere una porta e un metodo di identificazione tra i peer
 * Porta default: 9876
 * Lista contatti
 * Inserimento e invio comandi al server
 * Thread Demon aggiornamento IP contatti
 */

public class Client extends Thread{

	@Override
	public void run() {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		ciclo1:
		while (true) {
			System.out.println("Benvenuto!\n1 - Sign In\n2 - Sign Up");
			String choice1 = sc.nextLine();
			if (!choice1.matches("[1-2]")) {
				System.out.println("java.jchat.client.Client: Syntax Error");
				continue ciclo1;
			}
			if(choice1.equals("1")) {
				String signInLine = sc.nextLine();
				if (signInLine.matches(Connectivity.logInRegex)) {
				} else {
					System.out.println("java.jchat.client.Client: Syntax Error");
					continue ciclo1;
				}
				//Invio richiesta di sign In al Server
				
				//Ricezione risposta di sign In dal Server
			}
			else if(choice1.equals("2")) {
				//Sign Up
				//Inserimento credenziali per la creazione dell'account
				System.out.println("Inserisci <Nickname> <Password> <Password> per completare la registrazione");
				String regLine = sc.nextLine();
				//Controllo sintassi delle credenziali
				if(!regLine.matches(Connectivity.signUpRegex)) {
					System.out.println("java.jchat.client.Client: Syntax Error");
				} else {
					StringTokenizer st = new StringTokenizer(regLine," ");
					String nickname = st.nextToken();
					String password = st.nextToken();
					String password2 = st.nextToken();
					if(!password.equals(password2)) {
						System.out.println("java.jchat.client.Client: Password Missmatch");
					} else {
						try {
							//Composizione ed invio del messaggio al server via socket
							@SuppressWarnings("resource")
							Socket s = new Socket(Connectivity.SERVER_HOSTNAME,Connectivity.PORT);
							BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
							PrintWriter out = new PrintWriter(s.getOutputStream(),true);
							String msg = "SIGNUP " + nickname + " " + password;
							out.println(msg);
							//Ricezione e composizione del messaggio di risposta
							String rsp = in.readLine();
							//Caso d'errore: nickname già presente nel Database
							if(rsp.equals("SERVER 33 NicknameAlreadyExists"))
								continue ciclo1;
							System.out.println("Creazione account completata con successo");
							in.close();
							out.close();
							s.close();
						}catch(Exception e) {
							System.err.println(e);
						}
					}
				}
				
			}
		}
	}
	
	public static void main(String[] args) {
		new Client().start();
	}
	
}
