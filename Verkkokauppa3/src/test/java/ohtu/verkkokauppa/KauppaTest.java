package ohtu.verkkokauppa;

import org.junit.*;
import static org.mockito.Mockito.*;

public class KauppaTest {

    private Kauppa kauppa;
    private Varasto varasto;
    private Pankki pankki;
    private Viitegeneraattori viite;

    @Before
    public void setUp() {
        varasto = mock(Varasto.class);
        pankki = mock(Pankki.class);
        viite = mock(Viitegeneraattori.class);
        kauppa = new Kauppa(varasto, pankki, viite);
    }

    @Test
    public void ostoksenPaatyttyaPankinMetodiaTilisiirtoKutsutaan() {
        when(viite.uusi()).thenReturn(42);

        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");

        verify(pankki).tilisiirto(anyString(), anyInt(), anyString(), anyString(), anyInt());
    }

    @Test
    public void ostoksenPaatyttyaPankinMetodiaTilisiirtoKutsutaanOikeillaArvoilla() {
        when(viite.uusi()).thenReturn(15);

        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "kala", 8));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");

        verify(pankki).tilisiirto(eq("pekka"), eq(15), eq("12345"), anyString(), eq(8));
    }

    @Test
    public void ostoksenPaatyttyaPankinMetodiaTilisiirtoKutsutaanOikeillaArvoilla2() {
        when(viite.uusi()).thenReturn(18);

        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.saldo(2)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "kala", 8));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(1, "tomaatti", 2));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("maria", "7812");

        verify(pankki).tilisiirto(eq("maria"), eq(18), eq("7812"), anyString(), eq(10));
    }

    @Test
    public void ostoksenPaatyttyaPankinMetodiaTilisiirtoKutsutaanOikeillaArvoilla3() {
        when(viite.uusi()).thenReturn(5);

        when(varasto.saldo(6)).thenReturn(10);
        when(varasto.haeTuote(6)).thenReturn(new Tuote(1, "omena", 3));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(6);
        kauppa.lisaaKoriin(6);
        kauppa.tilimaksu("kalle", "111111");

        verify(pankki).tilisiirto(eq("kalle"), eq(5), eq("111111"), anyString(), eq(6));
    }

    @Test
    public void MetodiaTilisiirtoKutsutaanOikeillaArvoillaKunLisataanLoppuOlevaTuote() {
        when(viite.uusi()).thenReturn(5);

        when(varasto.saldo(6)).thenReturn(10);
        when(varasto.saldo(7)).thenReturn(0);
        when(varasto.haeTuote(6)).thenReturn(new Tuote(1, "omena", 3));
        when(varasto.haeTuote(7)).thenReturn(new Tuote(1, "kurkku", 1));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(6);
        kauppa.lisaaKoriin(7);
        kauppa.tilimaksu("kalle", "111111");

        verify(pankki).tilisiirto(eq("kalle"), eq(5), eq("111111"), anyString(), eq(3));
        verify(varasto, times(0)).haeTuote(7);
    }

    public void edellisenAsiakkaanTiedotNollaantuu() {
        when(viite.uusi()).thenReturn(5).thenReturn(6);

        when(varasto.saldo(6)).thenReturn(10);
        when(varasto.saldo(7)).thenReturn(5);
        when(varasto.haeTuote(6)).thenReturn(new Tuote(1, "omena", 3));
        when(varasto.haeTuote(7)).thenReturn(new Tuote(1, "kurkku", 1));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(6);
        kauppa.tilimaksu("kalle", "111111");

        verify(pankki).tilisiirto(eq("kalle"), eq(5), eq("111111"), anyString(), eq(3));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(7);
        kauppa.tilimaksu("heidi", "543");

        verify(pankki).tilisiirto(eq("heidi"), eq(6), eq("543"), anyString(), eq(1));
    }

    @Test
    public void pyydetaanUusiViiteJokaiseenMaksuun() {
        when(viite.uusi()).thenReturn(9).thenReturn(10).thenReturn(11);

        when(varasto.saldo(6)).thenReturn(10);
        when(varasto.saldo(7)).thenReturn(5);
        when(varasto.haeTuote(6)).thenReturn(new Tuote(1, "omena", 3));
        when(varasto.haeTuote(7)).thenReturn(new Tuote(1, "kurkku", 1));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(7);
        kauppa.tilimaksu("juha", "568111");

        verify(viite, times(1)).uusi();

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(6);
        kauppa.lisaaKoriin(6);
        kauppa.tilimaksu("heidi", "543");

        verify(viite, times(2)).uusi();

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(7);
        kauppa.tilimaksu("heidi", "543");

        verify(viite, times(3)).uusi();
    }

    @Test
    public void poistaminenKutsuuPalautaVarastoonMetodia() {
        when(varasto.saldo(1)).thenReturn(10);
        Tuote t = new Tuote(1, "mehu", 5);
        when(varasto.haeTuote(1)).thenReturn(t);

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.poistaKorista(1);
        
        verify(varasto, times(1)).palautaVarastoon(eq(t));
    }
}
