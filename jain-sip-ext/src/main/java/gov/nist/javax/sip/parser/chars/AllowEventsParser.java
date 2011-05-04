/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/*
* Conditions Of Use
*
* This software was developed by employees of the National Institute of
* Standards and Technology (NIST), an agency of the Federal Government.
* Pursuant to title 15 Untied States Code Section 105, works of NIST
* employees are not subject to copyright protection in the United States
* and are considered to be in the public domain.  As a result, a formal
* license is not needed to use the software.
*
* This software is provided by NIST as a service and is expressly
* provided "AS IS."  NIST MAKES NO WARRANTY OF ANY KIND, EXPRESS, IMPLIED
* OR STATUTORY, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTY OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NON-INFRINGEMENT
* AND DATA ACCURACY.  NIST does not warrant or make any representations
* regarding the use of the software or the results thereof, including but
* not limited to the correctness, accuracy, reliability or usefulness of
* the software.
*
* Permission to use this software is contingent upon your acceptance
* of the terms of this agreement
*
* .
*
*/
package gov.nist.javax.sip.parser.chars;

import gov.nist.javax.sip.header.*;
import gov.nist.core.*;
import java.text.ParseException;

/**
 * Parser for AllowEvents header.
 *
 * @version 1.2 $Revision: 1.8 $ $Date: 2009/07/17 18:57:57 $
 *
 * @author Olivier Deruelle
 * @author M. Ranganathan
 *
 *
 */
public class AllowEventsParser extends HeaderParser {

    /**
     * Creates a new instance of AllowEventsParser
     * @param allowEvents the header to parse
     */
    public AllowEventsParser(char[] allowEvents) {
        super(allowEvents);
    }

    /**
     * Constructor
     * @param lexer the lexer to use to parse the header
     */
    protected AllowEventsParser(Lexer lexer) {
        super(lexer);
    }

    /**
     * parse the AllowEvents String header
     * @return SIPHeader (AllowEventsList object)
     * @throws SIPParseException if the message does not respect the spec.
     */
    public SIPHeader parse() throws ParseException {

        if (debug)
            dbg_enter("AllowEventsParser.parse");
        AllowEventsList list = new AllowEventsList();

        try {
            headerName(TokenTypes.ALLOW_EVENTS);

            AllowEvents allowEvents = new AllowEvents();
            allowEvents.setHeaderName(SIPHeaderNames.ALLOW_EVENTS);

            this.lexer.SPorHT();
            this.lexer.match(TokenTypes.ID);
            Token token = lexer.getNextToken();
            allowEvents.setEventType(token.getTokenValue());

            list.add(allowEvents);
            this.lexer.SPorHT();
            while (lexer.lookAhead(0) == ',') {
                this.lexer.match(',');
                this.lexer.SPorHT();

                allowEvents = new AllowEvents();
                this.lexer.match(TokenTypes.ID);
                token = lexer.getNextToken();
                allowEvents.setEventType(token.getTokenValue());

                list.add(allowEvents);
                this.lexer.SPorHT();
            }
            this.lexer.SPorHT();
            this.lexer.match('\n');

            return list;
        } finally {
            if (debug)
                dbg_leave("AllowEventsParser.parse");
        }
    }


}
/*
 * $Log: AllowEventsParser.java,v $
 * Revision 1.8  2009/07/17 18:57:57  emcho
 * Converts indentation tabs to spaces so that we have a uniform indentation policy in the whole project.
 *
 * Revision 1.7  2006/07/13 09:02:01  mranga
 * Issue number:
 * Obtained from:
 * Submitted by:  jeroen van bemmel
 * Reviewed by:   mranga
 * Moved some changes from jain-sip-1.2 to java.net
 *
 * CVS: ----------------------------------------------------------------------
 * CVS: Issue number:
 * CVS:   If this change addresses one or more issues,
 * CVS:   then enter the issue number(s) here.
 * CVS: Obtained from:
 * CVS:   If this change has been taken from another system,
 * CVS:   then name the system in this line, otherwise delete it.
 * CVS: Submitted by:
 * CVS:   If this code has been contributed to the project by someone else; i.e.,
 * CVS:   they sent us a patch or a set of diffs, then include their name/email
 * CVS:   address here. If this is your work then delete this line.
 * CVS: Reviewed by:
 * CVS:   If we are doing pre-commit code reviews and someone else has
 * CVS:   reviewed your changes, include their name(s) here.
 * CVS:   If you have not had it reviewed then delete this line.
 *
 * Revision 1.3  2006/06/19 06:47:27  mranga
 * javadoc fixups
 *
 * Revision 1.2  2006/06/16 15:26:28  mranga
 * Added NIST disclaimer to all public domain files. Clean up some javadoc. Fixed a leak
 *
 * Revision 1.1.1.1  2005/10/04 17:12:35  mranga
 *
 * Import
 *
 *
 * Revision 1.5  2004/07/28 14:13:54  mranga
 * Submitted by:  mranga
 *
 * Move out the test code to a separate test/unit class.
 * Fixed some encode methods.
 *
 * Revision 1.4  2004/01/22 13:26:31  sverker
 * Issue number:
 * Obtained from:
 * Submitted by:  sverker
 * Reviewed by:   mranga
 *
 * Major reformat of code to conform with style guide. Resolved compiler and javadoc warnings. Added CVS tags.
 *
 * CVS: ----------------------------------------------------------------------
 * CVS: Issue number:
 * CVS:   If this change addresses one or more issues,
 * CVS:   then enter the issue number(s) here.
 * CVS: Obtained from:
 * CVS:   If this change has been taken from another system,
 * CVS:   then name the system in this line, otherwise delete it.
 * CVS: Submitted by:
 * CVS:   If this code has been contributed to the project by someone else; i.e.,
 * CVS:   they sent us a patch or a set of diffs, then include their name/email
 * CVS:   address here. If this is your work then delete this line.
 * CVS: Reviewed by:
 * CVS:   If we are doing pre-commit code reviews and someone else has
 * CVS:   reviewed your changes, include their name(s) here.
 * CVS:   If you have not had it reviewed then delete this line.
 *
 */
