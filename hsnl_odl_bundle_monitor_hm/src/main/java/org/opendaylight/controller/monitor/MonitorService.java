package org.opendaylight.controller.monitor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MonitorService extends Thread {

    private static ServerSocket monitorServiceLoSock;
    private static final int monitorServiceLoPort = 9001;
    private static Socket loClient;
    public List<SwitchPortStatTuple> switchPortStat = new ArrayList<>();

    public static Object messageLock = new Object();

    public void run() {

        // Wait for connection

        Thread loServerThread = new Thread(new Runnable() {

            public void run() {

                try {
                    System.out.println("Initial monitor server socket_1");
                    monitorServiceLoSock = new ServerSocket(monitorServiceLoPort);
                    System.out.println("Initial monitor server socket_2");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                do {
                    try {
                        System.out.println("monitor server socket begin");
                        loClient = monitorServiceLoSock.accept();
                        System.out.println("monitor server socket accept");

                        synchronized (messageLock) {
                            Thread thread = new Thread(new Runnable() {
                                public void run() {
                                    new MonitorServiceLocalRequest(loClient,
                                            switchPortStat);
                                }
                            });
                            thread.start();
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch bloc
                        e.printStackTrace();
                    }
                } while (true);
            }
        });
        loServerThread.start();

        Thread monitorServiceThread = new Thread(new Runnable() {

            @Override
            public void run() {

                int token = 1;

                SwitchPortStatCalc switchPortStatCalc = null;
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                while (true) {
                    StatisticHandling StatHandle = new StatisticHandling();
                    StatHandle.getStatistics();
                    
                    if (token == 1) {
                        switchPortStatCalc = new SwitchPortStatCalc();
                        switchPortStatCalc.setSwitchPortStatBase(StatHandle
                                .getSwitchPortStat());
                        //switchPortStatCalc.printSwitchPortStatBase();
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        token = 0;
                    } else {
                        switchPortStatCalc.setSwitchPortStatUpdate(StatHandle
                                .getSwitchPortStat());
                        //switchPortStatCalc.printSwitchPortStatUpdate();
                        if (switchPortStatCalc.getSwitchPortStatBase().size()
                                == switchPortStatCalc.getSwitchPortStatUpdate().size()) {
                            switchPortStatCalc.setSwitchPortStatProcess();
                            switchPortStatCalc.printSwitchPortStatResult();

                            synchronized (messageLock) {
                                // switchPortStatCalc.printSwitchPortStatResult();
                                switchPortStat = switchPortStatCalc.getSwitchPortStatResult();
                            }
                        }
                        else{
                            System.out.println("null!");
                        }
                        token = 1;
                    }

                }
            }
        });
        monitorServiceThread.start();
    }

}
