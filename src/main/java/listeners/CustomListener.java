package listeners;

import java.io.File;
import java.io.IOException;
import javax.mail.MessagingException;

import org.codehaus.plexus.util.ExceptionUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import controllers.InitMethod;
import utils.JiraUtil;
import utils.LogUtil;
import utils.SendMail;
import utils.ConfigReader;
import utils.ExtentManager;


/**
 * @Author Chandu
 * @Date 15-Nov-2018
 */

public class CustomListener extends SendMail implements ITestListener, ISuiteListener, IInvokedMethodListener 
{

	ExtentManager report;
	
	private String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Override
    public void onStart(ITestContext iTestContext) {
    	
    	LogUtil.infoLog(getClass(), iTestContext.getName());
        //System.out.println("I am in onStart method " + iTestContext.getName());
 //       iTestContext.setAttribute("WebDriver", this.getWebDriver());
        
        if(ConfigReader.getValue("JiraManagementTool").equalsIgnoreCase("Y")){
			//Jira Test management config
			JiraUtil.JIRA_CYCLE_ID = ConfigReader.getValue("getJiraCycleID");
			JiraUtil.JIRA_PROJECT_ID = ConfigReader.getValue("getJiraProjectID");
			JiraUtil.ZEPHYR_URL = ConfigReader.getValue("zephyr_url");
			JiraUtil.ZAPI_ACCESS_KEY = ConfigReader.getValue("zapi_access_key");
			JiraUtil.ZAPI_SECRET_KEY = ConfigReader.getValue("zapi_secret_key");
			
			//remaing details will instailized when Jira is selected a bug tracking tool
		}
		
		//setting up of Bug tracking "Jira" tool configuration
		if(ConfigReader.getValue("JiraBugTool").equalsIgnoreCase("Y")){
			JiraUtil.JIRA_URL = ConfigReader.getValue("getbugToolHostName");
			JiraUtil.USERNAME = ConfigReader.getValue("getbugToolUserName");
			JiraUtil.PASSWORD = ConfigReader.getValue("getbugToolPassword");
			JiraUtil.JIRA_PROJECT = ConfigReader.getValue("getbugToolProjectName");
			InitMethod.jiraapi = new JiraUtil();
		}
		
		//report = new ExtentManager();
		extent= ExtentManager.getReporter();
		
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("I am in onFinish method " + iTestContext.getName());
        //Do tier down operations for extentreports reporting!
        endTest();
        //report.getReporter().flush();
        if(!(getWebDriver()==null)){
        	getWebDriver().quit();
        }
      
        String htmlReportFile = System.getProperty("user.dir") +  ConfigReader.getValue("HtmlReportFullPath");
		File f = new File(htmlReportFile);
		if (f.exists()) {

			try {
				Runtime.getRuntime()
						.exec("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe \"" + htmlReportFile
								+ "\"");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		 if (ConfigReader.getValue("sendMail").equalsIgnoreCase("Y"))
				try {
					sendEmailToClient();
				} catch (IOException e1) {
					logStepFail(e1.getMessage());
					e1.printStackTrace();
				} catch (MessagingException e1) {
					logStepFail(e1.getMessage());
					e1.printStackTrace();
				}
				
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
    	LogUtil.infoLog(getClass(), "Testcase started: "+getTestMethodName(iTestResult) );
        
      //  System.out.println("I am in onTestStart method " +  getTestMethodName(iTestResult) + " start");
        //Start operation for extentreports.
    	String description=iTestResult.getMethod().getDescription();
    	createTest(iTestResult.getMethod().getMethodName(), description);
    	//report.setExtentTest(report.createTest(iTestResult.getMethod().getMethodName(), description));
    	//report.setExtentTest(report.test);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
    	String testName = getTestMethodName(iTestResult);
    	//String testName = getTestMethodName(iTestResult).split("_")[1].trim();
    
       // System.out.println("I am in onTestSuccess method " +  getTestMethodName(iTestResult) + " succeed");
        //Extentreports log operation for passed tests.
    	stepPass( "Test passed : "+iTestResult.getMethod().getMethodName());
       
        if(ConfigReader.getValue("JiraManagementTool").equalsIgnoreCase("Y")){
			InitMethod.jiraapi.updateJiraTestResults(testName, "This is Passed", "Pass");
		}

        getWebDriver().close();
        
        
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
    	String testName = getTestMethodName(iTestResult);
    	//String testName = getTestMethodName(iTestResult).split("_")[1].trim();
    	// report.getTest().setEndedTime(new Date());
    	//String testName = scenario.getName().split("_")[0].trim();
      //  System.out.println("I am in onTestFailure method " +  getTestMethodName(iTestResult) + " failed");

        //Get driver from BaseTest and assign to local webdriver variable.
        Object testClass = iTestResult.getInstance();
      //  WebDriver webDriver = ((BaseMethod) testClass).getWebDriver();
        //Take base64Screenshot screenshot.
        String base64Screenshot = "data:image/png;base64,"+((TakesScreenshot)getWebDriver()).
                getScreenshotAs(OutputType.BASE64);

        //Extentreports log and screenshot operations for failed tests.
        ErrorMsg=ExceptionUtils.getFullStackTrace(iTestResult.getThrowable());
        //logStepFail(ErrorMsg);
        LogUtil.infoLog(this.getClass(), "Failed :"+ErrorMsg);
        
        
       // report.stepFail("Test Failed : "+iTestResult.getMethod().getMethodName(), report.getExtentTest().addScreenCaptureFromBase64String(base64Screenshot));
        stepFail("Test Failed : "+iTestResult.getMethod().getMethodName(), base64Screenshot);
        try {
        	String path = null;
            try {
    			 path =takeScreenshot(getWebDriver(), getTestMethodName(iTestResult));
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
        	
		/*	String scFileName = "ScreenShot_"+System.currentTimeMillis();
			String screenshotFilePath = ConfigReader.getValue("screenshotPath")+"\\"+scFileName+".png";
		//	scenario.write("Current Page URL is " + GlobalUtil.getDriver().getCurrentUrl());

			byte[] screenshot = KeywordUtil.takeScreenshot(screenshotFilePath);
			scenario.embed(screenshot, "image/png");
*/
			//report the bug
			String bugID = "Please check the Bug tool Configuration";
			

			if(ConfigReader.getValue("JiraBugTool").equalsIgnoreCase("Y")){
				//getting the os name to report the bug
				String osName = System.getProperty("os.name"); 
				/*if(GlobalUtil.getCommonSettings().getExecutionEnv().equalsIgnoreCase("Remote")){
					osName = GlobalUtil.getCommonSettings().getRemoteOS();
				}*/
				bugID = InitMethod.jiraapi.reporIssue(testName, "Automated on OS: "+osName+",\n Automated on Browser: "+InitMethod.Browser+",\n Build Name: "+ConfigReader.getValue("getBuildNumber")+". \n\n\n\n"+InitMethod.ErrorMsg, path);
			}

			//updating the results in Testmangement tool
			
			if(ConfigReader.getValue("JiraManagementTool").equalsIgnoreCase("Y")){
				InitMethod.jiraapi.updateJiraTestResults(testName, "Please find the BUGID in "+ConfigReader.getValue("BugToolName")+" : "+bugID, "Fail");
			}
			 getWebDriver().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        
        
        
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
       // System.out.println("I am in onTestSkipped method "+  getTestMethodName(iTestResult) + " skipped");
        //Extentreports log operation for skipped tests.
       // ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
    	stepSkip(iTestResult.getMethod().getMethodName());
        getWebDriver().close();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        //System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
    }

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ISuite suite) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ISuite suite) {
		// TODO Auto-generated method stub
		
	}


}
