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

package javax.xml.registry.infomodel;

import java.util.*;
import java.sql.Timestamp;
import javax.xml.registry.*;

/**
 * AuditableEvent instances provide a long term record of events that effect a 
 * change of state in a RegistryObject. Such events are usually a result of a 
 * client initiated request. AuditableEvent instances are generated by the 
 * registry service to log such events.
 * <p>
 * Often such events effect a change in the life cycle of a RegistryObject. 
 * For example a client request could Create, Update, Deprecate or Delete a 
 * RegistryObject. No AuditableEvent is created for requests that do not alter
 * the state of a RegistryObject. Specifically, read-only requests do not generate 
 * an AuditableEvent.  
 * No AuditableEvent is generated for a RegistryObject when it is classified, 
 * assigned to a Package or associated with another Object.
 * <p>
 * A RegistryObject is associated with an ordered Collection of AuditableEvent 
 * instances that provide a complete audit trail for that Object.
 *
 *
 * @see RegistryObject
 * @author Farrukh S. Najmi
 */
public interface AuditableEvent extends RegistryObject {

    /** Gets the User associated with this object.
     *
     * <p><DL><DT><B>Capability Level: 1 </B></DL> 	 
     *
	 * @return the User that sent the request that generated this this AuditableEvent. Must not be null
     * @throws JAXRException	If the JAXR provider encounters an internal error
     *
     * @label requestor
     * @supplierCardinality 1
     * @directed
     * @associates <{User}>
     */
    User getUser() throws JAXRException;

    /** 
	 * Gets the Timestamp for when this event occurred. 
	 *
	 * <p><DL><DT><B>Capability Level: 1 </B></DL> 	 
	 *
	 * @return the timestamp that records the time the event occured
	 * @throws JAXRException	If the JAXR provider encounters an internal error
	 *
	 */
    Timestamp getTimestamp() throws JAXRException;

    /** 
	 * Gets the type of this event.
	 *
	 * <p><DL><DT><B>Capability Level: 1 </B></DL> 	 
	 *
	 * @see AuditableEvent#EVENT_TYPE_CREATED
	 * @return the type of this event
	 * @throws JAXRException	If the JAXR provider encounters an internal error
	 *
	 */
    int getEventType() throws JAXRException;

    /** 
	 * Gets the RegistryObject associated with this AuditableEvent. 
	 *
	 * <p><DL><DT><B>Capability Level: 1 </B></DL> 	 
	 *
	 * @return the RegistryObject that was the focus of this event
	 * @throws JAXRException	If the JAXR provider encounters an internal error
	 *
	 */
    RegistryObject getRegistryObject() throws JAXRException;

	/** An event where a RegistryObject is created. */
	public static final int EVENT_TYPE_CREATED=0;
	
	/** An event where a RegistryObject is deleted. */
	public static final int EVENT_TYPE_DELETED=1;
	
	/** An event where a RegistryObject is deprecated. */	
	public static final int EVENT_TYPE_DEPRECATED=2;

	/** An event where a RegistryObject is updated. */
	public static final int EVENT_TYPE_UPDATED=3;

	/** An event where a RegistryObject is versioned. */
	public static final int EVENT_TYPE_VERSIONED=4;
	
	/** An event where a RegistryObject is undeprecated. */	
	public static final int EVENT_TYPE_UNDEPRECATED=5;

}
