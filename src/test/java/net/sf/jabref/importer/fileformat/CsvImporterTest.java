package net.sf.jabref.importer.fileformat;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.importer.OutputPrinterToNull;
import net.sf.jabref.model.entry.BibEntry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CsvImporterTest {

    private CsvImporter importer;


    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        importer = new CsvImporter();
    }

    @Test
    public void testIsRecognizedFormat() throws IOException {
        try (InputStream stream = CsvImporter.class.getResourceAsStream("CsvCompleto.csv")) {
            assertTrue(importer.isRecognizedFormat(stream));
        }
    }

    //Teste para importação de arquivos CSV com todos os campos obrigatórios preenchidos
    @Test
    public void testImportCSVCompleto() throws IOException {
        try (InputStream is = CsvImporter.class.getResourceAsStream("CsvCompleto.csv")) {

            List<BibEntry> entries = importer.importEntries(is, new OutputPrinterToNull());

            BibEntry testEntry = entries.get(0);
            Assert.assertEquals("1936", testEntry.getField("year"));
            Assert.assertEquals("John Maynard Keynes", testEntry.getField("author"));
            Assert.assertEquals("The General Theory of Employment, Interest and Money", testEntry.getField("title"));

            testEntry = entries.get(1);
            Assert.assertEquals("2003", testEntry.getField("year"));
            Assert.assertEquals("Boldrin & Levine", testEntry.getField("author"));
            Assert.assertEquals("Case Against Intellectual Monopoly", testEntry.getField("title"));

            testEntry = entries.get(2);
            Assert.assertEquals("2004", testEntry.getField("year"));
            Assert.assertEquals("ROBERT HUNT AND JAMES BESSEN", testEntry.getField("author"));
            Assert.assertEquals("The Software Patent Experiment", testEntry.getField("title"));
        }
    }

    //Teste para importação de arquivo CSV com alguns campos obrigatórios preenchidos
    @Test
    public void testImportCSVIncompleto() throws IOException {
        try (InputStream is = CsvImporter.class.getResourceAsStream("CsvIncompleto.csv")) {

            List<BibEntry> entries = importer.importEntries(is, new OutputPrinterToNull());

            BibEntry testEntry = entries.get(0);
            Assert.assertEquals("1991", testEntry.getField("year"));
            Assert.assertEquals("Marco Uski", testEntry.getField("author"));
            Assert.assertEquals(" ", testEntry.getField("title"));

            testEntry = entries.get(1);
            Assert.assertEquals("1967", testEntry.getField("year"));
            Assert.assertEquals(" ", testEntry.getField("author"));
            Assert.assertEquals("Sandman", testEntry.getField("title"));
        }
    }

    @Test
    public void testGetFormatName() {
        assertEquals("CSV", importer.getFormatName());
    }

    @Test
    public void testGetExtensions() {
        assertEquals("csv", importer.getExtensions());
    }
}
