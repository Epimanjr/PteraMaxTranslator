package model;

import java.io.Serializable;

/**
 *
 * @author Maxime BLAISE
 */
public class Link implements Serializable {

    /**
     * First id.
     */
    private int id1;

    /**
     * Second id.
     */
    private int id2;

    /**
     * Create a link with specific id.
     *
     * @param id1 First id.
     * @param id2 Second id.
     */
    public Link(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Link other = (Link) obj;
        if (this.id1 != other.id1) {
            return false;
        }
        if (this.id2 != other.id2) {
            return false;
        }
        return true;
    }
    
    

    public int getId1() {
        return id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public int getId2() {
        return id2;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }

}
