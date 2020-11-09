package yapion.serializing.api;

import yapion.serializing.SerializeManager;

@SuppressWarnings({"java:S1610"})
public abstract class InstanceFactory<T> implements InstanceFactoryInterface<T> {

    /**
     * Add this ListSerializer to the SerializeManager by calling
     * {@link SerializeManager#add(InstanceFactoryInterface)}.
     */
    public final void add() {
        SerializeManager.add(this);
    }

}
