package diver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import graph.FindState;
import graph.FleeState;
import graph.Node;
import graph.NodeStatus;
import graph.SewerDiver;
import graph.Sewers;

public class McDiver extends SewerDiver {

    /** A set containing all id's of squares that McDiver has visited. **/
    public HashSet<Long> visited;

    /** Contains the id of the starting square McDiver is standing on. **/
    public long startingPosition;

    /** Contains all nodes that contain tiles with coins sorted based on the value of the coin as a
     * Collection. **/
    public ArrayList<Node> orderedNodes;

    /** Represents the size of the arraylist containing nodes with tiles with coins (not its
     * capacity). **/
    public int sizeOfArrayList;

    /** Contains all nodes that contain tiles with coins sorted based on the value of the coin as an
     * array. **/
    public Node[] arrayOfCoins;

    /** Cotains all nodes that contain tiles with coins that have been picked up. **/
    public Collection<Node> coinsPickedUp;

    /** The original number of total steps that McDiver had when fleeing. **/
    public int totalSteps;

    /** Find the ring in as few steps as possible. Once you get there, <br>
     * you must return from this function in order to pick<br>
     * it up. If you continue to move after finding the ring rather <br>
     * than returning, it will not count.<br>
     * If you return from this function while not standing on top of the ring, <br>
     * it will count as a failure.
     *
     * There is no limit to how many steps you can take, but you will receive<br>
     * a score bonus multiplier for finding the ring in fewer steps.
     *
     * At every step, you know only your current tile's ID and the ID of all<br>
     * open neighbor tiles, as well as the distance to the ring at each of <br>
     * these tiles (ignoring walls and obstacles).
     *
     * In order to get information about the current state, use functions<br>
     * currentLocation(), neighbors(), and distanceToRing() in state.<br>
     * You know you are standing on the ring when distanceToRing() is 0.
     *
     * Use function moveTo(long id) in state to move to a neighboring<br>
     * tile by its ID. Doing this will change state to reflect your new position.
     *
     * A suggested first implementation that will always find the ring, but <br>
     * likely won't receive a large bonus multiplier, is a depth-first walk. <br>
     * Some modification is necessary to make the search better, in general. */
    @Override
    public void find(FindState state) {
        // TODO : Find the ring and return.
        // DO NOT WRITE ALL THE CODE HERE. DO NOT MAKE THIS METHOD RECURSIVE.
        // Instead, write your method (it may be recursive) elsewhere, with a
        // good specification, and call it from this one.
        //
        // Working this way provides you with flexibility. For example, write
        // one basic method, which always works. Then, make a method that is a
        // copy of the first one and try to optimize in that second one.
        // If you don't succeed, you can always use the first one.
        //
        // Use this same process on the second method, flee.

        visited= new HashSet<>();
        startingPosition= state.currentLocation();
        findGreedy(state);
    }

    /** Implements a simple greedy-search algorithim that heads towards the neighboring square that
     * is smallest in distance to the ring from where McDiver is currently standing. This does not
     * account for obstacles like walls. Implemented Recursively. */
    public void findGreedy(FindState state) {
        if (state.distanceToRing() == 0) { return; }

        long u= state.currentLocation();
        visited.add(u);

        ArrayList<NodeStatus> neighbors= (ArrayList<NodeStatus>) state.neighbors();
        Collections.sort(neighbors);

        for (NodeStatus w : neighbors) { // for each neighbor w of u

            long wID= w.getId();

            if (!visited.contains(wID)) {// if w is unvisited
                state.moveTo(wID); // s.moveTo(w);
                findGreedy(state);
                if (state.distanceToRing() == 0) { return; }
                state.moveTo(u);
            }
        }

    }

    /** Flee --get out of the sewer system before the steps are all used, trying to <br>
     * collect as many coins as possible along the way. McDiver must ALWAYS <br>
     * get out before the steps are all used, and this should be prioritized above<br>
     * collecting coins.
     *
     * You now have access to the entire underlying graph, which can be accessed<br>
     * through FleeState. currentNode() and exit() will return Node objects<br>
     * of interest, and getNodes() will return a collection of all nodes on the graph.
     *
     * You have to get out of the sewer system in the number of steps given by<br>
     * stepToGo(); for each move along an edge, this number is <br>
     * decremented by the weight of the edge taken.
     *
     * Use moveTo(n) to move to a node n that is adjacent to the current node.<br>
     * When n is moved-to, coins on node n are automatically picked up.
     *
     * You must return from this function while standing at the exit. Failing <br>
     * to do so before steps run out or returning from the wrong node will be<br>
     * considered a failed run.
     *
     * Initially, there are enough steps to get from the starting point to the<br>
     * exit using the shortest path, although this will not collect many coins.<br>
     * For this reason, a good starting solution is to use the shortest path to<br>
     * the exit. */
    @Override
    public void flee(FleeState state) {
        // TODO: Get out of the sewer system before the steps are used up.
        // DO NOT WRITE ALL THE CODE HERE. Instead, write your method elsewhere,
        // with a good specification, and call it from this one.
        // exitPathImproved1(state);
        orderedNodes= (ArrayList<Node>) filterAndSortNodesWithCoins(
            new ArrayList<>(state.allNodes()));
        coinsPickedUp= new ArrayList<>();
        arrayOfCoins= new Node[orderedNodes.size()];
        sizeOfArrayList= orderedNodes.size();
        totalSteps= state.stepsToGo();

        exitPathImproved5(state);
    }

    /** Implements an Algorithim where McDiver heads towards the coin with the highest
     * value/distance ratio. Continuosuly does this until McDiver assumes the worst case scenario
     * for the number of steps left to the exit. At that point, he heads towards the exit instead.
     *
     * Implemented Recursively. */
    public void exitPathImproved5(FleeState state) {
        if (state.currentNode().equals(state.exit())) {}
        ArrayList<LinkedList<Node>> arrayOfPaths= new ArrayList<>();
        for (Node coinNode : orderedNodes) {
            LinkedList<Node> shortestPath= (LinkedList<Node>) A6.shortest(state.currentNode(),
                coinNode);
            arrayOfPaths.add(shortestPath);
        }
        ArrayList<Integer> weights= new ArrayList<>();
        for (LinkedList path : arrayOfPaths) {
            int weightOfPath= A6.pathSum(path);
            weights.add(weightOfPath);
        }
        ArrayList<Double> ratio= new ArrayList<>();
        for (int i= 0; i < arrayOfPaths.size(); i++ ) {
            double ratioVal= Math.pow(orderedNodes.get(i).getTile().coins(), 2) /
                Math.pow(weights.get(i), 2);
            ratio.add(ratioVal);
        }

        LinkedList<Node> exitPath= (LinkedList<Node>) A6.shortest(state.currentNode(),
            state.exit());
        LinkedList<Node> pathOfInterest;
        if (!ratio.isEmpty()) {
            if (totalSteps <= state.stepsToGo()) {
                pathOfInterest= arrayOfPaths.get(1);
            } else {
                int maxRatioIndex= ratio.indexOf(Collections.max(ratio));
                pathOfInterest= arrayOfPaths.get(maxRatioIndex);
            }
        } else {
            pathOfInterest= exitPath;
        }
        if (A6.pathSum(exitPath) + Sewers.MAX_EDGE_WEIGHT >= state.stepsToGo() -
            Sewers.MAX_EDGE_WEIGHT ||
            sizeOfArrayList <= 0) {
            for (Node node : exitPath) {
                if (!state.currentNode().equals(node)) {
                    state.moveTo(node);
                }
            }
        } else {
            state.moveTo(pathOfInterest.get(1));
            Node currentPosition= state.currentNode();
            if (currentPosition.getTile().coins() != currentPosition.getTile()
                .originalCoinValue()) {
                orderedNodes.remove(currentPosition);
                sizeOfArrayList-- ;
            }

            exitPathImproved5(state);
        }
    }

    /** Removes every node that doesn't contain a coin of the list that contains all nodes of the
     * graph. */
    private Collection<Node> filterAndSortNodesWithCoins(Collection<Node> allNodes) {
        for (Iterator<Node> it= allNodes.iterator(); it.hasNext();) {
            Node elem= it.next();
            if (elem.getTile().coins() <= 0) {
                it.remove();
            }
        }
        return allNodes;
    }

}
