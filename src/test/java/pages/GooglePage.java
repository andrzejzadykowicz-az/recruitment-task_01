package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class GooglePage {
    WebDriver driver;
    WebDriverWait wait;

    private By searchBox = By.name("q");

    public GooglePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void acceptCookies() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            // Szukamy przycisku, który zawiera tekst "Zaakceptuj"
            WebElement acceptBtn = shortWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Zaakceptuj') or contains(.,'Accept') or contains(.,'I agree')]")));
            acceptBtn.click();
        } catch (Exception e) {
            System.out.println("Info: Przycisk cookies nie wymagał kliknięcia.");
        }
    }

    public void searchFor(String text) {
    // 1. Czekamy na obecność elementu w kodzie strony
    WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(searchBox));
    
    // 2. Używamy JavaScriptu, żeby wpisać tekst, nawet jeśli coś zasłania pole
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("arguments[0].value = arguments[1];", element, text);
    
    // 3. Wysyłamy Enter (submit)
    element.submit();
}
}