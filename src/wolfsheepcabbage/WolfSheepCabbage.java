/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wolfsheepcabbage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author DSTIGANT
 */
public class WolfSheepCabbage {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        run();
//        test();
//        solve();
        solutionComparison();
    }

    public static void solutionComparison() {
        WSCState start = new WSCState();
        WSCSolver solver = new WSCSolver(start);

        long noBackTime, backTime, time = System.nanoTime();
        System.out.println("Steps = " + solver.computeSolution().size());
        System.out.println("Time Taken for solution without backing: \n\t"
                + (noBackTime = System.nanoTime() - time) + "ns");

        time = System.nanoTime();
        System.out.println("Steps = " + solver.computeSolutionWithBacking().size());
        System.out.println("Time Taken for solution with backing: \n\t"
                + (backTime = System.nanoTime() - time) + "ns");

        System.out.println("The solution that lacks backing takes\n\t"
                + ((double)(noBackTime)/backTime) + "x as much time.");
    }

    public static void solve(){
        WSCState start = new WSCState();
        WSCSolver solver = new WSCSolver(start);
        final int[] counter = {0};
        solver.computeSolution().forEach(s -> System.out.println(++counter[0] + ": " + s));
    }

    public static void test() {
        Scanner keyboard = new Scanner(System.in);
        String execute = keyboard.nextLine();
        System.out.println("Execute:" + execute);
        String[] split = execute.split(" ");
        for(int i = 0; i < split.length; ++i)
            System.out.println(i + ": " + split[i]);
    }
    
    public static void run() {
        Scanner keyboard = new Scanner( System.in );
        WSCState curState = new WSCState();
        while ( !curState.isLoss() && !curState.isWin() )
        {
            System.out.println( curState );
            String cmd = keyboard.nextLine();
            List<String> validCommands = curState.getValidMoves();
            if ( validCommands.contains(cmd) )
            {
                curState = curState.execute( cmd );
            }
            else
            {
                System.out.println("Invalid move" );
            }
        }
        System.out.println( curState );
        if ( curState.isLoss() )
        {
            System.out.println("You lose!");
        }
        else
        {
            System.out.println("You win!");
        }
    }
    
}
