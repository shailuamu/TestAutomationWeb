package init;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import Report.Report;


public class initLog {
	static initLog t;
	Logger LOGS = null;
	FileAppender appender = new FileAppender();
	
	public Logger initLog(){
		

	
		appender.setFile(Report.LOG_FILE);
		appender.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		appender.setAppend(true);
		appender.activateOptions();
		LOGS = Logger.getLogger("EXCECUTING_TEST");

		LOGS.addAppender(appender);
						
		return LOGS;
		
		}
	
	public static initLog getInstance(){
	if(t==null){
		t = new initLog();
	}
	return t;
}

}
