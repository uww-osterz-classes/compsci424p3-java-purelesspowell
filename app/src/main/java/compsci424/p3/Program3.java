/* COMPSCI 424 Program 3
 * Name:
 * 
 * This is a template. Program3.java *must* contain the main class
 * for this program. 
 * 
 * You will need to add other classes to complete the program, but
 * there's more than one way to do this. Create a class structure
 * that works for you. Add any classes, methods, and data structures
 * that you need to solve the problem and display your solution in the
 * correct format.
 */

package compsci424.p3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Semaphore;


/**
 * Main class for this program. To help you get started, the major
 * steps for the main program are shown as comments in the main
 * method. Feel free to add more comments to help you understand
 * your code, or for any reason. Also feel free to edit this
 * comment to be more helpful.
 */
public class Program3 {
    // Declare any class/instance variables that you need here.


    /**
     * @param args Command-line arguments. 
     * 
     * args[0] should be a string, either "manual" or "auto". 
     * 
     * args[1] should be another string: the path to the setup file
     * that will be used to initialize your program's data structures. 
     * To avoid having to use full paths, put your setup files in the
     * top-level directory of this repository.
     * - For Test Case 1, use "424-p3-test1.txt".
     * - For Test Case 2, use "424-p3-test2.txt".
     */
    public static void main(String[] args) throws FileNotFoundException {
       // Semaphore semaphore = new Semaphore();
        // Code to test command-line argument processing.
        // You can keep, modify, or remove this. It's not required.
        if (args.length < 2) {
            System.err.println("Not enough command-line arguments provided, exiting.");
            return;
        }
        System.out.println("Selected mode: " + args[0]);
        System.out.println("Setup file location: " + args[1]);

        // 1. Open the setup file using the path in args[1]
        String currentLine;
        BufferedReader setupFileReader;
        try {
            setupFileReader = new BufferedReader(new FileReader(args[1]));
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find setup file at " + args[1] + ", exiting.");
            return;
       }

        // 2. Get the number of resources and processes from the setup
        // file, and use this info to create the Banker's Algorithm
        // data structures
        int numResources;
        int numProcesses;
        try (BufferedReader br = new BufferedReader(new FileReader(args[1]))){
numResources = Integer.parseInt((br.readLine().split(" ")[0]));
numProcesses = Integer.parseInt((br.readLine().split(" ")[0]));

            int allocation[][] = new int[numProcesses][numResources];
            int max[][] = new int[numProcesses][numResources];
            int available[] = new int[numResources];

            String[] str1 = br.readLine().split(" ");
            for(int i = 0; i < numResources; i++){
                available[i] = Integer.parseInt(str1[i]);
            }

            for(int i =0; i < numProcesses; i++){
                String[] str2 = br.readLine().split(" ");
                for(int k = 0; k < numResources; k++){
                    max[i][k] = Integer.parseInt(str2[k]);
                }
            }

            for(int i =0; i < numProcesses; i++){
                String[] str3 = br.readLine().split(" ");
                for(int k = 0; k < numResources; k++){
                    allocation[i][k] = Integer.parseInt(str3[k]);
                }
            }


            System.out.println("Available " + Arrays.toString(available));
            System.out.println("Max " + Arrays.deepToString(max));
            System.out.println("Allocation " + Arrays.deepToString(max));
            BankersAlgorithm banker = new BankersAlgorithm(allocation,max,available);
//Perform allocation -> max check
            boolean allocationChecker = banker.checkAllocation(allocation,max);
boolean safeSystem = banker.isSafe();
if(allocationChecker && safeSystem){
    if(args[0] == "manual"){

    }
    if(Objects.equals(args[0], "auto")) {
        Random random = new Random();
Thread[] threads = new Thread[numProcesses];
for(int i = 0; i < numProcesses;i++){
    threads[i] = new Thread(() ->{
        for (int j = 0; j < 3; j++){
            int processId = random.nextInt(numProcesses);
            int req[] = generateRandomRequest(banker,processId);
            int rel[] = generateRandomRelease(banker,processId);

            try {
                banker.request(processId, req);
                banker.release(processId,rel);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    });
    threads[i].start();
}

for(Thread thread : threads){
    try{
        thread.join();
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
}
    }
}

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // For simplicity's sake, we'll use one try block to handle
        // possible exceptions for all code that reads the setup file.
       /* try {
            // Get number of resources
            currentLine = setupFileReader.readLine();
            if (currentLine == null) {
                System.err.println("Cannot find number of resources, exiting.");
                setupFileReader.close();
                return;
            }
            else {
                numResources = Integer.parseInt(currentLine.split(" ")[0]);
                System.out.println(numResources + " resources");
            }
 
            // Get number of processes
            currentLine = setupFileReader.readLine();
            if (currentLine == null) {
                System.err.println("Cannot find number of processes, exiting.");
                setupFileReader.close();
                return;
            }
            else {
                numProcesses = Integer.parseInt(currentLine.split(" ")[0]);
                System.out.println(numProcesses + " processes");
            }

            // Create the Banker's Algorithm data structures, in any
            // way you like as long as they have the correct size

//BankersAlgorithm banker = new BankersAlgorithm(allocation, max,available);

            // 3. Use the rest of the setup file to initialize the
            // data structures

            setupFileReader.close(); // done reading the file, so close it
        }
        catch (IOException e) {
            System.err.println("Something went wrong while reading setup file "
            + args[1] + ". Stack trace follows. Exiting.");
            e.printStackTrace(System.err);
            System.err.println("Exiting.");
            return;
        }*/

        // 4. Check initial conditions to ensure that the system is 
        // beginning in a safe state: see "Check initial conditions"
        // in the Program 3 instructions

        // 5. Go into either manual or automatic mode, depending on
        // the value of args[0]; you could implement these two modes
        // as separate methods within this class, as separate classes
        // with their own main methods, or as additional code within
        // this main method.


    }

    public static int[] generateRandomRequest(BankersAlgorithm banker, int processId){
        Random random = new Random();
        int request[] = new int[banker.getAvailable().length];
        for(int i = 0; i < request.length; i++){
            request[i] = random.nextInt((banker.getMax()[processId][i] + 1));
        }
        return request;
    }

    public static int[] generateRandomRelease(BankersAlgorithm banker, int processId){
        Random random = new Random();
        int[] release = new int[banker.getAllocation()[0].length];
        for(int i = 0; i < release.length;i++){
            release[i] = random.nextInt(banker.getAllocation()[processId][i]+1);
        }
        return release;
    }


}

