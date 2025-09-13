package com.qa.opencart.pages;

import com.microsoft.playwright.Page;

public class LoginPage {

	private Page page;

	// 1. String Locators - OR
	private String emailId = "//input[@id='input-email']";
	private String password = "//input[@id='input-password']";
	private String loginBtn = "//input[@value='Login']";
	private String forgotPwdLink = "//div[@class='form-group']//a[normalize-space()='Forgotten Password']";
	private String logoutLink = "//a[@class='list-group-item'][normalize-space()='Logout']";
	private String myAccountLink = "//a[@title='My Account']";
	private String accountPageHeader = "//h2[text()='My Account']";
	private String errorMessage = "//div[@class='alert alert-danger alert-dismissible']";

	// 2. page constructor:
	public LoginPage(Page page) {
		this.page = page;
	}
	
	// 3. page actions/methods:
	public String getLoginPageTitle() {
		return page.title();
	}
	
	public boolean isForgotPwdLinkExist() {
		return page.isVisible(forgotPwdLink);
	}
	
	public boolean doLogin(String appUserName, String appPassword) {
		System.out.println("App creds: " + appUserName + ":" + appPassword);
		
		try {
			// Fill login credentials
			page.fill(emailId, appUserName);
			page.fill(password, appPassword);
			
			// Click login button
			page.click(loginBtn);
			
			// Wait for page to load after login attempt
			page.waitForLoadState();
			
			// Check if login was successful by looking for multiple indicators
			// First, check if we're redirected to account page
			if (page.locator(accountPageHeader).isVisible()) {
				System.out.println("Login successful - Account page header found");
				return true;
			}
			
			// Alternative check: Look for My Account link in header (logged in state)
			if (page.locator(myAccountLink).isVisible()) {
				System.out.println("Login successful - My Account link found");
				return true;
			}
			
			// Try to wait for logout link with a shorter timeout
			try {
				page.locator(logoutLink).waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(10000));
				if (page.locator(logoutLink).isVisible()) {
					System.out.println("Login successful - Logout link found");
					return true;
				}
			} catch (Exception e) {
				System.out.println("Logout link not found within timeout, checking for error messages...");
			}
			
			// Check for login error messages
			if (page.locator(errorMessage).isVisible()) {
				String errorText = page.locator(errorMessage).textContent();
				System.out.println("Login failed with error: " + errorText);
				return false;
			}
			
			// If none of the success indicators are found, login likely failed
			System.out.println("Login failed - No success indicators found");
			return false;
			
		} catch (Exception e) {
			System.out.println("Login failed with exception: " + e.getMessage());
			return false;
		}
	}
}
