import java.io.IOException;

public class GestionGuerrier {
	private static java.util.Scanner scanner = new java.util.Scanner(System.in);
	private static RegistreGuerriers registreGuerriers;
	private static int seuil = 0;

	public static void main(String[] args) {
		try {
			registreGuerriers = new RegistreGuerriers();
			char continuer = 'O';
			System.out.print("Zone d'enregistrement de guerriers. Choisissez le seuil : ");
			encoderSeuil();
			System.out.println();
			while (continuer == 'O') {
				System.out.println(registreGuerriers.tousLesEmplacements());
				System.out.print("Nom du guerrier : ");
				String nomGuerrier = scanner.next();
				System.out.print("Score du guerrier : ");
				int score = scanner.nextInt();
				gererScoreGuerrier(nomGuerrier, score);
				System.out.print("Encore un ? (O/N) ");
				continuer = scanner.next().charAt(0);
				System.out.println();
			}
			registreGuerriers.fermerRegistre();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void encoderSeuil() {
		while (seuil == 0) {
			seuil = scanner.nextInt();
			if (seuil == 0) {
				System.out.println("Attention la valeur 0 sert a supprime un guerrier !");
				System.out.print("Seuil : ");
			}
		}
	}

	private static void gererScoreGuerrier(String nomGuerrier, int score) throws IOException {
		if (estDejaPresent(nomGuerrier)) {
			if (score == 0) {
				registreGuerriers.supprimerGuerrier(nomGuerrier);
				System.out.println("Le guerrier a ete supprime suite a un score de 0.");
			} else {
				mettreAJourGuerrier(nomGuerrier, score);
			}
		} else {
			if (score < seuil) {
				System.out.println("Score trop bas pour etre un bon guerrier.");
			} else {
				ajouterGuerrier(nomGuerrier, score);
			}
		}
	}

	private static void ajouterGuerrier(String nomGuerrier, int score) throws IOException {
		if (registreGuerriers.ajouterGuerrier(new Guerrier(nomGuerrier, score)) == -1) {
			System.out.println("Fichier complet.");
		} else {
			System.out.println("Ajout du guerrier.");
		}
	}

	private static void mettreAJourGuerrier(String nomGuerrier, int score) throws IOException {
		if (registreGuerriers.consulterGuerrier(nomGuerrier).getScore() >= score) {
			System.out.println("Le guerrier n'a pas ameliore son score.");
		} else {
			registreGuerriers.mettreAJourGuerrier(nomGuerrier, score);
			System.out.println("Score du guerrier mis a jour.");
		}
	}

	private static boolean estDejaPresent(String nom) throws IOException {
		return registreGuerriers.trouverAdresseGuerrier(nom) != -1;
	}
}
