/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Homework.ants;

import Homework.colony.Colony;
import Homework.colony.Outside;

/*
 * @author peete
 */
public class ChildAnt extends Ant {

    private static int nextId = 0;
    private Colony colony;

    public ChildAnt(Colony colony) {
        this.id = "BA" + String.format("%04d", nextId);
        this.colony = colony;
        nextId++;
    }

    /**
     * This is the take shelter action method.
     * While colony is under attack the ant is in shelter.
     */
    public void takeShelter() {
        try {
            logger.info(this + " goes to shelter");
            colony.goTorShelter(this);
            while (colony.isUnderAttack()) {
                threadSleep(100);
            }
            colony.leaveShelter(this);
            logger.info(this + " exits shelter");
        } catch (InterruptedException ex) {
        }
    }

    /**
     * This is the main action of child ant where he/she goes to eat and then to sleep.
     */
    private void mainAction() throws InterruptedException {
        logger.info(this + " goes to eating area");
        colony.goToEatingArea(this).getConsumableFood(this);
        logger.info(this + " eats");
        threadSleep(getRandomSleepTime(3000, 5000));
        colony.leaveEatingArea(this);
        logger.info(this + " goes to rest area");
        colony.goToRestArea(this);
        logger.info(this + " sleeps");
        threadSleep(4000);
        colony.leaveRestArea(this);
    }

    /**
     * Run method is where all the action calling is taken place.
     * It runs forever with the while(true) so the ant won't stop.
     * When the child ant is interrupted - colony gets attacked or system is paused - then it goes to the catch method.
     */
    @Override
    public void run() {
        try {
            colony.enter(this);
            logger.info(this + " enters colony");
        } catch (InterruptedException e) {
        }
        while (true) {
            while (!isStopped) {
                try {
                    if (!colony.isUnderAttack()) {
                        mainAction();
                    } else {
                        takeShelter();
                    }
                } catch (InterruptedException e) {
                    if (isStopped) {
                        while (isStopped) {
                            try {
                                threadSleep(100);
                            } catch (InterruptedException ex) {}
                        }
                    } else {
                        takeShelter();
                    }
                }
            }
        }
    }

}
