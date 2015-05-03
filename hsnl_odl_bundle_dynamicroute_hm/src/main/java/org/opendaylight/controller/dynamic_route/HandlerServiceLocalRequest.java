package org.opendaylight.controller.dynamic_route;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.opendaylight.controller.evaluator.EventDataTuple;

public class HandlerServiceLocalRequest {

    public List<EventDataTuple> eventData = new ArrayList<>();

    public HandlerServiceLocalRequest(Socket client) {

        try {

            ObjectInputStream inFromServer = new ObjectInputStream(
                    client.getInputStream());

            try {
                eventData = (List<EventDataTuple>) inFromServer.readObject();
                inFromServer.close();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public List<EventDataTuple> getEventData() {
        return eventData;
    }

}
