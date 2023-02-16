package com.emurugova;

import com.emurugova.config.CredentialsConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;

public class AddToCartTest extends TestBase{

public static CredentialsConfig credentials = ConfigFactory.create(CredentialsConfig.class);

    @Test
    void quantityCheckInCartTest() {
        String authorizationCookie =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .formParam("Email", credentials.login())
                        .formParam("Password", credentials.password())
                        .when()
                        .post("https://demowebshop.tricentis.com/login")
                        .then()
                        .statusCode(302)
                        .extract()
                        .cookie("NOPCOMMERCE.AUTH");

        open("/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(
                new Cookie("NOPCOMMERCE.AUTH", authorizationCookie));

        String response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&" +
                              "product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
                        .cookie("NOPCOMMERCE.AUTH", authorizationCookie)
                        .when()
                        .post("https://demowebshop.tricentis.com/addproducttocart/details/72/1")
                        .then()
                        .extract()
                        .path("updatetopcartsectionhtml");

        open("/");
        $(".ico-cart").shouldHave(text(response));
    }
}