#!/bin/sh
java -server -Xms1536m -Xmx1536m -XX:PermSize=128M -XX:MaxPermSize=256M -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -jar /home/vralev/test/mss-1.3-SNAPSHOT-jboss-4.2.3.GA/sip-balancer/sip-balancer-jar-with-dependencies.jar -mobicents-balancer-config=/home/vralev/test/mss-1.3-SNAPSHOT-jboss-4.2.3.GA/sip-balancer/lb-configuration.properties 
