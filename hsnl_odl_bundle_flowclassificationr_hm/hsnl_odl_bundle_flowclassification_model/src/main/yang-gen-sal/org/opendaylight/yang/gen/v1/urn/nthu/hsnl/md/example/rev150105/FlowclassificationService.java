package org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105;
import java.util.concurrent.Future;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput;
import org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleInput;
import org.opendaylight.yangtools.yang.binding.RpcService;


/**
**/
public interface FlowclassificationService
    extends
    RpcService
{




    /**
      Get src_ip , dst_ip , src_port , dst_port , app_name , ipPort , app_persent
    **/
    Future<RpcResult<Transmit5TupleOutput>> transmit5Tuple(Transmit5TupleInput input);

}

