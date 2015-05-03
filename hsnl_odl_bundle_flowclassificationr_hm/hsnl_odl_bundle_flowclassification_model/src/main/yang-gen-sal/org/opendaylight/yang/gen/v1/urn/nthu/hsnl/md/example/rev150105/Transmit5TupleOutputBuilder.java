package org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105;
import org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput.EchoResult;
import java.util.Map;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import java.util.HashMap;



public class Transmit5TupleOutputBuilder {

    private EchoResult _echoResult;
    private Map<Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput>> augmentation = new HashMap<>();

    public Transmit5TupleOutputBuilder() {
    } 


    public EchoResult getEchoResult() {
        return _echoResult;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput>> E getAugmentation(Class<E> augmentationType) {
        if (augmentationType == null) {
            throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
        }
        return (E) augmentation.get(augmentationType);
    }

    public Transmit5TupleOutputBuilder setEchoResult(EchoResult value) {
    
        this._echoResult = value;
        return this;
    }
    
    public Transmit5TupleOutputBuilder addAugmentation(Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput>> augmentationType, Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput> augmentation) {
        this.augmentation.put(augmentationType, augmentation);
        return this;
    }

    public Transmit5TupleOutput build() {
        return new Transmit5TupleOutputImpl(this);
    }

    private static final class Transmit5TupleOutputImpl implements Transmit5TupleOutput {

        public Class<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput> getImplementedInterface() {
            return org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput.class;
        }

        private final EchoResult _echoResult;
        private Map<Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput>> augmentation = new HashMap<>();

        private Transmit5TupleOutputImpl(Transmit5TupleOutputBuilder builder) {
            this._echoResult = builder.getEchoResult();
            this.augmentation.putAll(builder.augmentation);
        }

        @Override
        public EchoResult getEchoResult() {
            return _echoResult;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput>> E getAugmentation(Class<E> augmentationType) {
            if (augmentationType == null) {
                throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
            }
            return (E) augmentation.get(augmentationType);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((_echoResult == null) ? 0 : _echoResult.hashCode());
            result = prime * result + ((augmentation == null) ? 0 : augmentation.hashCode());
            return result;
        }

        @Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Transmit5TupleOutputImpl other = (Transmit5TupleOutputImpl) obj;
            if (_echoResult == null) {
                if (other._echoResult != null) {
                    return false;
                }
            } else if(!_echoResult.equals(other._echoResult)) {
                return false;
            }
            if (augmentation == null) {
                if (other.augmentation != null) {
                    return false;
                }
            } else if(!augmentation.equals(other.augmentation)) {
                return false;
            }
            return true;
        }
        
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("Transmit5TupleOutput [");
            boolean first = true;
        
            if (_echoResult != null) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append("_echoResult=");
                builder.append(_echoResult);
             }
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }
            builder.append("augmentation=");
            builder.append(augmentation.values());
            return builder.append(']').toString();
        }
    }

}
