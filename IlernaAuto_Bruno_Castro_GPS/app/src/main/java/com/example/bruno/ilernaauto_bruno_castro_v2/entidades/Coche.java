package com.example.bruno.ilernaauto_bruno_castro_v2.entidades;

public class Coche {

    private Integer id;
    private String marca;
    private String modelo;
    private String combustible;
    private Integer ano;
    private String tienda;

    public Coche() {
    }
        //constructor de insercion
    public Coche(String marca, String modelo, String combustible, Integer ano, String tienda) {
        this.marca = marca;
        this.modelo = modelo;
        this.combustible = combustible;
        this.ano = ano;
        this.tienda = tienda;
    }

    public Coche(Integer id, String marca, String modelo, String combustible, Integer ano, String tienda) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.combustible = combustible;
        this.ano = ano;
        this.tienda = tienda;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }
}
