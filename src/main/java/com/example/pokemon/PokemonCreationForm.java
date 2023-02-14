package com.example.pokemon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PokemonCreationForm {
  private final String name;

  private final String power;

  @JsonCreator
  public PokemonCreationForm(
      @JsonProperty("name") String name, @JsonProperty("power") String power) {
    this.name = name;
    this.power = power;
  }

  public String getName() {
    return name;
  }

  public String getPower() {
    return power;
  }
}
