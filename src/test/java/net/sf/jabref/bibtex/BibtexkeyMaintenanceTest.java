package net.sf.jabref.bibtex;

import java.io.IOException;
import java.io.StringWriter;
import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.exporter.LatexFieldFormatter;
import net.sf.jabref.model.database.BibDatabaseMode;
import net.sf.jabref.model.entry.BibEntry;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BibtexkeyMaintenanceTest {

    private BibEntryWriter writer;
    private static JabRefPreferences backup;


    @BeforeClass
    public static void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        backup = Globals.prefs;
    }

    @AfterClass
    public static void tearDown() {
        Globals.prefs.overwritePreferences(backup);
    }

    @Before
    public void setUpWriter() {
        writer = new BibEntryWriter(new LatexFieldFormatter(), true);
    }

    //Teste para bibtexkey com menos de 2 caracteres e não nulo (letra)
    @Test
    public void testBibtexkey1() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("A book", "book");

        entry.setCiteKey("a");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        Assert.assertNotEquals("a", entry.getCiteKey());
        System.out.println("Teste - bibtexkey com menos de 2 caracteres (letra)");
        System.out.println("Entrada: a");
        System.out.println("Saída:" + entry.getCiteKey());
        System.out.println("");
    }

    //Teste para bibtexkey com menos de 2 caracteres e não nulo (numero)
    @Test
    public void testBibtexkey2() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("A book", "book");

        entry.setCiteKey("5");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        Assert.assertNotEquals("5", entry.getCiteKey());
        System.out.println("Teste - bibtexkey com menos de 2 caracteres (numero)");
        System.out.println("Entrada: 5");
        System.out.println("Saída:" + entry.getCiteKey());
        System.out.println("");
    }

    //Teste para bibtexkey começando por caractere numerico
    @Test
    public void testBibtexkey3() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("An article", "article");

        entry.setCiteKey("1abc");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        Assert.assertNotEquals("1abc", entry.getCiteKey());

        System.out.println("Teste - bibtexkey começando com número");
        System.out.println("Entrada: 1abc");
        System.out.println("Saída:" + entry.getCiteKey());
        System.out.println("");
    }

    //Teste para bibtexkey valida (primeiro caractere maiusculo)
    @Test
    public void testBibtexkey4() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("Another book", "book");

        entry.setCiteKey("A12x");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        Assert.assertEquals("A12x", entry.getCiteKey());

        System.out.println("Teste - bibtexkey valida começando com maiuscula");
        System.out.println("Entrada: A12x");
        System.out.println("Saída:" + entry.getCiteKey());
        System.out.println("");
    }

    //Teste para bibtexkey valida (primeiro caractere minusculo)
    @Test
    public void testBibtexkey5() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("Another book", "book");

        entry.setCiteKey("bb8");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        Assert.assertEquals("bb8", entry.getCiteKey());

        System.out.println("Teste - bibtexkey valida começanco com minuscula");
        System.out.println("Entrada: A12x");
        System.out.println("Saída:" + entry.getCiteKey());
        System.out.println("");
    }

    //Teste para bibtexkey vazia
    @Test
    public void testBibtexkey6() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("Another article", "article");

        //entry.setCiteKey("");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        Assert.assertNull(entry.getCiteKey());

        System.out.println("Teste - bibtexkey vazia");
        System.out.println("Entrada:");
        System.out.println("Saída:" + entry.getCiteKey());
        System.out.println("");
    }
}