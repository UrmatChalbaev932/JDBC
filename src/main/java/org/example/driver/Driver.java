package org.example.driver;

import org.example.сonfigReader.ConfigReader;
import org.openqa.selenium.WebDriver;

public class Driver {

    private static WebDriver driver;


    private Driver() {
    }

    public WebDriver getDriver(){
        if(driver == null){
            switch (ConfigReader.getValues("browser").toLowerCase()){
                case "chrome":
                    driver = ChromeWebDriver.loadChromeWebDriver();
                    break;
                case "firefox":
                    driver = ui.driver.FireFoxWebDriver.loadFirefoxWebDriver();
                    break;
                default:
                    throw new IllegalArgumentException("You provided wrong Driver name");
            }
        }
        return driver;
    }

    public void closeDriver(){
        try {
        if (driver != null) {
            driver.close();
            driver.quit();
            driver = null;
        }
        } catch (Exception e){
            System.out.println("Error while closing driver");
        }
    }
}
