package com.github.dritter.hd.dlog;

import java.util.List;

public interface IQuery {

    /*
     * (non-Javadoc)
     * 
     * @see IRule#getBody()
     */
    List<Literal> getBody();

    /*
     * (non-Javadoc)
     * 
     * @see IRule#toString()
     */
    String toString();

}