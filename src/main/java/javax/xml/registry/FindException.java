/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package javax.xml.registry;

import javax.xml.registry.infomodel.*;

/**
 * A RegistryException that occurs during a find action.
 *
 * @author Farrukh S. Najmi
 */
public class FindException extends RegistryException {
	
	 /**
     * Constructs a <code>JAXRException</code> object with no
     * reason or embedded Throwable.
     */
    public FindException() {
        super();
		this.cause = null;
    }

    /**
     * Constructs a <code>JAXRException</code> object with the given
     * <code>String</code> as the reason for the exception being thrown.
     *
     * @param reason a description of what caused the exception
     */
    public FindException(String reason) {
        super(reason);
		this.cause = null;
    }

    /**
     * Constructs a <code>JAXRException</code> object with the given
     * <code>String</code> as the reason for the exception being thrown
     * and the given <code>Throwable</code> object as an embedded
     * Throwable.
     *
     * @param reason a description of what caused the exception
     * @param cause a <code>Throwable</code> object that is to
     *        be embedded in this <code>JAXRException</code> object
     */
    public FindException(String reason, Throwable cause) {
       super (reason);
       initCause(cause);
    }

    /**
     * Constructs a <code>JAXRException</code> object initialized
     * with the given <code>Throwable</code> object.
     *
     * @param cause	the Throwable that caused this Exception
     */
    public FindException(Throwable cause) {
		super (cause.toString());
		initCause(cause);
    }

}
