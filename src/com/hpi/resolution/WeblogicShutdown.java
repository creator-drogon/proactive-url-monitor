package com.hpi.resolution;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

public class WeblogicShutdown {
//
//	
	public static void initiate(String console_url,String username,String password,String server_name,String port) throws Exception{
		WebClient webClient = new WebClient();
		
		try {   
			HtmlPage login_page = webClient.getPage(console_url);
			HtmlInput username_input = login_page.getFirstByXPath("//*[@id=\"j_username\"]");
			HtmlInput passWordInput = login_page.getFirstByXPath("//*[@id=\"j_password\"]");
			username_input.setValueAttribute(username);
			passWordInput.setValueAttribute(password); 
			HtmlInput login_button = login_page.getFirstByXPath("//*[@id=\"loginData\"]/div[4]/span/input");
			HtmlPage main_page = login_button.click();
			System.out.println("Logging into Weblogic portal.");
			Thread.sleep(5000);
			HtmlElement server_button = main_page.getFirstByXPath("//div[@id='HomePagePortlet']/div[1]/div[6]/div[3]/ul[1]/li[1]/a[1]");
			//HtmlElement server_button = main_page.getFirstByXPath("//*[@id=\"HomePagePortlet\"]/div/div[6]/div[2]/ul/li[1]/a");
			HtmlPage server_page = server_button.click();
			Thread.sleep(5000);
			HtmlElement control_button = server_page.getFirstByXPath("//*[@id=\"ServerTablePagesBook\"]/div[1]/div/ul/li[2]/a");
			HtmlPage control_page = webClient.getPage(control_button.getAttribute("href"));
			Thread.sleep(5000);
			System.out.println("Control tab load successful");    
			System.out.println(server_name+" "+port);
			String server_representation = WeblogicShutdown.tableiteratorconfig(server_name,port,server_page); //presentation37_1032
			if(server_representation == "") {
				System.out.println("Server not found in the table.");
			}
			else {
				System.out.println("Problems found in "+server_representation+".\n Proceeding with resolution.");
				System.out.println(WeblogicShutdown.tableiteratorcontrol(server_representation,control_page));
			}
			webClient.close();
		}
		catch (Exception e) {
			webClient.close();
			throw new Exception(e);
		} 
	}
	
	
	
	public static String  tableiteratorconfig(String server_name,String port, HtmlPage page) throws Exception{
		String str="";
		List<String> cellno = new ArrayList<String>();
		final HtmlTable table = page.getHtmlElementById("genericTableFormtable");
		for (final HtmlTableRow row : table.getRows()) {
			//System.out.println("Found row");
			for (final HtmlTableCell cell : row.getCells())
				cellno.add(cell.asText());
			for(int i=0;i<cellno.size();i++) System.out.println(cellno.get(i));

			
//			if(cellno.get(3).equals(server_name)&& cellno.get(6).equals(port)){
//				System.out.println("   Found cell: " + cellno.get(1));//g2t0225
//				System.out.println("   Found cell: " + cellno.get(3));
//				System.out.println("   Found cell: " + cellno.get(6));
//				str=cellno.get(1);
//            }
			port = "30054";
			System.out.println(cellno.get(4) + " PORT " + cellno.get(6) + "    " +  cellno.get(7));
			if(cellno.get(4).equals(server_name)&& cellno.get(7).equals(port)){
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
		List<Object> checkbox_column = page.getByXPath("//*[@id=\"WLSServerControlTablePortletchosenContents\"]");
		int col_count = checkbox_column.size();
		for(int i = 0; i < col_count;i++) {
			HtmlCheckBoxInput cbox = (HtmlCheckBoxInput)checkbox_column.get(i);
			if(cbox.getAttribute("title").contains(server_representation)) {
				System.out.println();
				System.out.println("Found: "+cbox.getAttribute("title"));                                               
				cbox.click();
				Thread.sleep(500);  
				Shutdown(page); 
				Thread.sleep(5000);
			}                                 
		}
		return str;
	}
                
	public static String Shutdown(HtmlPage page) throws IOException, InterruptedException {
		HtmlButton shutdown = page.getFirstByXPath("//*[@id=\"genericTableForm\"]/div[2]/table[1]/tbody/tr/td[1]/div/button[4]");
		HtmlPage mod = shutdown.click();
		Thread.sleep(1000);
		HtmlElement force_shut = mod.getFirstByXPath("//*[@id=\"genericTableForm\"]/div[2]/table[1]/tbody/tr/td[1]/div/div[2]/div[2]/div/ul/li[2]");
		System.out.println("Found forced shutdown: "+force_shut);
		HtmlPage confirm_shut = force_shut.click();
		Thread.sleep(5000);
		HtmlButton fin_shut = confirm_shut.getFirstByXPath("//*[@id=\"WLSServersControlForceShutdownServersPortlet\"]/div/div/div[1]/div/button[1]");
		fin_shut.click();
		System.out.println("Confirmed Shutdown");
		return "success";
	}
}