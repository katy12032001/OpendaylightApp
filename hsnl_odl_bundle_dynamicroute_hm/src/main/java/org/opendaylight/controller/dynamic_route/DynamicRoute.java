package org.opendaylight.controller.dynamic_route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.opendaylight.controller.dynamic_route.internal.ForwardingPkt;
import org.opendaylight.controller.monitor.SwitchPortStatTuple;
import org.opendaylight.controller.sal.core.NodeConnector;

public class DynamicRoute {
    private HashMap<NodeConnector, List<NodeConnector>> allLinkPairs;
    private List<NodeConnector> Path;
    List<List<NodeConnector>> routes = null;
    protected List<NodeConnector> srcSwPortList;

    public List<List<NodeConnector>> getAllRoutes(NodeConnector start,
            NodeConnector end) {

        List<List<NodeConnector>> allRoutes = new ArrayList<>();
        int srcSwPortMapCount = 0;

        // As long as the "src-switch" of the points in the srcSwPortList
        // matches that of the given starting point, the switch of the point can
        // act as the first hop switch
        System.out.println(srcSwPortList.size());
        System.out.println(start.getNode());
        System.out.println(srcSwPortList.get(srcSwPortMapCount).getNode());
        for (srcSwPortMapCount = 0; srcSwPortMapCount < srcSwPortList.size(); srcSwPortMapCount++) {
            System.out.println("count" + srcSwPortMapCount);
            System.out.println(srcSwPortList.get(srcSwPortMapCount).getNode()
                    .toString());
            System.out.println(start.getNode().toString());
            if (srcSwPortList.get(srcSwPortMapCount).getNode().toString()
                    .compareTo(start.getNode().toString()) == 0) {
                System.out.println("get it");
                System.out.println(srcSwPortList.get(srcSwPortMapCount)
                        .getNode());
                System.out.println(start.getNode());
                // Get all destinations based on the first switch
                List<NodeConnector> allDests = allLinkPairs.get(srcSwPortList
                        .get(srcSwPortMapCount));
                if (allDests != null) {

                    for (NodeConnector firstHopDest : allDests) {

                        // Generate first hop route
                        List<NodeConnector> firstHopRoute = new ArrayList<>();
                        firstHopRoute.add(srcSwPortList.get(srcSwPortMapCount));
                        System.out.println("1 "
                                + srcSwPortList.get(srcSwPortMapCount));
                        firstHopRoute.add(firstHopDest);
                        System.out.println("2 " + firstHopDest);
                        System.out.println("get it 2");

                        // Recursive to find the next hop
                        Chain(allRoutes, firstHopRoute, firstHopDest, end);
                    }
                }
            }
        }
        return allRoutes;
    }

    public void Chain(List<List<NodeConnector>> allRoutes,
            List<NodeConnector> currRoute, NodeConnector currRightMost,
            NodeConnector end) {
        System.out.println("chain");
        // As long as the "dst-switch" of the current point matches that of the
        // end point, the switch can act as the end switch
        if (currRightMost.getNode().toString()
                .compareTo(end.getNode().toString()) == 0) {
            System.out.println("get end");
            allRoutes.add(currRoute);
            return;
        }

        int srcSwPortMapCount = 0;
        int i = 0;

        for (srcSwPortMapCount = 0; srcSwPortMapCount < srcSwPortList.size(); srcSwPortMapCount++) {

            // Check whether the route traverses the same switch previously
            for (i = 0; i < currRoute.size(); i++) {
                if (currRoute
                        .get(i)
                        .getNode()
                        .toString()
                        .compareTo(
                                srcSwPortList.get(srcSwPortMapCount).getNode()
                                        .toString()) == 0) {
                    break;
                }
            }

            // Avoid loop
            if (srcSwPortList.get(srcSwPortMapCount).getNode().toString()
                    .compareTo(currRightMost.getNode().toString()) == 0
                    && i == currRoute.size() - 1) {

                // If currRightMost point still has other destinations
                List<NodeConnector> currRightMostAllDests = allLinkPairs
                        .get(srcSwPortList.get(srcSwPortMapCount));

                if (currRightMostAllDests != null) {

                    for (NodeConnector currRightMostSingleDest : currRightMostAllDests) {

                        // Append the current rightmost point to the current
                        // route
                        List<NodeConnector> extendRoute = new ArrayList<NodeConnector>(
                                currRoute);
                        extendRoute.add(srcSwPortList.get(srcSwPortMapCount));
                        extendRoute.add(currRightMostSingleDest);

                        // Recursive to check until the currRightMost point has
                        // no other destinations or it matches the above
                        // condition
                        Chain(allRoutes, extendRoute, currRightMostSingleDest,
                                end);

                    }
                }
            }
        }
    }

    public void generatePath(NodeConnector src_nc, NodeConnector dst_nc,
            List<SwitchPortStatTuple> switchPortStat) {
        // getTopo();
        System.out.println("generatepath");
        getTopo();
        routes = getAllRoutes(src_nc, dst_nc);
        for (int i = 0; i < routes.size(); i++) {
            for (int j = 0; j < routes.get(i).size(); j++) {
                System.out.print(" Switch " + routes.get(i).get(j).getNode()
                        + "Port"
                        + routes.get(i).get(j).getNodeConnectorIDString());
            }
            System.out.println("\n Index" + i);
        }

        int minIndex = 0;

        // Create a buffer storing costs for every route
        long loadingStat[] = new long[routes.size()];
        long hopNumber[] = new long[routes.size()];

        // Evaluate the cost along the path
        int count = 0;

        System.out
                .println("INFO [net.floodlightcontroller.forwarding.Forwarding.buildDynamicRoute]\n"
                        + "    >> Cost evaluation");

        for (List<NodeConnector> route : routes) {
        	System.out
            	.println("INFO [net.floodlightcontroller.forwarding.Forwarding.buildDynamicRoute]\n"
                    + "    >> "+route.size()+ "    >> "+switchPortStat.size());
            loadingStat[count] = 0;
            hopNumber[count] = route.size();

            for (NodeConnector nodePortTuple : route) {

                for (int i = 0; i < switchPortStat.size(); i++) {
                    // Match SwDPID
                    if (switchPortStat
                            .get(i)
                            .getSwitchDPID()
                            .equalsIgnoreCase(
                                    nodePortTuple.getNode().toString())
                            && switchPortStat.get(i).getPortNumber()
                                    .equalsIgnoreCase(nodePortTuple.toString())) {

                        System.out.println("Match");

                        // Indicate which link is now in the congestion
                        // condition
                        if (String.valueOf(nodePortTuple.toString())
                                .equalsIgnoreCase("10")) {
                            // Accumulate the statistics
                            loadingStat[count] = loadingStat[count]
                                    + 10000
                                    * Long.parseLong(switchPortStat.get(i)
                                            .getReceiveBytes());
                            loadingStat[count] = loadingStat[count]
                                    + 10000
                                    * Long.parseLong(switchPortStat.get(i)
                                            .getTransmitBytes());
                        } else {
                            // Accumulate the statistics
                            loadingStat[count] = loadingStat[count]
                                    + Long.parseLong(switchPortStat.get(i)
                                            .getReceiveBytes());
                            loadingStat[count] = loadingStat[count]
                                    + Long.parseLong(switchPortStat.get(i)
                                            .getTransmitBytes());
                            System.out.println("Loading: "
                                    + nodePortTuple.toString() + ":"
                                    + switchPortStat.get(i).getReceiveBytes());
                            System.out.println("Loading: "
                                    + nodePortTuple.toString() + ":"
                                    + switchPortStat.get(i).getTransmitBytes());
                        }
                    }

                }
            }
            count++;
        }

        // Select route with minimum cost and minimum hop number
        for (count = 0; count < loadingStat.length; count++) {
            if (loadingStat[count] < loadingStat[minIndex]) {
                minIndex = count;
            } else if (loadingStat[count] == loadingStat[minIndex]) {
                if (hopNumber[count] < hopNumber[minIndex]) {
                    minIndex = count;
                }
            }
        }
        System.out.println(routes.get(minIndex));
         Path = routes.get(minIndex);

         //Path = routes.get(0);

    }

    public List<NodeConnector> getPath() {
        return Path;
    }

    public void getTopo() {
        System.out.println("gettopo");
        allLinkPairs = ForwardingPkt.allLinkPairs;
        System.out.println(allLinkPairs);
        srcSwPortList = ForwardingPkt.srcSwPortList;
        System.out.println(srcSwPortList);
    }
}
