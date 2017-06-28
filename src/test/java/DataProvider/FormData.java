package DataProvider;

import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

import Report.Report;

public class FormData {

	@DataProvider(name = "MethodName")
	public static Object[][] getBrandsData(Method m) {
		String testCaseName = m.getName();
		return Report.getData(testCaseName);
	}

	@DataProvider(name = "ParameterData")
	public static Object[][] importParameters() {
		return Report.importParameter();
	}

	@DataProvider(name = "BrandsDashboardPage")
	public static Object[][] getFormData() {
		String BrandsDashboardPage = "BrandsDashboardPage";
		return Report.getData(BrandsDashboardPage);
	}

	@DataProvider(name = "addNewImage")
	public static Object[][] getImageData() {
		String addNewImage = "addNewImage";
		return Report.getData(addNewImage);
	}

}
