package Report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import Execution.Execute;
import init.endTest;
import init.initLog;
import init.initTest;
import common.Constants;
import common.*;




public class Report {
	public static String LOG_FILE = null;
	public static String RESULT_FILE = null;
	public static String REPORT_FILE = null;
	public static String lstFolder = null;
	public static String screenShotFolder = null;
	public static LinkedHashMap<String,String> testCaseTime = new LinkedHashMap<String,String>();
	
	static boolean LOG_FOLDER = false;
	static boolean REPORT_FOLDER = false;
	static initLog log = new initLog();
	private static FileInputStream fs ;
	private static FileOutputStream fso;
	static FileWriter fstream ;
	static BufferedWriter out;
	private static datatable finalData = null;;
//	public static WebDriver driver = Execute.driver;	
	
	public static void myReport(){
		String logoFilePath = System.getProperty("user.dir")+"/TestData/BonzaiLogo.png";
		String logFolder;
		File reportFile;
		File lsDir;
		File todayDir;
		System.out.println(Constants.REPORT_DIR);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String dateStr = dateFormat.format(cal.getTime());
		String todaysDir = Constants.REPORT_DIR + "/" + dateStr;
		System.out.println(todaysDir);
		try{
			
			 reportFile = new File(Constants.REPORT_DIR);
			
			System.out.println(reportFile.exists());
			if(!reportFile.exists()){
				
				FileHandler.createDir(reportFile);
			}
			todayDir = new File(todaysDir);
			System.out.println(todayDir.exists());
			if(!todayDir.exists()){
				FileHandler.createDir(todayDir);
			}
			
			int numberOfSubfolders=0;
			File listDir[] = todayDir.listFiles();
			for (int i = 0; i < listDir.length; i++) {
			    if (listDir[i].isDirectory()) {
			            numberOfSubfolders++;
			        }
			}
			if(numberOfSubfolders>0){
				int nums = numberOfSubfolders +1;
				 lstFolder = todayDir +"/" + nums;
				System.out.println(lstFolder);
				 lsDir = new File(lstFolder);
				FileHandler.createDir(lsDir);
			}else{
				 lstFolder = todayDir +"/" + "1";
				 lsDir = new File(lstFolder);
				FileHandler.createDir(lsDir);
			}
			screenShotFolder = lstFolder + "/SCREENSHOTS";
			FileHandler.createDir(new File(screenShotFolder));
			
			LOG_FILE = lstFolder +"/" + "logs.txt";
			File lgFolder = new File(LOG_FILE);
			lgFolder.createNewFile();
			RESULT_FILE = lstFolder + "/"+"Report.html";
			REPORT_FILE = lstFolder + "/" +"Report.xlsx";
			System.out.println(reportFile.exists());
			System.out.println(todayDir.exists());
			FileUtils.copyFile(new File(Constants.SUITE_FILE_PATH), new File(REPORT_FILE));
			FileUtils.copyFileToDirectory(new File(logoFilePath),lsDir);
			finalData = new datatable(REPORT_FILE); 
			finalData.addSheet("FORM_RESULT");
			finalData.addSheet("VERIFY_RESULT");
			createVerifySheet();
			finalData.setCellDataByColNum("FORM_RESULT", 0, 1, "KEYWORDS");
			Report.log("Suite file created successfully");
		 } catch (IOException e) {
			Report.log("Not able to create report file");
			Report.error(e.getMessage());
			e.printStackTrace();
			
		}
		
	
		
	}
	
	public static String readParameter(String filepath, String propertyName){
		String paramname = null;
		try{
			FileInputStream fsProperty = new FileInputStream(filepath);
			Properties propertyFile = new Properties();
			propertyFile.load(fsProperty);
			paramname = propertyFile.getProperty(propertyName);
			 
		}

		catch(Exception e){
			e.printStackTrace();
		}
		
		return paramname;
		
	}
	
	
	
	public static void takeScreenShot(WebDriver driver,String screenshotName) {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String filePath = screenShotFolder  + "/"+screenshotName +".jpg";
	    try {
			FileUtils.copyFile(scrFile, new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}	   
	    
	}
	
	
	public static Object[][] importParameter(){
		

		int rowC = finalData.getRowCount("PARAMETER");
		System.out.println(rowC);
		Object c[][] = new Object[1][1];
		
		LinkedHashMap<String,String> table = new LinkedHashMap<String,String>();
		
		for(int i=2;i<=rowC;i++){
			String parameter =  finalData.getCellData("PARAMETER", 0, i).trim();
			String paramValue = finalData.getCellData("PARAMETER", 1, i).trim();
			table.put(parameter, paramValue);
		}
		c[0][0] = table;
		
		return c;
	
		
		
	}
	
public static LinkedHashMap<String,String>  impParameter(){
		

		int rowC = finalData.getRowCount("PARAMETER");
		System.out.println(rowC);
		Object c[][] = new Object[1][1];
		
		LinkedHashMap<String,String> table = new LinkedHashMap<String,String>();
		
		for(int i=1;i<=rowC;i++){
			String parameter =  finalData.getCellData("PARAMETER", 0, i).trim();
			String paramValue = finalData.getCellData("PARAMETER", 1, i).trim();
			table.put(parameter, paramValue);
		}
		
		
		return table;
	
		
		
	}
	
	public static Object[][] getData(String testCaseName){
		
		
		int testCaseRowNum = 1;
		while(!finalData.getCellData("FORMS", 0, testCaseRowNum).trim().toLowerCase().equalsIgnoreCase(testCaseName)){
			testCaseRowNum++;
		}
		
		int colStartRowNum = testCaseRowNum+1;
		int dataStartRowNum = testCaseRowNum+2;
		int rows = 0;
		while(!finalData.getCellData("FORMS", 0, dataStartRowNum+rows).trim().equals("")){
			rows++;
		}
		
		int cols=0;
		while(!finalData.getCellData("FORMS", cols, colStartRowNum).trim().equals("")){
			cols++;
		}
		
		Object testData[][] = new Object[rows][1];
		int i=0;
		for(int rNum = dataStartRowNum;rNum<dataStartRowNum+rows;rNum++){
			
			LinkedHashMap<String,String> table = new LinkedHashMap<String,String>();
			
			for(int cNum=0;cNum<cols;cNum++){
				String data = finalData.getCellData("FORMS", cNum, rNum);
				String colName= finalData.getCellData("FORMS", cNum, colStartRowNum) ;
				table.put(colName, data);
			}
			
			testData[i][0]=table;
			i++;
			
		}
		return testData;
	}
	//update test case status
		public static void updateTestCaseStatus(String TCID, String Status, String comment, String screenShot ){
			
			try{
					int tcRowNum = finalData.getCellRowNum(Constants.TEST_CASE_SHEET, Constants.TCID, TCID);
					if(Status.trim().equalsIgnoreCase(Constants.PASS)){
						finalData.setCellData(Constants.TEST_CASE_SHEET, Constants.STATUS, tcRowNum, Status);
						finalData.setCellColor(Constants.TEST_CASE_SHEET, Constants.STATUS, tcRowNum,"GREEN");
					}
					if(Status.trim().equalsIgnoreCase(Constants.FAIL)){
						finalData.setCellData(Constants.TEST_CASE_SHEET, Constants.STATUS, tcRowNum, Status);
						finalData.setCellColor(Constants.TEST_CASE_SHEET, Constants.STATUS, tcRowNum,"RED");
					}
					if(Status.trim().equalsIgnoreCase(Constants.NOT_RUN )|| Status.trim().equalsIgnoreCase("")){
						finalData.setCellData(Constants.TEST_CASE_SHEET, Constants.STATUS, tcRowNum, Status);
						finalData.setCellColor(Constants.TEST_CASE_SHEET, Constants.STATUS, tcRowNum,"YELLOW");
					}
					finalData.setCellData(Constants.TEST_CASE_SHEET, Constants.COMMENT, tcRowNum, comment);
							
					if(screenShot!=null){
						finalData.setCellData(Constants.TEST_CASE_SHEET, "SCREENSHOT_NAME", tcRowNum, screenShot);
						
					}
				
			}catch(Exception e){
				
			}
			
		}
		
		public static void updateTestCaseResult(String tcKey, String Status, String comment, String screenShot ){
			

			
			try{
					int tcRowNum = finalData.getCellRowNum(Constants.TEST_CASE_SHEET, Constants.TC_KEY, tcKey);
					if(Status.trim().equalsIgnoreCase(Constants.PASS)){
						finalData.setCellData(Constants.TEST_CASE_SHEET, Constants.STATUS, tcRowNum, Status);
						finalData.setCellColor(Constants.TEST_CASE_SHEET, Constants.STATUS, tcRowNum,"GREEN");
					}
					if(Status.trim().equalsIgnoreCase(Constants.FAIL)){
						finalData.setCellData(Constants.TEST_CASE_SHEET, Constants.STATUS, tcRowNum, Status);
						finalData.setCellColor(Constants.TEST_CASE_SHEET, Constants.STATUS, tcRowNum,"RED");
					}
					if(Status.trim().equalsIgnoreCase(Constants.NOT_RUN )|| Status.trim().equalsIgnoreCase("")){
						finalData.setCellData(Constants.TEST_CASE_SHEET, Constants.STATUS, tcRowNum, Status);
						finalData.setCellColor(Constants.TEST_CASE_SHEET, Constants.STATUS, tcRowNum,"YELLOW");
					}
					finalData.setCellData(Constants.TEST_CASE_SHEET, Constants.COMMENT, tcRowNum, comment);
							
					if(screenShot!=null){
						finalData.setCellData(Constants.TEST_CASE_SHEET, "SCREENSHOT_NAME", tcRowNum, screenShot);
						
					}
				
			}catch(Exception e){
				
			}
			
		
			
			
		}
		
		public static String testCaseID(String SuiteName,String testCaseName){
			String testCaseID = null;
			int rowCountSuite ;
			int rowCountTC;
			String TC_NAME;
			try{
				 rowCountSuite =finalData.getRowCount(Constants.SUITE);
				 rowCountTC = finalData.getRowCount(Constants.TEST_CASE_SHEET);
				 for(int i=2;i<=rowCountSuite;i++){
					 if(SuiteName.trim().equalsIgnoreCase(finalData.getCellData(Constants.SUITE, Constants.SUITE_ID, i)))
					 
					 	 for(int rowNum=2;rowNum<=rowCountTC;rowNum++){
								 TC_NAME= finalData.getCellData(Constants.TESTCASE_SUITE, Constants.TC_KEY, rowNum);
								if(TC_NAME.trim().equalsIgnoreCase(testCaseName)){
									testCaseID = finalData.getCellData(Constants.TESTCASE_SUITE, Constants.TCID, rowNum);
									return testCaseID; 
									}
						}
						}
								 
					 }
				
			catch(Exception e){
				Report.log("unable to find test case file");
				e.printStackTrace();
			}

			return testCaseID;
		}

		public static void putTestCaseExecutionTime(String tcKey,String t){
			
			testCaseTime.put(tcKey, t);
		}
		public  static boolean testCaseValidation(String SuiteName,String testCaseName){

			String EXECUTE_FLAG = "N";
			int rowCountSuite ;
			int rowCountTC;
			String TC_EXEC_FLAG = "N";
			String TC_NAME;
			
			try{
				
				
				 rowCountSuite =finalData.getRowCount(Constants.SUITE);
				 rowCountTC = finalData.getRowCount(Constants.TEST_CASE_SHEET);
								 
				 for(int i=2;i<=rowCountSuite;i++){
					 if(SuiteName.trim().equalsIgnoreCase(finalData.getCellData(Constants.SUITE, Constants.SUITE_ID, i)))
					 //String suiteName = datatable.getCellData(Constants.SUITE, Constants.SUITE_NAME, i);
					 EXECUTE_FLAG =	finalData.getCellData(Constants.SUITE, Constants.EXECUTE_FLAG, i);
					 if(EXECUTE_FLAG.trim().equalsIgnoreCase(Constants.EXECUTE_YES)){
							 for(int rowNum=2;rowNum<=rowCountTC;rowNum++){
								 TC_NAME= finalData.getCellData(Constants.TESTCASE_SUITE, Constants.TC_KEY, rowNum);
								if(TC_NAME.trim().equalsIgnoreCase(testCaseName)){
									 TC_EXEC_FLAG= finalData.getCellData(Constants.TESTCASE_SUITE, Constants.TC_EXECUTION_FLAG, rowNum);
									if(TC_EXEC_FLAG.trim().equalsIgnoreCase(Constants.EXECUTE_YES)){
										 						 
										 return true;	
									}else{
										//throw new SkipException("Skipping the test " + testCaseName +" EXECUTE_FLAG is N" );
										Report.log("Test case execution flag for test case id " + testCaseName + "is " + TC_EXEC_FLAG);
										
										}
										 
						}
						 
						}
								 
					 }
					 else{
						// throw new SkipException("Skipping the Suite " + SuiteName +" EXECUTE_FLAG is N" );
						// Report.log("Test case execution flag for suite id " + SuiteName + " is " + EXECUTE_FLAG);
						 //System.out.println("skip test");
					 }
					
					}
					
				}
				
			catch(Exception e){
				Report.log("unable to find test case file");
				e.printStackTrace();
			}
			
		return false;

		}

	
	public static void updateSuiteResult(){
		updateTestCaseStatus();
		int sheetRowCount = finalData.getRowCount(Constants.SUITE_NAME);
		int tcRowCount = finalData.getRowCount("TEST_CASE");
		HashSet<String> suiteID = new HashSet<String>();
		//get all suite ids
		for(int i=2;i<=sheetRowCount;i++){
			suiteID.add(finalData.getCellData(Constants.SUITE_NAME, "SUITE_ID", i));
			
		}
		
		Iterator<String> ls = suiteID.iterator();
		while(ls.hasNext()){
			String ids = ls.next();
			int tcNotRun =0;
			int tcPass = 0;
			int tcFail =0;
			for(int i=2;i<=tcRowCount;i++){
				String idsTC = finalData.getCellData("TEST_CASE", "SUITE_ID", i);
				if(ids.trim().equalsIgnoreCase(idsTC)){
					String tcKey = finalData.getCellData("TEST_CASE", "TC_KEY", i);
					String tcStatus = finalData.getCellData("TEST_CASE", "STATUS", i);
					if(tcStatus.trim().equalsIgnoreCase("NOT RUN")){
						tcNotRun++;
					}
					if(tcStatus.trim().equalsIgnoreCase("FAIL")){
						tcFail++;
					}
					if(tcStatus.trim().equalsIgnoreCase("PASS")){
						tcPass++;
					}
					
					
					
				}
			}
			if(tcFail>0){
				updateSuiteStatus(ids,"FAIL","Suite fails");
			}else if(tcPass>0){
				updateSuiteStatus(ids,"PASS","Suite PASS");
			}else if(tcPass == 0 && tcFail == 0 && tcNotRun == 0 ){
				updateSuiteStatus(ids,"NOT RUN","Suite NOT RUN");
			}else{
				updateSuiteStatus(ids,"NOT RUN","Suite NOT RUN");
			}
			
		}
		
		
	}
	
	
	public static void updateSuiteStatus(String suiteID, String Status, String comment ){
		
		try{
			
				int suiteRowNum = finalData.getCellRowNum(Constants.SUITE_NAME, Constants.SUITE_ID, suiteID);
				
				if(Status.trim().equalsIgnoreCase(Constants.PASS)){
					finalData.setCellData(Constants.SUITE_NAME, Constants.STATUS, suiteRowNum, Status);
					finalData.setCellColor(Constants.SUITE_NAME, Constants.STATUS, suiteRowNum,"GREEN");
				}
				else if(Status.trim().equalsIgnoreCase(Constants.FAIL)){
					finalData.setCellData(Constants.SUITE_NAME, Constants.STATUS, suiteRowNum, Status);
					finalData.setCellColor(Constants.SUITE_NAME, Constants.STATUS, suiteRowNum,"RED");
				}
				else if(Status.trim().equalsIgnoreCase(Constants.NOT_RUN) ){
					finalData.setCellData(Constants.SUITE_NAME, Constants.STATUS, suiteRowNum, "NOT RUN");
					finalData.setCellColor(Constants.SUITE_NAME,Constants.STATUS, suiteRowNum,"YELLOW");
				}
				else{
					finalData.setCellData(Constants.SUITE_NAME, Constants.STATUS, suiteRowNum, "NOT RUN");
					finalData.setCellColor(Constants.SUITE_NAME, Constants.STATUS, suiteRowNum,"YELLOW");
				}
				finalData.setCellData(Constants.SUITE_NAME, Constants.COMMENT, suiteRowNum, comment);
			
		}catch(Exception e){
			Report.log(e.getMessage());
			
		}
	}
	
	
	public static void finalHtmlReport(){
		updateSuiteResult();
		String Start_date = initTest.StrttimeAdd;
		String End_date = endTest.EndtimeAdd.get("END_TIME");
		
		//String End_date ="";
		try{
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String RUN_DATE = dateFormat.format(cal.getTime());
			
			String TCID,SUITE_ID,SUITE_DESC,STATUS,COMMENT,TC_KEY,TC_NAME,FORMS_VAL;
			 int suiteRow = finalData.getRowCount(Constants.SUITE_NAME);
			 int tcRowNum = finalData.getRowCount(Constants.TEST_CASE_SHEET);
			 LinkedHashSet<String> tcList =new LinkedHashSet<String>();
			 LinkedHashSet<String> tcListOld =new LinkedHashSet<String>();
			 LinkedHashSet<String> tcPassList =new LinkedHashSet<String>();
			 LinkedHashSet<String> tcFailList =new LinkedHashSet<String>();
			 LinkedHashSet<String> tcNotRunList =new LinkedHashSet<String>();
			 LinkedHashSet<String> screenShotList =new LinkedHashSet<String>();
			 
			 
			 for(int i=2;i<=tcRowNum;i++){
		    	String TCID1 = finalData.getCellData(Constants.TEST_CASE_SHEET, Constants.TCID, i);
		    	tcListOld.add(TCID1);
		    	 String TC_NAME1=finalData.getCellData(Constants.TEST_CASE_SHEET, Constants.TEST_NAME, i);
		    	 String STATUS1=finalData.getCellData(Constants.TEST_CASE_SHEET, Constants.STATUS, i);
		    	  if(STATUS1.equalsIgnoreCase(Constants.PASS)){
						  tcPassList.add(TCID1 +" : "+TC_NAME1);
				  }
				  if(STATUS1.equalsIgnoreCase(Constants.FAIL)){
					  tcFailList.add(TCID1 +" : "+TC_NAME1);
				  }
				  if(STATUS1.equalsIgnoreCase(Constants.NOT_RUN) || STATUS1.equalsIgnoreCase("NOT_RUN") || STATUS1.equalsIgnoreCase("")  ){
					  tcNotRunList.add(TCID1 +" : "+TC_NAME1);
				  }
			 } 
				String graphData = " google.charts.load(\"current\", {packages:[\"corechart\"]});google.charts.setOnLoadCallback(drawChart);function drawChart() {var data = google.visualization.arrayToDataTable([['Status', 'Count'],['Not Run',"+tcNotRunList.size()+"],['Pass',"+tcPassList.size()+"],['Fail',"+tcFailList.size()+"],]); var options = {width: 850, height: 350, slices: {  3: {offset: 0},  2: {offset: 0}, }, title: 'Automation Test Result',	colors: ['#B0B200', '#99ff99', '#ff1a1a'], is3D: true };; var chart = new google.visualization.PieChart(document.getElementById('autoGraph')); chart.draw(data, options);} \n";

			 fstream =new FileWriter(RESULT_FILE);;
			  out =new BufferedWriter(fstream);
			  out.newLine();
			  out.write("");
			  out.write("<!DOCTYPE html>\n");
			  out.write("<html>\n");
			  out.write("<HEAD>\n");
			  out.write(" <meta charset=\"ISO-8859-1\">\n");
			  out.write("<title>Automation Test Report</title>\n");
			//  <!-- Latest compiled and minified CSS -->
			  out.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">/n");
			 //<!-- Optional theme --> 
			  out.write("<!--[if lt IE 9]>\n");
			  out.write("<script src=\"https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js\"></script>\n");
			  out.write("<script src=\"https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js\"></script>\n");
			  out.write("<![endif]-->\n");
			  out.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css\" integrity=\"sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r\" crossorigin=\"anonymous\">\n");
			  out.write("<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>");
			  out.write("<script type=\"text/javascript\">\n");
			  out.write(graphData);
			  out.write("</script>\n");
			  out.write("</head>\n");
			  out.write("<body>\n");
			  out.write("<div>\n");
			  out.write("<nav class=\"navbar navbar-inverse navbar-fixed-top\" style=\"position:fixed;\" id=\"my-navbar\">\n");
			  out.write("<div class=\"container\">\n");
			  out.write("<div class=\"navbar-header\">\n");
			  out.write("<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse\"\">\n");
			  out.write("<span class=\"icon-bar\"></span>\n");
			  out.write("<span class=\"icon-bar\"></span>\n");
			  out.write("<span class=\"icon-bar\"></span>\n");
			  out.write("<span class=\"icon-bar\"></span>\n");
			  out.write("<span class=\"icon-bar\"></span>\n");
			  out.write("<span class=\"icon-bar\"></span>\n");
			  out.write("</button>\n");
			  out.write("<a class=\"navbar-brand\" style=\"margin-bottom:2px;\"><img src = 'BonzaiLogo.png'></img></a>\n");
			  //
			// BonzaiLogo.png out.write("<a class=\"navbar-brand\">Bonzai Test Report</a>\n");
			  out.write("</div>\n");
			  out.write("<div class=\"collapse navbar-collapse\" id=\"navbar-collapse\">\n");
			  out.write("<ul class=\"nav navbar-nav nav-tabs\" role=\"tablist\">\n");
			  out.write("<li role=\"presentation\" class=\"active\"><a href=\"#TestSummary\" aria-controls=\"TestSummary\" role=\"tab\" data-toggle=\"tab\">Test Summary</a></li>\n");
			  out.write("<li role=\"presentation\"><a href=\"#TestSuiteReport\" aria-controls=\"TestSuiteReport\" role=\"tab\" data-toggle=\"tab\">Test Suite Report</a></li>\n");
			  out.write("<li role=\"presentation\"><a href=\"#TestCaseReport\" aria-controls=\"TestCaseReport\" role=\"tab\" data-toggle=\"tab\">Test Case Report</a></li>\n");
			//  out.write("<li role=\"presentation\"><a href=\"#ExecutionReport\" aria-controls=\"ExecutionReport\" role=\"tab\" data-toggle=\"tab\">Execution Report</a></li>\n");
			  out.write("<li role=\"presentation\"><a href=\"#LogsReport\" aria-controls=\"LogsReport\" role=\"tab\" data-toggle=\"tab\">Logs Report</a></li>\n");
			  out.write("</ul>\n");
			  out.write("</div>\n");
			  out.write("</div>\n");
			  out.write("</nav>\n");
			  //tab content
			  out.write("<div class=\"tab-content\">\n");
			  out.write("<div role=\"tabpanel\" class=\"tab-pane active\" id=\"TestSummary\" style=\"margin-top: 60px;\">\n");
			  out.write("<div class=\"container\">\n");
			  out.write("<section>\n");
			  out.write("<div class=\"page-header\" >\n");
			  out.write("<h2 >Test Summary</h2>\n");
			  out.write("</div>\n");
			  out.write("<div class=\"row\">\n");
			  out.write("<div class=\"col-lg-6\">\n");
			  
			  out.write("<div class=\"table-responsive center-div\">\n");
			  out.write("<table class=\"table table-bordered table-hover\" style=\"width:100%;\">\n");
			  out.write("<tr class=\"well\">\n");
			  out.write("<td><b> PARAMETER</b></td>\n");
			  out.write("<td><b> VALUE</b></td>\n");
			  out.write("</tr>\n");
			  out.write("<tr>\n");
			  out.write("<td><b> Environment</b></td>\n");
			  out.write("<td>Bonzai</td>\n");
			  out.write("</tr>\n");
			  out.write("<tr>\n");
			  out.write("<td><b> Run Date</b></td>\n");
			  out.write("<td> "+RUN_DATE+"</td>\n");
			  out.write("</tr>\n");
			  out.write("<tr>\n");
	    	  out.write("<td><b> Run StartTime</b></td>\n");
			  out.write("<td>"+Start_date+" </td>\n");
			  out.write("</tr>\n");
			  out.write("<tr>\n");
			  out.write("<td><b> Run EndTime</b></td>\n");
			  out.write("<td>"+End_date+" </td>\n");
			  out.write("</tr>\n");
			  long stpc = Long.parseLong(initTest.EndtimeAdd.get("stpc"));
			  long etpc = Long.parseLong(endTest.EndtimeAdd.get("etpc"));
			  long timeDiff = etpc - stpc ;
				String timeTaken = Report.getExecutionTime(timeDiff);
			 // String timeTaken = "";
			  out.write("<tr>\n");
			  out.write("<td><b> Time Taken</b></td>\n");
			  out.write("<td>"+timeTaken+"</td>\n");
			  out.write("</tr>\n");
			  out.write("<tr>\n");
			  out.write("<td><b>Total Number of Test Cases</b></td>\n");
			  out.write("<td> "+tcListOld.size()+"</td>\n");
			  out.write("</tr>\n");
			  out.write("<tr>\n");
			  out.write("<td><b>Total No. of Test Case Executed</b></td>\n");
			  int cnt = tcListOld.size() - tcNotRunList.size();
			  out.write("<td> "+cnt+"</td>\n");
			  out.write("</tr>\n");
			  out.write("<tr>\n");
			  out.write("<td><b>Total No. of Test Case Pass</b></td>\n");
			  out.write("<td> "+tcPassList.size()+"</td>\n");
			  out.write("</tr>\n");
			  out.write("<tr>\n");
			  out.write("<td><b>Total No. of Test Case Fail</b></td>\n");
			  out.write("<td> "+tcFailList.size()+"</td>\n");
			  out.write("</tr>\n");
			  out.write("</table>\n");
			  out.write("</div>\n");
			  out.write("</div>\n");
			  
			  out.write("<div class=\"col-lg-6\">\n");
			  out.write("<div id=\"autoGraph\" style=\"width: 100%;\"></div>");
			  out.write("</div>\n");
			  out.write("</div>\n");
			  out.write("</section>\n");
			  out.write("</div>\n");
			  out.write("</div>\n");
			  out.write("<div role=\"tabpanel\" class=\"tab-pane fade\" id=\"TestSuiteReport\" style=\"margin-top: 60px;\">\n");
			  out.write("<div class=\"container\">\n");
			  out.write("<section>\n");
			  out.write("<div class=\"page-header\">\n");
			  out.write("<h2>Test Suite Report</h2>\n");
			  out.write("</div>\n");
			  out.write("<div class=\"table-responsive\">\n");
			  out.write("<table class=\"table table-bordered table-hover\" style=\"width:100%\">\n");
			  out.write("<tr class=\"well\">	\n");
			  out.write("<td><b>SUITE_ID</b></td>\n");
			  out.write("<td><b>SUITE_NAME</b></td>\n");
			  out.write("<td><b>SUITE_DESC</b></td>\n");
			  out.write("<td><b>STATUS</b></td>\n");
			  out.write("<td><b>COMMENT</b></td>\n");
			  out.write("</tr>\n");
			  // Add suite report from xlsx -- generate suite report
			  	
			  for(int i=2;i<=suiteRow;i++){
			     TCID = finalData.getCellData(Constants.SUITE_NAME, Constants.TSID, i);
			    	 SUITE_ID=finalData.getCellData(Constants.SUITE_NAME, Constants.SUITE_ID, i);
			    	 SUITE_DESC=finalData.getCellData(Constants.SUITE_NAME, Constants.SUITE_DESC, i);
			    	 STATUS=finalData.getCellData(Constants.SUITE_NAME, Constants.STATUS, i);
			    	 COMMENT=finalData.getCellData(Constants.SUITE_NAME, Constants.COMMENT, i);
			  
			  
			  out.write("<tr>\n");
			  out.write("<td>"+TCID+"</td>\n");
			  out.write("<td>"+SUITE_ID +"</td>\n");
			  out.write("<td>"+SUITE_DESC+"</td>\n");
			  
			  if(STATUS.equalsIgnoreCase(Constants.PASS)){
				  out.write("<td class=\"success\"> "+STATUS+"</td>\n");
				 
			  }
			  if(STATUS.equalsIgnoreCase(Constants.FAIL)){
				  out.write("<td class=\"danger\"> "+STATUS+"</td>\n");
				
			  }
			  if(STATUS.equalsIgnoreCase(Constants.NOT_RUN)){
				  out.write("<td>"+STATUS+"</td>\n");
			  }
			 
			  out.write("<td>"+COMMENT+"</td>\n");
			  out.write("</tr>\n");
			 
			  }
			  
			  out.write("</table>\n");
			  out.write("</div>\n");
			  out.write("</section>\n");
			  out.write("</div>\n");
			  out.write("</div>\n");
			  out.write("<div role=\"tabpanel\" class=\"tab-pane fade\" id=\"TestCaseReport\" style=\"margin-top: 60px;\">\n");
			  out.write("<div class=\"container\">\n");
			  out.write("<section>\n");
			  out.write("<div class=\"page-header\">\n");
			  out.write("<h2>Test Case Report</h2>\n");
			  out.write("</div>\n");
			  out.write("<div class=\"table-responsive\">\n");
			  out.write("<table class=\"table table-bordered table-hover\" style=\"width:100%\">\n");
			  out.write("<tr class=\"well\">\n");
			  out.write("<th>TC ID</th>\n");
			  out.write("<th>MODULE NAME</th>\n");
			  out.write("<th>TC DESCRIPTION</th>\n");
			  out.write("<th>TIME</th>\n");
			  out.write("<th>STATUS</th>\n");
			  out.write("<th>COMMENT</th>\n");
			//  out.write("<th>TEST DATA</th>\n");
			 // out.write("<th>SCREEN SHOTS</th>\n");
			  out.write("<th> </th>\n");
			  out.write("<th> </th>\n");
			  
			  out.write("</tr>\n");
			 
			  
			  for(int i=2;i<=tcRowNum;i++){
			    	 TCID = finalData.getCellData(Constants.TEST_CASE_SHEET, Constants.TCID, i);
			    	 SUITE_ID=finalData.getCellData(Constants.TEST_CASE_SHEET, Constants.SUITE_ID, i);
			    	 TC_KEY=finalData.getCellData(Constants.TEST_CASE_SHEET, Constants.TC_KEY, i);
			    	 tcList.add(TC_KEY);
			    	 TC_NAME=finalData.getCellData(Constants.TEST_CASE_SHEET, Constants.TEST_NAME, i);
			    	 STATUS=finalData.getCellData(Constants.TEST_CASE_SHEET, Constants.STATUS, i);
			    	 COMMENT=finalData.getCellData(Constants.TEST_CASE_SHEET, Constants.COMMENT, i);
			    	 FORMS_VAL=finalData.getCellData(Constants.TEST_CASE_SHEET, "FORM", i);
			    	  out.write("<tr>\n");
					  out.write("<td>"+TCID+"</td>\n");
					  out.write("<td>"+SUITE_ID+"</td>\n");
					  out.write("<td>"+TC_NAME+"</td>\n");
					/*  if(testCaseTime.containsKey(TC_KEY)){
					  out.write("<td>"+testCaseTime.get(TC_KEY)+"</td>\n");
					  }else{
						  out.write("<td> </td>\n");
					  }*/
					 // out.write("<td>"+STATUS+"</td>\n");
					  if(STATUS.equalsIgnoreCase(Constants.PASS)){
						  out.write("<td>"+testCaseTime.get(TC_KEY)+"</td>\n");
						  out.write("<td class=\"success\"> "+STATUS+"</td>\n");
						  tcPassList.add(TCID +" : "+TC_NAME);
					  }
					  
					  if(STATUS.equalsIgnoreCase(Constants.FAIL)){
						  out.write("<td>"+testCaseTime.get(TC_KEY)+"</td>\n");
						  out.write("<td class=\"danger\"> "+STATUS+"</td>\n");
						  tcFailList.add(TCID +" : "+TC_NAME);
					  }
					  if(STATUS.equalsIgnoreCase(Constants.NOT_RUN) || STATUS.equalsIgnoreCase("NOT_RUN") || STATUS.equalsIgnoreCase("")  ){
						  out.write("<td> </td>\n");
						  out.write("<td>"+"NOT_RUN"+"</td>\n");
						  tcNotRunList.add(TCID +" : "+TC_NAME);
					  }
					  
					  
					  out.write("<td>"+COMMENT+"</td>\n");
					  if(isTcFormExist(TC_KEY)){
						  out.write("<td><button type=\"button\" class=\"btn btn-default\" data-toggle=\"modal\" data-target=\"#"+TC_KEY+"\">\n");
						  out.write("<span class=\"glyphicon glyphicon-tasks\" aria-hidden=\"true\"></span>\n");
						  out.write("</button>\n");
						  out.write("</td>\n");
						  }
						  else{
							  out.write("<td></td>\n");
						  }
					  
					  if(isVerifyExist(TC_KEY)){
					  String TC_KEY_V = TC_KEY +"_Verify";
					  out.write("<td><button type=\"button\" class=\"btn btn-default\" data-toggle=\"modal\" data-target=\"#"+TC_KEY_V+"\">\n");
					  out.write("<span class=\"glyphicon glyphicon-th-list\" aria-hidden=\"true\"></span>\n");
					  out.write("</button>\n");
					  out.write("</td>\n");
					  }
					  else{
						  out.write("<td></td>\n");
					  }
					  
					 /* Iterator<String> s2 = tcList.iterator();
					  boolean fORMkeyFound = true;
					  while(s2.hasNext()){
					  int rowsCountofSheet = finalData.getRowCount("FORM_RESULT");
					  String fORMmethodName = s2.next();
					
					  int testCaseRowNum = 1;
					  
						while(!finalData.getCellData("FORM_RESULT", 0, testCaseRowNum).trim().equalsIgnoreCase(fORMmethodName)){
							System.out.println(finalData.getCellData("FORMS", 0, testCaseRowNum));
							testCaseRowNum++;
							if(rowsCountofSheet<testCaseRowNum){
								fORMkeyFound = false;
								break;
								
							}
						}
						
					  }
					  
					  
					  
					  
					  if(FORMS_VAL.trim().equalsIgnoreCase("Y") && fORMkeyFound){
					  out.write("<td><button type=\"button\" class=\"btn btn-default\" data-toggle=\"modal\" data-target=\"#"+TC_KEY+"\">\n");
					  out.write("<span class=\"glyphicon glyphicon-tasks\" aria-hidden=\"true\"></span>\n");
					  out.write("</button>\n");
					  out.write("</td>\n");
					  }else{
						  out.write("<td> </td>\n");
					  }*/
					  
					 
					  
					/*  if(!SCREENSHOT_NAME.trim().equalsIgnoreCase("")){
						  screenShotList.add(SCREENSHOT_NAME);
						  String sName = SCREENSHOT_NAME.split("/.")[0];
					  out.write("<td><button type=\"button\" class=\"btn btn-default\" data-toggle=\"modal\" data-target=\"#"+ sName+"\">\n");
					  out.write("<span class=\"glyphicon glyphicon-eye-open \" aria-hidden=\"true\"></span>\n");
					  out.write("</button>\n");
					  out.write("</td>\n");
					  }else{
						  out.write("<td> </td>\n");
					  }*/
						  
					  
					  
					  out.write("</tr>\n");
			  }
			  out.write("</table>\n");
			  out.write("</div>\n");
			  out.write("</section>\n");
			  out.write("</div>\n");
			  out.write("</div>\n");
			  out.write("<div role=\"tabpanel\" class=\"tab-pane fade\" id=\"LogsReport\" style=\"margin-top: 60px;\">\n");
			  out.write("<div class=\"container\">\n");
			  out.write("<section>\n");
			  out.write("<div class=\"page-header\" >\n");
			  out.write("<h2 >Logs Summary</h2>\n");
			  out.write("</div>\n");
			  out.write("<iframe src=\""+LOG_FILE +"\" width=\"100%\" height=\"300\">\n");
			  out.write("</iframe>\n");
			  out.write("</section>\n");
			  out.write("</div>\n");
			  out.write("</div>\n");
			  out.write("</div>\n");
			  
			  Iterator<String> s = tcList.iterator();
			  
			  while(s.hasNext()){
			  int rowsCountofSheet = finalData.getRowCount("FORM_RESULT");
			  String methodName = s.next();
			  System.out.println(methodName);
			  int testCaseRowNum = 1;
			  boolean keyFound = true;
				while(!finalData.getCellData("FORM_RESULT", 0, testCaseRowNum).trim().equalsIgnoreCase(methodName)){
					System.out.println(finalData.getCellData("FORMS", 0, testCaseRowNum));
					testCaseRowNum++;
					if(rowsCountofSheet<testCaseRowNum){
						keyFound = false;
						break;
						
					}
				}
						if(keyFound){
							
							out.write("<div id=\""+methodName +"\" class=\"modal fade\" role=\"dialog\" style=\"top:20%;outline: none;width:100%;\">\n");
							out.write("<div class=\"modal-dialog modal-lg\">\n");
							out.write("<div class=\"modal-content\" style=\"overflow:auto;height: 100%;\">\n");
							out.write("<div class=\"modal-header\">\n");
							out.write("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n");
							
							// tc name and ID
							int tcIdRowCount = finalData.getCellRowNum(Constants.TEST_CASE_SHEET, Constants.TC_KEY, methodName);
							String tcId = finalData.getCellData(Constants.TEST_CASE_SHEET, Constants.TCID, tcIdRowCount);
							String tcN = finalData.getCellData(Constants.TEST_CASE_SHEET, Constants.TEST_NAME, tcIdRowCount);
							out.write("<h4 class=\"modal-title\">"+tcId +" : "+tcN +"</h4>\n");
							out.write("</div>\n");
						    out.write("<div class=\"modal-body\">\n");
							out.write("<div class=\"table-responsive\">\n");
							out.write("<table class=\"table table-bordered table-hover\" style=\"width:100%\">\n");
							
						//	System.out.println(testCaseRowNum);
							int colStartRowNum = testCaseRowNum+1;
							int dataStartRowNum = testCaseRowNum+2;
							int rows = 0;
							while(!finalData.getCellData("FORM_RESULT", 0, dataStartRowNum+rows).trim().equals("")){
								rows++;
							}
							//System.out.println("total rows"+rows);
							
							int cols=0;
							while(!finalData.getCellData("FORM_RESULT", cols, colStartRowNum).trim().equals("")){
								cols++;
							}
							//System.out.println(cols);
							
							String[][] testData = new String[rows][cols];
							int i=0;
							int screenShotRowNum = 0;
							int screenShotColNum = 0;
							for(int rNum = colStartRowNum;rNum<colStartRowNum+1;rNum++){
								 out.write("<tr class=\"well\">\n");
								for(int cNum=0;cNum<cols;cNum++){
									testData[i][cNum] =  finalData.getCellData("FORM_RESULT", cNum, rNum);
										String sName =	testData[i][cNum];				
									  out.write("<th>"+testData[i][cNum]+"</th>\n");
									  if(sName.equalsIgnoreCase("SCREENSHOT")){
										  screenShotRowNum=rNum;
										  screenShotColNum=cNum;
										  
									  }
									  
								}
								i++;
								out.write("</tr>\n");
							}
							out.write("<tr>\n");
							int j=0;
							for(int rNum = dataStartRowNum;rNum<dataStartRowNum+rows;rNum++){
								 out.write("<tr>\n");
								for(int cNum=0;cNum<cols;cNum++){
									testData[j][cNum] =  finalData.getCellData("FORM_RESULT", cNum, rNum);
									if(screenShotColNum==cNum)	{
										String SCREENSHOT_NAME = testData[j][cNum];
										if(!SCREENSHOT_NAME.trim().equalsIgnoreCase("")){
											  screenShotList.add(SCREENSHOT_NAME);
											  String sName = SCREENSHOT_NAME.split("/.")[0];
										  out.write("<td><button type=\"button\" class=\"btn btn-default\" data-toggle=\"modal\" data-target=\"#"+ sName+"\">\n");
										  out.write("<span class=\"glyphicon glyphicon-eye-open \" aria-hidden=\"true\"></span>\n");
										  out.write("</button>\n");
										  out.write("</td>\n");
										  }else{
											  out.write("<td> </td>\n");
										  }
										
										
									}else{
									  out.write("<td>"+testData[j][cNum]+"</td>\n");
									}
								}
								j++;
								out.write("</tr>\n");
							}
							
							
							
							out.write("</table>\n");
							out.write("</div>\n");
							out.write("</div>\n");
							out.write("</div>\n");
							out.write("</div>\n");
							out.write("</div>\n"); 
						}
			  }
			  
			  //write modal for verify data
			  Iterator<String> m = tcList.iterator();
			  while(m.hasNext()){
				  int rowsCountofSheet = finalData.getRowCount("VERIFY_RESULT");
				  String methodName = m.next();
				 
				  int vRowCount = finalData.getRowCount("VERIFY_RESULT"); 
						String methodNameV=methodName+"_Verify";
						
						out.write("<div id=\""+methodNameV +"\" class=\"modal fade\" role=\"dialog\" style=\"top:20%;outline: none;width:100%;\">\n");
						out.write("<div class=\"modal-dialog modal-lg\">\n");
						out.write("<div class=\"modal-content\" style=\"overflow:auto;height: 100%;\">\n");
						out.write("<div class=\"modal-header\">\n");
						out.write("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n");
						int tcIdRowCount = finalData.getCellRowNum(Constants.TEST_CASE_SHEET, Constants.TC_KEY, methodName);
						String tcId = finalData.getCellData(Constants.TEST_CASE_SHEET, Constants.TCID, tcIdRowCount);
						String tcN = finalData.getCellData(Constants.TEST_CASE_SHEET, Constants.TEST_NAME, tcIdRowCount);
						out.write("<h4 class=\"modal-title\">"+tcId +" : "+tcN +"</h4>\n");
						out.write("</div>\n");
					    out.write("<div class=\"modal-body\">\n");
						out.write("<div class=\"table-responsive\">\n");
						out.write("<table class=\"table table-bordered table-hover\" style=\"width:100%\">\n");
	
						out.write("<tr class=\"well\">\n");
						out.write("<th>DATA_ROW_NO</th>\n");
						out.write("<th>TC_KEY</th>\n");
						out.write("<th>VERIFY_POINT</th>\n");
						out.write("<th>VERIFY_DESC</th>\n");
						out.write("<th>STATUS</th>\n");
						out.write("<th>COMMENT</th>\n");
	
						out.write("</tr>\n");
						for(int i=1;i<=vRowCount;i++){
							String getTcKey = finalData.getCellData("VERIFY_RESULT", "TC_KEY", i);
							if(getTcKey.trim().equalsIgnoreCase(methodName)){
								out.write("<tr>\n");
								out.write("<td>" + finalData.getCellData("VERIFY_RESULT", "DATA_ROW_NO", i)  +"</td>\n");
								out.write("<td>" +finalData.getCellData("VERIFY_RESULT", "TC_KEY", i) +"</td>\n");
								out.write("<td>" +finalData.getCellData("VERIFY_RESULT", "VERIFY_POINT", i) +"</td>\n");
								out.write("<td>" +finalData.getCellData("VERIFY_RESULT", "VERIFY_DESC", i) +"</td>\n");
								out.write("<td>" +finalData.getCellData("VERIFY_RESULT", "STATUS", i) +"</td>\n");
								out.write("<td>" +finalData.getCellData("VERIFY_RESULT", "COMMENT", i) +"</td>\n");
								out.write("</tr>\n");
							}
						}
						
	
				

				out.write("</table>\n");
				out.write("</div>\n");
				out.write("</div>\n");
				out.write("</div>\n");
				out.write("</div>\n");
				out.write("</div>\n");
								
						}
				 
				  
			  
			
			  
			  
			  // screen shot attached modal
			  Iterator<String> scrList = screenShotList.iterator();
			
			  while(scrList.hasNext()){
				  
				  String scrName = scrList.next();
				 // String sName = scrName.split("/.")[0];
				  String sName = scrName.split("/.")[0];
				  out.write("<div id=\""+sName+"\" class=\"modal fade\" role=\"dialog\" style=\"top:20%;outline: none;width:100%;\">\n");
				  out.write("<div class=\"modal-dialog\" style=\"width: 90%;>\n");
				  out.write("<div class=\"modal-content\" style=\"overflow:auto;height: 100%;\">\n");
				  out.write("<div class=\"modal-header\">\n");
				  out.write("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n");
				  //out.write("<h4 class=\"modal-title\">TS001 : Verify brands functionality  </h4>\n");
				  out.write("</div>\n");
				  out.write("<div class=\"modal-body\">\n");
				  out.write("<div class=\"table-responsive\">\n");
				  out.write("<img src=\"SCREENSHOTS/"+scrName+".jpg\" alt=\"Logs\">\n");
				  out.write("</div>\n");
				  out.write("</div>\n");
				  out.write("</div>\n");
				  out.write("</div>\n");
				  out.write("</div>\n");
				  out.write("</div>\n");
			  }
			
			
			
			  out.write("<script src=\"http://code.jquery.com/jquery-2.1.4.min.js\"></script>\n");
			  out.write("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" integrity=\"sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS\" crossorigin=\"anonymous\"></script>\n");
			  out.write("</body>\n");
			  out.write("</html>\n");
			  out.close();
			  
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public static void updateTestCaseStatus(){
		
		 int tcRowNum = finalData.getRowCount(Constants.TEST_CASE_SHEET);
		  for(int i=2;i<=tcRowNum;i++){
			  String  tcKey=finalData.getCellData(Constants.TEST_CASE_SHEET, Constants.TC_KEY, i);
			  
			  if(isVerifyExist(tcKey)){
					 LinkedHashMap<String,String> vStatus = new LinkedHashMap<String,String>();
					 String verifyPoint_rowN = null;
					 int ccn = 0;
					 int vtcRowNum = finalData.getRowCount("VERIFY_RESULT");
						for(int j=1;j<=vtcRowNum;j++){
							String getTcKey = finalData.getCellData("VERIFY_RESULT", "TC_KEY", j);
							if(getTcKey.trim().equalsIgnoreCase(tcKey)){
								verifyPoint_rowN = finalData.getCellData("VERIFY_RESULT", "VERIFY_POINT", j) + "_" + finalData.getCellData("VERIFY_RESULT", "DATA_ROW_NO", j);
								String status = finalData.getCellData("VERIFY_RESULT", "STATUS", j);
								vStatus.put(verifyPoint_rowN, status);
							}
						}
						
						int cc=0,ff=0;
						for(Map.Entry c:vStatus.entrySet()){
							
							String statusC = c.getValue().toString();
							if(statusC.equalsIgnoreCase("PASS")){
								cc++;
							}
							if(statusC.equalsIgnoreCase("FAIL")){
								ff++;
							}
						}
						
						if(ff>0){
							
							updateTestCaseResult(tcKey, "FAIL", "Test case fail due to verify points", "");
							
						}
						
						else{
							
							updateTestCaseResult(tcKey, "PASS", "Test case Pass", "");
						}
				  }
		  }
	}
	
	
	
	public static LinkedHashMap<String,String> getVerifyStatus(String tcKey){
		
		int vRowCount = finalData.getRowCount("VERIFY"); 
		LinkedHashMap<String,String> verifyPoint = new LinkedHashMap<String,String>();
		
		for(int i=1;i<=vRowCount;i++){
			String getTcKey = finalData.getCellData("VERIFY", "TC_KEY", i);
			if(getTcKey.trim().equalsIgnoreCase(tcKey)){
				String verifyPt = finalData.getCellData("VERIFY", "VERIFY_POINT", i);
				String verifyDesc= finalData.getCellData("VERIFY", "VERIFY_DESC", i);
				verifyPoint.put(verifyPt, verifyDesc);
			}
			
		}
		return verifyPoint;
	}
	
	public static void updateVerifyStatus(String tcKey,String vKey,String status,String dataRow){
		
		int vRowCount = finalData.getRowCount("VERIFY_RESULT"); 
		
		for(int i=1;i<=vRowCount;i++){
			String getTcKey = finalData.getCellData("VERIFY_RESULT", "TC_KEY", i);
			if(getTcKey.trim().equalsIgnoreCase(tcKey)){
				String dataRowNo = finalData.getCellData("VERIFY_RESULT", "DATA_ROW_NO", i);
				if(dataRowNo.equalsIgnoreCase(dataRow)){
				String verifyPt = finalData.getCellData("VERIFY_RESULT", "VERIFY_POINT", i);
				String verifyDesc= finalData.getCellData("VERIFY_RESULT", "VERIFY_DESC", i);
				if(verifyPt.trim().equalsIgnoreCase(vKey)){
					
					finalData.setCellData("VERIFY_RESULT", "STATUS", i, status);
					if(status.trim().equalsIgnoreCase("PASS")){
					finalData.setCellData("VERIFY_RESULT", "COMMENT", i, verifyDesc + " working fine");
					}
					if(status.trim().equalsIgnoreCase("FAIL")){
						finalData.setCellData("VERIFY_RESULT", "COMMENT", i, verifyDesc + " not working");
						}
				}
			}
			
		}
		}
		
	}
	
	public static void createVerifySheet(){
		
		finalData.addColumn("VERIFY_RESULT", "DATA_ROW_NO");
		finalData.addColumn("VERIFY_RESULT", "TCID");
		finalData.addColumn("VERIFY_RESULT", "TC_KEY");
		finalData.addColumn("VERIFY_RESULT", "VERIFY_POINT");
		finalData.addColumn("VERIFY_RESULT", "VERIFY_DESC");
		finalData.addColumn("VERIFY_RESULT", "STATUS");
		finalData.addColumn("VERIFY_RESULT", "COMMENT");
		finalData.addColumn("VERIFY_RESULT", "SCREENSHOT");
		
	}
	
	public static boolean isVerifyExist(String tcKey){
		boolean status = false;
		int vRowCount = finalData.getRowCount("VERIFY_RESULT"); 
		
		
		for(int i=1;i<=vRowCount;i++){
			String getTcKey = finalData.getCellData("VERIFY_RESULT", "TC_KEY", i);
			if(getTcKey.trim().equalsIgnoreCase(tcKey)){
				status = true;
				
			}
		}
			return status;
		
	}
	
	public static boolean isTcFormExist(String tcKey){
		boolean status = false;
		int vRowCount = finalData.getRowCount("FORM_RESULT"); 
		
		
		for(int i=1;i<=vRowCount;i++){
			String getTcKey = finalData.getCellData("FORM_RESULT", "KEYWORDS", i);
			if(getTcKey.trim().equalsIgnoreCase(tcKey)){
				status = true;
				
			}
		}
			return status;
		
	}
	
	public static void addVerifyPoints(String tcKey,String rowN){
		
		int vRowCount = finalData.getRowCount("VERIFY"); 
		
		
		for(int i=1;i<=vRowCount;i++){
			String getTcKey = finalData.getCellData("VERIFY", "TC_KEY", i);
			int vrRowCount = finalData.getRowCount("VERIFY_RESULT");
			if(getTcKey.trim().equalsIgnoreCase(tcKey)){
				String tcID = finalData.getCellData("VERIFY", "TCID", i);
				String verifyPt = finalData.getCellData("VERIFY", "VERIFY_POINT", i);
				String verifyDesc= finalData.getCellData("VERIFY", "VERIFY_DESC", i);
				finalData.setCellData("VERIFY_RESULT","DATA_ROW_NO" , vrRowCount+1, rowN);
				finalData.setCellData("VERIFY_RESULT","TCID" , vrRowCount+1, tcID);
				finalData.setCellData("VERIFY_RESULT","TC_KEY" , vrRowCount+1, getTcKey);
				finalData.setCellData("VERIFY_RESULT","VERIFY_POINT" , vrRowCount+1, verifyPt);
				finalData.setCellData("VERIFY_RESULT","VERIFY_DESC" , vrRowCount+1, verifyDesc);
				
			}
			
		}
		
			
	}
	
	public static boolean isVerify(String tcKey){
		int rCount = finalData.getRowCount("TEST_CASE"); 
		String vPoint =null;
		for(int i=1;i<=rCount;i++){
			String getTcKey = finalData.getCellData("TEST_CASE", "TC_KEY", i);
			if(getTcKey.trim().equalsIgnoreCase(tcKey)){
				 vPoint = finalData.getCellData("TEST_CASE", "VERIFY", i);
				break;
			}
		}
		
		if(vPoint.trim().equalsIgnoreCase("Y")){
			return true;
		}
		else if(vPoint.trim().equalsIgnoreCase(" ") || vPoint.trim().equalsIgnoreCase("N")){
				return false;
			}
		else {
			return false;
		}

	
	}

	public static void updateFormStatus(String keyWord, LinkedHashMap<String,String> formData){
		
		int valRowNum =finalData.getCellRowNum("FORM_RESULT", "KEYWORDS", keyWord);
		
		
		int formRowCount = finalData.getRowCount("FORM_RESULT");
	
		formRowCount =formRowCount+1;
		int colNum =0;
		//Add keyword header 
		
		
		// Add column header
		Set<String> keyVal = formData.keySet();
		
		String srVal = formData.get("SRNO");
		//int srVal1 = Integer.parseInt(srVal);
		if(srVal.trim().equalsIgnoreCase("1.0")){
			if(formRowCount!=2){
			finalData.setCellDataByColNum("FORM_RESULT", 0, formRowCount, "");
			formRowCount =formRowCount+1;
			}
			finalData.setCellDataByColNum("FORM_RESULT", colNum, formRowCount, keyWord);
			formRowCount=formRowCount+1;
			for(String keys: keyVal){
				
				finalData.setCellDataByColNum("FORM_RESULT", colNum, formRowCount , keys);
				colNum = colNum+1;
			}
			formRowCount=formRowCount+1;
		}
		
		int valColNum=0;
		for(String keys:  keyVal){
			String valData = formData.get(keys);
			finalData.setCellDataByColNum("FORM_RESULT", valColNum, formRowCount , valData);
			valColNum = valColNum+1;
		}
		int finalRowCount = finalData.getRowCount("FORM_RESULT");
		System.out.println(keyWord +"*******"+ finalRowCount);
			
		
	}
	
	
	
		
	// initialize the logs and enter debug log messages
	public static void log(String logMessage){
		
		log.initLog().debug(logMessage);
		
	}
	
	// enter log error messages
	public static void error(String errorMessage){
				
		log.initLog().error(errorMessage);
		
	}
	// enter info log messages
	public static void info(String infoMessage){
		log.initLog().info(infoMessage);
		
	}
	

	
public static String getExecutionTime(long executionTime){
	
	
	 String sMillis =String.valueOf(executionTime);
	    double dMillis = 0;

	    int days = 0;
	    int hours = 0;
	    int minutes = 0;
	    int seconds = 0;
	    int millis = 0;

	    String sTime;

	    try {
	        dMillis = Double.parseDouble(sMillis);
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }


	    seconds = (int)(dMillis / 1000) % 60;
	    millis = (int)(dMillis % 1000);

	    if (seconds > 0) {
	        minutes = (int)(dMillis / 1000 / 60) % 60;
	        if (minutes > 0) {
	            hours = (int)(dMillis / 1000 / 60 / 60) % 24;
	            if (hours > 0) {
	                days = (int)(dMillis / 1000 / 60 / 60 / 24);
	                if (days > 0) {
	                    sTime = days + ":" + hours + ":" + minutes + ":" + seconds + ":" + millis ;
	                } else {
	                    sTime = hours + ":" + minutes + ":" + seconds + ":" + millis ;
	                }
	            } else {
	                sTime ="00:" + minutes + ":" + seconds + ":" + millis ;
	            }
	        } else {
	            sTime = "00:00:" +seconds + ":" + millis;
	        }
	    } else {
	        sTime = "00:00:00:" +dMillis + " millisec";
	    }

	    System.out.println("time: " + sTime);
	    return sTime;
}
	
	
	
	
	
	

	
	
	
	
	
}
