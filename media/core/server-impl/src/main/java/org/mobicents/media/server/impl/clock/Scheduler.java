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

package org.mobicents.media.server.impl.clock;

import org.mobicents.media.server.spi.clock.Task;
import org.mobicents.media.server.spi.clock.Timer;

/**
 *
 * @author kulikov
 */
public class Scheduler implements Runnable {

    private volatile boolean started = false;
    private Thread worker;
    private long delay;
    private Taskset[] timeline = new Taskset[1000];
    private boolean blocked = false;
    private long second;
    
    private Timer timer;
    
    public Scheduler() {
        timer = new TimerImpl();
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
    
    public void start() {
        this.started = true;
        for (int i = 0; i < 1000; i++) {
            timeline[i] = new Taskset();
        }
        worker = new Thread(this, "Scheduler");
        worker.setPriority(Thread.MAX_PRIORITY);
        worker.start();
        
    }

    public void stop() {
        started = false;
        worker.interrupt();
    }

    public LocalTask execute(Task task) {
        //each new task is appended to the head
        LocalTask localTask = new LocalTask(task);
        timeline[0].tasks[timeline[0].pos++] = localTask;
        if (blocked) {
            blocked = false;
        }
        return localTask;
    }

    private class Taskset {

        private LocalTask[] tasks = new LocalTask[900];
        private int pos = 0;
        
    }

    @SuppressWarnings("static-access")
    public void run() {
        int time = 0;

        long start = 0;
        long finish = 0;

        long drift = 0;
        long latency = 0;

        int duration = 0;
        boolean found = false;
        
        LocalTask task = null;

        while (started) {
            //obtain task set which should be executed 
            //at this time
            Taskset set = timeline[time];
            
            //this taskset will be used for scheduling tasks with period of one second
            Taskset temp = new Taskset();
            
            //fixing current time when we start execution
            start = timer.getTimestamp();

            
            //running task for selected task set
            for (int i = 0; i < set.pos; i++) {
                task = set.tasks[i];
                
                //if current task is inactive then go to the next task
                if (!task.isActive()) {
                    continue;
                }
                
                //seconds > 0 means that this task was scheduled not for the current second
                //reschedule task if second not reaches yet required value
                if (task.seconds > 0 && task.seconds > second) {
                    temp.tasks[temp.pos++] = task;
                    continue;
                }
                
                //if task duration is zero we need to call task.perform() again
                //but we will restrict number of repeats to prevent infinite loop
                duration = 0;                
                int count = 0;
                while (duration == 0 && count < 10) {
                    try {
                        duration = task.perform();
                        count++;
                    } catch (Exception e) {
                    }
                }

                //scheduling next run for this task
                if (duration > 0) {
                    int future = time + duration;                
                    int milliseconds = future % 1000;
                    
                    task.seconds = second + future / 1000;
                
                    if (milliseconds == time) {
                        temp.tasks[temp.pos++] = task;
                    } else {
                        timeline[milliseconds].tasks[timeline[milliseconds].pos++] = task;
                    }
                }
            }

            //cleaning this task set
            timeline[time] = temp;
//            if (temp.pos > 0) {
//                timeline[time] = temp;
//            } else {
//                set.pos = 0;
//            }

            //next step is to find the next scheduled task
            //we are scrolling through timeline from the current time upto end 
            //of the current second for first not empty task set
            found = false;
            for (int i = time + 1; i < 1000; i++) {
                if (timeline[i].pos > 0) {
                    //non-empty task set found so we are calculating the 
                    //amount of time to wait before execution of this task set
                    delay = i - time;
                    found = true;
                    break;
                }
            }

            //if there are no any tasks scheduled within remainder of the
            //current second we are waiting for end of the second and then we should start
            //from time = 0 again because new tasks can be scheduled.
            //all new tasks always scheduled initialy at time = 0;
            if (!found) {
                delay = 1000 - time;
                second++;
            }

            //checking time when we are finished.
            finish = timer.getTimestamp();

            //calculating time spent for execution all above and 
            //correcting the amaont of time to wait.
            drift = (finish - start);
            latency = delay - drift;

            //it makes sense to wait if latency greater then 5 ms
            //but if latency less then of equals to zero then it means
            //that we have to wait infinetely until new task will be scheduled.
            if (latency >= 5) {
                try {
                    Thread.currentThread().sleep(latency);
                } catch (InterruptedException e) {
                    return;
                }
            }
            time += delay;
            if (time >= 1000) {
                time -= 1000;
            }
        }
    }
    
    public long getTimestamp() {
        return timer.getTimestamp();
    }
}
