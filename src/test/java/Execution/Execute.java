package Execution;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.text.Document;

import org.apache.tools.ant.types.Commandline.Argument;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Report.Report;
import common.Constants;
import init.initDriver;;

public class Execute {

	public static initDriver c = new initDriver();
	public static WebDriver driver = c.initDriver();
	static Actions action = new Actions(driver);
	static JavascriptExecutor js = ((JavascriptExecutor) driver);
	static WebDriverWait wait = new WebDriverWait(driver, 100);
	public static LinkedHashMap<String,String> windows = new LinkedHashMap<String,String>();
	public static String mainWindow = null;
	public static String secondWindow = null;
	public static String previewWindow = null;

	public static void openURL(String APP_URL) {

		driver.get(APP_URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		mainWindow = driver.getWindowHandle();
	}

	public static boolean executeVerifyKeyword(String verifyKey, LinkedHashMap<String, String> data) {

		boolean status = false;
		Method myMethod = null;
		Class<?> c = null;
		try {
			c = Class.forName("Execution.verifyPoints");
			Object createInstance = c.newInstance();
			Method[] h = c.getMethods();

			for (Method r : h) {
				if (r.getName().equalsIgnoreCase(verifyKey)) {
					int a = r.getParameterCount();

					if (a > 0) {
						myMethod = c.getDeclaredMethod(verifyKey, LinkedHashMap.class);
						status = (Boolean) myMethod.invoke(createInstance, data);
					} else {
						myMethod = c.getDeclaredMethod(verifyKey);
						status = (Boolean) myMethod.invoke(createInstance);
					}
				}
			}

		} catch (Exception e) {
			Report.log("unable to run keyword");

		}

		if (status) {
			return true;
		} else {
			return false;
		}

	}
	
	public static boolean executeV(String tckey,LinkedHashMap<String, String> data){
		LinkedHashMap<String,String> vReports = new LinkedHashMap<String,String>();
		int cc=0,ff=0;
		String srNo = data.get("SRNO");
		if(Report.isVerify(tckey))
		{
			Report.addVerifyPoints(tckey,srNo);
			LinkedHashMap<String,String> vKeys=Report.getVerifyStatus(tckey);
			try{
				
				for(Map.Entry m:vKeys.entrySet()){
					
					String vk = m.getKey().toString();
					boolean status =executeVerifyKeyword(vk,data);
					if(status){
						Report.updateVerifyStatus(tckey, vk, "PASS",srNo);
						vReports.put(vk, "PASS");
					}else{
						
						Report.updateVerifyStatus(tckey, vk, "FAIL",srNo);
						vReports.put(vk, "FAIL");
					}
				}
				
			}catch(Exception e){
				Report.log(e.getMessage());
				
			}
			finally{
				
				for(Map.Entry c:vReports.entrySet()){
					
					String statusC = c.getValue().toString();
					if(statusC.equalsIgnoreCase("PASS")){
						cc++;
					}
					if(statusC.equalsIgnoreCase("FAIL")){
						ff++;
					}
				}
				if(ff>0){
					
					data.replace("STATUS", "FAIL");
					data.replace("COMMENT", "Verify points fail");
					String screenshotName = tckey + "_" + srNo;
					data.put("SCREENSHOT", screenshotName);
					Report.takeScreenShot(driver,screenshotName);
					
				}
				
				else{
					
					data.replace("STATUS", "PASS");
					data.replace("COMMENT", "Verify points pass");
					String screenshotName = tckey + "_" + srNo;
					data.put("SCREENSHOT", screenshotName);
					Report.takeScreenShot(driver,screenshotName);
				}
				
				Report.updateFormStatus(tckey, data);
				
			}
			
		}
		if(ff>0){
			return false;
		}
		else {
			return true;
		}
		
	}

	public static boolean LoginToApplication(LinkedHashMap<String, String> data) {
		try {
			String tcID = Report.testCaseID("Login", "LoginToApplication");
			Login(data.get("USER_ID"),data.get("PASSWORD"));
			if(executeV("LoginToApplication",data)){
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			Report.takeScreenShot(driver,"LoginError");
			Report.error(e.getMessage());
			return false;
		}
	}
	
	/** ----------------------------------- Reusable Functions ---------------------------------------- **/

	/* Is element present function */
	public static boolean isElementPresent(By by, String msg) {
		try {
			driver.findElement(by);
			Report.log(msg);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/* Wait for page to load function */
	public static void waitForPageToLoad() {
		String pageLoadStatus;
		JavascriptExecutor js;
		do {
			js = (JavascriptExecutor) driver;
			pageLoadStatus = (String) js.executeScript("return document.readyState");
			try {
				Thread.sleep(4000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!pageLoadStatus.equals("complete"));
	}

	/* Is element visible method */
	public static boolean elementvisible(WebElement element, String msg) {
		try {
			boolean status = element.isDisplayed();
			Report.log(msg + "is visible");
			return status;
		} catch (NoSuchElementException e) {
			Report.log(msg + "is not visible");
			return false;
		}
	}

	/* Wait till element visible function */
	public static void waittillelementvisible(By by) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	/* For Logging in */
	public static void Login(String Uname, String Pwd) {
		driver.findElement(By.id("idUser")).clear();
		driver.findElement(By.id("idUser")).sendKeys(Uname);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(Pwd);
		driver.findElement(By.id("signInBtn")).click();
		waitForPageToLoad();
	}

	/* For Log Out */
	public static void LogOut() throws InterruptedException {
		driver.findElement(By.xpath("//button[contains(@class,'dummyLogout')]")).click();
		Thread.sleep(2000);
	}

	/* For handling the alert */
	public static String closeAlertAndGetItsText() {
		boolean acceptNextAlert = true;
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
				Report.log(alertText);
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
	
	/* For close other windows */
	
	public static void closeOtherWindows() {
		Set<String> allW = driver.getWindowHandles();
		int numOfWindows = allW.size();
		if(numOfWindows>1){
			allW.remove(mainWindow);
			Iterator<String> windows = allW.iterator();
			while(windows.hasNext()){
				driver.switchTo().window(windows.next());
				driver.close();
			}
		}
		driver.switchTo().window(mainWindow);
	}
}
