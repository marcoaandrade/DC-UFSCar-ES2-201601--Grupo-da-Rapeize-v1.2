package net.sf.jabref.bibtex;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Set;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.exporter.LatexFieldFormatter;
import net.sf.jabref.importer.ParserResult;
import net.sf.jabref.importer.fileformat.BibtexParser;
import net.sf.jabref.model.database.BibDatabaseMode;
import net.sf.jabref.model.entry.BibEntry;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParcialText {

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


    //teste do campos opcionais parcialmente preenchidos
    @Test
    public void testOP() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("1234", "article");
        //entrada de campos obrigatorios
        entry.setField("author", "NomeAutorTest");
        entry.setField("title", "titulo_test");
        entry.setField("journal", "Publicação_Test");
        entry.setField("year", "ano_test");
        entry.setField("volume", "nome_volume_test");
        entry.setField("pages", "numero_pagina_test");
        entry.setField("notes", "notas_test");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE +
                "  author  = {NomeAutorTest}," + Globals.NEWLINE +
                "  title   = {titulo_test}," + Globals.NEWLINE +
                "  journal = {Publicação_Test}," + Globals.NEWLINE +
                "  year    = {ano_test}," + Globals.NEWLINE +
                "  volume  = {nome_volume_test}," + Globals.NEWLINE +
                "  pages   = {numero_pagina_test}," + Globals.NEWLINE +
                "  notes   = {notas_test}," + Globals.NEWLINE +
                "}"+ Globals.NEWLINE;




        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void OProundTripTest() throws IOException {
        // @formatter:off
        String bibtexEntry = "@Article{chave_test," + Globals.NEWLINE +
                "  Author  = {NomeAutorTest}," + Globals.NEWLINE +
                "  Title   = {titulo_test}," + Globals.NEWLINE +
                "  Journal = {Publicação_Test}," + Globals.NEWLINE +
                "  Year    = {ano_test}," + Globals.NEWLINE +
                "  Volume  = {nome_volume_test}," + Globals.NEWLINE +
                "  Pages   = {numero_paginas_test}," + Globals.NEWLINE +
                "  Note    = {notas_test}," + Globals.NEWLINE +
                "}";
        // @formatter:on

        // read in bibtex string
        ParserResult result = BibtexParser.parse(new StringReader(bibtexEntry));

        Collection<BibEntry> entries = result.getDatabase().getEntries();
        assertEquals(1, entries.size());

        BibEntry entry = entries.iterator().next();
        assertEquals("chave_test", entry.getCiteKey());
        assertEquals(8, entry.getFieldNames().size());
        Set<String> fields = entry.getFieldNames();
        assertTrue(fields.contains("author"));
        assertEquals("NomeAutorTest", entry.getField("author"));

        //write out bibtex string
        StringWriter stringWriter = new StringWriter();
        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);
        String actual = stringWriter.toString();

        assertEquals(bibtexEntry, actual);
    }

    
}


