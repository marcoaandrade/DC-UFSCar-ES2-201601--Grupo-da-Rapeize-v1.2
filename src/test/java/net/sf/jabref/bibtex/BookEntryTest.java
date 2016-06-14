package net.sf.jabref.bibtex;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Set;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.exporter.LatexFieldFormatter;
import net.sf.jabref.model.database.BibDatabaseMode;
import net.sf.jabref.model.entry.BibEntry;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BookEntryTest {

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

    //Caso de teste 1: Todas as entradas obrigatórias preenchidas
    @Test
    public void testInsertBook1() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("1234", "book");

        entry.setField("title", "1234");
        entry.setField("publisher", "DC");
        entry.setField("year", "2013");
        entry.setField("author", "Scott Snyder");
        entry.setField("editor", "DC");
        entry.setField("bibtexkey", "W4");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String atual = stringWriter.toString();

        String esperado = Globals.NEWLINE + "@Book{W4," + Globals.NEWLINE + "  title     = {1234}," + Globals.NEWLINE
                + "  publisher = {DC}," + Globals.NEWLINE + "  year      = {2013}," + Globals.NEWLINE
                + "  author    = {Scott Snyder}," + Globals.NEWLINE + "  editor    = {DC}," + Globals.NEWLINE
                + "}" + Globals.NEWLINE;

        assertEquals(esperado, atual);
        assertEquals("W4", entry.getCiteKey());

    }
    


//Caso de teste 2: Entradas obrigatórias parcialmente preenchidas
    @Test
    public void testInsertBook2() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("Another book", "book");

        entry.setField("title", "Another book");
        entry.setField("author", "Brian K. Vaughan");
        entry.setField("editor", "Vertigo");
        entry.setField("bibtexkey", "V9");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String atual = stringWriter.toString();

        String esperado = Globals.NEWLINE + "@Book{V9," + Globals.NEWLINE + "  title  = {Another book},"
                + Globals.NEWLINE + "  author = {Brian K. Vaughan}," + Globals.NEWLINE + "  editor = {Vertigo},"
                + Globals.NEWLINE + "}" + Globals.NEWLINE;

        assertEquals("V9", entry.getCiteKey());
        assertEquals(4, entry.getFieldNames().size());
        Set<String> fields = entry.getFieldNames();
        assertTrue(fields.contains("author"));
        assertEquals("Brian K. Vaughan", entry.getField("author"));

        assertEquals(esperado, atual);
    }
    
     //Caso de teste 3: Entradas Obrigatórias e Opcionais totalmente preenchidas
    @Test
    public void testInsertBook3() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("Book X", "book");

        entry.setField("title", "Book X");
        entry.setField("publisher", "ABC");
        entry.setField("year", "2012");
        entry.setField("author", "Neal Adams");
        entry.setField("editor", "Editor EDT");
        entry.setField("bibtexkey", "X2");
        entry.setField("volume", "Three");
        entry.setField("series", "123");
        entry.setField("edition", "1st");
        entry.setField("note", "Note");
        entry.setField("number", "43");
        entry.setField("Address", "1234");
        entry.setField("Month", "January");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String atual = stringWriter.toString();

        String esperado = Globals.NEWLINE + "@Book{X2," + Globals.NEWLINE + "  title     = {Book X}," + Globals.NEWLINE
                + "  publisher = {ABC}," + Globals.NEWLINE + "  year      = {2012}," + Globals.NEWLINE
                + "  author    = {Neal Adams}," + Globals.NEWLINE + "  editor    = {Editor EDT}," + Globals.NEWLINE
                + "  volume    = {Three}," + Globals.NEWLINE + "  number    = {43}," + Globals.NEWLINE
                + "  series    = {123}," + Globals.NEWLINE + "  address   = {1234}," + Globals.NEWLINE
                + "  edition   = {1st}," + Globals.NEWLINE + "  month     = {January}," + Globals.NEWLINE
                + "  note      = {Note}," + Globals.NEWLINE + "}" + Globals.NEWLINE;

        assertEquals("X2", entry.getCiteKey());
        assertEquals(13, entry.getFieldNames().size());
        Set<String> fields = entry.getFieldNames();
        assertTrue(fields.contains("author"));
        assertEquals("Neal Adams", entry.getField("author"));

        assertEquals(esperado, atual);
        
        }
        
        //Caso de teste 4: Entradas opcionais parcialmente preenchidas (com entradas obrigatórias parcialmente preenchidas)
    @Test
    public void testInsertBook4() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("1234", "book");

        entry.setField("title", "1234");
        entry.setField("year", "2013");
        entry.setField("author", "Scott Snyder");
        entry.setField("bibtexkey", "W4");
        entry.setField("volume", "Three");
        entry.setField("edition", "1st");
        entry.setField("number", "43");
        entry.setField("month", "January");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String atual = stringWriter.toString();

        String esperado = Globals.NEWLINE + "@Book{W4," + Globals.NEWLINE + "  title   = {1234}," + Globals.NEWLINE
                + "  year    = {2013}," + Globals.NEWLINE + "  author  = {Scott Snyder}," + Globals.NEWLINE
                + "  volume  = {Three}," + Globals.NEWLINE + "  number  = {43}," + Globals.NEWLINE
                + "  edition = {1st}," + Globals.NEWLINE + "  month   = {January}," + Globals.NEWLINE + "}"
                + Globals.NEWLINE;

        assertEquals("W4", entry.getCiteKey());
        assertEquals(8, entry.getFieldNames().size());
        Set<String> fields = entry.getFieldNames();
        assertTrue(fields.contains("author"));
        assertEquals("Scott Snyder", entry.getField("author"));

        assertEquals(esperado, atual);
        }
    }
