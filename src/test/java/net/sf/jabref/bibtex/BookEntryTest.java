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
    
}
