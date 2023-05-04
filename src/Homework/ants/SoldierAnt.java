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
public class SoldierAnt extends Ant {

    private static int nextId = 0;
    private Colony colony;
    private Outside outside;


    public SoldierAnt(Colony colony, Outside outside) {
        this.id = "SA" + String.format("%04d", nextId);
        this.colony = colony;
        this.outside = outside;
        nextId++;
    }

    private void mainAction() throws InterruptedException {
        colony.goToInstructionArea(this);
        logger.info(this + " is training");
        threadSleep(getRandomSleepTime(2000, 8000));
        colony.leaveInstructionArea(this);
        colony.goToRestArea(this);
        logger.info(this + " is resting");
        threadSleep(2000);
        colony.leaveRestArea(this);
    }

    public void eat() throws InterruptedException {
        logger.info(this + " is eating");
        colony.goToEatingArea(this).getConsumableFood(this);
        threadSleep(3000);
        colony.leaveEatingArea(this);
    }

    private void dealWithThreat() throws InterruptedException {
        colony.exit(this);
        logger.info(this + " is dealing with the threat");
        outside.dealWithThreat(this);
        logger.info(this + " ant is resuming");
        colony.enter(this);
    }



    @Override
    public void run() {
            try {
                colony.enter(this);
                logger.info(this + " enters colony");
            } catch (InterruptedException e) {}
            while (true) {
                try {
                    for (int i = 0; i < 6; i++) {
                        mainAction();
                    }
                    eat();
                } catch (InterruptedException e) {
                    try {
                        if (isStopped) {
                            while (isStopped) {
                                Thread.sleep(100);
                            }
                        } else {
                            dealWithThreat();
                        }
                    } catch (InterruptedException ex) {}
                }
                }
        }
}
