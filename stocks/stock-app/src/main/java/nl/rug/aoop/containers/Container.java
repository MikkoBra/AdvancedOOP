package nl.rug.aoop.containers;

/**
 * Abstract class outlining the functionality of an object that holds objects of a specific type and allows for their
 * modification.
 *
 * @param <T> The type of object to be held in the Container.
 */
public abstract class Container<T> {

    /**
     * Method for adding an entry to the container.
     *
     * @param entry Object of the same type as the container T to be added to the container.
     */
    public abstract void addEntry(T entry);

    /**
     * Method for removing an entry from the container.
     *
     * @param id String reference to the entry to be removed from the container.
     */
    public abstract void removeEntry(String id);

    /**
     * Method for checking if an entry exists in a container.
     *
     * @param id String id representing the entry in the Container object.
     * @return True if the id was found, false if not.
     */
    public abstract boolean hasEntry(String id);

    /**
     * Method for retrieving information about a specific entry in the container.
     *
     * @param id String reference to the entry that was requested from the container.
     * @return Object of the type held in the container that was requested using String parameter id.
     */
    public abstract T getInfo(String id);

    /**
     * Method for checking whether a container is empty or not.
     *
     * @return Boolean denoting the truth value corresponding to whether the container is empty or not.
     */
    public abstract boolean isEmpty();

    /**
     * Method for retrieving the number of entries in the container.
     *
     * @return Integer value representing the number of entries in the Container object.
     */
    public abstract int getSize();
}
