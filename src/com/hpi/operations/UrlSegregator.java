package com.hpi.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;


	
	/**
	 * Index: src/appLaunch/loadExcel.java
	 * Created by Karan on 20.11.2020.
	 */
	public class UrlSegregator {

		public  UrlSegregator() throws KeyManagementException {
			
		}

	    public static void myProcesss1(String Location) throws Exception {

	    	//Location;
			 String []Cred= CredentialReader.initReader("",0, Location, "PingOne");
//			  final String username = Cred[0];//change accordingly
//			  final  String password = Cred[1];//change accordingly

	        String urlCsvPath = Cred[0];

	        String ResultCsvPath = Cred[1];

	        String FilteredCsv = Cred[2];

	       // String urlCsvPathDelivery = Cred[3];

	        List<String> urlList = getListUrl(urlCsvPath);

	        List<String> FileLocation = LocationList(urlCsvPath);

	        @SuppressWarnings("unused")
			List<String> FilteredUrl = getFilteredList(FilteredCsv);

	        System.out.println("\n" + "\n" + "######################Location of Tomcat Files for server Restart##################" + "\n"
	                + "\n"
	                + FileLocation);

	        Map<String, Integer> response = getResponseCode(urlList, Runtime.getRuntime().availableProcessors() * 10);

	        writeToCsv1(response, ResultCsvPath);

	        writeToCsv2(response, FilteredCsv);

	        getMapData(urlCsvPath);

	    //    filteredLocations(getMapData(urlCsvPath), FilteredUrl);
	        File fileFilter = new File(FilteredCsv); 
			File fileUrl = new File(urlCsvPath);
			List<String> lineFs = null;
			List<String> lineUs = null;
			try {
				lineFs = Files.readAllLines(fileFilter.toPath(), StandardCharsets.UTF_8);
				lineUs = Files.readAllLines(fileUrl.toPath(), StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// ------GET urls IN ARRAY FOR COMPARING----------------
			String[] filterList =new String[lineFs.size()]; int j=0;
			String[] urlList1 =new String[lineUs.size()]; int i=0;
			for (String lineF : lineFs) { 
			   String[] array = lineF.split(","); 
			   filterList[j]=array[0];
			   //System.out.println(filterList[j]+" ");
			   j++;		   
	      	}
			for (String lineU : lineUs) { 
				   String[] array = lineU.split(","); 
				   urlList1[i]=array[0];
				  // System.out.println(urlList[j]+" ");
				   i++;		
		      	}
	//1> Now filterList[j] has down Urls and urlList[i] has All Urls
	//2> We will compare this with Initial UrlList 
	//3> If Matched we append Location in FilteredUrl next col

	//reading file to get location of sp. url 
	CSVReader reader = new CSVReader(new FileReader(fileUrl));
	List<String[]> csvBodyLoc = reader.readAll();
	//String Location="";	

			for( int J=0;J<j;J++) {
				for( int I=0;I<i;I++) {//compare urls
					if(urlList1[I].equals(filterList[J])) {
						//System.out.println("sucess found, ready to write");
//						System.out.print(csvBodyLoc.get(I)[1]);
//						System.out.print(" "+csvBodyLoc.get(I)[2]+" ");
//						System.out.print(csvBodyLoc.get(I)[3]+"\n");
						//call of update fn
						for( int k=1;k<11;k++)
 						updateCSV(FilteredCsv,
								csvBodyLoc.get(I)[k] , J, k);

					}
				}
			}
			reader.close();

	    }
	    public static void updateCSV(String fileToUpdate, String replace,
			    int row, int col) throws Exception {

			File inputFile = new File(fileToUpdate);

			// Read existing file 
			CSVReader reader = new CSVReader(new FileReader(inputFile));
			List<String[]> csvBody = reader.readAll();
			// get CSV row column  and replace with by using row and column
			csvBody.get(row)[col+1] = replace;
			reader.close();

			// Write to CSV file which is open
			//CSVWriter writer = new CSVWriter(new FileWriter(inputFile));
			CSVWriter writer = new CSVWriter(new FileWriter(inputFile), CSVWriter.DEFAULT_SEPARATOR,
																		CSVWriter.NO_QUOTE_CHARACTER,
																		CSVWriter.NO_ESCAPE_CHARACTER,
																		CSVWriter.DEFAULT_LINE_END);

			writer.writeAll(csvBody);
			writer.flush();
			writer.close();
		}

	    private static void writeToCsv1(Map<String, Integer> response, String pathToCvsFile) throws IOException {
	        File csvFile = new File(pathToCvsFile);
	        FileWriter writer = new FileWriter(csvFile);
	        
	        writer.append("Url");
	        writer.append(',');
	        writer.append("StatusCode");
	        writer.append('\n');
	        //int Result;


	        for (Map.Entry<String, Integer> entry : response.entrySet()) {
	            writer.append(entry.getKey());
	            writer.append(',');
	            writer.append(entry.getValue().toString());
	            writer.append('\n');
	            writer.flush();
	        }
	        writer.close();
	    }

	    private static void writeToCsv2(Map<String, Integer> response, String pathToCvsFile) throws IOException {
	        File csvFile = new File(pathToCvsFile);
	        FileWriter writer = new FileWriter(csvFile);
	        writer.append("Url");
	        writer.append(',');
	        writer.append("StatusCode");
	        writer.append(',');
	        writer.append("Location");
	        writer.append(',');
	        writer.append("Server");
	        writer.append(',');
	        writer.append("Port");
	        writer.append(',');
	        writer.append("ServerType");
	        writer.append(',');
	        writer.append("URL Monitor Friendly Name");
	        writer.append(',');
	        writer.append("HPSME");
	        writer.append(',');
	        writer.append("PH");
	        writer.append(',');
	        writer.append("EPRID");
	        writer.append(',');
	        writer.append("Extra1");
	        writer.append(',');
	        writer.append("Extra2");
	       
	        writer.append('\n');
	       // int Result;

	        response.keySet().removeAll(
	                response.entrySet().stream()
	                        .filter(a -> a.getValue().equals(200))
	                        .map(e -> e.getKey()).collect(Collectors.toList()));
	        for (Map.Entry<String, Integer> entry : response.entrySet()) {
	            writer.append(entry.getKey());
	            writer.append(',');
	            writer.append(entry.getValue().toString());
	            writer.append(',');writer.append(',');writer.append(',');writer.append(',');writer.append(',');writer.append(',');writer.append(',');writer.append(',');writer.append(',');writer.append(',');writer.append(',');
	            writer.append('\n');
	            writer.flush();
	        }
	        writer.close();
	    }

	    private static Map<String, Integer> getResponseCode(List<String> urlList, Integer threads) throws IOException, InterruptedException {
	        final Map<String, Integer> response = Collections.synchronizedMap(new LinkedHashMap<>());
	        final ExecutorService executor = Executors.newFixedThreadPool(threads);
	        System.out.println("Created " + threads + " threads!");
	        for (final String url : urlList) {
	            executor.submit(() -> {
	                System.out.println("Start work with: " + url.toLowerCase() + " " + Thread.currentThread());

	                response.put(url, getStatusCode(url));
	                System.out.println("Put result in response: " + url.toLowerCase() + " " + Thread.currentThread());
	            });
	        }
	        while (((ThreadPoolExecutor) executor).getActiveCount() > 0) {
	            System.out.println("Checking... " + "Count working threads:  " + ((ThreadPoolExecutor) executor).getActiveCount());
	            executor.awaitTermination(5, TimeUnit.SECONDS);
	        }
	        executor.shutdown();
	        return response;
	    }


	    private static Integer getStatusCode(String url) {
	        try { //disableSSLCertificateChecking();
	            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
	            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.35 (KHTML, like Gecko) Chrome/49.0.2661.94 Safari/537.34");
	            con.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
	            con.setRequestProperty("Accept-Encoding", " gzip, deflate, sdch");
	            con.setInstanceFollowRedirects(true);
	            con.setReadTimeout(20000);
	          

	            con.connect();
	            con.getInputStream();
	            if (con.getResponseCode() != 301 && con.getResponseCode() != 302) {
	                return con.getResponseCode();
	            } else {
	                String redirectUrl = con.getHeaderField("Location");
	                return getStatusCode(redirectUrl);
	            }
	        } catch (MalformedURLException e) {
	            System.out.println(e.getMessage());
	            return 404;
	        } catch (IOException e) {
	            System.out.println(e.getMessage());
	            return -1;
	        }
	    }
	    private static void disableSSLCertificateChecking() {
	        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	            public X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }

				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					// TODO Auto-generated method stub

				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					// TODO Auto-generated method stub

				}

	        } };

	        try {
			SSLContext sc = null;
			try {
				sc = SSLContext.getInstance("TLS");
			} catch (NoSuchAlgorithmException noSuchAlgorithmException) {
				noSuchAlgorithmException.printStackTrace();
			}

			sc.init(null, trustAllCerts, new java.security.SecureRandom());

	            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	        } catch (KeyManagementException e) {
	            e.printStackTrace();
	        }
		}
	    private static List<String> getListUrl(String pathCsvFile) throws IOException {
	        List<String> list = new ArrayList<>();
	        File csvFile = new File(pathCsvFile);
	        CSVParser parser = CSVParser.parse(csvFile, Charset.defaultCharset(), CSVFormat.RFC4180);
	        for (CSVRecord csvRecord : parser) {
	            String url = csvRecord.iterator().next().split(";")[0];
	            if (url.startsWith("http")) {
	                list.add(url);
	            }
	        }


	        System.out.println("#########################Created list urls################################" + "\n"
	                + "\n");
	        return list;
	    }

	    private static List<String> LocationList(String pathCsvFile) throws IOException, CsvValidationException {
	        ArrayList<String> location = new ArrayList<>();
	        CSVReader reader = new CSVReader(new FileReader(pathCsvFile));
	        String[] nextLine;
	        while (null != (nextLine = reader.readNext())) {
	            location.add(nextLine[1]);

	        }
	        return location;
	    }

	    private static List<String> getFilteredList(String filtered) throws IOException {
	        List<String> filteredList = new ArrayList<>();
	        File csvFile = new File(filtered);
	        CSVParser parser = CSVParser.parse(csvFile, Charset.defaultCharset(), CSVFormat.RFC4180);
	        for (CSVRecord csvRecord : parser) {
	            String filterdUrl = csvRecord.iterator().next().split(";")[0];
	            if (filterdUrl.startsWith("http")) {
	                filteredList.add(filterdUrl);
	            }
	        }


	        System.out.println(
	                "###################Filtered list url###################" + "\n" + "\n"
	                        + filteredList + "\n");
	        return filteredList;
	    }

	    public static HashMap<String, String> getMapData(String path) throws IOException {
	        String line = "";
	        BufferedReader b = new BufferedReader(new FileReader(path));
	        HashMap<String, String> result = new HashMap<>();
	        while ((line = b.readLine()) != null) {
	            String[] parts = line.split(",");
	            String key = parts[0];
	            String value = parts[1];

	            result.put(key, value);
	        }

	        result.entrySet().forEach(entry -> {
	            System.out.println(entry.getKey() + " " + entry.getValue());
	        });b.close();
	        return result;

	    }
	  }