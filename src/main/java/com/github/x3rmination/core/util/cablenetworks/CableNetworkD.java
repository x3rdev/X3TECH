package com.github.x3rmination.core.util.cablenetworks;

import java.util.Set;

public class CableNetworkD {
    Set<CableNodeD> networkNodes;

    boolean isNodeInNetwork(CableNodeD node){
        return networkNodes.contains(node);
    }
}
