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
 *  This is the abstract class that all the other ant classes: ChildAnt, SoldierAnt and WorkerAnt extend.
 */
public abstract class Ant extends Thread {

    protected Logger logger = MyLogger.getMyLogger();

    protected String id;
    protected boolean isStopped = false;

    /**
     * This will be overwritten in the child classes
     */
    @Override
    public void run() {
    }

    /**
     * This method is for putting the thread to sleep for some time
     * @param milliseconds of sleep
     */
    public void threadSleep(int milliseconds) throws InterruptedException {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            logger.info(this + " ant interrupted");
            throw new InterruptedException();
        }
    }

    /**
     * Generates random sleep time
     * @param minMillis is the lower-bound of the generation
     * @param maxMillis is the upper-bound of the generation
     */
    public int getRandomSleepTime(int minMillis, int maxMillis) {
        Random rand = new Random();
        return rand.nextInt(maxMillis - minMillis + 1) + minMillis;
    }

    /**
     * This method is to simulate the ant eating food
     */
    public Integer consumeFood() {
        return 4;
    }

    @Override
    public String toString() {
        return id;
    }

    /**
     * This method is helper method for stopping the thread
     */
    public void stopThread() {
        isStopped = true;
        this.interrupt();
    }

    public void resumeThread() {
        isStopped = false;
    }
}
