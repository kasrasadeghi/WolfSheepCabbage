/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wolfsheepcabbage;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import wolfsheepcabbage.WSCState.ACTOR;
import static wolfsheepcabbage.WSCState.ACTOR.CABBAGE;
import static wolfsheepcabbage.WSCState.ACTOR.SHEEP;
import static wolfsheepcabbage.WSCState.ACTOR.WOLF;
import static wolfsheepcabbage.WSCState.ACTOR.NULL;

/**
 *
 * @author DSTIGANT
 */
public class WSCState
{

    static enum ACTOR {
        WOLF("W"), SHEEP("S"), CABBAGE("C"), NULL(",");
        
        private String symbol;
        ACTOR(String symbol) {
            this.symbol = symbol;
        }
        
        @Override
        public String toString() {
            return symbol;
        }

        static public List<ACTOR> getWSC() {
            return Arrays.asList(values()).subList(0,3);
        }
    }
    
    class Boat {
        private boolean isLeft;
        private ACTOR loaded;
        Boat() {
            isLeft = true;
            loaded = NULL;
        }
        Boat(boolean left, ACTOR a) {
            isLeft = left; 
            loaded = a;
        }
        
        void load(ACTOR a) {
            loaded = a;
        }
        ACTOR unload() {
            ACTOR output = loaded;
            loaded = NULL;
            return output;
        }
        void move() {
            isLeft = !isLeft;
        }
        Boat copy() {
            return new Boat(isLeft, loaded);
        }
        
        public String toString() {
            return (isLeft?"B" + loaded + "  ":"  B" + loaded);
        }
    }
    
    // add member variables here to keep track of state
    private List<String> log = new ArrayList<>();
    private List<ACTOR> left = new ArrayList<>();
    private List<ACTOR> right = new ArrayList<>();
    private Boat boat;
    
    public WSCState(List<ACTOR> l, List<ACTOR> r, Boat b) {
        this.left = l;
        this.right = r;
        this.boat = b;
    }
    
    // constructor - initialize state so that the wolf, the sheep, the cabbage, and the boat are
    // on the initial side of the river
    public WSCState()
    {
        boat = new Boat();
        left.addAll( Arrays.asList(ACTOR.values()).subList(0, 3));
        for (int i = 0; i < 3; i++) 
            right.add(NULL);
    }

    public List<String> getLog() {
        return log;
    }

    private void load(String actorName) {
        switch(actorName) {
            case "wolf":
                boat.load((boat.isLeft) ?
                        left.set(left.indexOf(WOLF), NULL) :
                        right.set(right.indexOf(WOLF), NULL) );
                break;
            case "sheep":
                boat.load((boat.isLeft) ?
                        left.set(left.indexOf(SHEEP), NULL) :
                        right.set(right.indexOf(SHEEP), NULL) );
                break;
            case "cabbage":
                boat.load((boat.isLeft) ?
                        left.set(left.indexOf(CABBAGE), NULL) :
                        right.set(right.indexOf(CABBAGE), NULL) );
                break;
        }
    }

    private void unload() {
        ACTOR a = boat.unload();
        switch(a) {
            case WOLF:
                a = (boat.isLeft) ? left.set(0, a) : right.set(0, a);
            case SHEEP:
                a = (boat.isLeft) ? left.set(0, a) : right.set(0, a);
                break;
            case CABBAGE:
                a = (boat.isLeft) ? left.set(0, a) : right.set(0, a);
                break;
        }
    }

    // copy - returns a new WSCState identical to this one
    public WSCState copy()
    {
        List<ACTOR> l = new ArrayList<>(this.left);
        List<ACTOR> r = new ArrayList<>(this.right);
        Boat b = this.boat.copy();
        return new WSCState(l, r, b);
    }
    
    // toString - returns a String representation of this state.
    // For example, if this state has the W and S on the first side with the boat and the C in the boat
    // toString() should return "WS |BC  |   "
    //
    // If this state has the W and C on one side with the empty boat and the S on the other side
    // toString() should return "W C|B   | S "
    //
    // If this state has the W on one side, the Boat with the sheep on the other side and the Cabbage on the far side:
    // toString() should return "W  |  BS|  C"
    @Override
    public String toString()
    {
        String output = "";
        for (ACTOR a : left)          
            output += a.toString();
        output += "|";
        output += boat;
        output += "|";
        for (ACTOR a : right)
            output += a.toString();
        return output;
    }

    // isLoss - returns true if the state is a loss - that is the W and S are on one side and the boat is on the other
    // or the S and the C are one side with the boat on the other
    public boolean isLoss()
    {
        List<ACTOR> otherSide = (boat.isLeft)? right : left;
        return otherSide.contains(WOLF) && otherSide.contains(SHEEP)
                || otherSide.contains(SHEEP) && otherSide.contains(CABBAGE);
    }
    
    // isWin - returns true if the state is a win - that is all three items are on the far side of the river
    public boolean isWin()
    {
        return right.containsAll(Arrays.asList(ACTOR.values()).subList(0, 3));
    }
    
    // execute - consumes a String like "load sheep" or "unload" or "cross" and returns a NEW
    // WSCState which is the same as this state but with the command executed
    // If the move is invalid (see below), return this state
    //
    // For example, if this state has the W and S on the first side with the boat and the C in the boat
    // execute("cross") will return a new state with the W and S on the first side and the C in the boat on the second side
    // execute("unload") will return a new state with the W, S, and C on the first side and the (empty) boat on the first side
    // execute("load wolf") will return this state since the boat already has an occupant
    //
    // If this state has the W and C on one side with the empty boat and the S on the other side
    // execute("load wolf") will return a new state with the W in the boat
    // execute("load cabbage") will return a new state with the C in the boat
    // execute("cross") will return a new state with the empty boat on the other side
    public WSCState execute( String str )
    {
        WSCState next = this.copy();
        String[] split = str.split(" ");

        next.log.add(str);
        switch(split[0]) {
            case "load": 
                next.load(split[1]);
                break;
            case "unload":
                next.unload();
                break;
            case "cross":
                next.boat.move();
                break;
        }
        return next;
    }
    
    // getValidMoves - returns a list of the valid commands
    //
    // WS |BC  |
    // For example, if this state has the W and S on the first side with the boat and the C in the boat
    // then the valid moves are:
    // "cross", "unload"
    //
    // W C|B   | S
    // If this state has the W and C on one side with the empty boat and the S on the other side
    // then the valid moves are
    // "load wolf", "load cabbage", "cross"
    public List<String> getValidMoves()
    {
        List<String> output = new ArrayList<>();
        if (isLoss() || isWin()) return output;
        output.add("cross");
        if (boat.loaded == NULL) {
            List<ACTOR> loadingSide = boat.isLeft? left: right;
            loadingSide.forEach(a -> {
                switch (a) {
                    case NULL:
                        break;
                    case WOLF:
                        output.add("load wolf");
                        break;
                    case SHEEP:
                        output.add("load sheep");
                        break;
                    case CABBAGE:
                        output.add("load cabbage");
                        break;
                }
            });
        } else output.add("unload");
        return output;
    }
}
