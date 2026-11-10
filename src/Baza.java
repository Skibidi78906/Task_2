import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Baza {
    List<Chelovek> ludi;

    public Baza() {
        ludi = new ArrayList<Chelovek>();
    }

    public Chelovek naytiPoId(String id) {
        for (int i = 0; i < ludi.size(); i++) {
            if (ludi.get(i).id.equals(id)) {
                return ludi.get(i);
            }
        }
        return null;
    }

    public int posledniyNomery() {
        int nomer = 0;
        for (int i = 0; i < ludi.size(); i++) {
            int c = Integer.parseInt(ludi.get(i).id);
            if (c > nomer) {
                nomer = c;
            }
        }
        return nomer;
    }

    public boolean chitatIzJson(String put) {
        ludi.clear();
        try {
            BufferedReader br = new BufferedReader(new FileReader(put));
            String stroka;
            Chelovek ch = null;
            boolean vnutri = false;
            while ((stroka = br.readLine()) != null) {
                stroka = stroka.trim();
                if (stroka.contains("\"ludi\"")) {
                    vnutri = true;
                    continue;
                }
                if (!vnutri) {
                    continue;
                }
                if (stroka.startsWith("}")) {
                    if (ch != null) {
                        ludi.add(ch);
                        ch = null;
                    }
                    continue;
                }
                if (stroka.startsWith("{")) {
                    ch = new Chelovek();
                    continue;
                }
                if (stroka.startsWith("]")) {
                    break;
                }
                if (stroka.contains("\"id\"")) {
                    ch.id = vydelit(stroka);
                } else if (stroka.contains("\"imya\"")) {
                    ch.imya = vydelit(stroka);
                } else if (stroka.contains("\"vozrast\"")) {
                    ch.vozrast = Integer.parseInt(vydelit(stroka));
                } else if (stroka.contains("\"gorod\"")) {
                    ch.gorod = vydelit(stroka);
                } else if (stroka.contains("\"pol\"")) {
                    ch.pol = vydelit(stroka);
                } else if (stroka.contains("\"opisaniye\"")) {
                    ch.opisaniye = vydelit(stroka);
                } else if (stroka.contains("\"foto\"")) {
                    ch.foto = vydelit(stroka);
                } else if (stroka.contains("\"sns\"")) {
                    ch.sns = vydelit(stroka);
                } else if (stroka.contains("\"interesy\"")) {
                    ch.interesy = vydelitMassiv(stroka);
                } else if (stroka.contains("\"lajki\"")) {
                    ch.lajki = vydelitMassiv(stroka);
                } else if (stroka.contains("\"druzya\"")) {
                    ch.druzya = vydelitMassiv(stroka);
                }
            }
            br.close();
            return ludi.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void sohranitVJson(String put) {
        try {
            FileWriter fw = new FileWriter(put);
            String s = "";
            s += "{\n";
            s += "  \"ludi\": [\n";
            for (int i = 0; i < ludi.size(); i++) {
                Chelovek ch = ludi.get(i);
                s += "    {\n";
                s += "      \"id\": \"" + ch.id + "\",\n";
                s += "      \"imya\": \"" + ch.imya + "\",\n";
                s += "      \"vozrast\": " + ch.vozrast + ",\n";
                s += "      \"gorod\": \"" + ch.gorod + "\",\n";
                s += "      \"pol\": \"" + ch.pol + "\",\n";
                s += "      \"opisaniye\": \"" + ch.opisaniye + "\",\n";
                s += "      \"foto\": \"" + ch.foto + "\",\n";
                s += "      \"sns\": \"" + ch.sns + "\",\n";
                s += "      \"interesy\": " + massivVStroku(ch.interesy) + ",\n";
                s += "      \"lajki\": " + massivVStroku(ch.lajki) + ",\n";
                s += "      \"druzya\": " + massivVStroku(ch.druzya) + "\n";
                s += "    }";
                if (i < ludi.size() - 1) {
                    s += ",";
                }
                s += "\n";
            }
            s += "  ]\n";
            s += "}\n";
            fw.write(s);
            fw.close();
            System.out.println("Sostoyaniye sohraneno v " + put);
        } catch (Exception e) {
            System.out.println("Ne poluchilos sohranit");
        }
    }

    private String massivVStroku(List<String> mas) {
        String s = "[";
        for (int i = 0; i < mas.size(); i++) {
            s += "\"" + mas.get(i) + "\"";
            if (i < mas.size() - 1) {
                s += ", ";
            }
        }
        s += "]";
        return s;
    }

    private String vydelit(String stroka) {
        int p = stroka.indexOf(':');
        String r = stroka.substring(p + 1).trim();
        r = r.replace("\"", "");
        if (r.endsWith(",")) {
            r = r.substring(0, r.length() - 1).trim();
        }
        return r;
    }

    private List<String> vydelitMassiv(String stroka) {
        List<String> r = new ArrayList<String>();
        int a = stroka.indexOf('[');
        int b = stroka.indexOf(']');
        if (a == -1 || b == -1) {
            return r;
        }
        String vnutri = stroka.substring(a + 1, b);
        vnutri = vnutri.trim();
        if (vnutri.length() == 0) {
            return r;
        }
        String[] chast = vnutri.split(",");
        for (int i = 0; i < chast.length; i++) {
            String o = chast[i].trim().replace("\"", "");
            r.add(o);
        }
        return r;
    }
}
