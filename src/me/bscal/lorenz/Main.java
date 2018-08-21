package me.bscal.lorenz;

import me.bscal.lorenz.tests.LorenzTest;
import me.bscal.lorenz.tests.StarFieldTest;

public class Main {

	private static LorenzTest app;

	private final static boolean debug = false;

	public static void main(String[] args) {
		app = new LorenzTest();
		if (!debug) {
			app.setDisplayStatView(false);
			app.setDisplayFps(false);
		}
		app.setShowSettings(true);
		app.start();
	}

}
