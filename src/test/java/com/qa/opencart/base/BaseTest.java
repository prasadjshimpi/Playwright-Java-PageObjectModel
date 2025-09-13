package com.qa.opencart.base;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;

//import com.aventstack.chaintest.plugins.ChainTestListener;  // Temporarily disabled
import com.microsoft.playwright.Page;
import com.qa.opencart.factory.PlaywrightFactory;
import com.qa.opencart.pages.HomePage;
import com.qa.opencart.pages.LoginPage;

import io.qameta.allure.Allure;

//@Listeners({ChainTestListener.class})  // Temporarily disabled for testing
public class BaseTest {

	private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
	
	PlaywrightFactory pf;
	Page page;
	protected Properties prop;

	protected HomePage homePage;
	protected LoginPage loginPage;

	@Parameters({ "browser" })
	@BeforeTest
	public void setup(String browserName) {
		logger.info("Setting up test with browser: {}", browserName);
		
		pf = new PlaywrightFactory();
		prop = pf.init_prop();

		if (browserName != null) {
			prop.setProperty("browser", browserName);
		}

		page = pf.initBrowser(prop);
		homePage = new HomePage(page);
		
		logger.info("Test setup completed successfully");
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		if (!result.isSuccess()) {
			logger.error("Test failed: {}", result.getMethod().getMethodName());
			captureScreenshot(result.getMethod().getMethodName());
		}
	}

	@AfterTest
	public void tearDown() {
		logger.info("Tearing down test environment");
		if (page != null && page.context() != null && page.context().browser() != null) {
			page.context().browser().close();
		}
		logger.info("Test teardown completed");
	}

	/**
	 * Capture screenshot for failed tests
	 */
	private void captureScreenshot(String testName) {
		try {
			byte[] screenshot = page.screenshot();
			Allure.addAttachment(testName + "_failure", "image/png", new java.io.ByteArrayInputStream(screenshot), "png");
			logger.info("Screenshot captured for failed test: {}", testName);
		} catch (Exception e) {
			logger.error("Failed to capture screenshot for test: {}", testName, e);
		}
	}

}
