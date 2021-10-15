package com.hpi.alert;

//import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
//import javax.mail.MessagingException;
import javax.mail.Multipart;
//import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

//import com.hpi.operations.CredentialReader;

public class EscalationMail {
	
//	public static void main(String[] args)throws IOException {
//		
//	    System.out.println("Alert mail Start");
//
//	    String emailID = "amit.kumar.singh@hp.com";
//
//	    
//	   sendEmail("System Genrated EPOSys Alert","Hi team,\nPFA. \n\nBest Regards,\nAutomation Team. \n",emailID);
//	}


///**
// * Utility method to send simple HTML email
// * @param subject
// * @param body
// * @param toEmail
// */
public static void sendMail( String subject, String messageBody,String toEmail){
    
	String FilteredCsv = "./resources/FilteredUrl.csv";
	
    String smtpHostServer = "smtp3.hp.com";
    Properties props = System.getProperties();
    props.put("mail.smtp.host", smtpHostServer);
    Session session = Session.getInstance(props, null);
	
    try
    {
      MimeMessage msg = new MimeMessage(session);
      //set message headers
      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
      msg.addHeader("format", "flowed");
      msg.addHeader("Content-Transfer-Encoding", "8bit");

      msg.setFrom(new InternetAddress("no_reply@hp.com", "NoReply-AUTOM9"));

      msg.setReplyTo(InternetAddress.parse("amit.kumar.singh@hp.com", false));

      msg.setSubject(subject, "UTF-8");

      msg.setText(messageBody, "UTF-8");

      msg.setSentDate(new Date());

      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
      System.out.println("Message is ready to be sent");
      // Create the message part
	   BodyPart messageBodyPart = new MimeBodyPart();
	   
	   // Now set the actual message
	   messageBodyPart.setText(messageBody);
	   
	   // Create a multipart message
	   Multipart multipart = new MimeMultipart();
	   
	   // Set text message part
	   multipart.addBodyPart(messageBodyPart);
	   
	   // Part two is attachment
	   if( FilteredCsv.length() > 0) {
		   messageBodyPart = new MimeBodyPart();
		   DataSource source = new FileDataSource(FilteredCsv);
		   messageBodyPart.setDataHandler(new DataHandler(source));
		   messageBodyPart.setFileName("ColdUrls.csv");
		   multipart.addBodyPart(messageBodyPart);
	   }
        
	   // Send the complete message parts
	   msg.setContent(multipart);
	   
	   // Send message
	  Transport.send(msg);  

      System.out.println("Mail Sent Successfully!!👍 ");
    }
    catch (Exception e) {
    	e.printStackTrace();
    	throw new RuntimeException(e);
      
    }

}
}