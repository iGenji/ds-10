public class EnregistrementGuerrier {
	private Guerrier guerrier;
	// Enregistrement est occupe
	public static final char OCCUPE = 'O';
	// Enregistrement est vide
	public static final char VIDE = 'V';
	// Enregistrement est supprime
	public static final char SUPPRIME = 'S';
	// Statut de l'enregistrement (O/V/S)
	private char statut;

	/**
	 * Construction objet par defaut
	 */
	public EnregistrementGuerrier() {
		guerrier = new Guerrier("", 0);
		setStatut(EnregistrementGuerrier.VIDE);
	}

	public EnregistrementGuerrier(Guerrier guerrier, char statut) {
		this.guerrier = guerrier;
		setStatut(statut);
	}

	public char getStatut() {
		return statut;
	}

	public void setStatut(char statut) {
		this.statut = statut;
	}
	
	public Guerrier getGuerrier() {
		return guerrier;
	}
	
	public String toString(){
		return guerrier +"\t"+statut;
	}	
}