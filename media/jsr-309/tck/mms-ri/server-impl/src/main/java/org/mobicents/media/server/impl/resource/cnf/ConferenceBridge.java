/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party
 * contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
 */

package org.mobicents.media.server.impl.resource.cnf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import org.mobicents.media.Buffer;
import org.mobicents.media.Format;
import org.mobicents.media.MediaSink;
import org.mobicents.media.MediaSource;
import org.mobicents.media.server.impl.AbstractSink;
import org.mobicents.media.server.impl.AbstractSinkSet;
import org.mobicents.media.server.impl.AbstractSource;
import org.mobicents.media.server.impl.AbstractSourceSet;
import org.mobicents.media.server.impl.BaseComponent;
import org.mobicents.media.server.impl.resource.Proxy;
import org.mobicents.media.server.spi.Endpoint;
import org.mobicents.media.server.spi.ResourceGroup;
import org.mobicents.media.server.spi.dsp.Codec;

/**
 *
 * @author kulikov
 */
public class ConferenceBridge extends BaseComponent implements ResourceGroup {

    protected Endpoint endpoint;
    public final static Format[] FORMATS = new Format[] {Codec.LINEAR_AUDIO};
    
    protected ConcurrentHashMap<String, Splitter> splitters = new ConcurrentHashMap();
    protected ConcurrentHashMap<String, AudioMixer> mixers = new ConcurrentHashMap();
    protected ConcurrentHashMap<String, Proxy> proxies = new ConcurrentHashMap();
    
    private Input input;
    private Output output;
    
    private static ArrayList<String> mediaTypes = new ArrayList();
    static {
        mediaTypes.add("audio");
    }
    
    public ConferenceBridge(String name) {
        super(name);
        input = new Input("CnfBridge(Input)");
        output = new Output("CnfBridge(Output)");
    }
    
    public Collection<String> getMediaTypes() {
        return mediaTypes;
    }
    
    public MediaSource getSource(String media) {        
        return media.equals("audio") ? output : null;
    }
    
    public MediaSink getSink(String media) {
        return media.equals("audio") ? input : null;
    }
    

    private class Input extends AbstractSinkSet {

        public Input(String name) {
            super(name);
        }
        
        @Override
        public AbstractSink createSink(MediaSource otherParty) {
            Splitter splitter = new Splitter("Splitter[CnfBridge]");            
            String id = splitter.getInput().getId();
            
            splitter.setEndpoint(otherParty.getEndpoint());
            splitter.setConnection(otherParty.getConnection());
            //splitter.start();

            splitters.put(id, splitter);
            
            Collection <AudioMixer> list = mixers.values();
            for (AudioMixer mixer: list) {
                if (!mixer.getConnection().getId().equals(otherParty.getConnection().getId())) {
                    Proxy proxy = new Proxy("Proxy[CnfBridge]");
                    proxies.put(splitter.getId(), proxy);
                    
                    proxy.getInput().connect(splitter);
                    proxy.getOutput().connect(mixer);
                    
                    proxy.start();
                }
            }
                        
            return splitter.getInput();
        }

        @Override
        public void onMediaTransfer(Buffer buffer) throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Format[] getFormats() {
            return FORMATS;
        }

        public boolean isAcceptable(Format format) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void destroySink(AbstractSink sink) {
            
        }
        
    }
    
    private class Output extends AbstractSourceSet {

        public Output(String name) {
            super(name);
        }

        @Override
        public void start() {
            Collection<AudioMixer> list = mixers.values();
            for (AudioMixer mixer: list) {
                mixer.start();
            }
        }
        
        @Override
        public AbstractSource createSource(MediaSink otherParty) {
            AudioMixer mixer = new AudioMixer("AudioMixer[CnfBridge]", otherParty.getEndpoint().getTimer());
            mixers.put(mixer.getOutput().getId(), mixer);
            mixer.setEndpoint(mixer.getEndpoint());
            mixer.setConnection(otherParty.getConnection());
            

            Collection<Splitter> list = splitters.values();
            for (Splitter splitter : list) {
                if (!splitter.getConnection().getId().equals(otherParty.getConnection().getId())) {
                    Proxy proxy = new Proxy("Proxy[CnfBridge]");
                    proxies.put(mixer.getId(), proxy);
                    
                    proxy.getInput().connect(splitter);
                    proxy.getOutput().connect(mixer);
                    
                    proxy.start();
                }
            }
            
            return (AbstractSource) mixer.getOutput();
        }

        @Override
        public void evolve(Buffer buffer, long timestamp, long sequenceNumber) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Format[] getFormats() {
            return FORMATS;
        }

        @Override
        public void destroySource(AbstractSource source) {
        }
        
        
    }

    public void start() {
    }

    public void stop() {
    }

}
