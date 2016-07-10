package de.nicokst.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

import de.nicokst.action.ActionType;
import de.nicokst.bootstrap.Bootstrap;
import de.nicokst.command.CommandExecutor;

public class Client extends Thread implements Entity, CommandExecutor {

	private Socket socket;

	private PrintWriter writer;
	private BufferedReader reader;
	
	private UUID uuid;
	
	public Client(Socket socket) {
		this.socket = socket;
		matchUUID();
		sendMessage(ActionType.TEXT_MESSAGE + ";" + Bootstrap.config.getValue("greet"));
	}
	
	public UUID getUnique() {
		return uuid;
	}
	
	private void matchUUID() {
		uuid = UUID.randomUUID();
	}
	
	public void run() {
		try {

			writer = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String line;
			while((line = reader.readLine()) != null) {
				System.out.println("[" + getUnique() + "] " + line);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public PrintWriter getWriter() {
		return writer;
	}
	
	public BufferedReader getReader() {
		return reader;
	}

	public void sendMessage(String message) {
		getWriter().println(ActionType.TEXT_MESSAGE + ";" + message);
	}
	
	public void disconnect(String message) {
		getWriter().println(ActionType.DISCONNECT_MESSAGE + ";" + message);
	}
	
	public void disconnect() {
		getWriter().println(ActionType.DISCONNECT);
	}

}
