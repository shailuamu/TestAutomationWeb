package init;

import java.util.LinkedHashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Report.Report;
import common.Constants;

public class initDriver {
	static initDriver c;
	public static WebDriver driver = null;
	
	public WebDriver initDriver(){
		
		LinkedHashMap<String,String> table =Report.impParameter();
		String BROWSER= table.get("BROWSER");
		if(BROWSER.equalsIgnoreCase(Constants.MOZILLA) || BROWSER.equalsIgnoreCase("firefox") ){
			driver = new  FirefoxDriver();
		}
		
		else if(BROWSER.equalsIgnoreCase(Constants.CHROME)){
			 System.setProperty("webdriver.chrome.driver", Constants.CHROME_PATH);
			  driver = new ChromeDriver();
		}
		
		
	//	driver = new HtmlUnitDriver();
		return driver;
		
	}
	
	
	public static initDriver getDriver(){
	if(c==null){
		c = new initDriver();
	}
	return c;
}
	
	
}
