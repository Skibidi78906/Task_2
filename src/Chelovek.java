import java.util.ArrayList;
import java.util.List;

public class Chelovek {
    String id;
    String imya;
    int vozrast;
    String gorod;
    String pol;
    String opisaniye;
    String foto;
    String sns;
    List<String> interesy;
    List<String> lajki;
    List<String> druzya;

    public Chelovek() {
        interesy = new ArrayList<String>();
        lajki = new ArrayList<String>();
        druzya = new ArrayList<String>();
    }

    public void pokazatKratko() {
        System.out.println(id + ". " + imya + ", " + vozrast + " let, " + gorod + ", interesy: " + interesy.toString());
    }

    public void pokazatPolnostyu() {
        System.out.println("Imya: " + imya);
        System.out.println("Pol: " + pol);
        System.out.println("Vozrast: " + vozrast);
        System.out.println("Gorod: " + gorod);
        System.out.println("Opisaniye: " + opisaniye);
        System.out.println("Foto: " + foto);
        System.out.println("Sns: " + sns);
        System.out.println("Interesy: " + interesy.toString());
        System.out.println("Lajki: " + lajki.toString());
        System.out.println("Druzya: " + druzya.toString());
    }
}
