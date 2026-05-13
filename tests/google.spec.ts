import { test, expect } from '@playwright/test';
import { GooglePage } from './pages/GooglePage';

test('użytkownik może wyszukiwać frazy w google', async ({ page }) => {
    const google = new GooglePage(page);

    await google.goto();
    await google.acceptCookies();
    await google.searchFor('Playwright vs Selenium');

    // Asercja w Playwright automatycznie ponawia próbę (retry), aż warunek zostanie spełniony
    await expect(page).toHaveURL(/.*search.*/);
    await expect(page).toHaveTitle(/.*Playwright.*/);

    // Pobieramy tytuły i wypisujemy je
    const results = await google.getSearchResultTitles();

    if (results.length === 0) {
        // Robimy zdjęcie - to najważniejsze!
        await page.screenshot({ path: 'google_result.png' });
        
        // Pobieramy tytuł strony - jeśli to "Captcha" lub "403 Forbidden", będziemy wiedzieć
        const pageTitle = await page.title();
        console.log("TYTUŁ STRONY:", pageTitle);
        
        // Wypisujemy początek kodu strony, żeby zobaczyć co tam jest
        const content = await page.content();
        console.log("FRAGMENT HTML:", content.substring(0, 500));
    }
});