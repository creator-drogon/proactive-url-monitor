package com.hpi.operations;

import java.io.File;
import java.util.Scanner;

public class CredentialReader {
	

	public static String[] initReader(String host, int port,String Location ,String whoCalled) {
	String user = "";
	String password = "";
	String consoleurl="";
	String urlCsvPath="";
	String ResultCsvPath="";
	String FilteredCsv="";
	//String [] cred;
	StringBuffer command = new StringBuffer();
	try {
		System.out.println("starting program for reading credentials");
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
			
			if (nextLine.contains("consoleurl")) {
				consoleurl = nextLine.split("-")[1].trim();
				continue;
			}
			if (nextLine.contains("urlCsvPath")) {
				urlCsvPath = nextLine.split("-")[1].trim();
				continue;
			}
			if (nextLine.contains("ResultCsvPath")) {
				ResultCsvPath = nextLine.split("-")[1].trim();
				continue;
			}
			if (nextLine.contains("FilteredCsv")) {
				FilteredCsv = nextLine.split("-")[1].trim();
				continue;
			}

			sc.close();
		}
		//	    System.out.println("User is "+user+" Password is "+password+" Command is "+command.toString());
		// set up session
	}catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	if(whoCalled.equals("weblogic"))
		return new String[] {user,password,consoleurl};
	else if((whoCalled.equals("tomcat"))||(whoCalled.equals("cap")))
		return null;// new String[] {user,password};
	else if((whoCalled.equals("EscalationMail")))
		return new String[] {user,password};
	else if((whoCalled.equals("PingOne"))||(whoCalled.equals("PingTwo")))
		return new String[] {urlCsvPath,ResultCsvPath,FilteredCsv};
	else 
		return null;

}
}
