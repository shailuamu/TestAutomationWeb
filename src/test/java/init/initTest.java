/*
 startTest : Start test function used to initialize test
 Creates date folder along with number of run within folder 
 */

package init;

import java.net.URL;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import DataProvider.FormData;
import Execution.Execute;
import Report.Report;

public class initTest {
	public static  String StrttimeAdd = null;
	public static Hashtable<String, String> EndtimeAdd = new  Hashtable<String, String>();	
	@BeforeSuite
	public void startTest() {

		Date date = new Date();
		String t = String.format("%tc", date);
		long stpc = date.getTime();
		EndtimeAdd.put("stpc", String.valueOf(stpc));
		StrttimeAdd = t;
		Report.myReport();
		Report.log("initiallization of testing");
	}
	
	@Test(priority = 1, dataProviderClass = FormData.class,dataProvider = "ParameterData")
	public static void openURL(LinkedHashMap<String, String> data){
		String APP_URL = data.get("APP_URL");
		Execute.openURL(APP_URL);
		//int s = Report.getVerifyStatus("LoginToApplication");
		//System.out.println(s);
	}
	
	
		
		
	
}
