package com.example.pokemon;

import com.example.exception.PokemonValidationException;
import com.example.power.Power;
import com.example.power.PowerRepository;
import com.example.power.PowerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

class PokemonServiceShould {

  PokemonRepository pokemonRepository;
  PowerRepository powerRepository;
  PowerService powerService;
  PokemonService pokemonService;
  Power power;
  Pokemon pikachuPokemon;
  Pokemon bulbasaurPokemon;
  PokemonUpdationForm pokemonUpdationForm;

  List<Pokemon> pokemons;

  PokemonCreationForm pokemonCreationForm;

  @BeforeEach
  void setUp() {

    power = new Power(1L, "fire");

    pikachuPokemon = new Pokemon(1, "Pikachu", power, "Pikachu.png");

    bulbasaurPokemon = new Pokemon(2, "Bulbasaur", power, "Bulbasaur.png");

    pokemons = List.of(pikachuPokemon, bulbasaurPokemon);

    pokemonCreationForm =
        new PokemonCreationForm(pikachuPokemon.getName(), pikachuPokemon.getPower().getName());

    pokemonUpdationForm = new PokemonUpdationForm(bulbasaurPokemon.getId(),bulbasaurPokemon.getName(),"Fire",bulbasaurPokemon.getImageUrl());

    pokemonRepository = Mockito.mock(PokemonRepository.class);

    powerRepository = Mockito.mock(PowerRepository.class);

    powerService = new PowerService(powerRepository);

    pokemonService = new PokemonService(pokemonRepository, powerService);
  }

  @Test
  @DisplayName("Test Get All Pokemons")
  void get() {

    Mockito.when(pokemonRepository.findAll()).thenReturn(pokemons);

    List<Pokemon> retunedPokemons = pokemonService.get();

    Mockito.verify(pokemonRepository).findAll();

    Assertions.assertThat(retunedPokemons).isEqualTo(pokemons);
  }

  @Test
  @DisplayName("Test Create Pokemon")
  void create() {

    Mockito.when(powerRepository.findByName(anyString())).thenReturn(power);

    Mockito.when(pokemonRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);

    Mockito.when(pokemonRepository.save(Mockito.any())).thenReturn(pikachuPokemon);

    Pokemon returnedPokemon = pokemonService.create(pokemonCreationForm);

    Mockito.verify(pokemonRepository).save(Mockito.any());
    Mockito.verify(powerRepository).findByName(anyString());
    Mockito.verify(pokemonRepository).existsByNameIgnoreCase(anyString());

    Assertions.assertThat(returnedPokemon).isEqualTo(pikachuPokemon);
  }

  @Test
  @DisplayName("Test Get Pokemon By Id")
  void getById() {
    Mockito.when(pokemonRepository.findById(anyInt())).thenReturn(Optional.ofNullable(pikachuPokemon));

    Pokemon returnedPokemon = pokemonService.getById(1);

    Mockito.verify(pokemonRepository).findById(anyInt());

    Assertions.assertThat(returnedPokemon).isEqualTo(pikachuPokemon);
  }

  @Test
  @DisplayName("Test Update Pokemon")
  void update() {

    Mockito.when(pokemonRepository.findById(anyInt())).thenReturn(Optional.ofNullable(bulbasaurPokemon));
    Mockito.when(powerRepository.findByName(anyString())).thenReturn(power);


    Mockito.when(pokemonRepository.findByNameIgnoreCase(anyString()))
        .thenReturn(Optional.ofNullable(bulbasaurPokemon));

    Mockito.when(pokemonRepository.update(Mockito.any())).thenReturn(bulbasaurPokemon);

    Pokemon updatedPokemon = pokemonService.update(pokemonUpdationForm);

    Mockito.verify(pokemonRepository).update(Mockito.any());
    Mockito.verify(pokemonRepository).findById(anyInt());
    Mockito.verify(pokemonRepository).findByNameIgnoreCase(anyString());
    Mockito.verify(powerRepository).findByName(anyString());


    Assertions.assertThat(updatedPokemon).isEqualTo(bulbasaurPokemon);
  }

  @Test
  @DisplayName("Test Delete Pokemon")
  void delete() {

    Mockito.when(pokemonRepository.existsById(anyInt())).thenReturn(true);

    pokemonService.delete(anyInt());

    Mockito.verify(pokemonRepository).deleteById(anyInt());
  }

  @Test
  @DisplayName("Test Pokemon-name Already exists")
  void pokemonWithNameExistsException() {
    Mockito.when(pokemonRepository.existsByNameIgnoreCase(anyString())).thenReturn(true);

    Assertions.assertThatThrownBy(() -> pokemonService.create(pokemonCreationForm));
    Assertions.catchThrowableOfType(
        () -> pokemonService.create(pokemonCreationForm), PokemonValidationException.class);
  }

  @Test
  @DisplayName("Test Pokemon-Id could not find in update")
  void pokemonIdNotFoundException() {

    Mockito.when(pokemonRepository.findById(anyInt())).thenReturn(Optional.empty());

    Assertions.assertThatThrownBy(() -> pokemonService.update(pokemonUpdationForm));

    Assertions.catchThrowableOfType(
        () -> pokemonService.update(pokemonUpdationForm), PokemonValidationException.class);
  }

  @Test
  @DisplayName("Test Pokemon-name Already found")
  void pokemonNameAlreadyFound() {
    Mockito.when(pokemonRepository.findById(anyInt())).thenReturn(Optional.ofNullable(bulbasaurPokemon));

    Mockito.when(pokemonRepository.findByNameIgnoreCase(anyString()))
        .thenReturn(Optional.ofNullable(pikachuPokemon));

    Assertions.assertThatThrownBy(() -> pokemonService.update(pokemonUpdationForm));

    Assertions.catchThrowableOfType(
        () -> pokemonService.update(pokemonUpdationForm), PokemonValidationException.class);
  }

  @Test
  @DisplayName("Test Pokemon-Id does not exist ")
  void pokemonIdNotExists() {
    Mockito.when(pokemonRepository.existsById(anyInt())).thenReturn(false);

    Assertions.assertThatThrownBy(() -> pokemonService.delete(anyInt()));

    Assertions.catchThrowableOfType(
        () -> pokemonService.delete(anyInt()), PokemonValidationException.class);
  }

  @Test
  @DisplayName("Test Update Pokemon-power ")
  void pokemonPowerUpdate() {
    Mockito.when(pokemonRepository.findById(anyInt())).thenReturn(Optional.ofNullable(bulbasaurPokemon));

    Mockito.when(pokemonRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

    Mockito.when(pokemonRepository.update(Mockito.any())).thenReturn(bulbasaurPokemon);

    Pokemon updatedPokemon = pokemonService.update(pokemonUpdationForm);

    Mockito.verify(pokemonRepository).update(Mockito.any());
    Mockito.verify(pokemonRepository).findById(anyInt());
    Mockito.verify(pokemonRepository).findByNameIgnoreCase(anyString());

    Assertions.assertThat(updatedPokemon).isEqualTo(bulbasaurPokemon);
  }

  @Test
  @DisplayName("Test Pokemon-Id does not found in getById")
  void pokemonIdNotFound() {
    Mockito.when(pokemonRepository.findById(anyInt()))
            .thenReturn(Optional.empty());

    Assertions.assertThatThrownBy(() -> pokemonService.getById(anyInt()));

    Assertions.catchThrowableOfType(
        () -> pokemonService.getById(anyInt()), PokemonValidationException.class);
  }
}
