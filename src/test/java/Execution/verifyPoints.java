package Execution;

import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.sun.corba.se.spi.orbutil.fsm.Action;

import Report.Report;
import common.datatable;
import init.initDriver;

public class verifyPoints {
	
	public static WebDriver driver = Execute.driver;
	
	 public static void highlightFail(WebElement element) {
	        for (int i = 0; i <2; i++) {
	            JavascriptExecutor js = (JavascriptExecutor) driver;
	            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: yellow; border: 4px solid red;");
	            }
	        }
	
	 public static void highlightPass(WebElement element) {
	        for (int i = 0; i <2; i++) {
	            JavascriptExecutor js = (JavascriptExecutor) driver;
	            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "border: 4px solid green;");
	            }
	        }
	 
	public static boolean isLogoPresent(){
		if(isElementPresent(By.id("brandLogo"), "Home Page Bonzai Logo is Present"))
		{	
		Report.log("Home Page Bonzai Logo is Present");
		highlightPass(driver.findElement(By.id("brandLogo")));
		return true;
		}
		else 
		{
			Report.log("Home Page Bonzai Logo is not Present");
			highlightFail(driver.findElement(By.id("brandLogo")));
			return false;
		}
	}

	public static boolean isElementPresent(By by, String msg) {
		try {
			driver.findElement(by);
			Report.log(msg);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	
	
	public static boolean verifyTextPresent(String textToVerify, String existOnPage) {

		if (textToVerify.trim().equalsIgnoreCase(existOnPage)) {

			return true;
		} else {
			return false;
		}
	}
	
	
	


	
	
	/* Is element visible method */	
	public static boolean elementvisible(WebElement element, String msg) {
		try {
			boolean status = element.isDisplayed();
			Report.log(msg + "is visible");
			return status;
		} catch (NoSuchElementException e) {
			System.out.println(msg + "is not visible");
			return false;
		}

	}
	
		
	}

