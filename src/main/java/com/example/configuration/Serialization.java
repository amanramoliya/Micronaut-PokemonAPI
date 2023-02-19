package com.example.configuration;

import com.example.pokemon.Pokemon;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Serialization implements ApplicationEventListener<StartupEvent> {
  private static final Logger log = LoggerFactory.getLogger(Serialization.class);

  private final ObjectMapper mapper;

  public Serialization(ObjectMapper mapper) {

    this.mapper = mapper;
  }

  @Override
  public void onApplicationEvent(StartupEvent event) {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }
}
