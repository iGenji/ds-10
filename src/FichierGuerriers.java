import java.io.IOException;
import java.io.RandomAccessFile;

public class FichierGuerriers { 

	private String nomFichier; // contient le nom complet du fichier. (path+nom)
	private RandomAccessFile fichier;			

	/**
	 *	Donne la longueur d'un enregisrement : 30 bytes pour le nom (15 caracteres), 
	 * + 4 bytes pour le score (int)
	 * + 2 bytes pour le statut(caractere unicode)
	 */
	static final int LONG_ENREG = 36;

	/**
	 *	Donne le nombre d'enregistrements : 5 
	 */
	static final int NOMBRE_ENREG = 5;

	/**
	 *	Donne la longueur du nom  : 15 caracteres
	 */
	static final int LONG_NOM = 30;

	/**
	 *	Donne la longueur du score : 1 int = 4 bytes
	 */
	static final int LONG_SCORE = 4;

	/**
	 *	Donne la longueur du statut  : 1 caractere(unicode)
	 */
	static final int LONG_STATUT = 2;

	/** constructeur de la classe Fichier.
	@param nomFichier String specifiant le chemin d'acces et le nom du fichier.
	 */
	public FichierGuerriers(String nom) {
		this.nomFichier = nom;
		fichier = null;
	}	

	/** ouverture du fichier en lecture/ecriture
	@throws java.io.IOException
	 */
	public void ouvrirFichier() throws IOException {
		fichier = new RandomAccessFile(nomFichier, "rw"); // ouverture en read/write
		if (fichier.length() == 0) {
			EnregistrementGuerrier eGuerrier = new EnregistrementGuerrier();
			for (int i = 0; i < FichierGuerriers.NOMBRE_ENREG; i++) {
				ecrireEnregistrement(eGuerrier);
			}
		}
	}

	/**  fermeture du fichier en lecture/ecriture
	@throws java.io.IOException si erreur lors de la fermeture.
	 */
	public void fermerFichier() throws IOException {
		fichier.close();
	}

	public void positionnerAuDebut() throws IOException {
		fichier.seek(0);
	}

	public void positionnerEn(int numeroGuerrier) throws IOException {
		fichier.seek(numeroGuerrier * LONG_ENREG);
	}

	/**
	 * methode d'ecriture d'un enregistrement
	 *	@param	eGuerrier l'enregistrement a ecrire
	 *	@exception	IOException
	 */
	public void ecrireEnregistrement(EnregistrementGuerrier eGuerrier) throws IOException {
		String s = formaterString(eGuerrier.getGuerrier().getNom(), LONG_NOM / 2);
		fichier.writeChars(s);
		fichier.writeInt(eGuerrier.getGuerrier().getScore());
		fichier.writeChar(eGuerrier.getStatut());
	}

	/**
	 * methode de lecture d'un enregistrement
	 *	@return	l'enregistrement lu
	 *	@exception	IOException
	 */	
	public EnregistrementGuerrier lireEnregistrement() throws IOException {
		String nom = lireString(LONG_NOM / 2);
		int score = fichier.readInt();
		char statut = fichier.readChar();
		return new EnregistrementGuerrier(new Guerrier(nom, score), statut);
	}

	/**
	 * methode de lecture d'une string
	 *	@param	taille	la taille de la string
	 *	@return	l'enregistrement lu
	 *  	@exception	IOException
	 */
	private String lireString(int taille) throws IOException {
		String ch = "";
		for (int i = 0; i < taille; i++) {
			ch += fichier.readChar();
		}
		return ch;
	}

	/**
	 * formatage d'une string a dimension en completant par des ' '
	 * ou en tronquant si necessaire	
	 *	@param	s		la string a completer
	 *	@param	taille	la taille de la string resultat
	 *	@return	la string mise a dimension
	 */
	public static String formaterString(String s, int taille) {
		String ch = "";
		for (int i = 0; i < taille; i++) {
			if (i < s.length()) {
				ch += s.charAt(i);
			} else {
				ch += " ";
			}
		}
		return ch;
	}
}