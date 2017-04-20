package grape.app.GrpaeApps;

import httpServer.booter;

/**
 * Hello world!
 *
 */
public class Appserver {
	public static void main(String[] args) {
		booter booter = new booter();
		System.out.println("GrapeApps!");
		try {
			System.setProperty("AppName", "GrapeApps");
			booter.start(106);
		} catch (Exception e) {

		}
	}
}
