package init;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.ITestResult;

import Report.Report;

public class TestNGListener implements ITestListener {

	@Override
	public void onFinish(ITestContext sm) {
		
	}

	@Override
	public void onStart(ITestContext sm) {
		
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult sm) {
		
		long startTime = sm.getStartMillis();
		long endTime = sm.getEndMillis();
		long timeDiff = endTime - startTime ;
		
		Date date = new Date(timeDiff);
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
		String dateFormatted = formatter.format(date);
		
		System.out.println("test name is on TestFailedButWithinSuccessPercentage : " + sm.getName() + " -- it takes time " + dateFormatted);
		
	}

	@Override
	public void onTestFailure(ITestResult sm) {
		
		long startTime = sm.getStartMillis();
		long endTime = sm.getEndMillis();
		long timeDiff = endTime - startTime ;
		String exTime = Report.getExecutionTime(timeDiff);

		System.out.println("time for execution : " + timeDiff);
		System.out.println("test name is onTestSuccess : " + sm.getName() + " -- it takes time " + exTime);
		String tcKey = sm.getName();
		Report.putTestCaseExecutionTime(tcKey, exTime);
		
	}

	@Override
	public void onTestSkipped(ITestResult sm) {
		
		long startTime = sm.getStartMillis();
		long endTime = sm.getEndMillis();
		long timeDiff = endTime - startTime ;
		
		Date date = new Date(timeDiff);
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
		String dateFormatted = formatter.format(date);
		
		System.out.println("test name is onTestSkipped : " + sm.getName() + " -- it takes time " + dateFormatted);
		
	}

	@Override
	public void onTestStart(ITestResult sm) {
		
		long startTime = sm.getStartMillis();
		long endTime = sm.getEndMillis();
		long timeDiff = endTime - startTime ;
		
		Date date = new Date(timeDiff);
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
		String dateFormatted = formatter.format(date);
		
		
	}

	@Override
	public void onTestSuccess(ITestResult sm) {
		
		long startTime = sm.getStartMillis();
		long endTime = sm.getEndMillis();
		long timeDiff = endTime - startTime ;
		String exTime = Report.getExecutionTime(timeDiff);

		System.out.println("time for execution : " + timeDiff);
		System.out.println("test name is onTestSuccess : " + sm.getName() + " -- it takes time " + exTime);
		String tcKey = sm.getName();
		Report.putTestCaseExecutionTime(tcKey, exTime);
		
	}

}
