/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Homework.rmi;

import Homework.run.Runner;
import Homework.utilities.MyLogger;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This is the server class for the RMI method.
 * @author peete
 */
public class ServerClass extends UnicastRemoteObject implements RemoteInterface {

    private Runner runner;

    protected ServerClass() throws RemoteException {
        MyLogger.init();
        runner = new Runner();
        runner.start();
    }

    @Override
    public Integer getNrOfAntsOutsideColony() {
        return runner.getOutside().getNrOfAnts();
    }
    @Override
    public Integer getNrOfAntsInsideColony() {
        return runner.getColony().getNrOfAntsInsideColony();
    }

    @Override
    public Integer getNrOfSoldierAntsDoingInstruction() {
        return runner.getColony().getInstructionArea().getNrOfAntsAtInstructionArea();
    }
    @Override
    public Integer getNrOfSoldierAntsRepellingInvasion() {
        return runner.getOutside().getNrOfAntsRepellingInvasion();
    }

    @Override
    public Integer getNrOfBabyAntsAtEatingArea() {
        return runner.getColony().getEatingArea().nrOfBabyAntsAtEatingArea();
    }

    @Override
    public Integer getNrOfBabyAntsAtShelter() {
        return runner.getColony().getShelter().nrOfChildAntsAtShelter();
    }

    @Override
    public void generateThreat() throws RemoteException {
        runner.getOutside().generateThreat();
    }

    public static void main(String[] args) {
        try {
            ServerClass server = new ServerClass();
            Naming.rebind("//127.0.0.1/server", server);
            System.out.println("The object MyObject was registered");
        } catch (Exception e) {
            System.out.println("Error: " +
                    e.getMessage()); e.printStackTrace();
        }

    }
}
