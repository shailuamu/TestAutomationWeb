package bonzai;

import java.util.LinkedHashMap;
import org.testng.annotations.Test;
import DataProvider.FormData;
import Execution.Execute;
import Report.Report;
import common.Constants;

public class Login {


	@Test(priority = 1, dataProviderClass = FormData.class, dataProvider = "MethodName")
	public void LoginToApplication(LinkedHashMap<String, String> data) {
		

		String tcID = Report.testCaseID("Login", "LoginToApplication");
		if (Report.testCaseValidation("Login", "LoginToApplication")) {
			 
			Report.log("Executing test for Login to application '");

			if (data.get("EXCECUTE_FLAG").toString().trim().equalsIgnoreCase("Y")) {
				Report.log("Application is running for " + data.get("USER_ID"));
				Execute.LoginToApplication(data);
				
			} 
		} else {
			Report.updateTestCaseStatus(tcID, Constants.NOT_RUN, "Login functionality testing flag in No", "");
			Report.info("Execution flag is not enabled for test case of SrNO :" + data.get("SRNO"));
		}

	}
	
	
	
}
