public class Guerrier {
	private int score;
	private String nom;

	public Guerrier(String nom, int score) {
		this.nom = nom;
		setScore(score);
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getNom() {
		return nom;
	}

	@Override
	public String toString() {
		return "Guerrier [score=" + score + ", nom=" + nom + "]";
	}
}
