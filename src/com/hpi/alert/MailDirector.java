package com.hpi.alert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class MailDirector {
//	   public static void main(String[] args) throws IOException {
//		   try {
//			readCSV_Mail("./resources/FailedUrl.csv",groupByEPRID());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 
//	   }
	//check if Failed.csv rowCount>1
	//true: OPTIONAL!(GroupBy App name)
	//read the corresponding SME data and call the mail function mentioning about down app
	//false:terminate
//	 public static String groupByEPRID() {
//		 //still to be worked on
//		return null;
//	}
	 public static void readCSV_Mail(String fileToR//, String sameEPRID
			 ) throws Exception {

		
		 System.out.println("We are in stage 3");
			String line = "";  
			String splitBy = ",";  
			try  {  
				//parsing a CSV file into BufferedReader class constructor  
				BufferedReader br = new BufferedReader(new FileReader (fileToR));  
				line = br.readLine();
				while ((line = br.readLine()) != null) {  //returns a Boolean value  
					String[] urldata = line.split(splitBy);    // use comma as separator
					String url = urldata[0].replaceAll("^\"+|\"+$", "");
					String EmailId= urldata[8];
					String EPRId= urldata[10];
					
					System.out.println("\n"+url+" "+" "+ EPRId+" "+EmailId+"\n");
					
					if(EmailId.equals("")||EmailId.equals(null)) {//also apply cond. for validation....
				     	System.out.println("We could not find a valid email reciver");
					}
					else
					{
						AcivateMailService(url, EPRId,EmailId );
					}
				}
				br.close();		
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				//ErrorMail(e);
				}
			//return null;
		}
	 
	 public static void AcivateMailService(String Url,String EPRId,String Reciver ) {
		  try {
			  String message = String.format("Hi team,%n PFA.%n %n %s  %s %n.Best Regards,%n Automation Team. %n",Url,EPRId);
			EscalationMail.sendMail("System Genrated EPOSys Alert",message,"karan.khanna@hp.com");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	 }
	 //*NB* *Only Call this function in all necessary catch blocks where the PO requires justification for failure. 
	 public static void ErrorMail(Exception e) throws IOException {
		 String stacktrace = ExceptionUtils.getStackTrace(e);
		 String message = String.format("Hi team,%n Please read the printStackTrace message below and take necessary action %n %n %s . %n Best Regards,%n Automation Team. %n ",stacktrace);
		 EscalationMail.sendMail("System Genrated EPOSys Error Alert",message,"karan.khanna@hp.com");
		 
	 }

	public static void ErrorMailScript(String s) throws Exception{
		// TODO Auto-generated method stub
		Exception e = null;
		String stacktrace = ExceptionUtils.getStackTrace(e);
		String message = String.format("Hi team,\nPlease read the printStackTrace message below and take necessary action\n\n %s . \n\nBest Regards,\nAutomation Team. \n",stacktrace);
		EscalationMail.sendMail("System Genrated EPOSys Error Alert",message,"karan.khanna@hp.com");
		
	}
}
