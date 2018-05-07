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
		// Dichiarazione variabili
		Scanner sc = new Scanner(System.in);
		ciclo1:
		while (true) {
			System.out.println("Benvenuto!\n1 - Sign In\n2 - Sign Up");
			String choice1 = sc.nextLine();
			try {
				Socket s = new Socket(InetAddress.getByName(Connectivity.SERVER_HOSTNAME),Connectivity.PORT);
			} catch (Exception e) {
				System.err.println(e);
			}
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
			}
		}
	}
	
	public static void main(String[] args) {
		new Client().start();
	}
	
}
