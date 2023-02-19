package com.example.pokemon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PokemonUpdationForm {
    private final Integer id;
    private final String name;
    private final String power;
    private final String imageUrl;

    @JsonCreator
    public PokemonUpdationForm(@JsonProperty("id")  Integer id,@JsonProperty("name")  String name,@JsonProperty("power")  String power,@JsonProperty("imageUrl")  String imageUrl) {
        this.id = id;
        this.name = name;
        this.power = power;
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getPower() {
        return power;
    }


    public String getImageUrl() {
        return imageUrl;
    }


}
