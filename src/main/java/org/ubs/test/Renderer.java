package org.ubs.test;

import java.rmi.UnexpectedException;

/**
 * Base interface for UI renderer.
 */
public interface Renderer {
    /**
     * Start game cycle.
     * @throws UnexpectedException something unexpected happened during the game.
     */
    void RunGame() throws UnexpectedException;
}
