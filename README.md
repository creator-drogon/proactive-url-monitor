# proactive-url-monitor
Proactively monitors joboss, tomcat healthcheck and jboss url and is capable of logging into the server and perform service restart for downtime 



Short Description of what this source code does -

MyScheduler.java contains the main method under package named driver_scheduler
The Package- 'operations' contains the stage1 and stage2 classes
We need to create the three csv files namely UrlList.csv, Results.csv & FilteredUrl.csv
The UrlList.csv contains all the required data needed during the process
We also need to have corresponding shell-command files in the txt file(e.g. Hermes.txt)

Flow:
The main method first runs the stage-1:
	stage1:The Up and Down Urls are segregated into two csv files, Results.csv & FilteredUrl.csv respectively; with necessary data
After stage one is complete we hold for 5sec(just for demo) and start with Stage-2
	stage2:It scans the FilteredUrl.csv and looks for corresponding Hermes.txt location & other data and executes it's content

