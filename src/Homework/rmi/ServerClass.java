/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Homework.rmi;

import Homework.run.Runner;
import Homework.utilities.MyLogger;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
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
    public Integer nrOfAntsOutsideColony() {
        return runner.getOutside().getNrOfAnts();
    }

    @Override
    public Integer nrOfAntsInsideColony() {
        return runner.getColony().getNrOfAntsInsideColony();
    }

    @Override
    public Integer nrOfSoldierAntsDoingInstruction() {
        return runner.getColony().getInstructionArea().getNrOfAntsAtInstructionArea();
    }

    @Override
    public Integer nrOfSoldierAntsRepellingInvasion() {
        return runner.getOutside().getNrOfAntsRepellingInvasion();
    }

    @Override
    public Integer nrOfBabyAntsAtEatingArea() {
        return runner.getColony().getEatingArea().nrOfBabyAntsAtEatingArea();
    }

    @Override
    public Integer nrOfBabyAntsAtShelter() {
        return runner.getColony().getShelter().nrOfChildAntsAtShelter();
    }

    @Override
    public void generateThreat() throws RemoteException {
        runner.getOutside().generateThreat();
    }

    public static void main(String[] args) {
        try {
                //Instantiate the object to be registered
            ServerClass obj = new ServerClass();
            Registry registry = LocateRegistry.createRegistry(1099); //Launch rmiregistry in localhot
            Naming.rebind("//127.0.0.1/MyObject", obj); //The object is accessible for clients
            System.out.println("The object MyObject was registered");
        } catch (Exception e) {
            System.out.println("Error: " +
                    e.getMessage()); e.printStackTrace();
        }

    }
}
