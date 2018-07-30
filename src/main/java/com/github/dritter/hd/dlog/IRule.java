package com.github.dritter.hd.dlog;

import java.util.List;

public interface IRule {

    /**
     * @return the head
     */
    Literal getHead();

    /**
     * @return the body
     */
    List<Literal> getBody();

    /**
     * @return String
     */
    String toString();

}