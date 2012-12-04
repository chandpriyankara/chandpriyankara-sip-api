/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.media.server.impl.resource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mobicents.media.server.impl.clock.TimerImpl;
import org.mobicents.media.server.impl.resource.test.TransmissionTester;
import org.mobicents.media.server.spi.Timer;

/**
 *
 * @author kulikov
 */
public class ProxyTest {

    private Proxy proxy;
    private Timer timer;
    private TransmissionTester tester;
    
    public ProxyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        timer = new TimerImpl();
        timer.start();
        proxy = new Proxy("test");
        tester = new TransmissionTester(timer);
    }

    @After
    public void tearDown() {
        timer.stop();
    }

    /**
     * Test of start method, of class Proxy.
     */
    @Test
    public void testTransmission() {
        tester.connect(proxy.getInput());
        tester.connect(proxy.getOutput());
        proxy.start();
        tester.start();
        assertTrue(tester.getMessage(), tester.isPassed());
        proxy.stop();
    }

}