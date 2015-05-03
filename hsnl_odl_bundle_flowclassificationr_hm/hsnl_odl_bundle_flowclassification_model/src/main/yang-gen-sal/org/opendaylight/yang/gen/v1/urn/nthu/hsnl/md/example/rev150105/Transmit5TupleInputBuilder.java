package org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105;
import java.util.Map;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import java.util.HashMap;



public class Transmit5TupleInputBuilder {

    private String _appName;
    private String _appPersent;
    private String _dstIp;
    private String _dstPort;
    private String _ipPort;
    private String _srcIp;
    private String _srcPort;
    private Map<Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleInput>> augmentation = new HashMap<>();

    public Transmit5TupleInputBuilder() {
    } 


    public String getAppName() {
        return _appName;
    }
    
    public String getAppPersent() {
        return _appPersent;
    }
    
    public String getDstIp() {
        return _dstIp;
    }
    
    public String getDstPort() {
        return _dstPort;
    }
    
    public String getIpPort() {
        return _ipPort;
    }
    
    public String getSrcIp() {
        return _srcIp;
    }
    
    public String getSrcPort() {
        return _srcPort;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleInput>> E getAugmentation(Class<E> augmentationType) {
        if (augmentationType == null) {
            throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
        }
        return (E) augmentation.get(augmentationType);
    }

    public Transmit5TupleInputBuilder setAppName(String value) {
    
        this._appName = value;
        return this;
    }
    
    public Transmit5TupleInputBuilder setAppPersent(String value) {
    
        this._appPersent = value;
        return this;
    }
    
    public Transmit5TupleInputBuilder setDstIp(String value) {
    
        this._dstIp = value;
        return this;
    }
    
    public Transmit5TupleInputBuilder setDstPort(String value) {
    
        this._dstPort = value;
        return this;
    }
    
    public Transmit5TupleInputBuilder setIpPort(String value) {
    
        this._ipPort = value;
        return this;
    }
    
    public Transmit5TupleInputBuilder setSrcIp(String value) {
    
        this._srcIp = value;
        return this;
    }
    
    public Transmit5TupleInputBuilder setSrcPort(String value) {
    
        this._srcPort = value;
        return this;
    }
    
    public Transmit5TupleInputBuilder addAugmentation(Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleInput>> augmentationType, Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleInput> augmentation) {
        this.augmentation.put(augmentationType, augmentation);
        return this;
    }

    public Transmit5TupleInput build() {
        return new Transmit5TupleInputImpl(this);
    }

    private static final class Transmit5TupleInputImpl implements Transmit5TupleInput {

        public Class<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleInput> getImplementedInterface() {
            return org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleInput.class;
        }

        private final String _appName;
        private final String _appPersent;
        private final String _dstIp;
        private final String _dstPort;
        private final String _ipPort;
        private final String _srcIp;
        private final String _srcPort;
        private Map<Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleInput>> augmentation = new HashMap<>();

        private Transmit5TupleInputImpl(Transmit5TupleInputBuilder builder) {
            this._appName = builder.getAppName();
            this._appPersent = builder.getAppPersent();
            this._dstIp = builder.getDstIp();
            this._dstPort = builder.getDstPort();
            this._ipPort = builder.getIpPort();
            this._srcIp = builder.getSrcIp();
            this._srcPort = builder.getSrcPort();
            this.augmentation.putAll(builder.augmentation);
        }

        @Override
        public String getAppName() {
            return _appName;
        }
        
        @Override
        public String getAppPersent() {
            return _appPersent;
        }
        
        @Override
        public String getDstIp() {
            return _dstIp;
        }
        
        @Override
        public String getDstPort() {
            return _dstPort;
        }
        
        @Override
        public String getIpPort() {
            return _ipPort;
        }
        
        @Override
        public String getSrcIp() {
            return _srcIp;
        }
        
        @Override
        public String getSrcPort() {
            return _srcPort;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleInput>> E getAugmentation(Class<E> augmentationType) {
            if (augmentationType == null) {
                throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
            }
            return (E) augmentation.get(augmentationType);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((_appName == null) ? 0 : _appName.hashCode());
            result = prime * result + ((_appPersent == null) ? 0 : _appPersent.hashCode());
            result = prime * result + ((_dstIp == null) ? 0 : _dstIp.hashCode());
            result = prime * result + ((_dstPort == null) ? 0 : _dstPort.hashCode());
            result = prime * result + ((_ipPort == null) ? 0 : _ipPort.hashCode());
            result = prime * result + ((_srcIp == null) ? 0 : _srcIp.hashCode());
            result = prime * result + ((_srcPort == null) ? 0 : _srcPort.hashCode());
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
            Transmit5TupleInputImpl other = (Transmit5TupleInputImpl) obj;
            if (_appName == null) {
                if (other._appName != null) {
                    return false;
                }
            } else if(!_appName.equals(other._appName)) {
                return false;
            }
            if (_appPersent == null) {
                if (other._appPersent != null) {
                    return false;
                }
            } else if(!_appPersent.equals(other._appPersent)) {
                return false;
            }
            if (_dstIp == null) {
                if (other._dstIp != null) {
                    return false;
                }
            } else if(!_dstIp.equals(other._dstIp)) {
                return false;
            }
            if (_dstPort == null) {
                if (other._dstPort != null) {
                    return false;
                }
            } else if(!_dstPort.equals(other._dstPort)) {
                return false;
            }
            if (_ipPort == null) {
                if (other._ipPort != null) {
                    return false;
                }
            } else if(!_ipPort.equals(other._ipPort)) {
                return false;
            }
            if (_srcIp == null) {
                if (other._srcIp != null) {
                    return false;
                }
            } else if(!_srcIp.equals(other._srcIp)) {
                return false;
            }
            if (_srcPort == null) {
                if (other._srcPort != null) {
                    return false;
                }
            } else if(!_srcPort.equals(other._srcPort)) {
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
            StringBuilder builder = new StringBuilder("Transmit5TupleInput [");
            boolean first = true;
        
            if (_appName != null) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append("_appName=");
                builder.append(_appName);
             }
            if (_appPersent != null) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append("_appPersent=");
                builder.append(_appPersent);
             }
            if (_dstIp != null) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append("_dstIp=");
                builder.append(_dstIp);
             }
            if (_dstPort != null) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append("_dstPort=");
                builder.append(_dstPort);
             }
            if (_ipPort != null) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append("_ipPort=");
                builder.append(_ipPort);
             }
            if (_srcIp != null) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append("_srcIp=");
                builder.append(_srcIp);
             }
            if (_srcPort != null) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append("_srcPort=");
                builder.append(_srcPort);
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
