package com.example.apppokemon;

public class Pokemon {
    String numeroPokemon;
    String nombrePokemon;
    String urlImg;
    public Pokemon(String numeroPokemon, String nombrePokemon, String urlImg) {
        this.numeroPokemon = numeroPokemon;
        this.nombrePokemon = nombrePokemon;
        this.urlImg = urlImg;
    }

    public String getNumeroPokemon() {
        return numeroPokemon;
    }

    public void setNumeroPokemon(String numeroPokemon) {
        this.numeroPokemon = numeroPokemon;
    }

    public String getNombrePokemon() {
        return nombrePokemon;
    }

    public void setNombrePokemon(String nombrePokemon) {
        this.nombrePokemon = nombrePokemon;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }
}
