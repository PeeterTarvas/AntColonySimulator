package Homework.run;

import Homework.ants.Ant;
import Homework.ants.ChildAnt;
import Homework.ants.SoldierAnt;
import Homework.ants.WorkerAnt;
import Homework.colony.Colony;
import Homework.colony.Outside;

import java.util.ArrayList;
import java.util.List;

public class Runner extends Thread {


    private final Colony colony = new Colony();
    private final Outside outside = new Outside(colony);
    private Integer numberOfAnts = 10000;
    private List<Ant> antList = new ArrayList<>();
    private boolean isStopped = false;

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
                        ant = new ChildAnt(colony, outside);
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

    public void stopProcess() {
        interrupt();
        for (Ant ant: antList) {
            ant.stopThread();
        }
    }

    public void resumeProcess() {
        isStopped = false;
        for (Ant ant: antList) {
            ant.resumeThread();
        }

    }

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