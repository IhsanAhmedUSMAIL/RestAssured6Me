package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class _07_GoRestCommentTest {

    // GoRest Comment ı API testini yapınız.

    Faker randomUretici = new Faker();

    int commentID=0;
    RequestSpecification reqSpec;

    @BeforeClass
    public void setup() {

        baseURI="https://gorest.co.in/public/v2/comments";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "4dfbf2dcfc020ac0ccbae72067a5962addff7336205c8c1af527d08cbbbc9476")
                .setContentType(ContentType.JSON)
                .build()
                ;
    }

    @Test
    public void createComment(){

        String fullName = randomUretici.name().fullName();
        String email = randomUretici.internet().emailAddress();
        String body = randomUretici.lorem().paragraph();


        Map<String, String> newComment=new HashMap<>();
        newComment.put("post_id", "82477");
        newComment.put("name", fullName);
        newComment.put("email", email);
        newComment.put("body", body);


        commentID=
        given()

                .spec(reqSpec)
                .body(newComment)
                .when()
                .post("")

                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().path("id")

                ;
    }

    @Test(dependsOnMethods = "createComment")
    public void getCommentById(){

        given()

                .spec(reqSpec)
                .when()
                .get(""+commentID)

                .then()
                .contentType(ContentType.JSON)
                .body("id", equalTo(commentID))

                ;
    }

    @Test (dependsOnMethods = "getCommentById")
    public void commentUpdate(){

        Map<String, String> updComment=new HashMap<>();
        updComment.put("name", "ihsan Usmail");

        given()

                .spec(reqSpec)
                .body(updComment)
                .when()
                .put(""+commentID)

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(commentID))
                .body("name", equalTo("ihsan Usmail"))

                ;

    }

    @Test(dependsOnMethods =  "commentUpdate")
    public void deleteComment(){

        given()

                .spec(reqSpec)
                .when()
                .delete(""+commentID)

                .then()
                .log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deleteComment")
    public void deleteCommentNegative() {

        given()
                .spec(reqSpec)
                .when()
                .delete(""+commentID)

                .then()
                .log().body()
                .statusCode(404)
                ;

    }

}
