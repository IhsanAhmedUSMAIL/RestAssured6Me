package GoRest;

import Model.User;
import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _06_GoRestUsersTest {

    //    https://gorest.co.in/public/v2/users   POST
//
//    giden body
//    {
//        "name":"{{$randomFullName}}",
//            "gender":"male",
//            "email":"{{$randomEmail}}",
//            "status":"active"
//    }
//
//    Authorization
//    Bearer 778c2d6925007a5e395329199c092232a319b0188714c4b8993347a73efd3d25
//
//
//    dönüşte 201
//    id extract

    Faker randomUretici = new Faker();
    int userID=0;
    RequestSpecification reqSpec;

    @BeforeClass
    public void setup() {

        baseURI="https://gorest.co.in/public/v2/users";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "4dfbf2dcfc020ac0ccbae72067a5962addff7336205c8c1af527d08cbbbc9476")
                .setContentType(ContentType.JSON)
                .build();

    }
    @Test(enabled = false)
    public void createUserJson(){

        String rndFullName= randomUretici.name().fullName();
        String rndEmail= randomUretici.internet().emailAddress();

        userID=

        given()
                .header("Authorization", "Bearer 4dfbf2dcfc020ac0ccbae72067a5962addff7336205c8c1af527d08cbbbc9476")
                .body("{ \"name\": \""+rndFullName+"\", \"email\": \""+rndEmail+"\", \"gender\": \"male\", \"status\": \"active\"}")
                .contentType(ContentType.JSON)

                .when()
                .post("https://gorest.co.in/public/v2/users")

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")

                ;
        System.out.println("userID = " + userID);

    }

    @Test
    public void createUserMAP(){

        String rndFullName= randomUretici.name().fullName();
        String rndEmail= randomUretici.internet().emailAddress();

        Map<String, String> newUser=new HashMap<>();
        newUser.put("name", rndFullName);
        newUser.put("gender", "male");
        newUser.put("email", rndEmail);
        newUser.put("status", "active");

        userID=

                given()
                        .spec(reqSpec)
                        .body(newUser) // giden body
                        .contentType(ContentType.JSON)

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")

        ;
        System.out.println("userID = " + userID);

    }

    @Test(enabled = false)
    public void createUserClass(){

        String rndFullName= randomUretici.name().fullName();
        String rndEmail= randomUretici.internet().emailAddress();

        User newUser=new User();
        newUser.name=rndFullName;
        newUser.email=rndEmail;
        newUser.gender="male";
        newUser.status="active";

        userID=
                given()// giden body, token, contentType
                        .header("Authorization", "Bearer 4dfbf2dcfc020ac0ccbae72067a5962addff7336205c8c1af527d08cbbbc9476")
                        .body(newUser) // giden body
                        .contentType(ContentType.JSON)

                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")

        ;
        System.out.println("userID = " + userID);

    }

    @Test(dependsOnMethods = "createUserMAP") // önce ID oluşacak sonra çalışacak
    public void getUserById() {

        given()
                .spec(reqSpec)

                .when()
                .get(""+userID)

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(userID))
        ;

    }

    @Test(dependsOnMethods = "getUserById")
    public void updateUser(){
        Map<String, String> updateUser=new HashMap<>();
        updateUser.put("name","ihsan USM");

        given()

                .spec(reqSpec)
                .body(updateUser)
                .when()
                .put(""+userID)

                .then()
                .statusCode(200)
                .body("id",equalTo(userID))
                .body("name", equalTo("ihsan USM"))

                ;

    }

    // user delete API testini yapınız
    @Test (dependsOnMethods = "updateUser")
    public void deleteUser(){

        given()

                .spec(reqSpec)
                .when()
                .delete(""+userID)

                .then()
                //.log().all()
                .statusCode(204)
        ;

    }

    // user delete negatif API testini yapınız

    @Test (dependsOnMethods = "deleteUser")
    public void deleteNegative(){

        given()

                .spec(reqSpec)
                .when()
                .delete(""+userID)

                .then()
                //.log().all()
                .statusCode(404)
        ;

    }

}
