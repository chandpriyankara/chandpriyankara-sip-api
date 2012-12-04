/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */
package org.mobicents.jcc.inap.protocol.parms;

/**
 * Address presentation restricted indicator.
 * 
 * @author kulikov
 */
public interface APRI {
    public final static int PRESENTATION_ALLOWED = 0;
    public final static int PRESENTATION_RESTRICTED = 1;
    public final static int ADDRESS_NOT_AVAILABLE = 2;
}
