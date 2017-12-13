/*
 *  Gettext Commons
 *
 *  Copyright (C) 2005  Felix Berger
 *  Copyright (C) 2005  Steffen Pingel
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.xnap.commons.i18n;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores a list of {@link I18n} objects by a String key.
 *
 * @author Steffen Pingel
 */
class I18nCache {

    /**
     * Map<String, List<I18n>>, list is synchronized too.
     */
    private final Map<String, I18n> cache = new ConcurrentHashMap<String, I18n>();

    I18nCache() {
    }

    public void clear() {
        cache.clear();
    }

    public I18n get(final String packageName, final Locale locale) {
        if (locale == null) {
            throw new NullPointerException("locale is null");
        }

        String key = getKey(packageName, locale);

        return cache.get(key);
    }

    public void put(String packageName, I18n i18n) {
        String key = getKey(packageName, i18n);

        cache.put(key, i18n);
    }

    public void visit(final Visitor visitor) {
        I18n[] i18ns;
        synchronized (cache) {
            i18ns = (I18n[]) cache.values().toArray();
        }

        for (int i = 0; i < i18ns.length; ++i) {
            I18n i18n = i18ns[i];
            visitor.visit(i18n);
        }

    }

    private String getKey(String packageName, I18n i18n) {
        return getKey(packageName, i18n.getLocale());
    }

    private String getKey(String packageName, Locale locale) {
        return packageName + "/" + locale.toString();
    }

    public static interface Visitor {
        void visit(I18n i18n);
    }

}
