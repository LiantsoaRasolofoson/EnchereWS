package com.example.enchere.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.enchere.exeption.RessourceException;
import com.example.enchere.modele.Compte;
import com.example.enchere.modele.HistoriqueEnchere;
import com.example.enchere.modele.Offre;
import com.example.enchere.modele.Utilisateur;
import com.example.enchere.modele.V_Enchere;
import com.example.enchere.repository.CompteRepository;
import com.example.enchere.repository.HistoriqueEnchereRepository;
import com.example.enchere.repository.OffreRepository;
import com.example.enchere.repository.UtilisateurRepository;
import com.example.enchere.repository.V_EnchereRepository;
import com.example.enchere.retour.ErrorRetour;
import com.example.enchere.retour.SuccessRetour;

@CrossOrigin("*")
@RestController
@RequestMapping("/offres")
public class OffreController {
    
    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private OffreRepository offreRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private V_EnchereRepository v_enchereRepository;

    @Autowired
    private HistoriqueEnchereRepository historiqueEnchereRepository;

    public HistoriqueEnchere gHistoriqueEnchere(Offre offre, Utilisateur utilisateur){
        HistoriqueEnchere he = new HistoriqueEnchere();
        he.setIdEnchere(offre.getIdEnchere());
        he.setIdOffre(offre.getIdOffre());
        he.setPrixOffre(offre.getPrixOffre());
        he.setDateOffre(offre.getDateOffre());
        he.setUtilisateur(utilisateur);
        return he;
    }

    public void saveOffreUser(Offre max, Offre offre)throws Exception{
        V_Enchere enchere = v_enchereRepository.getEnchere(offre.getIdEnchere());
        offre.checkUser(enchere);
        if( max != null ){
            offre.checkMontant(max);
        }
        else{
            offre.checkMontant(enchere);
        }
        Utilisateur utilisateur = utilisateurRepository.findById(offre.getIdUtilisateur()).orElseThrow(() 
            -> new RessourceException(new ErrorRetour("Le idUtilisateur : "+offre.getIdUtilisateur()+" n'existe pas",HttpStatus.NO_CONTENT.value()))
        );
        Compte userCompte = compteRepository.getCompte(offre.getIdUtilisateur());
        userCompte.checkSolde(offre.getPrixOffre());
        offre.setDateOffre(LocalDateTime.now());
        compteRepository.save(userCompte);
        offreRepository.save(offre);
        HistoriqueEnchere he = this.gHistoriqueEnchere(offre, utilisateur);
        historiqueEnchereRepository.save(he);
    }

    public void updateLastOffre(Offre max)throws Exception{
        Compte last = compteRepository.getCompte(max.getIdUtilisateur());
        last.setSolde(last.getSolde()+max.getPrixOffre());
        compteRepository.save(last);
    }

    @PostMapping("/rencherir")
    public @ResponseBody Map<String, Object> rencherir(@RequestBody Offre offre) throws Exception{
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
