package controllers;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * @Author Chandu
 * @Date 15-Nov-2018
 */

public class BrowserFactory extends InitMethod
{
	static WebDriver createDriver(String browser, String url) throws Exception
	{
		WebDriver driver;

		DesiredCapabilities capabilities;
		switch(browser.toLowerCase())
		{
		case "chrome":
			//WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;
			
		case "chrome_headless":
			//WebDriverManager.chromedriver().setup();
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("--headless");  
			chromeOptions.addArguments("--disable-gpu");  
			driver = new ChromeDriver(chromeOptions);
			break;

		case  "firefox":
			//WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();

			break;

		case  "ie":
			//WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();

			break;	

		case  "edge":
			//WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();

			break;

		/*
		 * case "unit": driver = new HtmlUnitDriver();
		 */
	

		default:
			throw new Exception("Please Provide a Valid Browser");
		}
			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driver.get(url);
		return driver;		
	}
}
