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

package org.jivesoftware.smackx.filetransfer;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.StreamInitiation;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * After the file transfer negotiation process is completed according to
 * JEP-0096, the negotation process is passed off to a particular stream
 * negotiator. The stream negotiator will then negotiate the chosen stream and
 * return the stream to transfer the file.
 *
 * @author Alexander Wenckus
 */
public abstract class StreamNegotiator {

    /**
     * Creates the initiation acceptance packet to forward to the stream
     * initiator.
     *
     * @param streamInitiationOffer The offer from the stream initatior to connect for a stream.
     * @param namespaces            The namespace that relates to the accepted means of transfer.
     * @return The response to be forwarded to the initator.
     */
    public StreamInitiation createInitiationAccept(
            StreamInitiation streamInitiationOffer, String [] namespaces) {
        StreamInitiation response = new StreamInitiation();
        response.setTo(streamInitiationOffer.getFrom());
        response.setFrom(streamInitiationOffer.getTo());
        response.setType(IQ.Type.RESULT);
        response.setPacketID(streamInitiationOffer.getPacketID());

        DataForm form = new DataForm(Form.TYPE_SUBMIT);
        FormField field = new FormField(
                FileTransferNegotiator.STREAM_DATA_FIELD_NAME);
        for (int i = 0; i < namespaces.length; i++) {
            field.addValue(namespaces[i]);
        }
        form.addField(field);

        response.setFeatureNegotiationForm(form);
        return response;
    }


    public IQ createError(String from, String to, String packetID, XMPPError xmppError) {
        IQ iq = FileTransferNegotiator.createIQ(packetID, to, from, IQ.Type.ERROR);
        iq.setError(xmppError);
        return iq;
    }

    Packet initiateIncomingStream(XMPPConnection connection, StreamInitiation initiation) throws XMPPException {
        StreamInitiation response = createInitiationAccept(initiation,
                getNamespaces());

        // establish collector to await response
        PacketCollector collector = connection
                .createPacketCollector(getInitiationPacketFilter(initiation.getFrom(), initiation.getSessionID()));
        connection.sendPacket(response);

        Packet streamMethodInitiation = collector
                .nextResult(SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (streamMethodInitiation == null) {
            throw new XMPPException("No response from file transfer initiator");
        }

        return streamMethodInitiation;
    }

    /**
     * Returns the packet filter that will return the initiation packet for the appropriate stream
     * initiation.
     *
     * @param from     The initiatior of the file transfer.
     * @param streamID The stream ID related to the transfer.
     * @return The <b><i>PacketFilter</b></i> that will return the packet relatable to the stream
     *         initiation.
     */
    public abstract PacketFilter getInitiationPacketFilter(String from, String streamID);


    abstract InputStream negotiateIncomingStream(Packet streamInitiation) throws XMPPException;

    /**
     * This method handles the file stream download negotiation process. The
     * appropriate stream negotiator's initiate incoming stream is called after
     * an appropriate file transfer method is selected. The manager will respond
     * to the initatior with the selected means of transfer, then it will handle
     * any negotation specific to the particular transfer method. This method
     * returns the InputStream, ready to transfer the file.
     *
     * @param initiation The initation that triggered this download.
     * @return After the negotation process is complete, the InputStream to
     *         write a file to is returned.
     * @throws XMPPException If an error occurs during this process an XMPPException is
     *                       thrown.
     */
    public abstract InputStream createIncomingStream(StreamInitiation initiation) throws XMPPException;

    /**
     * This method handles the file upload stream negotiation process. The
     * particular stream negotiator is determined during the file transfer
     * negotiation process. This method returns the OutputStream to transmit the
     * file to the remote user.
     *
     * @param streamID  The streamID that uniquely identifies the file transfer.
     * @param initiator The fully-qualified JID of the initiator of the file transfer.
     * @param target    The fully-qualified JID of the target or reciever of the file
     *                  transfer.
     * @return The negotiated stream ready for data.
     * @throws XMPPException If an error occurs during the negotiation process an
     *                       exception will be thrown.
     */
    public abstract OutputStream createOutgoingStream(String streamID,
            String initiator, String target) throws XMPPException;

    /**
     * Returns the XMPP namespace reserved for this particular type of file
     * transfer.
     *
     * @return Returns the XMPP namespace reserved for this particular type of
     *         file transfer.
     */
    public abstract String[] getNamespaces();

    /**
     * Cleanup any and all resources associated with this negotiator.
     */
    public abstract void cleanup();


}
