package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class _08_CountryTest {

Faker randomUretici = new Faker();

RequestSpecification reqSpec;
    String rndCountryName;
    String rndCountryCode;
String countryID;

    @BeforeClass
    public void Setup(){
        baseURI ="https://test.mersys.io/";

        Map<String, String> userCredential=new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe","true");

        Cookies cookies=
        given()

                .body(userCredential)
                .contentType(ContentType.JSON)
                .when()
                .post("/auth/login")

                .then()
                .log().all()
                .statusCode(200)
                .extract().response().getDetailedCookies()

                ;

        reqSpec = new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build()

                ;
    }

    @Test
    public void createCountry(){

        rndCountryName= randomUretici.address().country()+randomUretici.address().country();
        rndCountryCode= randomUretici.address().countryCode();

        Map<String, String> newCountry=new HashMap<>();
        newCountry.put("name", rndCountryName);
        newCountry.put("code", rndCountryCode);

                countryID=
        given()
                .spec(reqSpec)
                .body(newCountry)
                .when()
                .post("school-service/api/countries")

                .then()
                //.log().body()
                .statusCode(201)
                .extract().path("id")

                ;
    }

    // Aynı countryName ve code gönderildiğinde kayıt yapılmadığını yani
    // createCountryNegative testini yapınız, dönen montajın already kelimesini içerdiğini test ediniz.

    @Test (dependsOnMethods = "createCountry" )
    public void createCountryNegative(){

        Map<String, String> newCountry=new HashMap<>();
        newCountry.put("name", rndCountryName);
        newCountry.put("code", rndCountryCode);


        given()
                .spec(reqSpec)
                .body(newCountry)
                .when()
                .post("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"))

        ;

    }

    // update country testini yapınız
    @Test(dependsOnMethods = "createCountryNegative")
    public void updateCountry(){

        String newCountryName="Ethiopia"+randomUretici.number().digits(5);
        Map<String,String> updCountry=new HashMap<>();
        updCountry.put("id", countryID);
        updCountry.put("name", newCountryName);
        updCountry.put("code", "ETH");

        given()

                .spec(reqSpec)
                .body(updCountry)
                .when()
                .put("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(newCountryName))

        ;

    }

    // Delete Country testini yapınız
    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry(){

        given()

                .spec(reqSpec)
                .when()
                .delete("school-service/api/countries/"+countryID)

                .then()
                .log().body()
                .statusCode(200)

                ;
    }

    // Delete Country testinin Negative test halini yapınız
    // dönen mesajın "Country not found" olduğunu doğrulayınız
     @Test(dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative(){

        given()

                .spec(reqSpec)
                .when()
                .delete("school-service/api/countries/"+countryID)

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Country not found"))

                ;
    }

    // Aşağıdaki iki bölüm translate göndermemiz gerektiğindeki  seçeneklerimizdir.
    // 1. yöntem
    @Test
    public void createCountryAllParameter(){

        rndCountryName= randomUretici.address().country()+randomUretici.address().country();
        rndCountryCode= randomUretici.address().countryCode();

        Object[] arr =new Object[1];
        Map<String, Object> newCountry=new HashMap<>();
        newCountry.put("name", rndCountryName);
        newCountry.put("code", rndCountryCode);
        newCountry.put("translateName", arr); // arr yerine direk new Object[1] yazabilirsin


                given()
                        .spec(reqSpec)
                        .body(newCountry)
                        .when()
                        .post("school-service/api/countries")

                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
    }

    //2. yöntem (class tan)

    @Test
    public void createCountryAllParameterClass() {

        rndCountryName = randomUretici.address().country() + randomUretici.address().country();
        rndCountryCode = randomUretici.address().countryCode();

        Country newCountry = new Country();
        newCountry.name = rndCountryName;
        newCountry.code = rndCountryCode;
        newCountry.translateName = new Object[1];


        given()
                .spec(reqSpec)
                .body(newCountry)
                //.log().all()
                .when()
                .post("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")
        ;

    }

}
