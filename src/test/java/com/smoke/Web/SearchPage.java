package com.smoke.Web;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import controllers.BaseMethod;

public class SearchPage extends BaseMethod {

	public SearchPage(){
		PageFactory.initElements(getWebDriver(), this);
	}
	
	@FindBy(id= "searchDropdownBox")
	WebElement select_categories;
	
	@FindBy(id= "twotabsearchtextbox")
	WebElement input_search;
	@FindBy(id= "nav-search-submit-button")
	WebElement btn_search;
	@FindBys( { @FindBy (xpath= "//div[contains(@class,'AdHolder ')][last()]/following-sibling::div//h2/a"),})
	List<WebElement> all_items;
	@FindBy(id= "add-to-cart-button")
	WebElement btn_addToCart;
	@FindBy(xpath= "//h1[contains(text(),'Added to Cart')]")
	WebElement txt_addedToCart;
	
	public boolean searchItem(String search_category, String search_item) {
		
		selectByVisibleText(select_categories, search_category, "select category as"+search_category );
		inputText(input_search, search_item,"Enter " +search_item+ "in search filed");
		click(btn_search, "Click on search button");
		return true;
	}
	
	public boolean addToCart() {
		
		String parent_window = getWebDriver().getWindowHandle();
		click(all_items.get(0), "Click on search item");
		Set<String > all_window = getWebDriver().getWindowHandles();
		all_window.remove(parent_window);
		for(String window: all_window) {
			
			getWebDriver().switchTo().window(window);
			
			click(btn_addToCart, "Click on Add To Cart button");
			isWebElementVisible(txt_addedToCart, " Added item to cart");
			getWebDriver().switchTo().defaultContent();
		}
		getWebDriver().switchTo().window(parent_window);
		
		return true;
	}
}
