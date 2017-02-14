package com.inlab.racodemoapi.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by florencia.rimolo on 14/02/2017.
 */

public class PlansEstudiResult {
    @SerializedName("codi_pla")
    @Expose
    private Integer codiPla;
    @SerializedName("abreviatura")
    @Expose
    private String abreviatura;
    @SerializedName("descripcio")
    @Expose
    private String descripcio;

    public Integer getCodiPla() {
        return codiPla;
    }

    public void setCodiPla(Integer codiPla) {
        this.codiPla = codiPla;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }
}
