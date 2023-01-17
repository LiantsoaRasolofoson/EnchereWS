package com.example.enchere.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.enchere.modele.Compte;
import com.example.enchere.modele.Enchere;
import com.example.enchere.modele.Offre;
import com.example.enchere.repository.CompteRepository;
import com.example.enchere.repository.EnchereRepository;
import com.example.enchere.repository.OffreRepository;
import com.example.enchere.retour.SuccessRetour;

@CrossOrigin("*")
@RestController
@RequestMapping("/offres")
public class OffreController {
    
    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private EnchereRepository enchereRepository;

    @Autowired
    private OffreRepository offreRepository;

    public void saveOffreUser(Offre max, Offre offre)throws Exception{
        Enchere enchere = enchereRepository.getEnchere(offre.getIdEnchere());
        offre.checkUser(enchere);
        if( max != null ){
            offre.checkMontant(max);
        }
        else{
            offre.checkMontant(enchere);
        }
        Compte userCompte = compteRepository.getCompte(offre.getIdUtilisateur());
        userCompte.checkSolde(offre.getPrixOffre());
        compteRepository.save(userCompte);
        offreRepository.save(offre);
    }

    public void updateLastOffre(Offre max)throws Exception{
        Compte last = compteRepository.getCompte(max.getIdUtilisateur());
        last.setSolde(last.getSolde()+max.getPrixOffre());
        compteRepository.save(last);
    }

    @PostMapping
    public @ResponseBody Map<String, Object> createAvion(@RequestBody Offre offre) throws Exception{
        try{
            Offre max = offreRepository.getOffreMax(offre.getIdEnchere());
            offre.setDateOffre(LocalDateTime.now());
            saveOffreUser(max, offre);
            if( max != null ){
                updateLastOffre(max);
            }
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("data", new SuccessRetour("Votre offre a été ajouté avec succès"));
            return data;
        }
        catch(Exception e){
            throw e;
        }
    }

   
}
