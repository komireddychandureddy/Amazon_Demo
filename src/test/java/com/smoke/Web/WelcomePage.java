package com.smoke.Web;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import controllers.BaseMethod;

public class WelcomePage extends BaseMethod{

	
	
	public WelcomePage (){
		PageFactory.initElements( getWebDriver(), this);
	}
	
	@FindBy(id= "nav-link-accountList")
	WebElement link_account;
	@FindBy(id= "createAccountSubmit")
	WebElement btn_createNewAccount;
	
	
	public boolean gotoSignUp() {
		isWebElementVisible(link_account, "Verify Account and list tab");
		click(link_account, "Click on Account");
		click(btn_createNewAccount, "Click on Create Account button");
		return true;
	}
	
	public boolean welcomePage() {
		return	isWebElementVisible(link_account, "Verify Account and list tab in welcome page");	
	}
}
