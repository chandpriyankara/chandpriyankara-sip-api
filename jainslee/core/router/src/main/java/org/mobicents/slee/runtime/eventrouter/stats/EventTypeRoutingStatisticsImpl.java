/**
 * 
 */
package org.mobicents.slee.runtime.eventrouter.stats;

import javax.slee.EventTypeID;

import org.mobicents.slee.container.eventrouter.stats.EventRouterExecutorStatistics;
import org.mobicents.slee.container.eventrouter.stats.EventTypeRoutingStatistics;

/**
 * Impl of {@link EventRouterExecutorStatistics}. This class is not thread safe
 * due to performance overhead introduced when collecting stats, i.e., errors in
 * values due to multiple threads accessing the statistics state are not
 * important.
 * 
 * @author martins
 * 
 */
public class EventTypeRoutingStatisticsImpl implements
		EventTypeRoutingStatistics {

	private final EventTypeID eventTypeID;
	private long eventsRouted = 0L;
	private long routingTime = 0L;

	/**
	 * 
	 */
	public EventTypeRoutingStatisticsImpl(EventTypeID eventTypeID) {
		if (eventTypeID == null) {
			throw new NullPointerException("null event type id parameter");
		}
		this.eventTypeID = eventTypeID;
	}

	/**
	 * Adds the time for an event routing.
	 * 
	 * @param routingTime
	 *            the time spent to route the event, in milliseconds
	 */
	public void eventRouted(long routingTime) {
		this.eventsRouted++;
		this.routingTime += routingTime;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.eventrouter.stats.EventTypeRoutingStatistics#getAverageEventRoutingTime()
	 */
	public long getAverageEventRoutingTime() {
		return routingTime == 0L ? 0L : routingTime / eventsRouted;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.stats.EventTypeRoutingStatistics
	 * #getEventType()
	 */
	public EventTypeID getEventType() {
		return eventTypeID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.stats.EventTypeRoutingStatistics
	 * #getEventsRouted()
	 */
	public long getEventsRouted() {
		return eventsRouted;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.stats.EventTypeRoutingStatistics
	 * #getRoutingTime()
	 */
	public long getRoutingTime() {
		return routingTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return eventTypeID.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return eventTypeID
				.equals(((EventTypeRoutingStatisticsImpl) obj).eventTypeID);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return eventTypeID+" routing statistics: EVENTS = "+eventsRouted+", TIME = "+routingTime+", AVERAGE "+getAverageEventRoutingTime();
	}
}
