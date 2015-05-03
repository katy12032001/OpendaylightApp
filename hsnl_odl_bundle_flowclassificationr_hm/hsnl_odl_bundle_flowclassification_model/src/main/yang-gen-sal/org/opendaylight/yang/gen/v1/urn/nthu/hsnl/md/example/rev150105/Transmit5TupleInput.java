package org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.Augmentable;


public interface Transmit5TupleInput
    extends
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleInput>
{




    /**
    **/
    String getAppName();
    
    /**
    **/
    String getAppPersent();
    
    /**
    **/
    String getDstIp();
    
    /**
    **/
    String getDstPort();
    
    /**
    **/
    String getIpPort();
    
    /**
    **/
    String getSrcIp();
    
    /**
    **/
    String getSrcPort();

}

