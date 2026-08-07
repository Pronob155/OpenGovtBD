import asyncio
import re
from playwright import async_api
from playwright.async_api import expect

async def run_test():
    pw = None
    browser = None
    context = None

    try:
        # Start a Playwright session in asynchronous mode
        pw = await async_api.async_playwright().start()

        # Launch a Chromium browser in headless mode with custom arguments
        browser = await pw.chromium.launch(
            headless=True,
            args=[
                "--window-size=1280,720",
                "--disable-dev-shm-usage",
                "--ipc=host",
                "--single-process"
            ],
        )

        # Create a new browser context (like an incognito window)
        context = await browser.new_context()
        # Wider default timeout to match the agent's DOM-stability budget;
        # auto-waiting Playwright APIs (expect, locator.wait_for) inherit this.
        context.set_default_timeout(15000)

        # Open a new page in the browser context
        page = await context.new_page()

        # Interact with the page elements to simulate user flow
        # -> navigate
        await page.goto("http://localhost:8080")
        try:
            await page.wait_for_load_state("domcontentloaded", timeout=5000)
        except Exception:
            pass
        
        # -> Navigate to the Login page (/login) so the citizen can sign in.
        await page.goto("http://localhost:8080/login")
        try:
            await page.wait_for_load_state("domcontentloaded", timeout=5000)
        except Exception:
            pass
        
        # -> Click the "Log in as Citizen" button to submit the citizen demo login form.
        # Log in as Citizen button
        elem = page.get_by_role('button', name='Log in as Citizen', exact=True)
        await elem.click(timeout=10000)
        
        # -> Open the 'Polls' page by clicking the 'Polls' link on the dashboard.
        # how_to_vote Polls link
        elem = page.get_by_role('link', name='how_to_vote Polls', exact=True)
        await elem.click(timeout=10000)
        
        # --> Assertions to verify final state
        
        # --> Verify active polls are displayed
        # Assert: Poll option 'Trade License' is visible.
        await expect(page.locator("xpath=/html/body/div/div[2]/div[2]/div[1]/div/form/div[1]/label/span").nth(0)).to_have_text("Trade License", timeout=15000), "Poll option 'Trade License' is visible."
        # Assert: The 'Cast Vote' button is visible on the poll card.
        await expect(page.locator("xpath=/html/body/div/div[2]/div[2]/div[1]/div/form/button").nth(0)).to_have_text("Cast Vote", timeout=15000), "The 'Cast Vote' button is visible on the poll card."
        # Assert: '6 votes' is displayed indicating an active poll.
        await expect(page.locator("xpath=/html/body/div/div[2]/div[2]/div[1]/div/div[1]/span[2]").nth(0)).to_have_text("6 votes", timeout=15000), "'6 votes' is displayed indicating an active poll."
        
        # --> Verify voting options are available
        # Assert: The 'Trade License' voting option is visible.
        await expect(page.locator("xpath=/html/body/div/div[2]/div[2]/div[1]/div/form/div[1]/label/span").nth(0)).to_have_text("Trade License", timeout=15000), "The 'Trade License' voting option is visible."
        # Assert: The 'Land Record Mutation' voting option is visible.
        await expect(page.locator("xpath=/html/body/div/div[2]/div[2]/div[1]/div/form/div[2]/label/span").nth(0)).to_have_text("Land Record Mutation", timeout=15000), "The 'Land Record Mutation' voting option is visible."
        # Assert: The 'Birth Certificate Correction' voting option is visible.
        await expect(page.locator("xpath=/html/body/div/div[2]/div[2]/div[1]/div/form/div[3]/label/span").nth(0)).to_have_text("Birth Certificate Correction", timeout=15000), "The 'Birth Certificate Correction' voting option is visible."
        # Assert: The 'Police Clearance' voting option is visible.
        await expect(page.locator("xpath=/html/body/div/div[2]/div[2]/div[1]/div/form/div[4]/label/span").nth(0)).to_have_text("Police Clearance", timeout=15000), "The 'Police Clearance' voting option is visible."
        # Assert: The 'Cast Vote' action button is present.
        await expect(page.locator("xpath=/html/body/div/div[2]/div[2]/div[1]/div/form/button").nth(0)).to_have_text("Cast Vote", timeout=15000), "The 'Cast Vote' action button is present."
        await asyncio.sleep(5)

    finally:
        if context:
            await context.close()
        if browser:
            await browser.close()
        if pw:
            await pw.stop()

asyncio.run(run_test())
    
