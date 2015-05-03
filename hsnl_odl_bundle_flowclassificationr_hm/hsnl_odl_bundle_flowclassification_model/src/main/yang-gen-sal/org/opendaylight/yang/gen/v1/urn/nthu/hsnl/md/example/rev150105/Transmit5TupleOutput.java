package org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.Augmentable;


public interface Transmit5TupleOutput
    extends
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput>
{


    public enum EchoResult {
        Reachable(0),
        Unreachable(1),
        Error(2);
    
        int value;
        static java.util.Map<java.lang.Integer, EchoResult> valueMap;
    
        static {
            valueMap = new java.util.HashMap<>();
            for (EchoResult enumItem : EchoResult.values())
            {
                valueMap.put(enumItem.value, enumItem);
            }
        }
    
        private EchoResult(int value) {
            this.value = value;
        }
        
        /**
         * @return integer value
         */
        public int getIntValue() {
            return value;
        }
    
        /**
         * @param valueArg
         * @return corresponding EchoResult item
         */
        public static EchoResult forValue(int valueArg) {
            return valueMap.get(valueArg);
        }
    }


    /**
      Result types
    **/
    EchoResult getEchoResult();

}

