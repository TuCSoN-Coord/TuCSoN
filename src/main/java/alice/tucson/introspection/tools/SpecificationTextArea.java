/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA TextAreaDefaults.java -
 * Encapsulates default values for various settings Copyright (C) 1999 Slava
 * Pestov You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice remains
 * intact in all source distributions of this package.
 */
package alice.tucson.introspection.tools;

import java.awt.Color;
import alice.util.jedit.DefaultInputHandler;
import alice.util.jedit.SyntaxDocument;
import alice.util.jedit.SyntaxStyle;
import alice.util.jedit.TextAreaDefaults;
import alice.util.jedit.Token;

/**
 *
 * @author Unknown...
 *
 */
public class SpecificationTextArea extends TextAreaDefaults {

    private static SyntaxStyle[] getSyntaxStyles() {
        final SyntaxStyle[] styles = new SyntaxStyle[Token.ID_COUNT];
        styles[Token.COMMENT1] = new SyntaxStyle(new Color(0x808080), true,
                false);
        styles[Token.COMMENT2] = new SyntaxStyle(new Color(0x990033), true,
                false);
        styles[Token.KEYWORD1] = new SyntaxStyle(Color.blue, false, true);
        styles[Token.KEYWORD2] = new SyntaxStyle(Color.green, false, true);
        styles[Token.KEYWORD3] = new SyntaxStyle(Color.black, false, true);
        styles[Token.LITERAL1] = new SyntaxStyle(Color.BLUE, true, false);
        styles[Token.LITERAL2] = new SyntaxStyle(Color.BLUE, true, false);
        styles[Token.LABEL] = new SyntaxStyle(new Color(0x008000), false, false);
        styles[Token.OPERATOR] = new SyntaxStyle(Color.black, false, true);
        styles[Token.INVALID] = new SyntaxStyle(Color.red, false, false);
        return styles;
    }

    /**
     *
     */
    public SpecificationTextArea() {
        super();
        this.inputHandler = new DefaultInputHandler();
        this.inputHandler.addDefaultKeyBindings();
        this.document = new SyntaxDocument();
        this.editable = true;
        this.caretVisible = true;
        this.caretBlinks = true;
        this.electricScroll = 3;
        this.cols = 80;
        this.rows = 25;
        this.styles = SpecificationTextArea.getSyntaxStyles();
        this.caretColor = Color.red;
        this.selectionColor = new Color(0xccccff);
        this.lineHighlightColor = new Color(0xe0e0e0);
        this.lineHighlight = true;
        this.bracketHighlightColor = Color.black;
        this.bracketHighlight = true;
        this.eolMarkerColor = new Color(0x009999);
        this.eolMarkers = false;
        this.paintInvalid = true;
    }
}
