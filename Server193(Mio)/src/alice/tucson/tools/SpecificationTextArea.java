/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * TextAreaDefaults.java - Encapsulates default values for various settings
 * Copyright (C) 1999 Slava Pestov
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */
package alice.tucson.tools;

import alice.util.jedit.*;

import java.awt.Color;

public class SpecificationTextArea extends TextAreaDefaults
{
	public SpecificationTextArea()
	{
	    inputHandler = new DefaultInputHandler();
	    inputHandler.addDefaultKeyBindings();
	    document = new SyntaxDocument();
	    editable = true;

	    caretVisible = true;
	    caretBlinks = true;
	    electricScroll = 3;

	    cols = 80;
	    rows = 25;
	    styles = getSyntaxStyles();
	    caretColor = Color.red;
            selectionColor = new Color(0xccccff);
	    lineHighlightColor = new Color(0xe0e0e0);
	    lineHighlight = true;
	    bracketHighlightColor = Color.black;
	    bracketHighlight = true;
	    eolMarkerColor = new Color(0x009999);
	    eolMarkers = false;
	    paintInvalid = true;
	}

	private SyntaxStyle[] getSyntaxStyles()
	{
		SyntaxStyle[] styles = new SyntaxStyle[Token.ID_COUNT];
		styles[Token.COMMENT1] = new SyntaxStyle(new Color(0x808080),true,false);
		styles[Token.COMMENT2] = new SyntaxStyle(new Color(0x990033),true,false);
		styles[Token.KEYWORD1] = new SyntaxStyle(Color.blue,false,true);
		styles[Token.KEYWORD2] = new SyntaxStyle(Color.black,false,true);
		styles[Token.KEYWORD3] = new SyntaxStyle(Color.red,false,true);
		styles[Token.LITERAL1] = new SyntaxStyle(new Color(0x650099),false,false);
		styles[Token.LITERAL2] = new SyntaxStyle(new Color(0x650099),false,true);
		styles[Token.LABEL] = new SyntaxStyle(new Color(0x008000),false,false);
		styles[Token.OPERATOR] = new SyntaxStyle(Color.black,false,true);
		styles[Token.INVALID] = new SyntaxStyle(Color.red,false,true);

		return styles;
	}

}
