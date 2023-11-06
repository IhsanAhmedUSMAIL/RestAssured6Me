import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class _02_ApiTestExtract {

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


    @Test
    public void  extractingJsonPath2(){
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının state değerinin  "California"
        // olduğunu testNG Assertion ile doğrulayınız


        String stateName =
                given()
                        .when()
                        .get("https://api.zippopotam.us/us/90210")

                        .then()

                        .extract().path("places[0].state") // path i country olan değeri EXTRACT yap
                ;
        System.out.println("stateName = " + stateName);
        Assert.assertEquals(stateName, "California"); // alınan değer buna eşit mi

    }



    @Test
    public void  extractingJsonPath3(){
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının place name değerinin  "Beverly Hills"
        // olduğunu testNG Assertion ile doğrulayınız


        String placeName =
                given()
                        .when()
                        .get("https://api.zippopotam.us/us/90210")

                        .then()

                        .extract().path("places[0].'place name'") // place[0]["place name"] bu da olur
                ;
        System.out.println("placeName = " + placeName);
        Assert.assertEquals(placeName, "Beverly Hills"); // alınan değer buna eşit mi

    }

    @Test
    public void extractingJsonPath4() {
        // Soru : "https://gorest.co.in/public/v1/users" endpoint in den dönen
        // limit bilgisinin 10 olduğunu testNG ile doğrulayınız.

        int limit=
        given()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .extract().path("meta.pagination.limit")

                ;
        System.out.println("limit = " +limit);
        Assert.assertTrue(limit==10);
    }

    @Test
    public void extractingJsonPath5() {

        List<Integer> idler=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("data.id")

                ;
        System.out.println("idler = " +idler);

    }

    @Test
    public void extractingJsonPath6() {

        // yukardaki testten sonra aynı endpointin sonunda dönen bütün name leri yazdırınız
        List<String> names=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("data.name")

                ;
        System.out.println("names = " +names);

    }

    @Test
    public void extractingJsonPathResponseAll() {

        Response donenData=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().response()
                ;
        List<Integer> idler = donenData.path("data.id");
        List<String>  isimler = donenData.path("data.name");
        int limit = donenData.path("meta.pagination.limit");

        System.out.println("idler = " + idler);
        System.out.println("isimler = " + isimler);
        System.out.println("limit = " + limit);

        Assert.assertTrue(isimler.contains("Mahesh Menon"));
        Assert.assertTrue(idler.contains(5599126));
        Assert.assertTrue(limit==10);
    }

}


