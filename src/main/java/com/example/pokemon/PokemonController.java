package com.example.pokemon;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import java.util.List;

@Controller("/pokemon")
public class PokemonController {

  private final PokemonService pokemonService;

  public PokemonController(PokemonService pokemonService) {
    this.pokemonService = pokemonService;
  }

  @Get
  public List<Pokemon> getAll() {
    return pokemonService.get();
  }

  @Get("/{id}")
  public HttpResponse<Pokemon> getById(@PathVariable("id") Integer id) {
    return HttpResponse.ok(pokemonService.getById(id));
  }

  @Post
  public HttpResponse<Pokemon> create(@Body PokemonCreationForm pokemonForm) {
    return HttpResponse.created(pokemonService.create(pokemonForm));
  }

  @Put
  public HttpResponse<Pokemon> update(@Body PokemonUpdationForm pokemonForm) {

    return HttpResponse.created(pokemonService.update(pokemonForm));
  }

  @Delete("/{id}")
  public HttpResponse<Pokemon> delete(@PathVariable Integer id) {

    pokemonService.delete(id);
    return HttpResponse.ok();
  }
}
