package org.opendaylight.controller.flowclassification;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import net.floodlightcontroller.flowclassification.FlowRuleTuple;

public class FlowClassificationServiceLocalRequest {

    public FlowClassificationServiceLocalRequest(Socket client,
            List<FlowRuleTuple> flowRule, HashMap <String, String> appPercent) {

        try {

            ObjectOutputStream outToClient = new ObjectOutputStream(
                    client.getOutputStream());

            outToClient.writeObject(flowRule);
            outToClient.flush();
            
            outToClient.writeObject(appPercent);
            outToClient.flush();
            
            client.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}