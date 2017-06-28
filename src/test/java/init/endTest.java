package init;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import Execution.Execute;
import Report.Report;

public class endTest {
	
	public static Hashtable<String, String> EndtimeAdd = new  Hashtable<String, String>();	
	
	 @AfterSuite
		public void generateResult() {
			 Date date = new Date();
				String t = String.format("%tc", date );
				
			EndtimeAdd.put("END_TIME", t);
			long etpc = date.getTime();
			EndtimeAdd.put("etpc",String.valueOf(etpc));
			
			Report.finalHtmlReport();
			Report.log("End of testing" );
			//WebDriver driver = Execute.driver;
			//driver.get(Report.RESULT_FILE);
			
			
		    
		}
	 
	 
	
}
