package ohtu.intjoukkosovellus;

public class IntJoukko {

    public final static int KAPASITEETTI = 5, // aloitustalukon koko
            OLETUSKASVATUS = 5;  // luotava uusi taulukko on 
    // näin paljon isompi kuin vanha
    private int kasvatuskoko;     // Uusi taulukko on tämän verran vanhaa suurempi.
    private int[] luvut;      // Joukon luvut säilytetään taulukon alkupäässä. 
    private int alkioidenLkm;    // Tyhjässä joukossa alkioiden_määrä on nolla. 

    public IntJoukko() {
        this(KAPASITEETTI, OLETUSKASVATUS);
    }

    public IntJoukko(int kapasiteetti) {
        this(kapasiteetti, OLETUSKASVATUS);
    }

    public IntJoukko(int kapasiteetti, int kasvatuskoko) {
        if (kapasiteetti < 0) throw new IllegalArgumentException("Kapasiteetti ei voi olla negatiivinen!");
        
        if (kasvatuskoko < 0) throw new IllegalArgumentException("Kasvatuskoko ei voi olla negatiivinen!");
        
        this.luvut = new int[kapasiteetti];
        this.kasvatuskoko = kasvatuskoko;
    }

    public boolean lisaa(int luku) {
        if (!sisaltaa(luku)) {
            if (alkioidenLkm == luvut.length) {
                kasvata();
            }
            luvut[alkioidenLkm] = luku;
            alkioidenLkm++;
            return true;
        }
        return false;
    }

    private void kasvata() {
        int[] uusiTaulukko = new int[alkioidenLkm + kasvatuskoko];
        for (int i = 0; i < luvut.length; i++) {
            uusiTaulukko[i] = luvut[i];
        }
        luvut = uusiTaulukko;
    }

    public boolean sisaltaa(int luku) {
        return indeksi(luku) != -1;
    }

    private int indeksi(int luku) {
        for (int i = 0; i < alkioidenLkm; i++) {
            if (luku == luvut[i]) {
                return i;
            }
        }
        return -1;
    }

    public boolean poista(int luku) {
        int indeksi = indeksi(luku);
        if (indeksi != -1) {
            luvut[indeksi] = luvut[alkioidenLkm - 1];
            luvut[alkioidenLkm - 1] = 0;
            alkioidenLkm--;
            return true;
        }
        return false;
    }
    
    public int mahtavuus() {
        return alkioidenLkm;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < alkioidenLkm; i++) {
            sb.append(luvut[i]);
            if (i != alkioidenLkm - 1) sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    public int[] toIntArray() {
        int[] taulu = new int[alkioidenLkm];
        for (int i = 0; i < alkioidenLkm; i++) {
            taulu[i] = luvut[i];
        }
        return taulu;
    }

    public static IntJoukko yhdiste(IntJoukko a, IntJoukko b) {
        IntJoukko x = new IntJoukko(a.alkioidenLkm + b.alkioidenLkm);
        for (int i = 0; i < a.alkioidenLkm; i++) {
            x.lisaa(a.luvut[i]);
        }
        for (int i = 0; i < b.alkioidenLkm; i++) {
            x.lisaa(b.luvut[i]);
        }
        return x;
    }

    public static IntJoukko leikkaus(IntJoukko a, IntJoukko b) {
        IntJoukko y = new IntJoukko(a.alkioidenLkm);
        for (int i = 0; i < a.alkioidenLkm; i++) {
            if (b.sisaltaa(a.luvut[i])) y.lisaa(a.luvut[i]);
        }
        return y;
    }

    public static IntJoukko erotus(IntJoukko a, IntJoukko b) {
        IntJoukko z = new IntJoukko(a.alkioidenLkm);
        for (int i = 0; i < a.alkioidenLkm; i++) {
            if (!b.sisaltaa(a.luvut[i])) z.lisaa(a.luvut[i]);
        }
        return z;
    }
}
