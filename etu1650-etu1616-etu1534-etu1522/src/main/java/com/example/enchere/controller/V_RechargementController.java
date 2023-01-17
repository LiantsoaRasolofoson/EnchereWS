package com.example.enchere.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.enchere.exeption.RessourceException;
import com.example.enchere.modele.Compte;
import com.example.enchere.modele.RechargementValide;
import com.example.enchere.modele.V_Rechargement;
import com.example.enchere.repository.CompteRepository;
import com.example.enchere.repository.RechargementValideRepository;
import com.example.enchere.repository.V_RechargementRepository;
import com.example.enchere.retour.ErrorRetour;

@CrossOrigin("*")
@RestController
@RequestMapping("/rechargement")
public class V_RechargementController {
    
    @Autowired
    private V_RechargementRepository v_rechargementRepository;

    @Autowired
    private RechargementValideRepository rechargementValideRepository;

    @Autowired
    private CompteRepository compteRepository;

    @GetMapping
    public @ResponseBody Map<String, Object> nonValides() {
        try{
            Map<String, Object> data = new HashMap<String, Object>();
            List<V_Rechargement> nonValide = v_rechargementRepository.nonValides();
            if(nonValide.size() > 0) {
                data.put("data", nonValide);
            } else {
                data.put("message", "Aucun rechargement non validé");
            }
            return data;
        }
        catch(Exception e){
            throw new RessourceException(new ErrorRetour("Veuillez vérifier les informations",HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PutMapping
    public @ResponseBody Map<String, Object> confirmeRechargement(@RequestParam("idRechargement") int idRechargement, @RequestParam("idCompte") int idCompte, @RequestParam("montant") double montant) {
        try{
            Compte compte = compteRepository.findById(idCompte).get();
            double nouveauSolde = compte.getSolde() + montant;
            compte.setSolde(nouveauSolde);
            compteRepository.save(compte);
            
            Date dateValidation = Date.valueOf(LocalDate.now());
            RechargementValide rv = new RechargementValide(idRechargement, dateValidation);
            rechargementValideRepository.save(rv);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("data", "Confirmation réussie, rechargement validé");
            return data;
        }
        catch(Exception e){
            throw new RessourceException(new ErrorRetour("Erreur de la confirmation",HttpStatus.BAD_REQUEST.value()));
        }
    }
}
