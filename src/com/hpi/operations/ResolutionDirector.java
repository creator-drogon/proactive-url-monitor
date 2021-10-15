package com.hpi.operations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import com.hpi.resolution.WeblogicShutdown;
import com.hpi.resolution.WeblogicStart;
//import com.hpi.resolution.weblogicRestarter;
import com.hpi.resolution.UrlRestarter;

public class ResolutionDirector {

	public static void myProcess2() throws Exception {
		
		//System.out.println("Hi therter");




		String line = "";  
		String splitBy = ",";


		//String FilterCsv = "./resources/FilteredUrl.csv";
		try  {  
			//parsing a CSV file into BufferedReader class constructor  
			BufferedReader br = new BufferedReader(new FileReader ("./resources/optimizedList1.csv"));
			line = br.readLine();
			while ((line = br.readLine()) != null) {  //returns a Boolean value  
				String[] urldata = line.split(splitBy);    // use comma as separator
				String url = urldata[0].replaceAll("^\"+|\"+$", "");
				String Location = urldata[2].replaceAll("^\"+|\"+$", "");
				String Server = urldata[3].replaceAll("^\"+|\"+$", "");
  				int Port = Integer.valueOf(urldata[4].replaceAll("^\"+|\"+$", ""));
				String ServerType = urldata[5].replaceAll("^\"+|\"+$", "");
				//System.out.println("\n"+url+" "+Location+" "+Server+" "+Port+" "+ServerType+"\n");
				
				if(ServerType.equals("tomcat")) {
			     	//invoke code for tomcat
					//recheck method to be introduced 
					UrlRestarter.initiate(Server,Port,Location);
					 continue;
				}
				else if(ServerType.equals("weblogic")) {
					String []Cred= CredentialReader.initReader(Server, Port, Location, "weblogic");//initReader("","","","weblogic");
					//invoke code for weblogic
					URL aURL = new URL(url);
					String console_url=Cred[2];
					String server_name=Server;
							//aURL.getHost();
					String machine_name=server_name.substring(0, server_name.indexOf("."));
					String port=Integer.toString( aURL.getPort());
					//System.out.println( console_url+","+Cred[0]+","+Cred[1] +","+server_name+","+machine_name+","+port);
				    WeblogicShutdown.initiate(  console_url,Cred[0],Cred[1],machine_name,port); 
				    //Thread.sleep(16000);
				    WeblogicStart.initiate( console_url,Cred[0],Cred[1],machine_name,port); 
				    continue;
				    
				}

				else if(ServerType.equals("linux")) {
					//invoke code for jboss
				}
				//System.out.println("urldata [First Name=" + urldata[0] + ", Last Name=" + urldata[1] + ", Designation=" + urldata[2] + ", Contact=" + urldata[3] + ", Salary= " + urldata[4] +"]");  
			}
			br.close();
		}   
		catch (IOException e) {  
			e.printStackTrace();  
		}
	}
}
