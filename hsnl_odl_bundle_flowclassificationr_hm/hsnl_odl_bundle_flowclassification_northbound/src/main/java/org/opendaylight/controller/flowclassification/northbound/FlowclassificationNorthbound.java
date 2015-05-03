package org.opendaylight.controller.flowclassification.northbound;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.opendaylight.controller.transfer_fivetuple.service.api.FlowclassificationServiceAPI;
import org.opendaylight.controller.sal.utils.ServiceHelper;

@Path("/")
public class FlowclassificationNorthbound {
   
    @Path("/transfer_fivetuple/{src_ip},{dst_ip},{src_port},{dst_port},{app},{ipport},{apppercent}")
    @PUT
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Destination reachable"),
        @ResponseCode(code = 503, condition = "Internal error"),
        @ResponseCode(code = 503, condition = "Destination unreachable") })
    public Response transfer_fivetuple(@PathParam(value = "src_ip") String src_ip,@PathParam(value = "dst_ip") String dst_ip,
    		@PathParam(value = "src_port") String src_port ,@PathParam(value = "dst_port") String dst_port
    		,@PathParam(value = "app") String app,@PathParam(value = "ipport") String ipport,@PathParam(value = "apppercent") String apppercent) {
    	FlowclassificationServiceAPI transfer_fivetuple= (FlowclassificationServiceAPI) ServiceHelper.getGlobalInstance(
    			FlowclassificationServiceAPI.class, this);
        if (transfer_fivetuple == null) {
            /* Ping service not found. */
            return Response.ok(new String("No transfer_fivetuple service")).status(500)
                    .build();
        }
        if (transfer_fivetuple.Transfer_fivetuple(src_ip, dst_ip, src_port, dst_port, app, ipport, apppercent))
            return Response.ok(new String("Reachable")).build();

        return Response.ok(new String("Unreachable")).status(503)
                .build();
    }
}
