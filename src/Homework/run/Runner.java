/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Homework.run;

import Homework.ants.Ant;
import Homework.ants.ChildAnt;
import Homework.ants.SoldierAnt;
import Homework.ants.WorkerAnt;
import Homework.colony.Colony;
import Homework.colony.Outside;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an instance runner class so that in the GUI parts of the
 * application the instance could be run more smooth, it's also a separate thread.
 */
public class Runner extends Thread {

    private final Colony colony = new Colony();
    private final Outside outside = new Outside(colony);
    private Integer numberOfAnts = 10000;
    private List<Ant> antList = new ArrayList<>();
    private boolean isStopped = false;

    /**
     * Main method where the simulation of the ant creation happens.
     * Also, when stop is called the thread is interrupted so that the creation of new ats will be immediate.
     */
    @Override
    public void run() {
        Ant ant;
        int counter = 0;
        while (true) {
            try {
                while (numberOfAnts != 0) {
                    if (counter == 3) {
                        ant = new SoldierAnt(colony, outside);
                    } else if (counter == 4) {
                        ant = new ChildAnt(colony);
                        counter = 0;
                    } else {
                        ant = new WorkerAnt(colony, outside);
                    }
                    Thread.sleep(ant.getRandomSleepTime(800, 3500));
                    ant.start();
                    antList.add(ant);
                    counter++;
                    numberOfAnts--;
                }
            } catch (InterruptedException e) {
                if (isStopped) {
                    while (isStopped) {
                        sleep();
                    }
                }
            }
        }
    }

    /**
     * Helper method for stopping all the threads that ants have.
     */
    public void stopProcess() {
        interrupt();
        for (Ant ant: antList) {
            ant.stopThread();
        }
    }

    /**
     * Helper method for resuming all the threads that ants have.
     */
    public void resumeProcess() {
        isStopped = false;
        for (Ant ant: antList) {
            ant.resumeThread();
        }

    }

    /**
     * Helper method for putting this thread to sleep.
     */
    private void sleep() {
        while (isStopped) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Colony getColony() {
        return colony;
    }

    public Outside getOutside() {
        return outside;
    }
}