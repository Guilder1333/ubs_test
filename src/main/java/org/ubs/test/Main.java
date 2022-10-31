package org.ubs.test;

import java.rmi.UnexpectedException;

public class Main {
    public static void main(String[] args) throws UnexpectedException {
        Rules rules = new DefaultRules();
        Game game = new Game(rules);

        Renderer renderer = new ConsoleGameRenderer(game, rules);
        renderer.RunGame();
    }
}
