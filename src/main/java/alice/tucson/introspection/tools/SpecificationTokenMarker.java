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
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tucson.introspection.tools;

import javax.swing.text.Segment;
import alice.util.jedit.KeywordMap;
import alice.util.jedit.Token;
import alice.util.jedit.TokenMarker;

/**
 *
 * @author Unknown...
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class SpecificationTokenMarker extends TokenMarker {

    private static KeywordMap respectKeywords;

    /**
     *
     * @return the Object representing associations between String
     *         representation of keywords and keywords token
     */
    public static synchronized KeywordMap getKeywords() {
        if (SpecificationTokenMarker.respectKeywords == null) {
            SpecificationTokenMarker.respectKeywords = new KeywordMap(false);
            SpecificationTokenMarker.respectKeywords.add("reaction",
                    Token.KEYWORD1);
            SpecificationTokenMarker.respectKeywords.add("out", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("in", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("rd", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("inp", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("rdp", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("no", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("nop", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("get", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("set", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("uin", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("urd", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords
                    .add("uinp", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords
                    .add("urdp", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("uno", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords
                    .add("unop", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("out_all",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("in_all",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("rd_all",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("no_all",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("spawn",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("current_predicate",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("current_tuple",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("current_source",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("current_target",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("current_time",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("event_predicate",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("event_tuple",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("event_source",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("event_target",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("event_time",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("start_predicate",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("start_tuple",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("start_source",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("start_target",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("start_time",
                    Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords.add("pre", Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("req", Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("request",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("inv", Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("invocation",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords
                    .add("post", Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords
                    .add("resp", Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("response",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("compl",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("completion",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("success",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("failure",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("intra",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("inter",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("from_tc",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("to_tc",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("from_agent",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("to_agent",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords
                    .add("endo", Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("exo", Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("before",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("after",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("operation",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("internal",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("link_in",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("link_out",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("between",
                    Token.KEYWORD2);
            SpecificationTokenMarker.respectKeywords.add("is", Token.KEYWORD3);
            SpecificationTokenMarker.respectKeywords
                    .add("exec", Token.LITERAL1);
            SpecificationTokenMarker.respectKeywords.add("solve",
                    Token.LITERAL1);
        }
        return SpecificationTokenMarker.respectKeywords;
    }

    private final KeywordMap keywords;
    private int lastKeyword;
    private int lastOffset;

    /**
     *
     */
    public SpecificationTokenMarker() {
        this(SpecificationTokenMarker.getKeywords());
    }

    /**
     *
     * @param keys
     *            the keywords to highlight
     */
    public SpecificationTokenMarker(final KeywordMap keys) {
        super();
        this.keywords = keys;
    }

    @Override
    public byte markTokensImpl(final byte token, final Segment line,
            final int lineIndex) {
        final char[] array = line.array;
        final int offset = line.offset;
        this.lastOffset = offset;
        this.lastKeyword = offset;
        final int len = line.count + offset;
        boolean backslash = false;
        loop: for (int i = offset; i < len; i++) {
            final int i1 = i + 1;
            final char c = array[i];
            if (c == '\\') {
                backslash ^= true;
                continue;
            }
            switch (token) {
                case Token.NULL:
                    switch (c) {
                        case '%':
                            if (backslash) {
                                backslash = false;
                            }
                            this.doKeyword(line, i);
                            this.addToken(i - this.lastOffset, token);
                            this.addToken(len - i, Token.COMMENT1);
                            this.lastOffset = len;
                            this.lastKeyword = len;
                            break loop;
                        case '#':
                            if (backslash) {
                                backslash = false;
                            } else {
                                if (this.doKeyword(line, i)) {
                                    break;
                                }
                                this.addToken(i - this.lastOffset, token);
                                this.addToken(len - i, Token.LABEL);
                                this.lastOffset = len;
                                this.lastKeyword = len;
                                break loop;
                            }
                            break;
                        default:
                            backslash = false;
                            if (!Character.isLetterOrDigit(c) && c != '_') {
                                this.doKeyword(line, i);
                            }
                            break;
                    }
                    break;
                case Token.COMMENT1:
                case Token.LITERAL1:
                    if (backslash) {
                        backslash = false;
                    } else if (c == '"') {
                        this.addToken(i1 - this.lastOffset, token);
                        // token = Token.NULL;
                        this.lastOffset = i1;
                        this.lastKeyword = i1;
                    }
                    break;
                case Token.LITERAL2:
                    if (backslash) {
                        backslash = false;
                    } else if (c == '\'') {
                        this.addToken(i1 - this.lastOffset, Token.LITERAL1);
                        // token = Token.NULL;
                        this.lastOffset = i1;
                        this.lastKeyword = i1;
                    }
                    break;
                default:
                    throw new InternalError("Invalid state: " + token);
            }
        }
        if (token == Token.NULL) {
            this.doKeyword(line, len);
        }
        switch (token) {
            case Token.LITERAL1:
            case Token.LITERAL2:
                this.addToken(len - this.lastOffset, Token.INVALID);
                // token = Token.NULL;
                break;
            case Token.KEYWORD2:
                this.addToken(len - this.lastOffset, token);
                break;
            default:
                this.addToken(len - this.lastOffset, token);
                break;
        }
        return token;
    }

    private boolean doKeyword(final Segment line, final int i) {
        final int i1 = i + 1;
        final int len = i - this.lastKeyword;
        final byte id = this.keywords.lookup(line, this.lastKeyword, len);
        if (id != Token.NULL) {
            if (this.lastKeyword != this.lastOffset) {
                this.addToken(this.lastKeyword - this.lastOffset, Token.NULL);
            }
            this.addToken(len, id);
            this.lastOffset = i;
        }
        this.lastKeyword = i1;
        return false;
    }
}
