import java.io.IOException;

public class RegistreGuerriers {
	private static FichierGuerriers fichier;

	public RegistreGuerriers() throws IOException {
		fichier = new FichierGuerriers("Guerrier.dta");
		fichier.ouvrirFichier();
	}
	
	public void fermerRegistre() throws IOException {
		fichier.fermerFichier();
	}
	
	private int trouverHomeAdress(String nom) {
//		return Math.abs(nom.hashCode() % FichierGuerriers.NOMBRE_ENREG);  // methode correcte
		if (nom.charAt(0) == 'A') {
			return 0;
		}
		return 3;
	}
	
	/**
	 * Recherche l'adresse du guerrier dans le fichier
	 * @param nom Nom du guerrier a trouver l'adresse
	 * @return l'adresse du guerrier, -1 si pas present
	 * @throws IOException
	 */
	public int trouverAdresseGuerrier(String nom) throws IOException {
		String nomFormat = FichierGuerriers.formaterString(nom, FichierGuerriers.LONG_NOM/2);
		int adresse = trouverHomeAdress(nomFormat);
		for (int i = 0; i < FichierGuerriers.NOMBRE_ENREG; i++, adresse++) {
			fichier.positionnerEn(adresse % FichierGuerriers.NOMBRE_ENREG);
			EnregistrementGuerrier eGuerrier = fichier.lireEnregistrement();
			if (eGuerrier.getGuerrier().getNom().equals(nomFormat) && eGuerrier.getStatut() != EnregistrementGuerrier.SUPPRIME)
				return adresse % FichierGuerriers.NOMBRE_ENREG; // Guerrier trouve
			if (eGuerrier.getStatut() == EnregistrementGuerrier.VIDE)
				return -1;  // Guerrier non present 
		}
		return -1;
	}
	
	/**
	 * Recherche l'adresse d'ajout du guerrier dans un fichier.
	 * Ne verifie pas si deja present.
	 * @param nom Nom du guerrier
	 * @return l'adresse du guerrier,fichier complet
	 * @throws IOException
	 */
	private int trouverAdressePourAjout(String nom) throws IOException {
		String nomFormat = FichierGuerriers.formaterString(nom, FichierGuerriers.LONG_NOM/2);
		int adresse = trouverHomeAdress(nomFormat);
		for (int i = 0; i < FichierGuerriers.NOMBRE_ENREG; i++, adresse++) {
			fichier.positionnerEn(adresse % FichierGuerriers.NOMBRE_ENREG);
			EnregistrementGuerrier eGuerrier = fichier.lireEnregistrement();
			if (eGuerrier.getStatut() != EnregistrementGuerrier.OCCUPE) 
				return adresse % FichierGuerriers.NOMBRE_ENREG; // Emplacement libre (supprime ou vide)
		}
		return -1; // Complet
	}
	
	/**
	 * Ajoute le guerrier passe en parametre si le fichier n'est pas plein.
	 * Ne verifie pas si le guerrier est deja present.
	 * @param guerrier le guerrier a ajouter
	 * @return le numero du guerrier ajoute ou -1 si le fichier est plein
	 * @throws IOException
	 */
	public int ajouterGuerrier(Guerrier guerrier) throws IOException {
		if (guerrier == null)
			throw new IllegalArgumentException();
		int adresse = trouverAdressePourAjout(guerrier.getNom());
		if (adresse == -1) // Fichier complet
			return -1;
		fichier.positionnerEn(adresse);
		fichier.ecrireEnregistrement(new EnregistrementGuerrier(guerrier, 'O'));
		return adresse;
	}
	
	/**
	 * Supprime le guerrier dont le nom est passe en parametre
	 * @param nomGuerrier le numero du guerrier recherche
	 * @return le guerrier ou null si aucun guerrier ne correspond a ce numero
	 * @throws IOException
	 */
	public Guerrier supprimerGuerrier(String nomGuerrier) throws IOException {
		if (nomGuerrier == null || nomGuerrier.equals(""))
			return null;
		int adresse = trouverAdresseGuerrier(nomGuerrier);
		if (adresse == -1) // Guerrier pas present
			return null;
		fichier.positionnerEn(adresse);
		EnregistrementGuerrier eGuerrier = fichier.lireEnregistrement();
		eGuerrier.setStatut(EnregistrementGuerrier.SUPPRIME);
		fichier.positionnerEn(adresse);
		fichier.ecrireEnregistrement(eGuerrier);
		return eGuerrier.getGuerrier();
	}

	/**
	 * Met a jour le score du guerrier dont le nom est passe en parametre
	 * @param nomGuerrier le nom du guerrier recherche
	 * @param nouveauMontant le nouveau montant
	 * @return le guerrier ou null si aucun guerrier ne correspond a ce nom
	 * @throws IOException
	 */
	public Guerrier mettreAJourGuerrier(String nomGuerrier, int nouveauScore) throws IOException {
		if (nomGuerrier == null || nomGuerrier.equals(""))
			return null;
		int adresse = trouverAdresseGuerrier(nomGuerrier);
		if (adresse == -1) // Guerrier pas present
			return null;
		fichier.positionnerEn(adresse);
		EnregistrementGuerrier eGuerrier = fichier.lireEnregistrement();
		eGuerrier.getGuerrier().setScore(nouveauScore);
		fichier.positionnerEn(adresse);
		fichier.ecrireEnregistrement(eGuerrier);
		return eGuerrier.getGuerrier();
	}
	
	/**
	 * Recherche le guerrier dont le nom est passe en parametre
	 * @param nomGuerrier le nom du guerrier a consulter
	 * @return le guerrier ou null si aucun guerrier ne correspond a ce numero
	 * @throws IOException
	 */
	public Guerrier consulterGuerrier(String nomGuerrier) throws IOException {
		if (nomGuerrier == null || nomGuerrier.equals(""))
			return null;
		int adresse = trouverAdresseGuerrier(nomGuerrier);
		if (adresse == -1)
			return null;
		fichier.positionnerEn(adresse);
		return fichier.lireEnregistrement().getGuerrier();
	}
	
	/**
	 * Renvoie sous forme d'un String tous les emplacements du fichier
	 * @return tous les emplacements et les donnees presentes 
	 * @throws IOException
	 */
	public String tousLesEmplacements() throws IOException {
		fichier.positionnerAuDebut(); // Partir depuis le debut
		String s = "";
		for (int i = 0; i < FichierGuerriers.NOMBRE_ENREG; i++) {
			int numGuerrier = i + 1;
			s += "Guerrier " + numGuerrier + " - ";
			EnregistrementGuerrier eGuerrier = fichier.lireEnregistrement();
			Guerrier guerrier = eGuerrier.getGuerrier();
			if (eGuerrier.getStatut() != EnregistrementGuerrier.OCCUPE) {
				s += eGuerrier.getStatut() + "\n";
			} else {
				s += guerrier.getNom() + " " + guerrier.getScore() + " " + eGuerrier.getStatut() + "\n";
			}
		}
		return s;
	}
}
