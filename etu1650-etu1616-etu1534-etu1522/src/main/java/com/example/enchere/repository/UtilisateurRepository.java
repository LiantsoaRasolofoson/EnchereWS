package com.example.enchere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.enchere.modele.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    
    @Query(value = "SELECT * FROM Utilisateur user WHERE user.email = ?1 AND user.motDePasse = ?2", nativeQuery = true)
    public Utilisateur login(String email, String motDePasse);

}