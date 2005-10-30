/* Copyright (C) 2005 Tim Fennell
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the license with this software. If not,
 * it can be found online at http://www.fsf.org/licensing/licenses/lgpl.html
 */
package net.sourceforge.stripes.util;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Queue;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Common utilty methods that are useful when working with reflection.
 *
 * @author Tim Fennell
 */
public class ReflectUtil {
    /** Static helper class, shouldn't be constructed. */
    private ReflectUtil() {}

    /**
     * Holds a map of commonly used interface types (mostly collections) to a class that
     * implements the interface and will, by default, be instantiated when an instance
     * of the iterface is needed.
     */
    protected static final Map<Class,Class> interfaceImplementations = new HashMap<Class,Class>();

    static {
        interfaceImplementations.put(Collection.class, ArrayList.class);
        interfaceImplementations.put(List.class,       ArrayList.class);
        interfaceImplementations.put(Set.class,        HashSet.class);
        interfaceImplementations.put(SortedSet.class,  TreeSet.class);
        interfaceImplementations.put(Queue.class,      LinkedList.class);
        interfaceImplementations.put(Map.class,        HashMap.class);
        interfaceImplementations.put(SortedMap.class,  TreeMap.class);
    }

    /**
     * Looks up the default implementing type for the supplied interface. This is done
     * based on a static map of known common interface types and implementing classes.
     *
     * @param iface an interface for which an implementing class is needed
     * @return a Class object representing the implementing type, or null if one is
     *         not found
     */
    public static Class getImplementingClass(Class iface) {
        return interfaceImplementations.get(iface);
    }

    public static <T> T getInterfaceInstance(Class<T> interfaceType)
            throws InstantiationException, IllegalAccessException {
        Class impl = getImplementingClass(interfaceType);
        if (impl == null) {
            throw new InstantiationException(
                    "Stripes needed to instantiate a property who's declared type as an " +
                    "interface (which obviously cannot be instantiated. The interface is not " +
                    "one that Stripes is aware of, so no implementing class was known. The " +
                    "interface type was: '" + interfaceType.getName() + "'. To fix this " +
                    "you'll need to do one of three things. 1) Change the getter/setter methods " +
                    "to use a concrete type so that Stripes can instantiate it. 2) in the bean's " +
                    "setContext() method pre-instantiate the property so Stripes doesn't have to. " +
                    "3) Bug the Stripes author ;)  If the interface is a JDK type it can easily be " +
                    "fixed. If not, if enough people ask, a generic way to handle the problem " +
                    "might get implemented.");
        }
        else {
            return (T) impl.newInstance();
        }
    }
}
