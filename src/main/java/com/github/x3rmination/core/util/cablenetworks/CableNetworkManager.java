package com.github.x3rmination.core.util.cablenetworks;

import java.util.List;

public class CableNetworkManager {

    List<CableNetworkD> gameNetworks;

    List<CableNodeD> terminalNodes;

    void addCable(CableNodeD cable){
        //this will figure out the connections to this cable and attach to a network
        crawlForNode(cable);
    }

    List<CableNodeD> getAllTerminalNodes(){
        //get nodes on the end that getEnergy
        computeTerminalNodes();
        return null;
    }

    CableNetworkD getNetworkFor(CableNodeD node){
        //iterate trough all networks and finds if any already contain this node
        return null;
    }

    private void crawlForNode(CableNodeD cable) {
        //go S/W/E/N/U/D and find all the neighbour nodes
        //check if any of neighbour nodes is part of an existent network getNetworkFor
    }

    private void computeTerminalNodes(){
        //will look in all networks and figure out what nodes are terminal (energized)
        //terminalNodes.add(node that was found as terminal)
    }
}
