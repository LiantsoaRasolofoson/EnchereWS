package com.example.enchere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.enchere.modele.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    
    @Query(value = "SELECT user FROM Utilisateur user WHERE user.logins = ?1 AND user.motDePasse = ?2", nativeQuery = true)
    public Utilisateur logins(String logins, String motDePasse);

}