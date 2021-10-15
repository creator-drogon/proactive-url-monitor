package com.hpi.main;

import com.hpi.alert.MailDirector;
import com.hpi.operations.RemoveDuplicates;
import com.hpi.operations.ResolutionDirector;
import com.hpi.operations.UrlSegregator;

public class MyScheduler {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

//
//		UrlSegregator.myProcesss1("./resources/PingResources.txt");
//
//		Thread.sleep(5000);
//		System.out.println("Now working with stage 2" + " 😉\n");
//
//		RemoveDuplicates.removeDuplicates("./resources/FilteredUrl.csv", "./resources/optimizedList1.csv");

		ResolutionDirector.myProcess2();
		System.out.println("Now working with stage 3" + "😎\n");

		UrlSegregator.myProcesss1("./resources/PingResourceFinal.txt");

		System.out.println("Now working with stage 3 Notification" + "👽\n");
		MailDirector.readCSV_Mail("resources/FailedUrl.csv");


	}

}
