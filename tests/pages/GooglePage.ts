import { Page, Locator } from '@playwright/test';

export class GooglePage {
    readonly page: Page;
    readonly searchBox: Locator;

    constructor(page: Page) {
        this.page = page;
        // Selektor textarea[name="q"] jest bardzo stabilny
        this.searchBox = page.locator('textarea[name="q"]');
    }

    async goto() {
        await this.page.goto('https://www.google.com');
    }

    async acceptCookies() {
        // Zamiast szukać po tekście "Accept", szukamy przycisku, który ma ID zaczynające się od L2AGLb 
        // (to jest standardowy ID przycisku 'Zaakceptuj wszystko' w Google)
        const acceptBtn = this.page.locator('#L2AGLb');
        
        try {
            if (await acceptBtn.isVisible({ timeout: 5000 })) {
                await acceptBtn.click();
                await acceptBtn.waitFor({ state: 'hidden' });
            }
        } catch (e) {
            console.log("Okno cookies nie wymagało interakcji.");
        }
    }

    async searchFor(text: string) {
        // Zamiast klikać, budujemy adres URL wyszukiwania
        const query = text.replace(' ', '+');
        await this.page.goto(`https://www.google.com/search?q=${query}`);
        
        // Czekamy chwilę, aż strona "osiądzie"
        await this.page.waitForLoadState('networkidle');
    }

    async getSearchResultTitles() {
        // Czekamy na załadowanie czegokolwiek, co przypomina wynik
        await this.page.waitForTimeout(2000); // Mały "oddech" dla strony

        // Szukamy szeroko: h3 LUB zielone linki pod tytułami (cite) LUB główne kontenery wyników
        const selectors = [
            'h3',
            'cite',
            '.LC20lb',
            '[data-testid="result-title"]',
            'a h3'
        ];

        const titles: string[] = [];
        
        for (const selector of selectors) {
            const found = await this.page.locator(selector).allTextContents();
            if (found.length > 0) {
                titles.push(...found);
            }
        }

        return [...new Set(titles)] // Usuwamy duplikaty
            .map(t => t.trim())
            .filter(t => t.length > 2); // Odrzucamy śmieciowe krótkie teksty
    }
}