package net.sf.jabref.export.layout;

import net.sf.jabref.export.layout.format.HTMLChars;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HTMLCharsTest {

    @Test
    public void testBasicFormat() {

        LayoutFormatter layout = new HTMLChars();

        assertEquals("", layout.format(""));

        assertEquals("hallo", layout.format("hallo"));

        assertEquals("Réflexions sur le timing de la quantité", layout
                .format("Réflexions sur le timing de la quantité"));

        assertEquals("h&aacute;llo", layout.format("h\\'allo"));

        assertEquals("&#305; &#305;", layout.format("\\i \\i"));
        assertEquals("&#305;", layout.format("\\i"));
        assertEquals("&#305;", layout.format("\\{i}"));
        assertEquals("&#305;&#305;", layout.format("\\i\\i"));

        assertEquals("&#319;&#305;", layout.format("\\Lmidot\\i"));

        assertEquals("&ntilde; &ntilde; &iacute; &#305; &#305;", layout.format("\\~{n} \\~n \\'i \\i \\i"));
    }

    @Test
    public void testLaTeXHighlighting() {

        LayoutFormatter layout = new HTMLChars();

        assertEquals("<em>hallo</em>", layout.format("\\emph{hallo}"));
        assertEquals("<em>hallo</em>", layout.format("{\\emph hallo}"));

        assertEquals("<em>hallo</em>", layout.format("\\textit{hallo}"));
        assertEquals("<em>hallo</em>", layout.format("{\\textit hallo}"));

        assertEquals("<b>hallo</b>", layout.format("\\textbf{hallo}"));
        assertEquals("<b>hallo</b>", layout.format("{\\textbf hallo}"));
    }

	/*
     * Is missing a lot of test cases for the individual chars...
	 */
}