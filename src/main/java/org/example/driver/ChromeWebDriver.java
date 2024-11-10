package org.example.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.сonfigReader.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class ChromeWebDriver {

    public static WebDriver loadChromeWebDriver(){
    WebDriverManager.chromedriver().setup();
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--remote-allow-origins=*");
    options.addArguments("--disable-extensions");
    options.addArguments("--window-size-1920,1080");
    options.addArguments("--no-sandbox");
    if(Boolean.parseBoolean(ConfigReader.getValues("headless"))){
        options.addArguments("--headless");
    }
    WebDriver driver = new ChromeDriver(options);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(7));
    driver.manage().window().maximize();
    return driver;
    }
}
