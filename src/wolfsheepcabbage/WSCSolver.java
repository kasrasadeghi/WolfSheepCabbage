/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wolfsheepcabbage;

import java.util.*;

/**
 *
 * @author DSTIGANT
 */
public class WSCSolver
{
    WSCState state;
    public WSCSolver(WSCState state) {
        this.state = state;
    }

    // returns a list of commands to solve the game from the given state
    public List<String> computeSolution()
    {
        List<String> visited = new ArrayList<>();
        Queue<WSCState> queue = new LinkedList<>();
        queue.offer(state);
        while(!queue.peek().isWin()) {
            //find nonlosses and extend them
            WSCState previous = queue.poll();
            List<String> moves = previous.getValidMoves();
            if (!visited.contains(previous.toString())){
                visited.add(previous.toString());
                for (String cmd : moves)
                    if (!previous.execute(cmd).isLoss())
                            queue.offer(previous.execute(cmd));
            }
        }
        return queue.peek().getLog();
    }
}
