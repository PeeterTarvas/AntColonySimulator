    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package Homework.colony;

    import Homework.ants.Ant;
    import Homework.ants.SoldierAnt;
    import Homework.ants.WorkerAnt;
    import Homework.utilities.MyLogger;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.concurrent.BrokenBarrierException;
    import java.util.concurrent.CyclicBarrier;
    import java.util.concurrent.locks.ReadWriteLock;
    import java.util.concurrent.locks.ReentrantReadWriteLock;
    import java.util.logging.Logger;
    import java.util.stream.Collectors;

    /**
     *
     * @author peete
     */
    public class Outside {

        private ArrayList<Ant> ants = new ArrayList<>();
        private ArrayList<SoldierAnt> soldierAnts = new ArrayList<>();
        private ReadWriteLock antsListLock =  new ReentrantReadWriteLock();
        private ReadWriteLock soldierAntsListLock =  new ReentrantReadWriteLock();
        private Colony colony;
        private int nrOfSoldierAntsNeeded = 0;
        private CyclicBarrier cyclicBarrier;
        private Logger logger = MyLogger.getMyLogger();

        public Outside(Colony colony) {
            this.colony = colony;
            colony.setOutside(this);
        }

        /**
         * @return the number of ants outside the colony.
         */
        public Integer getNrOfAnts() {
            int nrOfAnts = 0;
            nrOfAnts += getNrOfAntsRepellingInvasion();
            antsListLock.writeLock().lock();
            nrOfAnts += ants.size();
            antsListLock.writeLock().unlock();
            return nrOfAnts;
        }

        /**
         * @return nr of ants dealing with the invasion of the colony.
         */
        public Integer getNrOfAntsRepellingInvasion() {
            int nrOfAnts = 0;
            soldierAntsListLock.writeLock().lock();
            nrOfAnts += soldierAnts.size();
            soldierAntsListLock.writeLock().unlock();
            return nrOfAnts;
        }

        /**
         * Add ant to the Outside area object, if it is a soldier ant then they have a different list.
         * @param ant to be added to the colony.
         */
        public void addAnt(Ant ant) {
            if (SoldierAnt.class == ant.getClass()) {
                soldierAntsListLock.writeLock().lock();
                soldierAnts.add((SoldierAnt) ant);
                soldierAntsListLock.writeLock().unlock();
            } else {
                antsListLock.writeLock().lock();
                ants.add(ant);
                antsListLock.writeLock().unlock();
            }
        }

        /**
         * Remove ant from the outside area.
         * @param ant to be removed.
         */
        public void removeAnt(Ant ant) {
            if (SoldierAnt.class == ant.getClass()) {
                soldierAntsListLock.writeLock().lock();
                soldierAnts.remove((SoldierAnt) ant);
                soldierAntsListLock.writeLock().unlock();
            } else {
                antsListLock.writeLock().lock();
                ants.remove(ant);
                antsListLock.writeLock().unlock();
            }
        }

        /**
         * Harvest new food that ant brings back to colony.
         * @param ant that harvests food.
         */
        public void harvestFood(WorkerAnt ant) throws InterruptedException {
            ant.threadSleep(4000);
            ant.getFood(5);
        }

        /**
         * If soldier ant is sent to outside to deal with an invasion then he/she calls this area.
         * The soldier ant will wait for other ants, this is realized with CyclicBarrier, then fights the invasion for 20 seconds.
         * @param ant that deals with the invasion
         */
        public void dealWithThreat(Ant ant) throws InterruptedException {
            logger.info(ant + " has arrived");
            try {
                cyclicBarrier.await();
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
            logger.info(ant + " is fighting");
            ant.threadSleep(20000);
            logger.info(ant + " fight ended");
            colony.setColonyIsUnderAttackFalse();
        }

        /**
         * This method is for generating a threat to the colony by setting colonyIsUnderAttack = true,
         * initing the CyclicBarrier and interrupting the Soldier Ants inside the colony, so they will deal with the threat instantly.
         */
        public void generateThreat() {
            logger.info("THREAT");
            colony.setColonyIsUnderAttackTrue();
            List<Ant> antList =  colony.getSoldierAnts();
            this.nrOfSoldierAntsNeeded = antList.size();
            cyclicBarrier = new CyclicBarrier(nrOfSoldierAntsNeeded);
            for (int i = 0; i < this.nrOfSoldierAntsNeeded; i++) {
                antList.get(i).interrupt();
            }
        }

        public ArrayList<Ant> getAnts() {
            return ants;
        }

        public ArrayList<SoldierAnt> getSoldierAnts() {
            return soldierAnts;
        }

        public List<Ant> getWorkerAnts() {
            return ants
                    .stream()
                    .filter(a -> a.getClass() == WorkerAnt.class)
                    .collect(Collectors.toList());
        }
    }
