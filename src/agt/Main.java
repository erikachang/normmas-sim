import jacamo.infra.JaCaMoLauncher;
import jason.JasonException;

public class Main {
	public static void main(String[] args) {
		try {
			JaCaMoLauncher.main(new String[] {"checkpoint.jcm"});
		} catch (JasonException e) {
			e.printStackTrace();
		}
	}
}
