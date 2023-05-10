/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Homework.ants;

import Homework.colony.Colony;
import Homework.colony.Outside;

/**
 *
 * @author peete
 */
public class WorkerAnt extends Ant {

    private Integer food = 0;
    private static int nextId = 0;
    private Colony colony;
    private Outside outside;
    private boolean isEvenNr;

    public WorkerAnt(Colony colony, Outside outside) {
        this.id = "WA" + String.format("%04d", nextId);
        this.colony = colony;
        this.outside = outside;
        if (nextId % 2 == 0) {
            this.isEvenNr = true;
        }
        nextId += 1;
    }

    /**
     * This is the main action of the harvester ant.
     */
    private void harvestAction() throws InterruptedException {
        colony.exit(this);
        logger.info(this + " exit colony");
        outside.harvestFood(this);
        logger.info(this + " harvest food");
        colony.enter(this);
        logger.info(this + " enters colony");
        colony.goToFoodStorageArea(this).placeFoodAtStorageArea(this);
        logger.info(this + " places food at storage");
        colony.leaveFoodStorageArea(this);
    }

    /**
     * This is the main action that the food moving ants have.
     */
    private void movingAction() throws InterruptedException {
        colony.goToFoodStorageArea(this).getFoodFromStorageArea(this);
        logger.info(this + " gets food from storage");
        colony.leaveFoodStorageArea(this);
        threadSleep(getRandomSleepTime(1000, 3000));
        colony.goToEatingArea(this).placeFoodAtEatingArea(this);
        logger.info(this + " places food at eating area");
        colony.leaveEatingArea(this);
    }

    /**
     * This is the action where the worker ant takes a break.
     */
    private void takeBreak() throws InterruptedException {
        logger.info(this + " eats");
        colony.goToEatingArea(this).getConsumableFood(this);
        threadSleep(3000); // eating
        colony.leaveEatingArea(this);
        logger.info(this + " rests");
        colony.goToRestArea(this);
        threadSleep(1000); // sleeps
        colony.leaveRestArea(this);
    }

    /**
     * Run method is where all the action calling is taken place.
     * It runs forever with the while(true) so the ant won't stop.
     */
    @Override
    public void run() {
        int counter = 0;
        try {
            colony.enter(this);
        } catch (InterruptedException e) {
        }
        logger.info(this + " enters colony");
        while (true) {
            try {
                while (counter < 10) {
                    if (isEvenNr) {
                        movingAction();
                    } else {
                        harvestAction();
                    }
                    counter++;
                }
                logger.info(this + " goes on a break");
                counter = 0;
                takeBreak();
            } catch (InterruptedException e) {
                while (isStopped) {
                    try {
                        threadSleep(100);
                    } catch (InterruptedException ex) {}
                }
            }
        }
    }

    /**
     * Method for giving food to the eating area
     */
    public Integer giveFood() {
        Integer temp = food;
        food = 0;
        return temp;
    }

    /**
     * Method for getting food from storage area, so it can be sent to eating area
     */
    public Integer getFood(Integer foodAmount) {
        food += foodAmount;
        return foodAmount;
    }

    @Override
    public String toString() {
        return id;
    }
}
