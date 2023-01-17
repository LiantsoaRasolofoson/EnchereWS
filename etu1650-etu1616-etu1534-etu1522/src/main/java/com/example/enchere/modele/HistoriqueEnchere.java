package com.example.enchere.modele;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "HistoriqueEnchere")
public class HistoriqueEnchere {
    
    private String id;
    private V_Enchere enchere;
    private Offre offre;

    public void setId(String id){
        this.id = id;
    }

    public void setEnchere(V_Enchere enchere){
        this.enchere = enchere;
    }

    public void setOffre(Offre offre){
        this.offre = offre;
    }

    public String getId(){
        return this.id;
    }

    public V_Enchere getEnchere(){
        return this.enchere;
    }

    public Offre getOffre(){
        return this.offre;
    }

    public HistoriqueEnchere(){

    }
}
