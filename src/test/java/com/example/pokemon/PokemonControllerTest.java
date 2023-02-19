package com.example.pokemon;

import com.example.power.Power;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@MicronautTest
class PokemonControllerTest {

  @Test
  @DisplayName("Test getAllPokemon")
  void getAll(@NotNull RequestSpecification requestSpecification) {
    List<Pokemon> pokemons =
        List.of(
            RestAssured.given(requestSpecification)
                .when()
                .get("/pokemon")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(Pokemon[].class));
    Assertions.assertThat(pokemons.size()).isGreaterThan(-1);
  }

  @Test
  @DisplayName("Test createPokemon")
  void createPokemon(@NotNull RequestSpecification requestSpecification) {
    PokemonCreationForm pokemon = new PokemonCreationForm("Ivyasaur6", "Fire");
    Pokemon createdPokemon =
        RestAssured.given(
                requestSpecification.header("Content-type", "application/json").body(pokemon))
            .when()
            .post("/pokemon")
            .then()
            .assertThat()
            .statusCode(201)
            .extract()
            .as(Pokemon.class);

    Assertions.assertThat(createdPokemon.getName()).isEqualTo(pokemon.getName());
    Assertions.assertThat(createdPokemon.getPower().getName()).isEqualTo(pokemon.getPower());
    Assertions.assertThat(createdPokemon.getId()).isNotNull();
    Assertions.assertThat(createdPokemon.getImageUrl()).isNotNull();
  }

  @Test
  @DisplayName("Test updatePokemon")
  void updatePokemon(@NotNull RequestSpecification requestSpecification) {

    PokemonUpdationForm pokemonToUpdate = new PokemonUpdationForm(18,"Bulbasaur4","Water","abc.png");
    Pokemon updatedPokemon =
        RestAssured.given(
                requestSpecification
                    .header("Content-type", "application/json")
                    .body(pokemonToUpdate))
            .when()
            .put("/pokemon")
            .then()
            .extract()
            .as(Pokemon.class);

    Assertions.assertThat(updatedPokemon.getName()).isEqualTo(pokemonToUpdate.getName());
    Assertions.assertThat(updatedPokemon.getPower().getName())
        .isEqualTo(pokemonToUpdate.getPower());
    Assertions.assertThat(updatedPokemon.getId()).isNotNull();
    Assertions.assertThat(updatedPokemon.getImageUrl()).isEqualTo(pokemonToUpdate.getImageUrl());
  }

  @Test
  @DisplayName("Test getPokemonById")
  void getPokemonById(@NotNull RequestSpecification requestSpecification) {
    Pokemon pokemon =
        RestAssured.given(requestSpecification)
            .when()
            .get("/pokemon/19")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(Pokemon.class);

    Assertions.assertThat(pokemon.getId()).isEqualTo(19);
    Assertions.assertThat(pokemon.getName()).isEqualTo("Ivyasaur");
    Assertions.assertThat(pokemon.getImageUrl())
        .isEqualTo(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/19.png");
    Assertions.assertThat(pokemon.getPower().getName()).isEqualTo("Fire");
  }

  @Test
  @DisplayName("Test deletePokemonById")
  void deletePokemonById(@NotNull RequestSpecification requestSpecification) {
    RestAssured.given(requestSpecification)
        .when()
        .delete("/pokemon/22")
        .then()
        .assertThat()
        .statusCode(200);
  }
}
