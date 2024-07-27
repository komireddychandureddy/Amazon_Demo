package utils;

import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

/**
 * @Author Chandu
 * @Date 15-Nov-2018
 */	 
	//OB: ExtentReports extent instance created here. That instance can be reachable by getReporter() method.
	 
	public class ExtentManager {
	 
	    public static ExtentReports extent;
	   // public ExtentTest test;
	    static ExtentSparkReporter spark;
	    //Map extentTestMap = new HashMap();
	    public  static ThreadLocal<ExtentTest>  extent_test = new ThreadLocal<ExtentTest>();

	 
	    public static ExtentReports getReporter(){
	        //if(extent_test == null){
	            //Set HTML reporting file location
	            String workingDir = System.getProperty("user.dir");
	            extent = new ExtentReports();
	            spark = new ExtentSparkReporter(workingDir+ ConfigReader.getValue("HtmlReportFullPath"));
	            extent.attachReporter(spark);
	            extent.setSystemInfo("HostName", " Windows PC");
	            extent.setSystemInfo("Author", "Chandu");
	            extent.setSystemInfo("ProjectName", "Amazon Demo");
	            extent.setSystemInfo("Enivorment", "QA");
	            
	            
	       // }
	        return extent;
	    }
	
		/*
		 * public synchronized ExtentTest getTest() { return this.test; } public
		 * synchronized void setTest(ExtentTest test) { this.test =test; }
		 */
	    
	    public void createTest(String testcaseName) {
			  
			 //test= extent.createTest(testcaseName);
			 this.setExtentTest(extent.createTest(testcaseName));
		      
		    }
	 
	  public void createTest(String testcaseName,String testcaseDescritption)
		  {
		  
		  //test= extent.createTest(testcaseName, testcaseDescritption); 
		  this.setExtentTest(extent.createTest(testcaseName, testcaseDescritption));
		  }
	    public ExtentTest getExtentTest() {
	        return extent_test.get();
	    }
	    public void  setExtentTest(ExtentTest test) {
	    	extent_test.set(test);
	    	
	    }
	    
	 
	    public void endTest() {
	        extent.flush();
	    }
	 
		/*
		 * public synchronized void stepInfo(String stepName) { test.log(Status.INFO,
		 * "Test Info : "+stepName); } public void stepSkip(String stepName) {
		 * test.log(Status.SKIP, "Test Skipped : "+stepName); } public void
		 * stepPass(String stepName) { test.log(Status.PASS, "Test Pass : "+stepName); }
		 * public void stepFail(String stepName) { test.log(Status.FAIL,
		 * "Test Fail : "+stepName); } public void stepFail(String stepName, Media
		 * screenshotPath ) { test.log(Status.FAIL, "Test Fail : "+stepName,
		 * screenshotPath);
		 * 
		 * }
		 * 
		 */		/*
		 * public void stepError(String stepName) {
		 * ExtentTestManager.getTest().log(Status.ERROR, "Test Error : "+stepName); }
		 */
	  public void stepWarning(String stepName) {
		  getExtentTest().log(Status.WARNING, "Test Warning : "+stepName);
	}

	  
	  public void stepInfo(String stepName) {
		  getExtentTest().log(Status.INFO, "Test Info : "+stepName);
    }
    public void stepSkip(String stepName) {
    	getExtentTest().log(Status.SKIP, "Test Skipped : "+stepName);
  }
    public void stepPass(String stepName) {
    	getExtentTest().log(Status.PASS, "Test Pass : "+stepName);
  }
    public void stepFail(String stepName) {
    	getExtentTest().log(Status.FAIL, "Test Fail : "+stepName);
  }
    public void stepFail(String stepName, String screenshotPath ) {
    	getExtentTest().log(Status.FAIL, "Test Fail : "+stepName, MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotPath).build());
	  	
	  }

	/*
	 * public void stepError(String stepName) {
	 * ExtentTestManager.getTest().log(Status.ERROR, "Test Error : "+stepName); }
	 */
	/*
	 * public void stepWarning(String stepName) { test.log(Status.WARNING,
	 * "Test Warning : "+stepName); }
	 */
	/*
	 * public ExtentTest createTest(String testcaseName,String testcaseDescritption)
	 * {
	 * 
	 * test= extent.createTest(testcaseName, testcaseDescritption); setTest(test);
	 * return test; }
	
	  public ExtentTest createTest(String testcaseName) {
		  
			 test= extent.createTest(testcaseName);
			 setTest(test);
		        return test;
		    }
	   */
	
	  
	  
}
