/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Homework.ants;

import Homework.utilities.MyLogger;

import java.util.Random;
import java.util.logging.Logger;

/**
 *
 * @author peete    
 */
public class Ant extends Thread {

    protected Logger logger = MyLogger.getMyLogger();

    protected String id;

    protected boolean isStopped = false;

    @Override
    public void run() {

    }

    public void threadSleep(int milliseconds) throws InterruptedException {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            logger.info(this + " ant interrupted");
            throw new InterruptedException();
        }
    }

    public int getRandomSleepTime(int minMillis, int maxMillis) {
        Random rand = new Random();
        return rand.nextInt(maxMillis - minMillis + 1) + minMillis;
    }

    public Integer consumeFood() {
        return 4;
    }

    @Override
    public String toString() {
        return id;
    }

    public void stopThread() {
        isStopped = true;
        this.interrupt();
    }

    public void resumeThread() {
        isStopped = false;
    }
}
