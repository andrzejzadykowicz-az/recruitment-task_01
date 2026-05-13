import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.awaitility.Awaitility.await;
import java.time.Duration;

public class RecruitmentTest {
    WebDriver driver;

    @BeforeMethod
    public void setup() {
        // WebDriverManager automatycznie znajdzie Twoją wersję 148.x
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Niezbędne w Codespaces
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        
        driver = new ChromeDriver(options);
    }

    @Test
    public void testGoogle() {
        driver.get("https://www.google.com");
        System.out.println("Tytuł strony: " + driver.getTitle());
        Assert.assertTrue(driver.getTitle().contains("Google"));
    }

    @Test
    public void googleTitleTest() {
        driver.get("https://www.google.com");
        
        // Przykład użycia Awaitility - czekamy aż tytuł będzie zawierał "Google"
        await("Czekam na tytuł strony")
            .atMost(Duration.ofSeconds(5))
            .until(() -> driver.getTitle().contains("Google"));
            
        System.out.println("Test Selenium zaliczony! Tytuł to: " + driver.getTitle());
        Assert.assertTrue(driver.getTitle().contains("Google"));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}