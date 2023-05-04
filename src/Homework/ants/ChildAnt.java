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
public class ChildAnt extends Ant {

    private static int nextId = 0;
    private Colony colony;

    public ChildAnt(Colony colony, Outside outside) {
        this.id = "BA" + String.format("%04d", nextId);
        this.colony = colony;
        nextId++;
    }

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

    private void mainAction() throws InterruptedException {
        colony.goToEatingArea(this).getConsumableFood(this);
        threadSleep(getRandomSleepTime(3000, 5000));
        colony.leaveEatingArea(this);
        colony.goToRestArea(this);
        threadSleep(4000);
        colony.leaveRestArea(this);
    }

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
