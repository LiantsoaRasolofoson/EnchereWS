package com.example.enchere.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.enchere.exeption.RessourceException;
import com.example.enchere.modele.Categorie;
import com.example.enchere.repository.CategorieRepository;
import com.example.enchere.retour.ErrorRetour;

@CrossOrigin("*")
@RestController
@RequestMapping("/categorie")
public class CategorieController {
    
    @Autowired
    private CategorieRepository categorieRepository;

    @PostMapping("/insertion")
    public @ResponseBody Map<String, Object> insertion(@RequestBody Categorie categorie) throws Exception {
        try{
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("data", categorieRepository.save(categorie));
            return data;
        }
        catch(Exception e){
            throw new RessourceException(new ErrorRetour("Veuillez vérifier les informations",HttpStatus.NOT_FOUND.value()));
        }
    }

    @GetMapping("/liste")
    public @ResponseBody Map<String, Object> liste() throws Exception {
        try{
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("data", categorieRepository.findAll());
            return data;
        }
        catch(Exception e){
            throw new RessourceException(new ErrorRetour("Veuillez vérifier les informations",HttpStatus.NOT_FOUND.value()));
        }
    }
}
