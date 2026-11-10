import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Baza baza = new Baza();
        String put = "ludi.json";
        if (!baza.chitatIzJson(put)) {
            System.out.println("Fayl " + put + " ne nayden");
            return;
        }
        System.out.println("Zachitano: " + baza.ludi.size() + " ludey");
        proverkaTselosti(baza);
        Scanner sc = new Scanner(System.in);
        boolean rabotaet = true;
        while (rabotaet) {
            pokazatMenyu();
            String vbor = chitatStroku(sc);
            if (vbor.equals("0")) {
                System.out.println("Vsee");
                rabotaet = false;
            } else if (vbor.equals("1")) {
                vseLudi(baza);
            } else if (vbor.equals("2")) {
                kartaCheloveka(baza, sc);
            } else if (vbor.equals("3")) {
                filterPoGorodu(baza, sc);
            } else if (vbor.equals("4")) {
                filterPoVozrastu(baza, sc);
            } else if (vbor.equals("5")) {
                gruppirovkaPoGorodu(baza);
            } else if (vbor.equals("6")) {
                podborPoInteresam(baza, sc);
            } else if (vbor.equals("7")) {
                obshhieZnaki(baza, sc);
            } else if (vbor.equals("8")) {
                vamiMozhetZnat(baza, sc);
            } else if (vbor.equals("9")) {
                dovatCheloveka(baza, sc);
            } else if (vbor.equals("10")) {
                postavitLajk(baza, sc);
            } else if (vbor.equals("11")) {
                dovatSvyaz(baza, sc);
            } else if (vbor.equals("12")) {
                baza.sohranitVJson(put);
            } else {
                System.out.println("Ne poyal nomer");
            }
        }
        sc.close();
    }

    static void pokazatMenyu() {
        System.out.println();
        System.out.println("======= MENYU =======");
        System.out.println("1. Vse ludi");
        System.out.println("2. Karta cheloveka");
        System.out.println("3. Poisk po gorodu");
        System.out.println("4. Filter po vozrastu");
        System.out.println("5. Gruppirovka po gorodu");
        System.out.println("6. Podbor po interesam");
        System.out.println("7. Obshhie znaki");
        System.out.println("8. Vami mozhet znat");
        System.out.println("9. Dovat cheloveka");
        System.out.println("10. Postavit lajk");
        System.out.println("11. Dovat svyaz");
        System.out.println("12. Sohranit v json");
        System.out.println("0. Vyjti");
        System.out.print("Nomer: ");
    }

    static String chitatStroku(Scanner sc) {
        return sc.nextLine().trim();
    }

    static void vseLudi(Baza baza) {
        for (int i = 0; i < baza.ludi.size(); i++) {
            Chelovek ch = baza.ludi.get(i);
            ch.pokazatKratko();
        }
    }

    static Chelovek vybratCheloveka(Baza baza, Scanner sc) {
        System.out.print("Vvedite id: ");
        String id = chitatStroku(sc);
        Chelovek ch = baza.naytiPoId(id);
        if (ch == null) {
            System.out.println("Takogo cheloveka net");
        }
        return ch;
    }

    static void kartaCheloveka(Baza baza, Scanner sc) {
        Chelovek ch = vybratCheloveka(baza, sc);
        if (ch == null) {
            return;
        }
        ch.pokazatPolnostyu();
        System.out.println("Kolichestvo lajkov: " + ch.lajki.size());
    }

    static void filterPoGorodu(Baza baza, Scanner sc) {
        System.out.print("Vvedite gorod: ");
        String gorod = chitatStroku(sc);
        List<Chelovek> res = baza.ludi.stream()
                .filter(ch -> ch.gorod.equalsIgnoreCase(gorod))
                .collect(Collectors.toList());
        if (res.size() == 0) {
            System.out.println("V " + gorod + " nikogo net");
            return;
        }
        for (int i = 0; i < res.size(); i++) {
            res.get(i).pokazatKratko();
        }
    }

    static void filterPoVozrastu(Baza baza, Scanner sc) {
        System.out.print("Minimalniy vozrast: ");
        int min = Integer.parseInt(chitatStroku(sc));
        System.out.print("Maksimalniy vozrast: ");
        int max = Integer.parseInt(chitatStroku(sc));
        List<Chelovek> res = baza.ludi.stream()
                .filter(ch -> ch.vozrast >= min && ch.vozrast <= max)
                .collect(Collectors.toList());
        if (res.size() == 0) {
            System.out.println("Takih net");
            return;
        }
        for (int i = 0; i < res.size(); i++) {
            res.get(i).pokazatKratko();
        }
    }

    static void gruppirovkaPoGorodu(Baza baza) {
        Map<String, Long> poGorodam = baza.ludi.stream()
                .collect(Collectors.groupingBy(ch -> ch.gorod, Collectors.counting()));
        for (Map.Entry<String, Long> e : poGorodam.entrySet()) {
            System.out.println(e.getKey() + " - " + e.getValue() + " chelovek");
        }
    }

    static void podborPoInteresam(Baza baza, Scanner sc) {
        Chelovek dlyaKogo = vybratCheloveka(baza, sc);
        if (dlyaKogo == null) {
            return;
        }
        List<Chelovek> res = new ArrayList<Chelovek>();
        for (int i = 0; i < baza.ludi.size(); i++) {
            Chelovek ch = baza.ludi.get(i);
            if (ch.id.equals(dlyaKogo.id)) {
                continue;
            }
            if (skolkObshhich(dlyaKogo, ch) > 0) {
                res.add(ch);
            }
        }
        res.sort(new Comparator<Chelovek>() {
            public int compare(Chelovek a, Chelovek b) {
                return skolkObshhich(dlyaKogo, b) - skolkObshhich(dlyaKogo, a);
            }
        });
        if (res.size() == 0) {
            System.out.println("Nikogo s obshhimi interesami net");
            return;
        }
        for (int i = 0; i < res.size(); i++) {
            Chelovek ch = res.get(i);
            System.out.println(ch.id + ". " + ch.imya + " - obshhich interesov: " + skolkObshhich(dlyaKogo, ch));
            System.out.println("   " + obshhieInteresy(dlyaKogo, ch).toString());
        }
    }

    static int skolkObshhich(Chelovek a, Chelovek b) {
        int kol = 0;
        for (int i = 0; i < a.interesy.size(); i++) {
            if (b.interesy.contains(a.interesy.get(i))) {
                kol++;
            }
        }
        return kol;
    }

    static List<String> obshhieInteresy(Chelovek a, Chelovek b) {
        List<String> res = new ArrayList<String>();
        for (int i = 0; i < a.interesy.size(); i++) {
            if (b.interesy.contains(a.interesy.get(i))) {
                res.add(a.interesy.get(i));
            }
        }
        return res;
    }

    static void obshhieZnaki(Baza baza, Scanner sc) {
        Chelovek perviy = vybratCheloveka(baza, sc);
        if (perviy == null) {
            return;
        }
        Chelovek vtoroy = vybratCheloveka(baza, sc);
        if (vtoroy == null) {
            return;
        }
        List<Chelovek> res = new ArrayList<Chelovek>();
        for (int i = 0; i < perviy.druzya.size(); i++) {
            String id = perviy.druzya.get(i);
            if (vtoroy.druzya.contains(id)) {
                Chelovek ch = baza.naytiPoId(id);
                if (ch != null && !ch.id.equals(perviy.id) && !ch.id.equals(vtoroy.id)) {
                    res.add(ch);
                }
            }
        }
        if (res.size() == 0) {
            System.out.println("Obshhikh znakomyh net");
            return;
        }
        for (int i = 0; i < res.size(); i++) {
            res.get(i).pokazatKratko();
        }
    }

    static void vamiMozhetZnat(Baza baza, Scanner sc) {
        Chelovek dlyaKogo = vybratCheloveka(baza, sc);
        if (dlyaKogo == null) {
            return;
        }
        List<Chelovek> res = new ArrayList<Chelovek>();
        for (int i = 0; i < baza.ludi.size(); i++) {
            Chelovek ch = baza.ludi.get(i);
            if (ch.id.equals(dlyaKogo.id)) {
                continue;
            }
            if (dlyaKogo.druzya.contains(ch.id)) {
                continue;
            }
            res.add(ch);
        }
        res.sort(new Comparator<Chelovek>() {
            public int compare(Chelovek a, Chelovek b) {
                return obshhieDruzya(dlyaKogo, b) - obshhieDruzya(dlyaKogo, a);
            }
        });
        int kol = 0;
        for (int i = 0; i < res.size(); i++) {
            Chelovek ch = res.get(i);
            int o = obshhieDruzya(dlyaKogo, ch);
            if (o <= 0) {
                break;
            }
            System.out.println((kol + 1) + ". " + ch.imya + " - obshhikh druzey: " + o);
            kol++;
            if (kol == 3) {
                break;
            }
        }
        if (kol == 0) {
            System.out.println("Nikogo rekomendovat");
        }
    }

    static int obshhieDruzya(Chelovek a, Chelovek b) {
        int kol = 0;
        for (int i = 0; i < a.druzya.size(); i++) {
            if (b.druzya.contains(a.druzya.get(i))) {
                kol++;
            }
        }
        return kol;
    }

    static void dovatCheloveka(Baza baza, Scanner sc) {
        Chelovek ch = new Chelovek();
        ch.id = String.valueOf(baza.posledniyNomery() + 1);
        System.out.print("Imya: ");
        ch.imya = chitatStroku(sc);
        System.out.print("Vozrast: ");
        ch.vozrast = Integer.parseInt(chitatStroku(sc));
        System.out.print("Gorod: ");
        ch.gorod = chitatStroku(sc);
        System.out.print("Pol: ");
        ch.pol = chitatStroku(sc);
        System.out.print("Opisaniye: ");
        ch.opisaniye = chitatStroku(sc);
        System.out.print("Foto (ssylka): ");
        ch.foto = chitatStroku(sc);
        System.out.print("Sns (ssylka): ");
        ch.sns = chitatStroku(sc);
        System.out.print("Interesy cherez zapyatuyu: ");
        String mass = chitatStroku(sc);
        String[] chast = mass.split(",");
        for (int i = 0; i < chast.length; i++) {
            String o = chast[i].trim();
            if (o.length() > 0) {
                ch.interesy.add(o);
            }
        }
        baza.ludi.add(ch);
        System.out.println("Chelovek s id " + ch.id + " dodan");
    }

    static void postavitLajk(Baza baza, Scanner sc) {
        Chelovek kto = vybratCheloveka(baza, sc);
        if (kto == null) {
            return;
        }
        Chelovek komu = vybratCheloveka(baza, sc);
        if (komu == null) {
            return;
        }
        if (komu.lajki.contains(kto.id)) {
            System.out.println("Lajk uzhe yetsya");
            return;
        }
        komu.lajki.add(kto.id);
        System.out.println("Lajk ot " + kto.imya + " postavljen");
    }

    static void dovatSvyaz(Baza baza, Scanner sc) {
        Chelovek a = vybratCheloveka(baza, sc);
        if (a == null) {
            return;
        }
        Chelovek b = vybratCheloveka(baza, sc);
        if (b == null) {
            return;
        }
        if (a.druzya.contains(b.id)) {
            System.out.println("Uzhe est svyaz");
            return;
        }
        a.druzya.add(b.id);
        b.druzya.add(a.id);
        System.out.println("Svyaz mezdu " + a.imya + " i " + b.imya + " dodana");
    }

    static void proverkaTselosti(Baza baza) {
        int oshibki = 0;
        for (int i = 0; i < baza.ludi.size(); i++) {
            Chelovek ch = baza.ludi.get(i);
            for (int j = 0; j < ch.druzya.size(); j++) {
                if (baza.naytiPoId(ch.druzya.get(j)) == null) {
                    System.out.println("U " + ch.imya + " svyaz s neznaim id " + ch.druzya.get(j));
                    oshibki++;
                }
            }
            for (int j = 0; j < ch.lajki.size(); j++) {
                if (baza.naytiPoId(ch.lajki.get(j)) == null) {
                    System.out.println("U " + ch.imya + " lajk ot neznaim id " + ch.lajki.get(j));
                    oshibki++;
                }
            }
            if (!ch.foto.startsWith("http")) {
                System.out.println("U " + ch.imya + " ne to foto");
                oshibki++;
            }
            if (!ch.sns.startsWith("http")) {
                System.out.println("U " + ch.imya + " ne to sns");
                oshibki++;
            }
        }
        System.out.println("Proverka ssylok zavershenaya, oshibok: " + oshibki);
    }
}
