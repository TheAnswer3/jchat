package client;

import java.net.*;
import java.io.*;
import java.util.*;

/* TODO Scegliere una porta e un metodo di identificazione tra i peer
 * Porta default: 9876
 * Lista contatti
 * Inserimento e invio comandi al server
 * Thread Demon aggiornamento IP contatti
 */

public class Client {

	public static void main(String[] args) {
		// Dichiarazione variabili
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
				//Sign In
			}
			else if(choice1.equals("2")) {
				//Sign Up
			}
			String line1 = sc.nextLine();
			if (line1.matches(utils.Connectivity.logInRegex)) {
			} else {
				System.out.println("java.jchat.client.Client: Syntax Error");
				continue ciclo1;
			}
		}

	}

}
