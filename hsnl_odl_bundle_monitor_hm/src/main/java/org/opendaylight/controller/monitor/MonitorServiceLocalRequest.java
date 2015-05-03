package org.opendaylight.controller.monitor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class MonitorServiceLocalRequest {

    public MonitorServiceLocalRequest(Socket client,
            List<SwitchPortStatTuple> switchPortStat) {

        try {

            ObjectOutputStream outToClient = new ObjectOutputStream(
                    client.getOutputStream());
            outToClient.writeObject(switchPortStat);
            outToClient.flush();
            client.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
