package ohtu.intjoukkosovellus;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntJoukkoKaksiparametrisellaKonstruktorillaTest extends IntJoukkoTest {

    @Before
    public void setUp() {
        joukko = new IntJoukko(4, 2);
        joukko.lisaa(10);
        joukko.lisaa(3);
    }

    @Test
    public void luoJoukon() {
        boolean heittaaPoikkeuksen = false;
        try {
            IntJoukko i = new IntJoukko(-1, 2);
        } catch (Exception e) {
            heittaaPoikkeuksen = true;
        }
        assertFalse(heittaaPoikkeuksen);
    }

    @Test
    public void kapasiteettiEiVoiOllaNegatiivinen() {
        boolean heittaaPoikkeuksen = false;
        try {
            IntJoukko i = new IntJoukko(-1, 2);
        } catch (Exception e) {
            heittaaPoikkeuksen = true;
        }
        assertTrue(heittaaPoikkeuksen);
    }

    @Test
    public void kasvatusKokoEiVoiOllaNegatiivinen() {
        boolean heittaaPoikkeuksen = false;
        try {
            IntJoukko i = new IntJoukko(1, -2);
        } catch (Exception e) {
            heittaaPoikkeuksen = true;
        }
        assertTrue(heittaaPoikkeuksen);
    }
}
