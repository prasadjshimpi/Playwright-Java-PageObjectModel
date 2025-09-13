# Playwright Java Page Object Model Framework Documentation

## Overview

This is a comprehensive test automation framework built using **Microsoft Playwright** with **Java**, following the **Page Object Model (POM)** design pattern. The framework is designed for testing the OpenCart e-commerce application and includes features like parallel execution, cross-browser testing, extent reporting, and CI/CD integration.

## Framework Architecture

```
Playwright-Java-PageObjectModel/
├── src/
│   ├── main/java/com/qa/opencart/
│   │   ├── constants/          # Application constants
│   │   ├── factory/            # Browser factory and utilities
│   │   ├── listeners/          # TestNG listeners for reporting
│   │   └── pages/              # Page Object classes
│   └── test/java/com/qa/opencart/
│       ├── base/               # Base test class
│       ├── tests/              # Test classes
│       └── examples/           # Example test classes
├── src/test/resources/
│   ├── config/                 # Configuration files
│   └── testrunners/            # TestNG suite files
├── target/                     # Maven build directory
├── test-output/                # TestNG output
├── screenshot/                 # Screenshots directory
├── build/                      # Extent reports output
└── pom.xml                     # Maven configuration
```

## Key Components

### 1. Dependencies (pom.xml)

The framework uses the following key dependencies:

- **Playwright**: `1.52.0` - Microsoft Playwright for browser automation
- **TestNG**: `7.11.0` - Testing framework for test execution
- **ExtentReports**: `5.1.2` - Advanced reporting capabilities
- **Selenium**: `4.32.0` - Additional WebDriver utilities (if needed)

### Maven Configuration Features:
- **Parallel Execution**: Configured with `forkCount=3` and `reuseForks=true`
- **Java 8 Compatibility**: Source and target set to Java 1.8
- **Suite Integration**: Points to TestNG regression suite by default

### 2. PlaywrightFactory (Browser Management)

**Location**: `src/main/java/com/qa/opencart/factory/PlaywrightFactory.java`

This is the core factory class responsible for browser initialization and management.

#### Key Features:
- **Thread-Safe Design**: Uses `ThreadLocal` for parallel execution
- **Multi-Browser Support**: Supports Chromium, Firefox, Safari, Chrome, and Edge
- **Configuration-Driven**: Browser selection via properties file
- **Screenshot Capability**: Built-in screenshot functionality

#### Supported Browsers:
```java
- chromium (Playwright's Chromium)
- firefox (Playwright's Firefox)  
- safari (Playwright's WebKit)
- chrome (Google Chrome)
- edge (Microsoft Edge)
```

#### Thread-Safe Implementation:
```java
private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
private static ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
private static ThreadLocal<Page> tlPage = new ThreadLocal<>();
private static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
```

#### Screenshot Feature:
- Captures full-page screenshots
- Saves to `/screenshot/` directory with timestamp
- Returns Base64 encoded string for reporting integration

### 3. Configuration Management

**Location**: `src/test/resources/config/config.properties`

#### Configuration Properties:
```properties
browser = chrome                                    # Browser selection
url = https://naveenautomationlabs.com/opencart/   # Application URL
username = prasadjs86@gmail.com                     # Test credentials
password = Selenium@12345                           # Test credentials
headless = false                                    # Headless mode setting
```

### 4. Page Object Model Implementation

The framework follows a clean Page Object Model pattern with three main components:

#### A. Page Object Structure:
1. **Locators** - String-based selectors (CSS, XPath)
2. **Constructor** - Page initialization with Playwright Page object
3. **Methods** - Page-specific actions and validations

#### B. HomePage Class
**Location**: `src/main/java/com/qa/opencart/pages/HomePage.java`

**Features**:
- Title and URL validation methods
- Product search functionality
- Navigation to login page
- Clean locator management using CSS and text-based selectors

**Key Methods**:
```java
public String getHomePageTitle()                    # Get page title
public String getHomePageURL()                      # Get current URL
public String doSearch(String productName)         # Perform product search
public LoginPage navigateToLoginPage()             # Navigate to login page
```

#### C. LoginPage Class
**Location**: `src/main/java/com/qa/opencart/pages/LoginPage.java`

**Features**:
- Login functionality with credential validation
- Forgot password link verification
- XPath-based locators for form elements
- Success/failure validation with logout link check

**Key Methods**:
```java
public String getLoginPageTitle()                   # Get login page title
public boolean isForgotPwdLinkExist()              # Check forgot password link
public boolean doLogin(String username, String password)  # Perform login
```

### 5. Base Test Class

**Location**: `src/test/java/com/qa/opencart/base/BaseTest.java`

This class provides the foundation for all test classes:

#### Features:
- **Browser Parameter Support**: Accepts browser parameter from TestNG
- **Property Management**: Loads configuration properties
- **Page Object Initialization**: Creates page object instances
- **Setup & Teardown**: Handles browser lifecycle

#### Key Functionality:
```java
@Parameters({"browser"})
@BeforeTest
public void setup(String browserName)              # Test setup with browser parameter

@AfterTest  
public void tearDown()                              # Browser cleanup
```

### 6. Test Implementation

#### A. HomePageTest
**Location**: `src/test/java/com/qa/opencart/tests/HomePageTest.java`

**Test Cases**:
- `homePageTitleTest()` - Validates home page title
- `homePageURLTest()` - Validates current URL
- `searchTest()` - Data-driven product search testing

**Data Provider Example**:
```java
@DataProvider
public Object[][] getProductData() {
    return new Object[][] {
        {"Macbook"}, {"iMac"}, {"Samsung"}
    };
}
```

#### B. LoginPageTest  
**Location**: `src/test/java/com/qa/opencart/tests/LoginPageTest.java`

**Test Cases**:
- `loginPageNavigationTest()` - Navigation validation
- `forgotPwdLinkExistTest()` - UI element verification
- `appLoginTest()` - End-to-end login functionality

**Test Execution Priority**:
```java
@Test(priority = 1) // Navigation test
@Test(priority = 2) // UI validation  
@Test(priority = 3) // Login functionality
```

### 7. Constants Management

**Location**: `src/main/java/com/qa/opencart/constants/AppConstants.java`

Centralized storage for application constants:
```java
public static final String HOME_PAGE_TITLE = "Your Store";
public static final String LOGIN_PAGE_TITLE = "Account Login";
```

### 8. Extent Reporting System

**Location**: `src/main/java/com/qa/opencart/listeners/ExtentReportListener.java`

#### Features:
- **TestNG Listener Integration**: Implements `ITestListener`
- **Thread-Safe Reporting**: Uses `ThreadLocal<ExtentTest>`
- **Automatic Screenshots**: Captures screenshots on test events
- **Rich HTML Reports**: Generates detailed HTML reports

#### Report Configuration:
- **Output Directory**: `./build/`
- **Report File**: `TestExecutionReport.html`
- **Report Title**: "Open Cart Automation Test Results"

#### Screenshot Integration:
- Captures screenshots on test pass, fail, and skip
- Embeds Base64 screenshots directly in reports
- Links screenshots to specific test methods

### 9. TestNG Suite Configuration

#### A. Regression Suite
**Location**: `src/test/resources/testrunners/testng_regressions.xml`

**Features**:
- **Parallel Execution**: `parallel="tests"` with `thread-count="5"`
- **Cross-Browser Testing**: Different browsers for different test classes
- **Extent Report Integration**: Configured listener
- **Parameterized Execution**: Browser parameters per test

#### B. Load Testing Suite
**Location**: `src/test/resources/testrunners/testng_suite.xml`

**Features**:
- **High Concurrency**: 32 parallel test executions
- **Load Testing**: Multiple instances of the same test classes
- **Thread Management**: `thread-count="4"` with `parallel="tests"`

### 10. CI/CD Integration (Jenkins)

**Location**: `Jenkinsfile`

#### Pipeline Stages:

1. **Build Stage**:
   - Maven clean package
   - JUnit report generation
   - Artifact archival

2. **QA Deployment**:
   - Deployment to QA environment

3. **Regression Testing**:
   - Executes regression test suite
   - Uses Maven Surefire plugin
   - Catches errors without failing pipeline

4. **Report Publishing**:
   - Publishes HTML Extent reports
   - Integrates with Jenkins HTML Publisher

#### Pipeline Configuration:
```groovy
tools {
    maven 'maven'
}

stages {
    stage('Regression Automation Test') {
        sh "mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/testng_regressions.xml"
    }
    
    stage('Publish Extent Report') {
        publishHTML([
            reportDir: 'build',
            reportFiles: 'TestExecutionReport.html',
            reportName: 'HTML Extent Report'
        ])
    }
}
```

## Framework Benefits

### 1. **Scalability**
- Thread-safe design supports parallel execution
- Page Object Model enables easy maintenance
- Modular architecture allows easy extension

### 2. **Cross-Browser Compatibility**
- Supports 5 different browsers
- Parameterized browser selection
- Consistent behavior across browsers

### 3. **Robust Reporting**
- Detailed HTML reports with screenshots
- Test execution metrics and trends
- Integration with CI/CD pipelines

### 4. **Maintainability**
- Clear separation of concerns
- Centralized configuration management
- Reusable page objects and utilities

### 5. **CI/CD Ready**
- Jenkins pipeline integration
- Maven-based build system
- Automated report generation

## Getting Started

### Prerequisites
- Java 8 or higher
- Maven 3.6+
- Git

### Setup Instructions

1. **Clone Repository**:
   ```bash
   git clone <repository-url>
   cd Playwright-Java-PageObjectModel
   ```

2. **Install Dependencies**:
   ```bash
   mvn clean install
   ```

3. **Install Playwright Browsers**:
   ```bash
   mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
   ```

4. **Run Tests**:
   ```bash
   # Run regression suite
   mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/testng_regressions.xml
   
   # Run with specific browser
   mvn clean test -Dbrowser=firefox
   ```

### Configuration Customization

1. **Update Application URL**:
   Edit `src/test/resources/config/config.properties`
   ```properties
   url = https://your-application-url.com
   ```

2. **Add New Browser**:
   Update `PlaywrightFactory.java` switch statement
   ```java
   case "newbrowser":
       tlBrowser.set(getPlaywright().chromium().launch(...));
       break;
   ```

3. **Create New Page Object**:
   ```java
   public class NewPage {
       private Page page;
       
       // Locators
       private String elementLocator = "selector";
       
       // Constructor
       public NewPage(Page page) {
           this.page = page;
       }
       
       // Methods
       public void performAction() {
           page.click(elementLocator);
       }
   }
   ```

## Best Practices

### 1. **Locator Strategy**
- Prefer CSS selectors over XPath when possible
- Use text-based selectors for better readability
- Keep locators as private strings in page classes

### 2. **Wait Strategy**
- Use Playwright's built-in waiting mechanisms
- Implement explicit waits for dynamic content
- Avoid Thread.sleep() except for debugging

### 3. **Test Data Management**
- Use TestNG DataProviders for test data
- Store sensitive data in properties files
- Consider external data sources for large datasets

### 4. **Error Handling**
- Implement proper exception handling
- Use assertions for test validations
- Capture screenshots on failures

### 5. **Parallel Execution**
- Design tests to be independent
- Avoid shared test data
- Use ThreadLocal for thread safety

## Troubleshooting

### Common Issues

1. **Browser Launch Failures**:
   - Ensure Playwright browsers are installed
   - Check browser compatibility with OS
   - Verify headless mode settings

2. **Test Failures**:
   - Check application availability
   - Verify locator stability
   - Review extent reports for details

3. **Parallel Execution Issues**:
   - Ensure test independence
   - Check thread-safety of shared resources
   - Verify ThreadLocal usage

### Debug Tips
- Enable Playwright debug mode: `DEBUG=pw:api`
- Use browser developer tools for locator validation
- Check extent reports for detailed failure analysis
- Review Jenkins console output for CI/CD issues

## Conclusion

This Playwright Java Page Object Model framework provides a robust, scalable, and maintainable solution for web application testing. With its comprehensive reporting, cross-browser support, and CI/CD integration, it serves as an excellent foundation for enterprise-level test automation projects. 