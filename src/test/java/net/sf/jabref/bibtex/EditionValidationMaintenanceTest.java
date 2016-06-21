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

import static org.junit.Assert.assertEquals;

public class EditionValidationMaintenanceTest {

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

    //Teste 1: Caracteres numericos
    @Test
    public void testEdition1() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("A book", "book");

        entry.setField("title", "A book");
        entry.setField("publisher", "DC");
        entry.setField("year", "1899");
        entry.setField("author", "Scott Snyder");
        entry.setField("editor", "DC");
        entry.setField("edition", "1");
        entry.setCiteKey("W4");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String atual = stringWriter.toString();

        String esperado = Globals.NEWLINE + "@Book{W4," + Globals.NEWLINE + "  title     = {A book}," + Globals.NEWLINE
                + "  publisher = {DC}," + Globals.NEWLINE + "  author    = {Scott Snyder}," + Globals.NEWLINE
                + "  editor    = {DC}," + Globals.NEWLINE + "}" + Globals.NEWLINE;

        assertEquals(esperado, atual);
        Assert.assertNull(entry.getField("edition"));
    }

    //Teste 2: Caracteres do tipo letra
    @Test
    public void testEdition2() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("A book", "book");

        entry.setField("title", "A book");
        entry.setField("publisher", "DC");
        entry.setField("year", "1899");
        entry.setField("author", "Scott Snyder");
        entry.setField("editor", "DC");
        entry.setField("edition", "a");
        entry.setCiteKey("W4");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String atual = stringWriter.toString();

        String esperado = Globals.NEWLINE + "@Book{W4," + Globals.NEWLINE + "  title     = {A book}," + Globals.NEWLINE
                + "  publisher = {DC}," + Globals.NEWLINE + "  author    = {Scott Snyder}," + Globals.NEWLINE
                + "  editor    = {DC}," + Globals.NEWLINE + "}" + Globals.NEWLINE;

        assertEquals(esperado, atual);
        Assert.assertNull(entry.getField("edition"));
    }

    //Teste 3: Caracteres alfanumericos
    @Test
    public void testEdition3() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("A book", "book");

        entry.setField("title", "A book");
        entry.setField("publisher", "DC");
        entry.setField("year", "1899");
        entry.setField("author", "Scott Snyder");
        entry.setField("editor", "DC");
        entry.setField("edition", "edition12");
        entry.setCiteKey("W4");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String atual = stringWriter.toString();

        String esperado = Globals.NEWLINE + "@Book{W4," + Globals.NEWLINE + "  title     = {A book}," + Globals.NEWLINE
                + "  publisher = {DC}," + Globals.NEWLINE + "  author    = {Scott Snyder}," + Globals.NEWLINE
                + "  editor    = {DC}," + Globals.NEWLINE + "}" + Globals.NEWLINE;

        assertEquals(esperado, atual);
        Assert.assertNull(entry.getField("edition"));
    }

    //Teste 4: Padrozinacao incorreta
    @Test
    public void testEdition4() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("A book", "book");

        entry.setField("title", "A book");
        entry.setField("publisher", "DC");
        entry.setField("year", "1899");
        entry.setField("author", "Scott Snyder");
        entry.setField("editor", "DC");
        entry.setField("edition", "1ª edicao");
        entry.setCiteKey("W4");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String atual = stringWriter.toString();

        String esperado = Globals.NEWLINE + "@Book{W4," + Globals.NEWLINE + "  title     = {A book}," + Globals.NEWLINE
                + "  publisher = {DC}," + Globals.NEWLINE + "  author    = {Scott Snyder}," + Globals.NEWLINE
                + "  editor    = {DC}," + Globals.NEWLINE + "}" + Globals.NEWLINE;

        assertEquals(esperado, atual);
        Assert.assertNull(entry.getField("edition"));
    }

    //Teste 5: Padronizacao correta
    @Test
    public void testEdition5() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("A book", "book");

        entry.setField("title", "A book");
        entry.setField("publisher", "DC");
        entry.setField("year", "1899");
        entry.setField("author", "Scott Snyder");
        entry.setField("editor", "DC");
        entry.setField("edition", "5ª");
        entry.setCiteKey("W4");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String atual = stringWriter.toString();

        String esperado = Globals.NEWLINE + "@Book{W4," + Globals.NEWLINE + "  title     = {A book}," + Globals.NEWLINE
                + "  publisher = {DC}," + Globals.NEWLINE + "  author    = {Scott Snyder}," + Globals.NEWLINE
                + "  editor    = {DC}," + Globals.NEWLINE + "  edition   = {5ª}," + Globals.NEWLINE + "}"
                + Globals.NEWLINE;

        assertEquals(esperado, atual);
        assertEquals("5ª", entry.getField("edition"));
    }

    //Teste 6: Padronizado mas sem valor numerico
    @Test
    public void testEdition6() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("A book", "book");

        entry.setField("title", "A book");
        entry.setField("publisher", "DC");
        entry.setField("year", "1899");
        entry.setField("author", "Scott Snyder");
        entry.setField("editor", "DC");
        entry.setField("edition", "ª");
        entry.setCiteKey("W4");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String atual = stringWriter.toString();

        String esperado = Globals.NEWLINE + "@Book{W4," + Globals.NEWLINE + "  title     = {A book}," + Globals.NEWLINE
                + "  publisher = {DC}," + Globals.NEWLINE + "  author    = {Scott Snyder}," + Globals.NEWLINE
                + "  editor    = {DC}," + Globals.NEWLINE + "}"
                + Globals.NEWLINE;

        assertEquals(esperado, atual);
        Assert.assertNull(entry.getField("edition"));
    }
}
