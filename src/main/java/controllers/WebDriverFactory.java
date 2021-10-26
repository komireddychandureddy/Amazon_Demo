/**
 * 
 */
package controllers;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import utils.ConfigReader;
import utils.LogUtil;

/**
 * @Author Chandu
 * @Date 15-Nov-2018
 */
public class WebDriverFactory extends BrowserFactory
{
	public static ThreadLocal<WebDriver> wd = new ThreadLocal<WebDriver>();

	/*@Parameters({ "browser" , "url" })
	@BeforeMethod
	public void beforeMethod(String browser, String url) throws Exception
	{		
		//InitMethod.Browser=browser;
		//InitMethod.WebsiteURL=url;
		InitMethod.Browser=ConfigReader.getValue("Browser");
		InitMethod.WebsiteURL=ConfigReader.getValue("BASE_URL");
		new WebDriverFactory();
		WebDriver driver = WebDriverFactory.createDriver(browser,url);
		setWebDriver(driver);
	}*/
	
		@BeforeMethod
		public void beforeMethod() throws Exception
		{		
			InitMethod.Browser=ConfigReader.getValue("Browser");
			InitMethod.WebsiteURL=ConfigReader.getValue("BASE_URL");
			LogUtil.infoLog(getClass(), "Browser: "+Browser+"  URL: "+WebsiteURL);
			new WebDriverFactory();
			WebDriver driver = WebDriverFactory.createDriver(Browser,WebsiteURL);
			setWebDriver(driver);
		}

	public void setWebDriver(WebDriver driver) 
	{
		wd.set(driver);
	}

	/*public static WebDriver getWebDriver() 
	{
		return wd.get();
	}*/
	public WebDriver getWebDriver() 
	{
		return wd.get();
	}


	@AfterMethod
	public void afterMethod() 
	{
		//getWebDriver().close();	
	}


}
