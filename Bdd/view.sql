CREATE OR REPLACE VIEW V_Utilisateur AS (
    SELECT
        Utilisateur.*, Genre.valeur 
    FROM Utilisateur JOIN Genre ON Utilisateur.idGenre = Genre.idGenre
);

CREATE OR REPLACE VIEW V_Compte AS (
    SELECT 
        V_Utilisateur.nom, 
        V_Utilisateur.prenom, 
        V_Utilisateur.contact, 
        V_Utilisateur.adresse, 
        V_Utilisateur.valeur as genre, 
        V_Utilisateur.dateDeNaissance,
        Compte.*
    FROM V_Utilisateur JOIN Compte ON V_Utilisateur.idUtilisateur = Compte.idUtilisateur
);


CREATE OR REPLACE VIEW V_Enchere AS (
    SELECT 
        Enchere.*, 
        Categorie.valeur as categorie, 
        Commission.taux, 
        status(Enchere.idEnchere) as statusEnchere
    FROM 
        Enchere JOIN Categorie ON Enchere.idCategorie = Categorie.idCategorie
        JOIN Commission ON Commission.idCommission = Enchere.idCommission
);

CREATE OR REPLACE VIEW V_Offre AS (
    SELECT 
        Offre.*,
        V_Enchere.nom, 
        V_Enchere.descriptions, 
        V_Enchere.prixEnchere, 
        V_Enchere.prixMin, 
        V_Enchere.categorie, 
        V_Enchere.dateEnchere, 
        V_Utilisateur.nom as nomUser , 
        V_Utilisateur.prenom, 
        V_Utilisateur.email, 
        V_Utilisateur.contact, 
        V_Utilisateur.adresse, 
        V_Utilisateur.valeur as genre,
        V_Utilisateur.dateDeNaissance
    FROM Offre JOIN V_Enchere ON V_Enchere.idEnchere = Offre.idEnchere
    JOIN V_Utilisateur ON V_Utilisateur.idUtilisateur = Offre.idUtilisateur
);

CREATE OR REPLACE VIEW V_Rechargement AS (
    SELECT Rechargement.*, Compte.numero 
    FROM Rechargement JOIN Compte ON Rechargement.idCompte = Compte.idCompte
);

CREATE OR REPLACE VIEW V_Stat AS (
    SELECT idCategorie, categorie, count(idCategorie) 
    FROM V_Enchere WHERE statusEnchere = 'Vendu' GROUP BY idCategorie, categorie
);