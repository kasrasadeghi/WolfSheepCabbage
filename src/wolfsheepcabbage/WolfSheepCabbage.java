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
        solve();
    }
    public static void solve(){
        WSCState start = new WSCState();
        WSCSolver solver = new WSCSolver(start);
        System.out.println("before");
        solver.computeSolution().forEach(System.out::println);
        System.out.println("after");
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
