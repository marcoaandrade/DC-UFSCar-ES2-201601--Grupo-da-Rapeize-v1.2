﻿package net.sf.jabref.bibtex;

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


     //Caso 1: TODOS os Campos Obrigatorios preenchidos
    @Test
    public void testOBGT() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("1234", "article");
        entry.setField("author", "NomeAutorTest");
        entry.setField("title", "titulo_test");
        entry.setField("journal", "Publicação_Test");
        entry.setField("year", "ano_test");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE +
                "  author  = {NomeAutorTest}," + Globals.NEWLINE +
                "  title   = {titulo_test}," + Globals.NEWLINE +
                "  journal = {Publicação_Test}," + Globals.NEWLINE +
                "  year    = {ano_test}," + Globals.NEWLINE +
                "}"+ Globals.NEWLINE;




        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void OBGTroundTripTest() throws IOException {
        // @formatter:off
        String bibtexEntry = "@Article{chave_test," + Globals.NEWLINE +
                "  Author  = {NomeAutorTest}," + Globals.NEWLINE +
                "  Title   = {titulo_test}," + Globals.NEWLINE +
                "  Journal = {Publicação_Test}," + Globals.NEWLINE +
                "  Year    = {ano_test}" + Globals.NEWLINE +
                "}";
        // @formatter:on

        // read in bibtex string
        ParserResult result = BibtexParser.parse(new StringReader(bibtexEntry));

        Collection<BibEntry> entries = result.getDatabase().getEntries();
        assertEquals(1, entries.size());

        BibEntry entry = entries.iterator().next();
        assertEquals("chave_test", entry.getCiteKey());
        assertEquals(5, entry.getFieldNames().size());
        Set<String> fields = entry.getFieldNames();
        assertTrue(fields.contains("author"));
        assertEquals("NomeAutorTest", entry.getField("author"));

        //write out bibtex string
        StringWriter stringWriter = new StringWriter();
        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);
        String actual = stringWriter.toString();

        assertEquals(bibtexEntry, actual);
    }


    //Caso 2: Campos Obrigatórios PARCIALMENTE preenchidos
    @Test
    public void testOBGP() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("1234", "article");
        entry.setField("author", "NomeAutorTest");
        entry.setField("title", "titulo_test");
        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE +
                "  author = {NomeAutorTest}," + Globals.NEWLINE +
                "  title  = {titulo_test}," + Globals.NEWLINE +
               "}"+ Globals.NEWLINE;

        // @formatter:on
        assertEquals(expected, actual);
    }
    
    @Test
    public void OBGProundTripTest() throws IOException {
        // @formatter:off
        String bibtexEntry = "@Article{," + Globals.NEWLINE +
                "  Author  = {NomeAutorTest}," + Globals.NEWLINE +
                "  Title   = {titulo_test}," + Globals.NEWLINE +
                "}";
        // @formatter:on

        // read in bibtex string
        ParserResult result = BibtexParser.parse(new StringReader(bibtexEntry));

        Collection<BibEntry> entries = result.getDatabase().getEntries();
        assertEquals(1, entries.size());

        BibEntry entry = entries.iterator().next();
        assertEquals(null, entry.getCiteKey());
        assertEquals(2, entry.getFieldNames().size());
        Set<String> fields = entry.getFieldNames();
        assertTrue(fields.contains("author"));
        assertEquals("NomeAutorTest", entry.getField("author"));

        //write out bibtex string
        StringWriter stringWriter = new StringWriter();
        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);
        String actual = stringWriter.toString();

        assertEquals(bibtexEntry, actual);
    }
    
    
    //Caso 3: Campos obrigatórios vazios
    @Test
    public void testVazio() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("1234", "article");
        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE +
                       "}"+ Globals.NEWLINE;
        // @formatter:on
        assertEquals(expected, actual);
    }

    @Test
    public void VazioroundTripTest() throws IOException {
        // @formatter:off
        String bibtexEntry = "@Article{," + Globals.NEWLINE +
                              "}";
        // @formatter:on

        // read in bibtex string
        ParserResult result = BibtexParser.parse(new StringReader(bibtexEntry));

        Collection<BibEntry> entries = result.getDatabase().getEntries();
        assertEquals(1, entries.size());

        BibEntry entry = entries.iterator().next();
        assertEquals(null, entry.getCiteKey());
        assertEquals(0, entry.getFieldNames().size());

        //write out bibtex string
        StringWriter stringWriter = new StringWriter();
        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);
        String actual = stringWriter.toString();

        assertEquals(bibtexEntry, actual);
    }
    
    //Caso 4: Campos opcionais PARCIALMENTE preenchidos
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


    //Caso 5: Campos Opcionais totalmente preenchidos
    @Test
    public void testOT() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("1234", "article");
        //entrada de campos obrigatorios
        entry.setField("author", "NomeAutorTest");
        entry.setField("title", "titulo_test");
        entry.setField("journal", "Publicação_Test");
        entry.setField("year", "ano_test");
        entry.setField("volume", "nome_volume_test");
        entry.setField("number", "numero_edição_test");
        entry.setField("pages", "numero_pagina_test");
        entry.setField("month", "mes_publicação_test");
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
                "  number  = {numero_edição_test}," + Globals.NEWLINE +
                "  pages   = {numero_pagina_test}," + Globals.NEWLINE +
                "  month   = {mes_publicação_test}," + Globals.NEWLINE +
                "  notes   = {notas_test}," + Globals.NEWLINE +
                "}"+ Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void OTroundTripTest() throws IOException {
        // @formatter:off
        String bibtexEntry = "@Article{chave_test," + Globals.NEWLINE +
                "  Author  = {NomeAutorTest}," + Globals.NEWLINE +
                "  Title   = {titulo_test}," + Globals.NEWLINE +
                "  Journal = {Publicação_Test}," + Globals.NEWLINE +
                "  Year    = {ano_test}," + Globals.NEWLINE +
                "  Volume  = {nome_volume_test}," + Globals.NEWLINE +
                "  Number  = {numero_edição_test}," + Globals.NEWLINE +
                "  Pages   = {numero_paginas_test}," + Globals.NEWLINE +
                "  Month   = {mes_publicação_test}," + Globals.NEWLINE +
                "  Note    = {notas_test}," + Globals.NEWLINE +
                "}";
        // @formatter:on

        // read in bibtex string
        ParserResult result = BibtexParser.parse(new StringReader(bibtexEntry));

        Collection<BibEntry> entries = result.getDatabase().getEntries();
        assertEquals(1, entries.size());

        BibEntry entry = entries.iterator().next();
        assertEquals("chave_test", entry.getCiteKey());
        assertEquals(10, entry.getFieldNames().size());
        Set<String> fields = entry.getFieldNames();
        assertTrue(fields.contains("author"));
        assertEquals("NomeAutorTest", entry.getField("author"));

        //write out bibtex string
        StringWriter stringWriter = new StringWriter();
        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);
        String actual = stringWriter.toString();

        assertEquals(bibtexEntry, actual);
    }
    
    //teste artigo com todos os campos preenchidos
    @Test
    public void testaAll() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("1234", "article");
        //entrada de campos obrigatorios
        entry.setField("author", "NomeAutorTest");
        entry.setField("title", "titulo_test");
        entry.setField("journal", "Publicação_Test");
        entry.setField("year", "ano_test");
        entry.setField("volume", "nome_volume_test");
        entry.setField("number", "numero_ediçao_test");
        entry.setField("pages", "numero_pagina_test");
        entry.setField("month", "mes_publicaçao_test");
        entry.setField("abstract", "teste_resumo_artigo");
        entry.setField("comment", "teste_comment");
        entry.setField("crossref", "teste_crossref");
        entry.setField("doi", "teste_doi");
        entry.setField("keywords", "teste_keywords");
        entry.setField("notes", "notas_test");
        entry.setField("owner", "teste_owner");
        entry.setField("review", "teste_revisao_artigo");
        entry.setField("timestamp", "teste_timestamp");
        entry.setField("url", "teste_url");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE +
                "  author    = {NomeAutorTest},"         + Globals.NEWLINE +
                "  title     = {titulo_test},"           + Globals.NEWLINE +
                "  journal   = {Publicação_Test},"       + Globals.NEWLINE +
                "  year      = {ano_test},"              + Globals.NEWLINE +
                "  volume    = {nome_volume_test},"      + Globals.NEWLINE +
                "  number    = {numero_ediçao_test},"    + Globals.NEWLINE +
                "  pages     = {numero_pagina_test},"    + Globals.NEWLINE +
                "  month     = {mes_publicaçao_test},"   + Globals.NEWLINE +
                "  abstract  = {teste_resumo_artigo},"   + Globals.NEWLINE +
                "  comment   = {teste_comment},"         + Globals.NEWLINE +
                "  crossref  = {teste_crossref},"        + Globals.NEWLINE +
                "  doi       = {teste_doi},"             + Globals.NEWLINE +
                "  keywords  = {teste_keywords},"        + Globals.NEWLINE +
                "  notes     = {notas_test},"            + Globals.NEWLINE +
                "  owner     = {teste_owner},"           + Globals.NEWLINE +
                "  review    = {teste_revisao_artigo},"  + Globals.NEWLINE +
                "  timestamp = {teste_timestamp},"       + Globals.NEWLINE +
                "  url       = {teste_url},"             + Globals.NEWLINE +
                "}"                                      + Globals.NEWLINE;




        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void AllroundTripTest() throws IOException {
        // @formatter:off
        String bibtexEntry = "@Article{chave_test,"     + Globals.NEWLINE +
                "  Author    = {NomeAutorTest},"        + Globals.NEWLINE +
                "  Title     = {titulo_test},"          + Globals.NEWLINE +
                "  Journal   = {Publicação_Test},"      + Globals.NEWLINE +
                "  Year      = {ano_test},"             + Globals.NEWLINE +
                "  Volume    = {nome_volume_test},"     + Globals.NEWLINE +
                "  Number    = {numero_ediçao_test},"   + Globals.NEWLINE +
                "  Pages     = {numero_paginas_test},"  + Globals.NEWLINE +
                "  Month     = {mes_publicaçao_test},"  + Globals.NEWLINE +
                "  Note      = {notas_test},"           + Globals.NEWLINE +
                "  Abstract  = {teste_resumo_artigo},"  + Globals.NEWLINE +
                "  Comment   = {testte_comment},"       + Globals.NEWLINE +
                "  Crossref  = {teste_crossref},"       + Globals.NEWLINE +
                "  Doi       = {teste_doi},"            + Globals.NEWLINE +
                "  Keywords  = {teste_keywords},"       + Globals.NEWLINE +
                "  Owner     = {teste_owner},"          + Globals.NEWLINE +
                "  Review    = {teste_revisao_artigo}," + Globals.NEWLINE +
                "  Timestamp = {teste_timestamp},"      + Globals.NEWLINE +
                "  Url       = {teste_url},"            + Globals.NEWLINE +

                "}";
        // @formatter:on

        // read in bibtex string
        ParserResult result = BibtexParser.parse(new StringReader(bibtexEntry));

        Collection<BibEntry> entries = result.getDatabase().getEntries();
        assertEquals(1, entries.size());

        BibEntry entry = entries.iterator().next();
        assertEquals("chave_test", entry.getCiteKey());
        assertEquals(19, entry.getFieldNames().size());
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
