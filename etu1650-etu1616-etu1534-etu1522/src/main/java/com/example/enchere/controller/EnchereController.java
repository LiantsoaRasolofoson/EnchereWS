package com.example.enchere.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.enchere.exeption.RessourceException;
import com.example.enchere.modele.Condition;
import com.example.enchere.modele.Status;
import com.example.enchere.modele.V_Enchere;
import com.example.enchere.repository.V_EnchereRepository;
import com.example.enchere.retour.ErrorRetour;

@CrossOrigin("*")
@RestController
@RequestMapping("/encheres")
public class EnchereController {

    @Autowired
    private V_EnchereRepository v_enchereRepository;

    V_Enchere v = new V_Enchere();

    Status [] status = {
        new Status(1,"Non demarrer"),
        new Status(2,"En Cours"),
        new Status(3,"Vendu"),
        new Status(4,"Non-Vendu")
    };

    @PostMapping("/recherches")
    public @ResponseBody Map<String, Object> recherches(@RequestBody Condition condition ){
        try{
            String c = condition.conditionRequete();
            Map<String, Object> data = new HashMap<String, Object>();
            List <V_Enchere> liste = v.getAll(c);
            data.put("data", liste);
            return data;
        }
        catch(Exception e){
            throw new RessourceException(new ErrorRetour(e.getMessage(),HttpStatus.BAD_REQUEST.value()));
        }
    }

    @GetMapping("/listeEnchere/{idUtilisateur}")
    public @ResponseBody Map<String, Object> listeEnchere(@PathVariable int idUtilisateur){
        try{
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("data",v_enchereRepository.getAll(idUtilisateur));
            return data;
        }
        catch(Exception e){
            throw new RessourceException(new ErrorRetour(e.getMessage(),HttpStatus.BAD_REQUEST.value()));
        }
    }

}
