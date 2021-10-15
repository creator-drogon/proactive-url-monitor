package com.hpi.resolution;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.hpi.alert.MailDirector;
import com.hpi.operations.CredentialDecode;

public class UrlRestarter {

	UrlRestarter(){
		
	}
	
	public static void initiate(String host, int port,String Location ) {
		// TODO Auto-generated method stub
		String user = "";
		String password = "";
		StringBuffer command = new StringBuffer();
		try {
			System.out.println("starting program");
			File file = new File(Location);
			Scanner sc = new Scanner(file);
			String secretkey="AutomationHPI";//To be done smt abt it --> fetch from ext encry file
			command.append("\n");
			while (sc.hasNextLine()) {

				String nextLine = sc.nextLine();

				if (nextLine.contains("username")) {
					user = nextLine.split("-")[1].trim();
					user= CredentialDecode.decrypt(user, secretkey);
					continue;
				}

				if (nextLine.contains("password")) {
					password = nextLine.split("-")[1].trim();
					password= CredentialDecode.decrypt(password, secretkey);
					continue;
				}

				command.append(nextLine.trim()).append("\n");
			}
			//	    System.out.println("User is "+user+" Password is "+password+" Command is "+command.toString());
			// set up session
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("PreferredAuthentications", "publickey,gssapi-with-mic,keyboard-interactive,password");
			config.put("StrictHostKeyChecking", "no");

			session.setConfig(config);
			session.connect();
			//	System.out.println("Connected");
			if (session.isConnected())
				System.out.println(" Connected to Server");

			ChannelShell channel = null;
			channel = (ChannelShell) session.openChannel("shell");
			
			channel.setInputStream(new ByteArrayInputStream(command.toString().getBytes(StandardCharsets.UTF_8)));
			channel.setOutputStream(System.out);
			
			channel.connect();
			if (channel.isConnected())
				System.out.println("Open Connection in Shell mode");
			printOutput(channel);
			channel.disconnect();
			session.disconnect();
			sc.close();
			System.out.println("Disconnected from Server");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printOutput(ChannelShell channel) {
		// TODO Auto-generated method stub
		System.out.println("output method");
		try {
			InputStream in = channel.getInputStream();
			String s;
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((s = br.readLine()) != null) {
				System.out.println(s);
				try {
					if (s.contains("$") & (s.contains("start") || s.contains("stop"))) {
						Thread.sleep(20000);
					} 
				} finally {
					// TODO: handle finally clause
					 if (s.toLowerCase().contains("error")||s.toLowerCase().contains("not found") )
						{MailDirector.ErrorMailScript(s);}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
