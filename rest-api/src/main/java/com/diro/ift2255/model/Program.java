package com.diro.ift2255.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Représente un programme universitaire (ex. baccalauréat, certificat)
 * tel que retourné par l’API des programmes.
 * Cette classe est utilisée pour la désérialisation JSON.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Program {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("structure")
    private String structure;

    public Program() {}

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getStructure() { return structure; }
}