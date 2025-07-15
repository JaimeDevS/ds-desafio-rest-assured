package com.devsuperior.dsmovie.controllers;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsuperior.dsmovie.tests.TokenUtil;

import io.restassured.http.ContentType;

public class ScoreControllerRA {
	

	private Long existingMovieId, nonExistingMovieId;
	private String adminUsername, adminPassword;
	private String adminToken;
	private Map<String, Object> scores = new HashMap<>();
	
	@BeforeEach
	public void setup() throws JSONException {
		baseURI = "http://localhost:8080";
		
		adminUsername = "alex@gmail.com";
		adminPassword = "123456";
		
		nonExistingMovieId = 100L;
		
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);		
	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {	
		scores.put("movieId", nonExistingMovieId);
		scores.put("score", 4);
		
		JSONObject newScores = new JSONObject(scores);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(newScores)
		.when()
				.put("/scores")
			.then()
				.statusCode(404)
				.body("error", equalTo("Recurso não encontrado"));
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		scores.put("score", 4);
		
		JSONObject newScores = new JSONObject(scores);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(newScores)
		.when()
				.put("/scores")
			.then()
				.statusCode(422)
				.body("error", equalTo("Dados inválidos"));
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		scores.put("movieId", existingMovieId);
		scores.put("score", -4);
		
		JSONObject newScores = new JSONObject(scores);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(newScores)
		.when()
				.put("/scores")
			.then()
				.statusCode(422);
	}
}
