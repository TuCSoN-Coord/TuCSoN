/*
 * LogicReaction.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
 */
package alice.respect;
import alice.tuprolog.*;

/**
 * Defines the reaction type managed by ReSpect Reactor
 * (logic reactions).
 */
public class LogicReaction implements alice.tuplecentre.Reaction, java.io.Serializable{

    private Struct reaction;

    public LogicReaction(){
    }

    public LogicReaction(Struct t){
        reaction=t;
    }

    public String toString(){
        return reaction.toString();
    }

    public Struct getStructReaction(){
        return reaction;
    }
      
}

