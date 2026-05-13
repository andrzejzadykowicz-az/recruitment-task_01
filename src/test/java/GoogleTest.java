import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import pages.GooglePage; // Importujemy naszą klasę strony

public class GoogleTest {
    WebDriver driver;

    @BeforeMethod
    public void setup() {
        // Konfiguracja managera i opcji przeglądarki
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); 
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @Test
    public void googleSearchTest() {
        // 1. Inicjalizacja Page Objectu
        GooglePage google = new GooglePage(driver);
        
        // 2. Przejście na stronę
        driver.get("https://www.google.com");
        
        // 3. Akcje z wykorzystaniem Page Objectu (czytelne i proste)
        google.acceptCookies();
        google.searchFor("Playwright vs Selenium");
        
        // 4. Asercja
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("search"));
        
        // Dodatkowa weryfikacja - czy URL ma naszą frazę
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("Playwright"), "URL nie zawiera szukanej frazy!");
        
        System.out.println("Test zakończony sukcesem! Obecny adres to: " + currentUrl);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}