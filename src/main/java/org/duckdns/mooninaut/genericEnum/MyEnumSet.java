/*
 * Copyright (c) 2003, 2022, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package org.duckdns.mooninaut.genericEnum;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

public abstract sealed class MyEnumSet<E extends MyEnum<E>> extends AbstractSet<E>
        implements Cloneable, java.io.Serializable permits MyJumboEnumSet, MyRegularEnumSet
{
    /**
     * The class of all the elements of this set.
     */
    final transient Class<E> elementType;

    /**
     * All of the values comprising E.  (Cached for performance.)
     */
    final transient MyEnum<?>[] universe;

    MyEnumSet(Class<E>elementType, MyEnum<?>[] universe) {
        this.elementType = elementType;
        this.universe    = universe;
    }

    /**
     * Creates an empty enum set with the specified element type.
     *
     * @param <E> The class of the elements in the set
     * @param elementType the class object of the element type for this enum
     *     set
     * @return An empty enum set of the specified type.
     * @throws NullPointerException if {@code elementType} is null
     */
    public static <E extends MyEnum<E>> MyEnumSet<E> noneOf(Class<E> elementType) {
        MyEnum<?>[] universe = getUniverse(elementType);
        if (universe == null)
            throw new ClassCastException(elementType + " not an enum");

        if (universe.length <= 64)
            return new MyRegularEnumSet<>(elementType, universe);
        else
            return new MyJumboEnumSet<>(elementType, universe);
    }

    /**
     * Creates an enum set containing all of the elements in the specified
     * element type.
     *
     * @param <E> The class of the elements in the set
     * @param elementType the class object of the element type for this enum
     *     set
     * @return An enum set containing all the elements in the specified type.
     * @throws NullPointerException if {@code elementType} is null
     */
    public static <E extends MyEnum<E>> MyEnumSet<E> allOf(Class<E> elementType) {
        MyEnumSet<E> result = noneOf(elementType);
        result.addAll();
        return result;
    }

    /**
     * Adds all of the elements from the appropriate enum type to this enum
     * set, which is empty prior to the call.
     */
    abstract void addAll();

    /**
     * Creates an enum set with the same element type as the specified enum
     * set, initially containing the same elements (if any).
     *
     * @param <E> The class of the elements in the set
     * @param s the enum set from which to initialize this enum set
     * @return A copy of the specified enum set.
     * @throws NullPointerException if {@code s} is null
     */
    public static <E extends MyEnum<E>> MyEnumSet<E> copyOf(MyEnumSet<E> s) {
        return s.clone();
    }

    /**
     * Creates an enum set initialized from the specified collection.  If
     * the specified collection is an {@code EnumSet} instance, this static
     * factory method behaves identically to {@link #copyOf(MyEnumSet)}.
     * Otherwise, the specified collection must contain at least one element
     * (in order to determine the new enum set's element type).
     *
     * @param <E> The class of the elements in the collection
     * @param c the collection from which to initialize this enum set
     * @return An enum set initialized from the given collection.
     * @throws IllegalArgumentException if {@code c} is not an
     *     {@code EnumSet} instance and contains no elements
     * @throws NullPointerException if {@code c} is null
     */
    public static <E extends MyEnum<E>> MyEnumSet<E> copyOf(Collection<E> c) {
        if (c instanceof MyEnumSet) {
            return ((MyEnumSet<E>)c).clone();
        } else {
            if (c.isEmpty())
                throw new IllegalArgumentException("Collection is empty");
            Iterator<E> i = c.iterator();
            E first = i.next();
            MyEnumSet<E> result = MyEnumSet.of(first);
            while (i.hasNext())
                result.add(i.next());
            return result;
        }
    }

    /**
     * Creates an enum set with the same element type as the specified enum
     * set, initially containing all the elements of this type that are
     * <i>not</i> contained in the specified set.
     *
     * @param <E> The class of the elements in the enum set
     * @param s the enum set from whose complement to initialize this enum set
     * @return The complement of the specified set in this set
     * @throws NullPointerException if {@code s} is null
     */
    public static <E extends MyEnum<E>> MyEnumSet<E> complementOf(MyEnumSet<E> s) {
        MyEnumSet<E> result = copyOf(s);
        result.complement();
        return result;
    }

    /**
     * Creates an enum set initially containing the specified element.
     *
     * Overloadings of this method exist to initialize an enum set with
     * one through five elements.  A sixth overloading is provided that
     * uses the varargs feature.  This overloading may be used to create
     * an enum set initially containing an arbitrary number of elements, but
     * is likely to run slower than the overloadings that do not use varargs.
     *
     * @param <E> The class of the specified element and of the set
     * @param e the element that this set is to contain initially
     * @throws NullPointerException if {@code e} is null
     * @return an enum set initially containing the specified element
     */
    public static <E extends MyEnum<E>> MyEnumSet<E> of(E e) {
        MyEnumSet<E> result = noneOf(e.getDeclaringClass());
        result.add(e);
        return result;
    }

    /**
     * Creates an enum set initially containing the specified elements.
     *
     * Overloadings of this method exist to initialize an enum set with
     * one through five elements.  A sixth overloading is provided that
     * uses the varargs feature.  This overloading may be used to create
     * an enum set initially containing an arbitrary number of elements, but
     * is likely to run slower than the overloadings that do not use varargs.
     *
     * @param <E> The class of the parameter elements and of the set
     * @param e1 an element that this set is to contain initially
     * @param e2 another element that this set is to contain initially
     * @throws NullPointerException if any parameters are null
     * @return an enum set initially containing the specified elements
     */
    public static <E extends MyEnum<E>> MyEnumSet<E> of(E e1, E e2) {
        MyEnumSet<E> result = noneOf(e1.getDeclaringClass());
        result.add(e1);
        result.add(e2);
        return result;
    }

    /**
     * Creates an enum set initially containing the specified elements.
     *
     * Overloadings of this method exist to initialize an enum set with
     * one through five elements.  A sixth overloading is provided that
     * uses the varargs feature.  This overloading may be used to create
     * an enum set initially containing an arbitrary number of elements, but
     * is likely to run slower than the overloadings that do not use varargs.
     *
     * @param <E> The class of the parameter elements and of the set
     * @param e1 an element that this set is to contain initially
     * @param e2 another element that this set is to contain initially
     * @param e3 another element that this set is to contain initially
     * @throws NullPointerException if any parameters are null
     * @return an enum set initially containing the specified elements
     */
    public static <E extends MyEnum<E>> MyEnumSet<E> of(E e1, E e2, E e3) {
        MyEnumSet<E> result = noneOf(e1.getDeclaringClass());
        result.add(e1);
        result.add(e2);
        result.add(e3);
        return result;
    }

    /**
     * Creates an enum set initially containing the specified elements.
     *
     * Overloadings of this method exist to initialize an enum set with
     * one through five elements.  A sixth overloading is provided that
     * uses the varargs feature.  This overloading may be used to create
     * an enum set initially containing an arbitrary number of elements, but
     * is likely to run slower than the overloadings that do not use varargs.
     *
     * @param <E> The class of the parameter elements and of the set
     * @param e1 an element that this set is to contain initially
     * @param e2 another element that this set is to contain initially
     * @param e3 another element that this set is to contain initially
     * @param e4 another element that this set is to contain initially
     * @throws NullPointerException if any parameters are null
     * @return an enum set initially containing the specified elements
     */
    public static <E extends MyEnum<E>> MyEnumSet<E> of(E e1, E e2, E e3, E e4) {
        MyEnumSet<E> result = noneOf(e1.getDeclaringClass());
        result.add(e1);
        result.add(e2);
        result.add(e3);
        result.add(e4);
        return result;
    }

    /**
     * Creates an enum set initially containing the specified elements.
     *
     * Overloadings of this method exist to initialize an enum set with
     * one through five elements.  A sixth overloading is provided that
     * uses the varargs feature.  This overloading may be used to create
     * an enum set initially containing an arbitrary number of elements, but
     * is likely to run slower than the overloadings that do not use varargs.
     *
     * @param <E> The class of the parameter elements and of the set
     * @param e1 an element that this set is to contain initially
     * @param e2 another element that this set is to contain initially
     * @param e3 another element that this set is to contain initially
     * @param e4 another element that this set is to contain initially
     * @param e5 another element that this set is to contain initially
     * @throws NullPointerException if any parameters are null
     * @return an enum set initially containing the specified elements
     */
    public static <E extends MyEnum<E>> MyEnumSet<E> of(E e1, E e2, E e3, E e4,
                                                              E e5)
    {
        MyEnumSet<E> result = noneOf(e1.getDeclaringClass());
        result.add(e1);
        result.add(e2);
        result.add(e3);
        result.add(e4);
        result.add(e5);
        return result;
    }

    /**
     * Creates an enum set initially containing the specified elements.
     * This factory, whose parameter list uses the varargs feature, may
     * be used to create an enum set initially containing an arbitrary
     * number of elements, but it is likely to run slower than the overloadings
     * that do not use varargs.
     *
     * @param <E> The class of the parameter elements and of the set
     * @param first an element that the set is to contain initially
     * @param rest the remaining elements the set is to contain initially
     * @throws NullPointerException if any of the specified elements are null,
     *     or if {@code rest} is null
     * @return an enum set initially containing the specified elements
     */
    @SafeVarargs
    public static <E extends MyEnum<E>> MyEnumSet<E> of(E first, E... rest) {
        MyEnumSet<E> result = noneOf(first.getDeclaringClass());
        result.add(first);
        for (E e : rest)
            result.add(e);
        return result;
    }

    /**
     * Creates an enum set initially containing all of the elements in the
     * range defined by the two specified endpoints.  The returned set will
     * contain the endpoints themselves, which may be identical but must not
     * be out of order.
     *
     * @param <E> The class of the parameter elements and of the set
     * @param from the first element in the range
     * @param to the last element in the range
     * @throws NullPointerException if {@code from} or {@code to} are null
     * @throws IllegalArgumentException if {@code from.compareTo(to) > 0}
     * @return an enum set initially containing all of the elements in the
     *         range defined by the two specified endpoints
     */
    public static <E extends MyEnum<E>> MyEnumSet<E> range(E from, E to) {
        if (from.compareTo(to) > 0)
            throw new IllegalArgumentException(from + " > " + to);
        MyEnumSet<E> result = noneOf(from.getDeclaringClass());
        result.addRange(from, to);
        return result;
    }

    /**
     * Adds the specified range to this enum set, which is empty prior
     * to the call.
     */
    abstract void addRange(E from, E to);

    /**
     * Returns a copy of this set.
     *
     * @return a copy of this set
     */
    @SuppressWarnings("unchecked")
    public MyEnumSet<E> clone() {
        try {
            return (MyEnumSet<E>) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Complements the contents of this enum set.
     */
    abstract void complement();

    /**
     * Throws an exception if e is not of the correct type for this enum set.
     */
    final void typeCheck(E e) {
        Class<?> eClass = e.getClass();
        if (eClass != elementType && eClass.getSuperclass() != elementType)
            throw new ClassCastException(eClass + " != " + elementType);
    }

    /**
     * Returns all of the values comprising E.
     * The result is uncloned, cached, and shared by all callers.
     */
    private static <E extends MyEnum<E>> E[] getUniverse(Class<E> elementType) {
        return MyEnum.getUniverse(elementType);
    }
}
