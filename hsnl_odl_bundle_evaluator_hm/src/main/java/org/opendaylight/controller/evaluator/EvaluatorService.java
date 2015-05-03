package org.opendaylight.controller.evaluator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.opendaylight.controller.monitor.SwitchPortStatTuple;

public class EvaluatorService extends Thread {

    public List<SwitchPortStatTuple> switchPortStat = new ArrayList<>();

    public void run() {

        // Connect to monitor service for statistics
        Thread evalServiceThread = new Thread(new Runnable() {

            public void run() {

                do {

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    System.out
                            .println("INFO [org.opendaylight.controller.evaluator.EvaluatorService]\n"
                                    + "    >> Connect to MonitorService.");

                    // Connect to MonitorService: Receive monitor statistics
                    InetAddress hostMonitor = null;
                    Socket linkMonitor = null;

                    try {
                        hostMonitor = InetAddress.getByName("localhost");
                        linkMonitor = new Socket(hostMonitor, 9001);

                        ObjectInputStream inFromServer = new ObjectInputStream(
                                linkMonitor.getInputStream());

                        try {
                            switchPortStat = (List<SwitchPortStatTuple>) inFromServer
                                    .readObject();
                            linkMonitor.close();

                            // Evaluation Process
                            EvaluatorCalc evaluatorCalc = new EvaluatorCalc();
                            evaluatorCalc.setEventFileInput();
                            evaluatorCalc.setEventFileProcess(switchPortStat);

                            // Connect to HandlerService: Send evaluation result
                            InetAddress hostHandler = null;
                            Socket linkHandler = null;
                            
                            hostHandler = InetAddress.getByName("localhost");
                            linkHandler = new Socket(hostHandler, 9004);
                            ObjectOutputStream outToClient = new ObjectOutputStream(
                                    linkHandler.getOutputStream());
                            outToClient.writeObject(evaluatorCalc
                                    .getEventData());
                            outToClient.flush();
                            linkHandler.close();

                        } catch (ClassNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } while (true);

            }
        });
        evalServiceThread.start();

    }

}
