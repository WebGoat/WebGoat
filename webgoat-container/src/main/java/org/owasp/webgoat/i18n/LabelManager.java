
package org.owasp.webgoat.i18n;

import org.owasp.webgoat.session.LabelDebugger;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Locale;


/**
 *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for
 * free software projects.
 *
 * @version $Id: $Id
 * @author dm
 */
@Component
public class LabelManager
{
	private static final long serialVersionUID = 1L;

	private LabelProvider labelProvider;
	private LabelDebugger labelDebugger;
	private Locale locale = new Locale(LabelProvider.DEFAULT_LANGUAGE);

	/**
	 * <p>Constructor for LabelManagerImpl.</p>
	 *
	 * @param labelProvider a {@link LabelProvider} object.
	 */
	protected LabelManager(LabelProvider labelProvider, LabelDebugger labelDebugger) {
		this.labelDebugger = labelDebugger;
		this.labelProvider = labelProvider;
	}

	/** {@inheritDoc} */
	public void setLocale(Locale locale)
	{
		if (locale != null)
		{
			this.locale = locale;
		}
	}

	/** {@inheritDoc} */
	public String get(String labelKey, Object... params)
	{
		String label = labelProvider.get(locale, labelKey, params);
		if (labelDebugger.isEnabled()) {
			label = "<font color=\"#00CD00\">" + label + "</font>";
		}
		return label;
	}

}
