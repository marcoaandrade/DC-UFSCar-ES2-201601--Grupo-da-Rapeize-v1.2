package net.sf.jabref.importer.fileformat;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.importer.OutputPrinterToNull;
import net.sf.jabref.model.entry.BibEntry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the BibtexImporter.
 * That importer is only used for --importToOpen, which is currently untested
 * <p>
 * TODO:
 * 1. Add test for --importToOpen
 * 2. Move these tests to the code opening a bibtex file
 */
public class BibtexImporterTest {

    private BibtexImporter importer;

    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        importer = new BibtexImporter();
    }

    @Test
    public void testIsRecognizedFormat() throws IOException {
        try (InputStream stream = BibtexImporterTest.class.getResourceAsStream("BibtexImporter.examples.bib")) {
            assertTrue(importer.isRecognizedFormat(stream));
        }
    }

    @Test
    public void testImportEntries() throws IOException {
        try (InputStream stream = BibtexImporterTest.class.getResourceAsStream("BibtexImporter.examples.bib")) {
            List<BibEntry> bibEntries = importer.importEntries(stream, new OutputPrinterToNull());

            //Os testes para livro e artigo foram adicionados nesse código  (Default 4 + Book/Article 11 = 15 Entradas)
            assertEquals(15, bibEntries.size());

            for (BibEntry entry : bibEntries) {
                //TESTES DEFAULT
                if (entry.getCiteKey().equals("aksin")) {
                    assertEquals("Aks{\\i}n, {\\\"O}zge and T{\\\"u}rkmen, Hayati and Artok, Levent and {\\c{C}}etinkaya, " +
                                    "Bekir and Ni, Chaoying and B{\\\"u}y{\\\"u}kg{\\\"u}ng{\\\"o}r, Orhan and {\\\"O}zkal, Erhan",
                            entry.getField("author"));
                    assertEquals("aksin", entry.getField("bibtexkey"));
                    assertEquals("2006", entry.getField("date"));
                    assertEquals("Effect of immobilization on catalytic characteristics", entry.getField("indextitle"));
                    assertEquals("#jomch#", entry.getField("journaltitle"));
                    assertEquals("13", entry.getField("number"));
                    assertEquals("3027-3036", entry.getField("pages"));
                    assertEquals("Effect of immobilization on catalytic characteristics of saturated {Pd-N}-heterocyclic " +
                            "carbenes in {Mizoroki-Heck} reactions", entry.getField("title"));
                    assertEquals("691", entry.getField("volume"));
                } else if (entry.getCiteKey().equals("stdmodel")) {
                    assertEquals("A \\texttt{set} with three members discussing the standard model of particle physics. " +
                                    "The \\texttt{crossref} field in the \\texttt{@set} entry and the \\texttt{entryset} field in " +
                                    "each set member entry is needed only when using BibTeX as the backend",
                            entry.getField("annotation"));
                    assertEquals("stdmodel", entry.getField("bibtexkey"));
                    assertEquals("glashow,weinberg,salam", entry.getField("entryset"));
                } else if (entry.getCiteKey().equals("set")) {
                    assertEquals("A \\texttt{set} with three members. The \\texttt{crossref} field in the \\texttt{@set} " +
                            "entry and the \\texttt{entryset} field in each set member entry is needed only when using " +
                            "BibTeX as the backend", entry.getField("annotation"));
                    assertEquals("set", entry.getField("bibtexkey"));
                    assertEquals("herrmann,aksin,yoon", entry.getField("entryset"));
                } else if (entry.getCiteKey().equals("Preissel2016")) {
                    assertEquals("Heidelberg", entry.getField("address"));
                    assertEquals("Preißel, René", entry.getField("author"));
                    assertEquals("Preissel2016", entry.getField("bibtexkey"));
                    assertEquals("3., aktualisierte und erweiterte Auflage", entry.getField("edition"));
                    assertEquals("978-3-86490-311-3", entry.getField("isbn"));
                    assertEquals("Versionsverwaltung", entry.getField("keywords"));
                    assertEquals("XX, 327 Seiten", entry.getField("pages"));
                    assertEquals("dpunkt.verlag", entry.getField("publisher"));
                    assertEquals("Git: dezentrale Versionsverwaltung im Team : Grundlagen und Workflows",
                            entry.getField("title"));
                    assertEquals("http://d-nb.info/107601965X", entry.getField("url"));
                    assertEquals("2016", entry.getField("year"));
                }
                //BOOK
                //Todos os campos preenchidos - Obrigatorios, Opcionais e Extras
                else if (entry.getCiteKey().equals("06key")) {
                    assertEquals("Book X", entry.getField("title"));
                    assertEquals("06key", entry.getField("bibtexkey"));
                    assertEquals("ABC", entry.getField("publisher"));
                    assertEquals("2012", entry.getField("year"));
                    assertEquals("Neal Adams", entry.getField("author"));
                    assertEquals("Editor EDT", entry.getField("editor"));
                    assertEquals("Ten", entry.getField("volume"));
                    assertEquals("421", entry.getField("number"));
                    assertEquals("1", entry.getField("series"));
                    assertEquals("Port Pier", entry.getField("address"));
                    assertEquals("21st", entry.getField("edition"));
                    assertEquals("August", entry.getField("month"));
                    assertEquals("Note-Import", entry.getField("note"));
                    assertEquals("Abstract-Import", entry.getField("abstract"));
                    assertEquals("Comment-IMport", entry.getField("comment"));
                    assertEquals("teste-crossref", entry.getField("crossref"));
                    assertEquals("10.14195/978-989-26-1163-1", entry.getField("doi"));
                    assertEquals("teste-keywords", entry.getField("keywords"));
                    assertEquals("teste-review", entry.getField("review"));
                    assertEquals("teste-url", entry.getField("url"));
                }
                //Campos opcionais parcialmente preenchidos
                else if (entry.getCiteKey().equals("05key")) {
                    assertEquals("Import-OpPartial", entry.getField("title"));
                    assertEquals("05key", entry.getField("bibtexkey"));
                    assertEquals("2016", entry.getField("year"));
                    assertEquals("Scott Banks", entry.getField("author"));
                    assertEquals("Five", entry.getField("volume"));
                    assertEquals("51", entry.getField("number"));
                    assertEquals("February", entry.getField("month"));
                    assertEquals("3rd", entry.getField("edition"));
                }
                //Campos opcionais totalmente preenchidos
                else if (entry.getCiteKey().equals("04key")) {
                    assertEquals("Import-OpTotal", entry.getField("title"));
                    assertEquals("04key", entry.getField("bibtexkey"));
                    assertEquals("2020", entry.getField("year"));
                    assertEquals("Scott Snydel", entry.getField("author"));
                    assertEquals("Quatro", entry.getField("volume"));
                    assertEquals("52", entry.getField("number"));
                    assertEquals("4", entry.getField("series"));
                    assertEquals("Address-Import", entry.getField("address"));
                    assertEquals("1st", entry.getField("edition"));
                    assertEquals("January", entry.getField("month"));
                    assertEquals("Note", entry.getField("note"));
                }
                //Ano negativo
                else if (entry.getCiteKey().equals("03key")) {
                    assertEquals("Java-Imp", entry.getField("title"));
                    assertEquals("03key", entry.getField("bibtexkey"));
                    assertEquals("Novadel", entry.getField("publisher"));
                    assertEquals("-2010", entry.getField("year"));
                    assertEquals("Rafael To Import", entry.getField("author"));
                    assertEquals("Hello World", entry.getField("editor"));
                }
                //Campos Obrigatorios parcialmente preenchidos
                else if (entry.getCiteKey().equals("02key")) {
                    assertEquals("Another Book - Import", entry.getField("title"));
                    assertEquals("02key", entry.getField("bibtexkey"));
                    assertEquals("Brian K. Vaughan - Import", entry.getField("author"));
                    assertEquals("Vertigo - IMport", entry.getField("editor"));
                }
                //Campos obrigatorios totalmente preenchidos
                else if (entry.getCiteKey().equals("01key")) {
                    assertEquals("Book-Import-Title", entry.getField("title"));
                    assertEquals("01key", entry.getField("bibtexkey"));
                    assertEquals("BookImport-Publisher", entry.getField("publisher"));
                    assertEquals("20", entry.getField("year"));
                    assertEquals("Autor Importado", entry.getField("author"));
                    assertEquals("DC", entry.getField("editor"));
                }
                //ARTICLE
                //Campos Obrigatorios totalmente preenchidos
                else if (entry.getCiteKey().equals("chave01")) {
                    assertEquals("Titulo-Importar-Teste", entry.getField("title"));
                    assertEquals("chave01", entry.getField("bibtexkey"));
                    assertEquals("Journal-Importar-Teste", entry.getField("journal"));
                    assertEquals("2010", entry.getField("year"));
                    assertEquals("Autor-Importar-Teste", entry.getField("author"));
                }
                //Campos Obrigatorios parcialmente preenchidos
                else if (entry.getCiteKey().equals("chave02")) {
                    assertEquals("Titulo-Imp-Parcial-Teste", entry.getField("title"));
                    assertEquals("chave02", entry.getField("bibtexkey"));
                    assertEquals("Autor-Imp-Parcial-Teste", entry.getField("author"));
                }
                //Campos opcionais totalmente preenchidos
                else if (entry.getCiteKey().equals("chave03")) {
                    assertEquals("Titulo-Importa", entry.getField("title"));
                    assertEquals("chave03", entry.getField("bibtexkey"));
                    assertEquals("Journal-Importar", entry.getField("journal"));
                    assertEquals("2012", entry.getField("year"));
                    assertEquals("Autor-Importar", entry.getField("author"));
                    assertEquals("1st Volume", entry.getField("volume"));
                    assertEquals("Number-Teste", entry.getField("number"));
                    assertEquals("356", entry.getField("pages"));
                    assertEquals("feb", entry.getField("month"));
                    assertEquals("note-test", entry.getField("note"));
                }
                //Campos opcionais parcialmente preenchidos
                else if (entry.getCiteKey().equals("chave04")) {
                    assertEquals("Titulo-Importar-Teste", entry.getField("title"));
                    assertEquals("chave04", entry.getField("bibtexkey"));
                    assertEquals("Journal-Importar-Teste", entry.getField("journal"));
                    assertEquals("2013", entry.getField("year"));
                    assertEquals("Autor-Importar-Teste", entry.getField("author"));
                    assertEquals("1st Volume", entry.getField("volume"));
                    assertEquals("Number-Teste", entry.getField("number"));
                    assertEquals("jan", entry.getField("month"));
                }
                //Todos os campos preenchidos- Obrigatorios, opcionais e extras
                else if (entry.getCiteKey().equals("chave05")) {
                    assertEquals("Title-All", entry.getField("title"));
                    assertEquals("chave05", entry.getField("bibtexkey"));
                    assertEquals("Journal-All", entry.getField("journal"));
                    assertEquals("1988", entry.getField("year"));
                    assertEquals("Autor-All", entry.getField("author"));
                    assertEquals("1", entry.getField("volume"));
                    assertEquals("4", entry.getField("number"));
                    assertEquals("2", entry.getField("pages"));
                    assertEquals("jan", entry.getField("month"));
                    assertEquals("nota-teste-Import", entry.getField("note"));
                    assertEquals("abstract-teste", entry.getField("abstract"));
                    assertEquals("comment-import", entry.getField("comment"));
                    assertEquals("crossref-import", entry.getField("crossref"));
                    assertEquals("10.1590/s1984-02922008000100021", entry.getField("doi"));
                    assertEquals("keywords-import", entry.getField("keywords"));
                    assertEquals("review-teste", entry.getField("review"));
                    assertEquals("www.ted.com", entry.getField("url"));
                }
            }
        }
    }

    @Test
    public void testGetFormatName() {
        assertEquals("BibTeX", importer.getFormatName());
    }

    @Test
    public void testGetExtensions() {
        assertEquals("bib", importer.getExtensions());
    }
}
