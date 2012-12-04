package org.mobicents.slee.resource.diameter.rf.events.avp;

import net.java.slee.resource.diameter.rf.events.avp.MediaInitiatorFlag;
import net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * 
 * SdpMediaComponentImpl.java
 *
 * <br>Project:  mobicents
 * <br>11:19:56 PM Apr 11, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SdpMediaComponentImpl extends GroupedAvpImpl implements SdpMediaComponent {

  public SdpMediaComponentImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public SdpMediaComponentImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#getAuthorizedQos()
   */
  public String getAuthorizedQos() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.AUTHORIZED_QOS, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#getMediaInitiatorFlag()
   */
  public MediaInitiatorFlag getMediaInitiatorFlag() {
    return (MediaInitiatorFlag) getAvpAsEnumerated(DiameterRfAvpCodes.MEDIA_INITIATOR_FLAG, DiameterRfAvpCodes.TGPP_VENDOR_ID, MediaInitiatorFlag.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#getSdpMediaDescriptions()
   */
  public String[] getSdpMediaDescriptions() {
    return getAvpsAsUTF8String(DiameterRfAvpCodes.SDP_MEDIA_DESCRIPTION, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#getSdpMediaName()
   */
  public String getSdpMediaName() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.SDP_MEDIA_NAME, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#getTgppChargingId()
   */
  public String getTgppChargingId() {
    return getAvpAsOctetString(DiameterRfAvpCodes.TGPP_CHARGING_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#hasAuthorizedQos()
   */
  public boolean hasAuthorizedQos() {
    return hasAvp(DiameterRfAvpCodes.AUTHORIZED_QOS, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#hasMediaInitiatorFlag()
   */
  public boolean hasMediaInitiatorFlag() {
    return hasAvp(DiameterRfAvpCodes.MEDIA_INITIATOR_FLAG, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#hasSdpMediaName()
   */
  public boolean hasSdpMediaName() {
    return hasAvp(DiameterRfAvpCodes.SDP_MEDIA_NAME, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#hasTgppChargingId()
   */
  public boolean hasTgppChargingId() {
    return hasAvp(DiameterRfAvpCodes.TGPP_CHARGING_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#setAuthorizedQos(String)
   */
  public void setAuthorizedQos( String authorizedQos ) {
    addAvp(DiameterRfAvpCodes.AUTHORIZED_QOS, DiameterRfAvpCodes.TGPP_VENDOR_ID, authorizedQos);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#setMediaInitiatorFlag(net.java.slee.resource.diameter.rf.events.avp.MediaInitiatorFlag)
   */
  public void setMediaInitiatorFlag( MediaInitiatorFlag mediaInitiatorFlag ) {
    addAvp(DiameterRfAvpCodes.MEDIA_INITIATOR_FLAG, DiameterRfAvpCodes.TGPP_VENDOR_ID, mediaInitiatorFlag.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#setSdpMediaDescription(String)
   */
  public void setSdpMediaDescription( String sdpMediaDescription ) {
    addAvp(DiameterRfAvpCodes.SDP_MEDIA_DESCRIPTION, DiameterRfAvpCodes.TGPP_VENDOR_ID, sdpMediaDescription);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#setSdpMediaDescriptions(String[])
   */
  public void setSdpMediaDescriptions( String[] sdpMediaDescriptions ) {
    for(String sdpMediaDescription : sdpMediaDescriptions) {
      setSdpMediaDescription(sdpMediaDescription);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#setSdpMediaName(String)
   */
  public void setSdpMediaName( String sdpMediaName ) {
    addAvp(DiameterRfAvpCodes.SDP_MEDIA_NAME, DiameterRfAvpCodes.TGPP_VENDOR_ID, sdpMediaName);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#setTgppChargingId(byte[])
   */
  public void setTgppChargingId( String tgppChargingId ) {
    addAvp(DiameterRfAvpCodes.TGPP_CHARGING_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppChargingId);
  }

}
