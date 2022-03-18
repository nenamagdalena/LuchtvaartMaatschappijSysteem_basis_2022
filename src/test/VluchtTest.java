package test;

import main.domeinLaag.*;

import static main.domeinLaag.Vlucht.isBezet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

public class VluchtTest {

	static LuchtvaartMaatschappij lvm ;
	static Fabrikant f1, f2;
	static VliegtuigType vtt1, vtt2, vtt3;
	static Vliegtuig vt1, vt2, vt3;
	static Luchthaven lh1, lh2, lh3, lh4;
	static Vlucht vl1, vl2, vl3;

	@BeforeEach
	public void initialize() {
		try {
			lvm = new LuchtvaartMaatschappij("NLM");
			f1 = new Fabrikant("Airbus","G. Dejenelle");
			vtt1 = f1.creeervliegtuigtype("A-200", 140);
			Calendar datum = Calendar.getInstance();
			datum.set(2000, 01, 01);
			vt1 = new Vliegtuig(lvm, vtt1, "Luchtbus 100", datum);
			Land l1 = new Land("Nederland", 31);
			Land l2 = new Land("Belgi�", 32);
			lh1 = new Luchthaven("Schiphol", "ASD", true, l1);
			lh2 = new Luchthaven("Tegel", "TEG", true, l2);
			Calendar vertr = Calendar.getInstance();
			vertr.set(2020, 03, 30, 14, 15, 0);
			Calendar aank = Calendar.getInstance();
			aank.set(2020, 03, 30, 15, 15, 0);
			vl1 = new Vlucht(vt1, lh1, lh2, vertr, aank );
			vertr.set(2020, 4, 1, 8, 15, 0);
			aank.set(2020, 4, 1, 9, 15, 0);
			vl2 = new Vlucht(vt1, lh1, lh2, vertr, aank );

			// Voor de Business Rules
			f2 = new Fabrikant("Fokker", "Anthony Fokker");
			vtt2 = f2.creeervliegtuigtype("Fokke-1", 120);
			vt2 = new Vliegtuig(lvm, vtt2, "Fokke1", datum);
			Land l3 = new Land("Frankrijk", 33);
			lh3 = new Luchthaven("Bonn", "BON", true, l3);
			lh4 = new Luchthaven("Charles de Gaule", "CDG", true, l3);

			// Vlucht van 1-7-2025 12:43 t/m 1-7-2025 15:36 Schiphol - Charles de Gaule
			vertr.set(2025, 7, 1, 12, 43, 0);
			aank.set(2025, 7, 1, 15, 36, 0);
			vl3 = new Vlucht(vt2, lh1, lh4, vertr, aank);

			// Voor nieuwe testmethoden
			vtt3 = f2.creeervliegtuigtype(null, 0);
			vt3 = new Vliegtuig(lvm, vtt3, null, datum);


		} catch (Exception e){
			String errorMessage =  "Exception: " + e.getMessage();
			System.out.println(errorMessage); 
		}
	}

	/**
	 * Business rule 1:
	 * De bestemming moet verschillen van het vertrekpunt van de vlucht.
	 */
	
	@Test
	public void testBestemmingMagNietGelijkZijnAanVertrek_False() {
		Vlucht vlucht = new Vlucht();
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh1);
			Luchthaven bestemming = vlucht.getBestemming();
			assertTrue(bestemming == null);
			vlucht.zetBestemming(lh1);
			// De test zou niet verder mogen komen: er moet al een exception gethrowd zijn.
			bestemming = vlucht.getBestemming();
			assertFalse(bestemming.equals(lh1));
		}
		catch(IllegalArgumentException e) {
			Luchthaven bestemming = vlucht.getBestemming();
			assertFalse(bestemming.equals(lh1));
		}
	}

	@Test
	public void testBestemmingMagNietGelijkZijnAanVertrek_True() {
		Vlucht vlucht = new Vlucht();
		Luchthaven bestemming;
		try {
			vlucht.zetVliegtuig(vt1);
			vlucht.zetVertrekpunt(lh2);
			bestemming = vlucht.getBestemming();
			assertTrue(bestemming == null);
			vlucht.zetBestemming(lh1);
			bestemming = vlucht.getBestemming();
			assertTrue(bestemming.equals(lh1));
		}
		catch(IllegalArgumentException e) {
			bestemming = vlucht.getBestemming();
			assertTrue(bestemming.equals(lh1));
		}
	}

	/**
	 * Business rule 2:
	 * De vertrektijd en aankomsttijd moeten geldig zijn en in de toekomst liggen.
	 */

	@Test
	public void testVertrektijdOngeldig(){
		Vlucht vlucht = new Vlucht();
		Calendar vertrektijd = Calendar.getInstance();
		vertrektijd.set(2025, 9, 31, 24, 00, 0);
		try{
			vlucht.zetVliegtuig(vt2);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertrektijd);
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
	@Test
	public void testAankomsttijdOngeldig(){
		Vlucht vlucht = new Vlucht();
		Calendar vertrektijd = Calendar.getInstance();
		vertrektijd.set(2025, 9, 30, 24, 00, 0);
		Calendar aankomsttijd = Calendar.getInstance();
		aankomsttijd.set(2025, 9, 30, 24, 01, 0);
		try{
			vlucht.zetVliegtuig(vt2);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertrektijd);
			vlucht.zetAankomstTijd(aankomsttijd);
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
	@Test
	public void testVertrektijdAankomsttijdBeidenGeldig(){
		Vlucht vlucht = new Vlucht();
		Calendar vertrektijd = Calendar.getInstance();
		vertrektijd.set(2025, 9, 30, 12, 00, 0);
		Calendar aankomsttijd = Calendar.getInstance();
		aankomsttijd.set(2025, 9, 30, 12, 01, 0);
		try{
			vlucht.zetVliegtuig(vt2);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertrektijd);
			vlucht.zetAankomstTijd(aankomsttijd);
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
	@Test
	public void testVertrektijdVerlopen(){
		Vlucht vlucht = new Vlucht();
		Calendar vertrektijd = Calendar.getInstance();
		vertrektijd.set(Calendar.MINUTE, vertrektijd.get(Calendar.MINUTE)-1);
		try{
			vlucht.zetVliegtuig(vt2);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertrektijd);
		}
		catch (Exception e){
			System.out.println(e);
		}
	}

	/**
	 * Business rule 4:
	 * Een vliegtuig kan maar voor ��n vlucht tegelijk gebruikt worden.
	 */

	@Test
	public void testVertrektijdValtOnderAndereVlucht() {
		Vlucht vlucht = new Vlucht();
		Calendar vertr = Calendar.getInstance();
		vertr.set(2025, 07, 01, 15, 35, 0);
		Calendar aank = Calendar.getInstance();
		aank.set(2025, 07, 1, 16, 36, 0);
		try {
			vlucht.zetVliegtuig(vt2);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			vlucht.zetAankomstTijd(aank);

			assertTrue(isBezet(vt2, vertr));

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Test
	public void testAankomsttijdValtOnderAndereVlucht() {
		Vlucht vlucht = new Vlucht();
		Calendar vertr = Calendar.getInstance();
		vertr.set(2025, 07, 01, 11, 36, 0);
		Calendar aank = Calendar.getInstance();
		aank.set(2025, 07, 1, 12, 44, 0);
		try {
			vlucht.zetVliegtuig(vt2);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			vlucht.zetAankomstTijd(aank);

			assertTrue(isBezet(vt2, aank));

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Test
	public void testVertrektijdEnAankomstTijdValtOnderAndereVlucht() {
		Vlucht vlucht = new Vlucht();
		Calendar vertr = Calendar.getInstance();
		vertr.set(2025, 07, 01, 12, 42, 0);
		Calendar aank = Calendar.getInstance();
		aank.set(2025, 07, 1, 15, 37, 0);
		Calendar midden = Calendar.getInstance();
		midden.set(2025, 07, 1, 13, 37, 0);
		try {
			vlucht.zetVliegtuig(vt2);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			vlucht.zetAankomstTijd(aank);

			assertTrue(isBezet(vt2, midden));

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Test
	public void testVertrektijdEnAankomstTijdValtNietOnderAndereVlucht() {
		Vlucht vlucht = new Vlucht();
		Calendar vertr = Calendar.getInstance();
		vertr.set(2025, 07, 01, 15, 37, 0);
		Calendar aank = Calendar.getInstance();
		aank.set(2025, 07, 1, 16, 37, 0);
		try {
			vlucht.zetVliegtuig(vt2);
			vlucht.zetVertrekpunt(lh3);
			vlucht.zetBestemming(lh1);
			vlucht.zetVertrekTijd(vertr);
			vlucht.zetAankomstTijd(aank);

			assertFalse(isBezet(vt2, vertr));

		} catch (Exception e) {
			System.out.println(e);
		}
	}
 	/**
	Twee nieuwe testmethoden Nena
  	**/

	@Test
	public void testVliegtuigHeeftGeenCapaciteit() {
		Vlucht vlucht = new Vlucht();
		try {
			vlucht.zetVliegtuig(vt3);

			assertEquals(0, vt3.geefCapaciteit());

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Test
	public void testVliegtuigHeeftGeenNaam() {
		Vlucht vlucht = new Vlucht();
		try {
			vlucht.zetVliegtuig(vt3);

			assertEquals(null, vt3.geefNaam());

		} catch (Exception e) {
			System.out.println(e);
		}
	}




}
