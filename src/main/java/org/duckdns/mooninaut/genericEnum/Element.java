// Element.java by Clement Cherlin is marked CC0 1.0.
//
// To view a copy of this mark, visit https://creativecommons.org/publicdomain/zero/1.0/
//
// See LICENSE-CC0 in this repository for the full text of the license

package org.duckdns.mooninaut.genericEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public sealed abstract class Element<SELF extends Element<SELF, E>, E extends ElementType<E, SELF>> {

    protected final List<Element<?, ?>> children = new ArrayList<>();
    protected final E type;

    protected Element(E type) {
        this.type = type;
    }

    public E type() {
        return type;
    }

    public String typeName() {
        return type().name().toLowerCase(Locale.ROOT);
    }

    public SELF appendChild(final Element<?, ?> element) {
        if (!type().canContain(element.type())) {
            throw new IllegalArgumentException("Element type " + typeName() + " cannot contain " + element.typeName());
        }
        children.add(element);
        return (SELF) this;
    }

    public List<Element<?, ?>> children() {
        return Collections.unmodifiableList(children);
    }

    public boolean hasChildOfType(final ElementType<?, ?> elementType) {
        return children().stream().anyMatch(child -> child.type() == elementType);
    }

    @Override
    public String toString() {
        if (children.isEmpty()) {
            return "<" + typeName() + ">\n</" + typeName() + ">";
        }
        return "<" + typeName() + ">\n" +
                children().stream()
                        .map(Element::toString)
                        .collect(Collectors.joining("\n")) +
                "\n</" + typeName() + ">";
    }

    public static final class HtmlElement extends Element<HtmlElement, ElementType.HTML> {
        public HtmlElement() {
            super(ElementType.HTML);
        }
    }

    public static final class BodyElement extends Element<BodyElement, ElementType.BODY> {
        public BodyElement() {
            super(ElementType.BODY);
        }
    }

    public static final class PElement extends Element<PElement, ElementType.P> {
        public PElement() {
            super(ElementType.P);
        }
    }

    public static final class TextNode extends Element<TextNode, ElementType.TEXT> {
        private final String text;
        public TextNode(String text) {
            super(ElementType.TEXT);
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
