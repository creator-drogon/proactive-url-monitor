package com.hpi.resolution;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import java.lang.Thread;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
//import com.gargoylesoftware.htmlunit.WaitingRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

//import ENCODE_DECODE.ENCODE;

//import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
//import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

public class WeblogicStart {
	
	
	public static void initiate(String console_url,String username,String password,String server_name,String port) throws Exception{
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = new WebClient();
		try {
			HtmlPage login_page = webClient.getPage(console_url);
			HtmlInput username_input = login_page.getFirstByXPath("//*[@id=\"j_username\"]");
			HtmlInput passWordInput = login_page.getFirstByXPath("//*[@id=\"j_password\"]");
			username_input.setValueAttribute((username));
			passWordInput.setValueAttribute((password)); 
			System.out.println("Entered login info. Clicking login button.");
			HtmlInput login_button = login_page.getFirstByXPath("//*[@id=\"loginData\"]/div[4]/span/input");
			HtmlPage main_page = login_button.click();
			Thread.sleep(5000);
			HtmlElement server_button = main_page.getFirstByXPath("//div[@id='HomePagePortlet']/div[1]/div[6]/div[3]/ul[1]/li[1]/a[1]");

			//HtmlElement server_button = main_page.getFirstByXPath("//*[@id=\"HomePagePortlet\"]/div/div[6]/div[2]/ul/li[1]/a");
			String servers_url = server_button.getAttribute("href");
			//System.out.println(servers_url);
			HtmlPage server_page = webClient.getPage(servers_url);
			Thread.sleep(5000);
			HtmlElement control_button = server_page.getFirstByXPath("//*[@id=\"ServerTablePagesBook\"]/div[1]/div/ul/li[2]/a"); 
			HtmlPage control_page = webClient.getPage(control_button.getAttribute("href"));
			Thread.sleep(5000);
			System.out.println("Control tab load successful");                                            
			String server_representation = WeblogicStart.tableiteratorconfig(server_name,port,server_page); //presentation37_1032
			if(server_representation == "") {
				System.out.println("Server not found in the table.");
			}
			else {
				System.out.println("Problems found in "+server_representation+".\n Proceeding with resolution.");
				System.out.println(WeblogicStart.tableiteratorcontrol(server_representation,control_page));
			}
			webClient.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} 
		return;
	}
                
	public static String  tableiteratorconfig(String server_name,String port, HtmlPage page) throws Exception{
		String str="";
		List<String> cellno = new ArrayList<String>();
		final HtmlTable table = page.getHtmlElementById("genericTableFormtable");
		for (final HtmlTableRow row : table.getRows()) {
			//System.out.println("Found row");
			for (final HtmlTableCell cell : row.getCells()) 
				cellno.add(cell.asText());//                
			
			if(cellno.get(3).equals(server_name)&& cellno.get(6).equals(port)){
				System.out.println("   Found cell: " + cellno.get(1));//g2t0225       
				System.out.println("   Found cell: " + cellno.get(3));
				System.out.println("   Found cell: " + cellno.get(6));
				str=cellno.get(1);
			}
			cellno.clear(); 
		} 
		return str;
	}
	
	public static String  tableiteratorcontrol(String server_representation, HtmlPage page) throws Exception{
		String str = "";
		List<HtmlElement> checkbox_column = page.getByXPath("//*[@id=\"WLSServerControlTablePortletchosenContents\"]");
		int col_count = checkbox_column.size();
		for(int i = 0; i < col_count;i++) {
			HtmlCheckBoxInput cbox = (HtmlCheckBoxInput)checkbox_column.get(i);
			if(cbox.getAttribute("title").contains(server_representation)) {
				System.out.println();
				System.out.println("Found: "+cbox.getAttribute("title"));                                               
				cbox.click();
				Thread.sleep(500);  
				Start(page);
				System.out.println();
			}                                 
		}
		return str;
	}
                
	public static String Start(HtmlPage page) throws Exception{
		HtmlElement start_button = page.getFirstByXPath("//*[@id=\"genericTableForm\"]/div[2]/table[1]/tbody/tr/td[1]/div/button[1]");
		System.out.println("Start Button Class after click: "+start_button.getAttribute("class"));	
		if(start_button.getAttribute("class").equals("formButton")) {   
			HtmlPage start_page = start_button.click();
			Thread.sleep(2000);//System.out.println(start_page.asXml());
			System.out.println("Start Clicked");
			HtmlButton confirm_start = start_page.getFirstByXPath("//*[@id=\"WLSServersControlStartServersPortlet\"]/div/div/div[1]/div/button[1]");
			confirm_start.click();
			System.out.println("Yes clicked");////*[@id="WLSServersControlStartServersPortlet"]/div/div/div[4]/div/button[1]
			return "success";
		}
		else {
			System.out.println("failure");
			return "failure";
		}    
	}
}             	