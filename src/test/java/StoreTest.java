import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoreTest {

    private WebDriver driver;
    Random random = new Random();

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://prod-kurs.coderslab.pl/index.php");                                                         //tested website
    }

    @Test
    public void searchProductTest() {                                                                                   //search engine test
        List<String> products = getStrings();                                                                           //reference to list of products below

        int randomValue = random.nextInt(50);
        int searchedElement = randomValue % products.size();                                                            //randomly generated ordinal number of product from the list

        WebElement searchInput = driver.findElement(By.cssSelector(".ui-autocomplete-input"));                          //search engine
        searchInput.sendKeys(products.get(searchedElement));
        searchInput.submit();

        System.out.println(searchedElement + " " + products.get(searchedElement));                                      //printout of ordinal number and name of the searched product from the list
        List<WebElement> elements = driver.findElements(By.cssSelector(".product-description"));
        for (WebElement element : elements) {
            String elementsDescription = element.getText().toLowerCase();
            Assert.assertTrue(elementsDescription.contains(products.get(searchedElement).toLowerCase()));               //assertion, if search engine found correct products
            System.out.println(elementsDescription);                                                                    //printout descriptions of all products found
        }
    }

    @Test
    public void accountCreator() {                                                                                      //new users registration test using random generated data
        String[] maleName = {"Karol", "Dawid", "Łukasz", "Jacek", "Tomasz", "Fabian"};                                  // \
        String[] maleSurname = {"Kowalski", "Nowak", "Wiśniewski", "Iksiński", "Maleńczuk", "Bondarczuk"};              //  \
        String[] femaleName = {"Karolina", "Monika", "Łucja", "Martyna", "Anna", "Antonina"};                           //   \
        String[] femaleSurname = {"Kowalska", "Nowak", "Wiśniewska", "Iksińska", "Maleńczuk", "Bondarczuk"};            //     example core data - please note, that some surnames in Polish have male and female forms
        String[] addressStreet = {"The Mall", "Fleet", "Old", "Clerk", "Market", "Royal"};                              //   /
        String[] cities = {"London", "Bristol", "Glasgow", "Edinburgh", "Liverpool", "Belfast"};                        //  /
        String[] domain = {"mail.uk", "mail.com", "email.uk", "email.com", "post.com", "post.uk"};                      // /

        driver.findElement(By.id("_desktop_user_info")).click();                                                        //go to Sing in

        for (int i = 0; i < 2; i++) {                                                                                   //generator of multiple random accounts, currently set on 2

            int gender = random.nextInt(2);                                                                       //random integer from 0 to 1 to set random gender
            int randomAddress = random.nextInt(addressStreet.length);                                                   // \
            int randomCities = random.nextInt(cities.length);                                                           //  random integers corresponding to the street, city and domain in the tables above
            int randomDomain = random.nextInt(domain.length);                                                           // /
            int randomPostCode = random.nextInt(2);                                                              //random integer from 0 to 1 for purpose of generation random UK post code
            int randomMaleName = random.nextInt(maleName.length);                                                       // \
            int randomMaleSurname = random.nextInt(maleSurname.length);                                                 //  random integer corresponding to the male name and in the table above
            int randomFemaleName = random.nextInt(femaleName.length);                                                   // \
            int randomFemaleSurname = random.nextInt(femaleSurname.length);                                             //  similar as above for female

            String numericAddress = RandomStringUtils.randomNumeric(1, 3);                                              //random street number, set from 1 to 3 digits
            String numericMail = RandomStringUtils.randomNumeric(2, 6);                                                 //random number from 2 to 6 digits to be used in email address
            String address = addressStreet[randomAddress] + " Street " + numericAddress;                                //generator of full street address including street name and random number
            String maleEmailAddress = StringUtils.stripAccents(maleName[randomMaleName] + "." +                   // \
                    maleSurname[randomMaleSurname] + numericMail + "@" + domain[randomDomain]);                         //  final email address using male name, surname, random number and domain
            String femaleEmailAddress = StringUtils.stripAccents(femaleName[randomFemaleName] + "." +             // \
                    femaleSurname[randomFemaleSurname] + numericMail + "@" + domain[randomDomain]);                     //  similar as above for female

            String[] postCode1 = {RandomStringUtils.randomAlphabetic(1, 2).toUpperCase() +                              // \
                    RandomStringUtils.randomNumeric(1) + RandomStringUtils.randomAlphabetic(1)             //  table with 2 random generators that covers first part of UK post codes in one of formats: "AA9A", "A9A", "A9", "A99", "AA9" or "AA99", where A signifies a letter and 9 a digit
                    .toUpperCase(), RandomStringUtils.randomAlphabetic(1, 2).toUpperCase() +                            //  /
                    RandomStringUtils.randomNumeric(1, 2)};                                                             // /
            String postCode = postCode1[randomPostCode] + " " + RandomStringUtils.randomNumeric(1) +              // \
                    RandomStringUtils.randomAlphabetic(2).toUpperCase();                                          //  generator of final post code, combined with before generated first part and now generated second part; final format of post code is one of: "AA9A 9AA", "A9A 9AA", "A9 9AA", "A99 9AA", "AA9 9AA" or "AA99 9AA", where A signifies a letter and 9 a digit

            driver.findElement(By.cssSelector(".no-account > a")).click();                                              //go to 'create an account'
            driver.findElements(By.name("id_gender")).get(gender).click();                                              //choosing gender accurate to previously generated number - 0 for male and 1 for female
            WebElement name = driver.findElement(By.name("firstname"));                                                 // \
            WebElement surname = driver.findElement(By.name("lastname"));                                               //  variables for WebElements - name, surname and email
            WebElement email = driver.findElement(By.name("email"));                                                    // /

            if (gender == 0) {
                name.sendKeys(maleName[randomMaleName]);                                                                // \
                surname.sendKeys(maleSurname[randomMaleSurname]);                                                       //  input of random male name, surname and email into the form
                email.sendKeys(maleEmailAddress.toLowerCase());                                                         // /
            } else {
                name.sendKeys(femaleName[randomFemaleName]);                                                            // \
                surname.sendKeys(femaleSurname[randomFemaleSurname]);                                                   //  similar as above for female
                email.sendKeys(femaleEmailAddress.toLowerCase());                                                       // /
            }

            driver.findElement(By.name("password")).sendKeys("Pass1234");                                 //input password
            driver.findElement(By.cssSelector(".btn.btn-primary.form-control-submit.float-xs-right")).click();          //submit form
            driver.findElement(By.cssSelector(".account")).click();                                                     //account settings
            driver.findElement(By.id("address-link")).click();                                                          //add new address
            driver.findElement(By.name("address1")).sendKeys(address);                                                  // \
            driver.findElement(By.name("city")).sendKeys(cities[randomCities]);                                         //  input street, city and postcode to form
            driver.findElement(By.name("postcode")).sendKeys(postCode);                                                 // /
            driver.findElement(By.cssSelector(".btn.btn-primary.float-xs-right")).click();                              //submit form

            String details = driver.findElement(By.cssSelector(".address-body")).getText();                             // \
            if (gender == 0) {                                                                                          //  \
                Assert.assertTrue(details.contains(maleName[randomMaleName]));                                          //   \
                Assert.assertTrue(details.contains(maleSurname[randomMaleSurname]));                                    //    \
            }else {                                                                                                     //     \
                Assert.assertTrue(details.contains(femaleName[randomFemaleName]));                                      //      Assertion
                Assert.assertTrue(details.contains(femaleSurname[randomFemaleSurname]));                                //     /
            }                                                                                                           //    /
            Assert.assertTrue(details.contains(address));                                                               //   /
            Assert.assertTrue(details.contains(cities[randomCities]));                                                  //  /
            Assert.assertTrue(details.contains(postCode));                                                              // /

            driver.findElement(By.cssSelector(".logout.hidden-sm-down")).click();                                       //logout
        }
    }

    @Test
    public void filteringTest() {
        driver.findElement(By.id("category-6")).click();
        List<WebElement> checkBoxes = driver.findElements(By.cssSelector(".facet-label"));
        checkBoxes.get(8).click();
        String price = checkBoxes.get(8).getText();


        BigDecimal lowerBound = BigDecimal.valueOf(Double.valueOf(price.substring(1, 6)));
        BigDecimal higherBound = BigDecimal.valueOf(Double.valueOf(price.substring(10, 16)));
        BigDecimal priceFromProduct = BigDecimal
                .valueOf(Double.valueOf(driver.findElement(By.cssSelector(".thumbnail-container"))
                        .getText().split("\\r?\\n")[2].substring(1, 6)));

        if (lowerBound.compareTo(priceFromProduct) < 0 || higherBound.compareTo(priceFromProduct) > 0) {
            System.out.println("Cena mieści się w granicy");
        } else {
            System.out.println("Cena poza granicą!");
        }
    }

    @Test
    public void shopping() {
        driver.findElement(By.id("category-3")).click();
        driver.findElement(By.cssSelector(".thumbnail.product-thumbnail")).click();
        Select dropdown = new Select(driver.findElement(By.id("group_1")));
        dropdown.selectByVisibleText("M");
        String price1 = driver.findElement(By.cssSelector(".current-price")).getText();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn.btn-primary.add-to-cart")));
        element.click();
        WebElement element2 = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn.btn-secondary")));
        element2.click();
        wait.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn.btn-secondary"))));
        driver.findElement(By.id("category-6")).click();
        driver.findElement(By.cssSelector(".thumbnail.product-thumbnail")).click();
        String price2 = driver.findElement(By.cssSelector(".current-price")).getText();
        driver.findElement(By.cssSelector(".btn.btn-primary.add-to-cart")).click();
    }

    private List<String> getStrings() {
        List<String> products = new ArrayList<>();
        products.add("T-Shirt");
        products.add("Sweater");
        products.add("Mug");
        products.add("Framed Poster");
        products.add("Cushion");
        products.add("Vector Graphics");
        products.add("Notebook");
        return products;
    }
}