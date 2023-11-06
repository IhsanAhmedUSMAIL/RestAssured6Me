import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _01_ApiTest {

    @Test
    public void test1(){

        given()
                // Hazırlık işlemleri
                .when()
                // endpoint (url), metod u verip istek gönderiliyor

                .then()
                 // assertion, test, data işlemleri
        ;
    }

    @Test
    public void ContentTypeTest() {

        given()
                // hazırlık kısmı boş
                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                .log().body() // dönen body json data, log().All() da data dışındaki her şeyi görürsün.
                .statusCode(200) // test kısmı olduğundan (postman) assertion status code 200 mü
                .contentType(ContentType.JSON) // dönen datanın tipi JSON mı
        ;
    }

    @Test
    public void checkCountryInResponseBody(){

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")
                .then()
                //.log().body()
                .statusCode(200) // assertion
                .body("country", equalTo("United States")) // assertion
                // body nin country değişkeni "United States" eşit mi

                ;
    }

    // Soru : "https://api.zippopotam.us/us/90210" ednpoint inde dönen
    // place dizisinin ilk elemanın state değerini "California"
    // olduğunu doğrulayınız

    @Test
    public void checkStateInResponseBody() {

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                .statusCode(200)
                .body("places[0].state", equalTo("California")) // places in [0]. elementin .state değeri. index vermezsen tüm stateleri verir

        ;

    }


    @Test
    public void checkHasItem(){

        // Soru : "https://api.zippopotam.us/tr/01000" ednpoint in dönen
        // place dizisinin herhangi bir elemanında "Dörtağaç Köyü" değerinin
        // olduğunu doğrulayınız

        given()

                .when()
                .get("https://api.zippopotam.us/tr/01000")

                .then()
                //.log().body()
                .body("places.'place name'", hasItem("Dörtağaç Köyü"))
                .statusCode(200)
        ;
    }

    // Soru : "https://api.zippopotam.us/us/90210" ednpoint in dönen
    // place dizisinin dizi uzunluğunu 1 olduğunu doğrulayınız.

    @Test
    public void boddyArrayHasSizeTest(){

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                //.log().body()
                .body("places", hasSize(1)) // places in item size 1 e eşit mi

                ;


    }

    @Test
    public void boddyArrayHasSizeTest2(){

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                .body("places.size()", equalTo(1)) // places in item size 1 e eşit mi

        ;

    }

    @Test
    public void  combiningTest(){

        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                .statusCode(200)
                .body("places", hasSize(1))
                .body("places[0].state", equalTo("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                ;
    }

    @Test
    public void pathParamTest(){

        given()
                .pathParam("ulke","us")
                .pathParam("postaKod",90210)
                .log().uri() // request link çalışmadan önceki hali

                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKod}")

                .then()
                .statusCode(200)
        ;

    }

    @Test
    public void queryParamTest(){
        //https://gorest.co.in/public/v1/users?page=3
        given()
                .param("page",1) // ?page=1 şeklinde linke ekleniyor (birden fazla ek gönderebilirsin)
                //.param("pag", 2)
                //.param("pa", 3)
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .statusCode(200)
                .log().body()
        ;
    }

    // https://gorest.co.in/public/v1/users?page=3
    // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
    // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.

    @Test
    public void queryParamTest2() {

        for (int i = 1; i <= 10; i++) {
            given()
                    .param("page", i)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .statusCode(200)
                    .log().body()
                    .body("meta.pagination.page", equalTo(i))
            ;

        }
    }

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    @BeforeClass
    public void setup(){
        baseURI = "https://gorest.co.in/public/v1";

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .log(LogDetail.URI) // log().uri()
                .build();
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200) // statusCode(200)
                .log(LogDetail.BODY)
                .expectContentType(ContentType.JSON)
                .build();

    }

    @Test
    public void requestResponseSpecification(){

        given()
                .param("page",1)
                .spec(requestSpec)

                .when()
                .get("/users")// http hok ise baseUri baş tarafına gelir.

                .then()
                .spec(responseSpec)
        ;
    }

    @Test
    public void  extractingJsonPath(){

        String countryName =
        given()
                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                .extract().path("country") // path i country olan değeri EXTRACT yap
        ;
        System.out.println("country = " + countryName);
        Assert.assertEquals(countryName, "United States"); // alınan değer buna eşit mi

    }

}
