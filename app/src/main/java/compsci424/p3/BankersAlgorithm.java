package compsci424.p3;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class BankersAlgorithm extends Thread {
    private int available[];
    private int max[][];
    private int allocation[][];
    private int request[][];
    private int need[][];
    private int work[];
    private boolean finish[];
    private int total[]; //A 1-dimensional array of integers that shows the total number of instances of each resource type in the system.
private Semaphore mutex;
    public BankersAlgorithm(int allocation[][], int max[][], int available[]) {
        this.allocation = allocation;
        this.max = max; // records max num of units of resource j that process i will ever request
        this.available = available; //stores num of unallocated units of each resource
        this.total = new int[available.length]; //same length as available
        this.need = new int[max.length][max[0].length];
        this.work = new int[available.length]; // same as available
        this.finish = new boolean[max.length];
this.mutex = new Semaphore(1);
        //calc total
        for (int i = 0; i < available.length; i++) {
            int totalNum = available[i];
            for (int[] temp : allocation) {
                totalNum += temp[i];

            }
            total[i] = totalNum;
        }

        //calc need
        for (int i = 0; i < allocation.length; i++) {
            for (int j = 0; j < available.length; j++) {
                need[i][j] = max[i][j] - allocation[i][j]; //Need[i][j] = Max[i][j] - Allocation[i][j]
            }
        }

        // calc work Work[j] = Available[j] work is equal to available?
        for (int i = 0; i < available.length; i++) {
            work[i] = available[i];
        }
        //calc finish for now just initialize false
        Arrays.fill(finish, false); //fills array finish with false values


    }



    public synchronized boolean request(int processId, int request[]) throws InterruptedException {
        //if not volid return false
        //if valid return true
        mutex.acquire();
        if (!isValidRequest(processId, request)) {
            return false;
        }
        for (int i = 0; i < request.length; i++) {
            allocation[processId][i] += request[i];
            available[i] -= request[i];
            total[i] -= request[i];
        }
        boolean safe = isSafe(); //AFTER CHANGE CAPTURE IF IT IS SAFE
        if (!safe) {
            for (int i = 0; i < request.length; i++) {
                allocation[processId][i] -= request[i];
                available[i] += request[i];
                total[i] += request[i];
            }
        }
        mutex.release();
        return safe;
        //Temp allocate the resoures and check if it works
        // if they end up not workiing revert changes

        //else return rtrue
    }

    public synchronized void release(int processId, int release[]) throws InterruptedException {
        //check if reelease is ok
        mutex.acquire();
        if (!isValidRelease(processId, release)) {
            System.out.println("INVALID, METHOD RELEASE");
        } else {
            for (int i = 0; i < release.length; i++) {
                allocation[processId][i] -= release[i];
                available[i] += release[i]; //update available and total after release
                total[i] += release[i];
            }
        }
        mutex.release();
        //release resources
    }

    public int[] getAvailable() {
        return available;
    }

    public void setAvailable(int[] available) {
        this.available = available;
    }

    public int[][] getMax() {
        return max;
    }

    public void setMax(int[][] max) {
        this.max = max;
    }

    public int[][] getAllocation() {
        return allocation;
    }

    public void setAllocation(int[][] allocation) {
        this.allocation = allocation;
    }

    public boolean isValidRequest(int processId, int request[]) {
        //is request valid
        for (int i = 0; i < request.length; i++) {
            if (request[i] < 0 || request[i] > max[processId][i] || request[i] > available[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidRelease(int processId, int release[]) {
        //method just checks if release is ok
        for (int i = 0; i < request.length; i++) {
            if (release[i] < 0 || release[i] > allocation[processId][i]) {
                return false;
            }
        }
        return true;
    }

    public boolean isSafe() {
        //check if change is ok
        int counter = 0;
        boolean safe = false;
        while (counter < max.length) {
            boolean here = false;
            for (int i = 0; i < max.length; i++) {
                if (!finish[i]) {
                    boolean give = true;
                    for (int j = 0; j < max.length; j++) {
                        if (need[i][j] > work[j]) {
                            give = false;
                            break;
                        }
                    }
                    if (give) {
                        for (int k = 0; k < work.length; k++) {
                            work[k] += allocation[i][k];
                        }
                        finish[i] = true;

                        counter++;
                        here = true;

                    }
                }
            }
            if (!here) {
                break;
            }
        }
        if (counter == max.length) {
            System.out.println("SAFE STATE");
            safe = true;
        } else {
            System.out.println("NOT SAFE STATE");
        }



        return safe;

}
public boolean checkAllocation(int allocation[][], int max[][]){
    for(int i = 0; i < allocation.length; i++){
        for (int k = 0; k < allocation[i].length; k++){
            if(allocation[i][k] > max[i][k]){
                System.out.println("There is a value in allocation larger than max, exit");

                return false;
            }


        }
    }
    return true;
}



}

