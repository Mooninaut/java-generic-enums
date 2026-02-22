// ElementType.java by Clement Cherlin is marked CC0 1.0.
//
// To view a copy of this mark, visit https://creativecommons.org/publicdomain/zero/1.0/
//
// See LICENSE-CC0 in this repository for the full text of the license

package org.duckdns.mooninaut.genericEnum;

import java.util.List;
import java.util.Map;

public abstract sealed class ElementType<E extends ElementType<E, EL>, EL extends Element<EL, E>> extends MyEnum<ElementType<?, ?>> {
    protected ElementType(String name, int ordinal) {
        super(name, ordinal);
    }

    public static final HTML HTML = new HTML();
    public static final BODY BODY = new BODY();
    public static final P P = new P();
    public static final TEXT TEXT = new TEXT();

    public static final List<ElementType<?, ?>> VALUES = List.of(HTML, BODY, P, TEXT);
    public static final Map<String, ElementType<?, ?>> DIRECTORY = makeDirectory(VALUES);

    abstract boolean canContain(ElementType<?, ?> elementType);

    public static ElementType<?, ?> valueOf(String name) {
        return DIRECTORY.get(name);
    }

    public abstract EL createElement();

    public static final class HTML extends ElementType<HTML, Element.HtmlElement> {
        private HTML() {
            super("HTML", 0);
        }

        @Override
        boolean canContain(ElementType<?, ?> elementType) {
            return elementType == BODY;
        }

        @Override
        public Element.HtmlElement createElement() {
            return new Element.HtmlElement();
        }

    }

    public static final class BODY extends ElementType<BODY, Element.BodyElement> {
        private BODY() {
            super("BODY", 1);
        }

        @Override
        boolean canContain(ElementType<?, ?> elementType) {
            return elementType == P;
        }

        @Override
        public Element.BodyElement createElement() {
            return new Element.BodyElement();
        }
    }

    public static final class P extends ElementType<P, Element.PElement> {
        private P() {
            super("P", 2);
        }

        @Override
        boolean canContain(ElementType<?, ?> elementType) {
            return elementType == TEXT;
        }

        @Override
        public Element.PElement createElement() {
            return new Element.PElement();
        }
    }

    public static final class TEXT extends ElementType<TEXT, Element.TextNode> {
        private TEXT() {
            super ("TEXT", 3);
        }

        @Override
        boolean canContain(ElementType<?, ?> elementType) {
            return false;
        }

        @Override
        public Element.TextNode createElement() {
            return new Element.TextNode("");
        }

        public Element.TextNode createElement(final String text) {
            return new Element.TextNode(text);
        }
    }
}
