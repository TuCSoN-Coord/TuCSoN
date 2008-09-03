package alice.tucson.tools;

import alice.tucson.api.*;
import java.net.*;

public class Ping {

	public static void main(String[]  args){
		try {
			boolean ok = Tucson.isActive(InetAddress.getByName(args[0]));
			if (ok){
				System.out.println("TuCSoN node available at "+args[0]);
			} else {
				System.out.println("TuCSoN node not available at "+args[0]);
			}
		} catch (Exception ex){
			System.out.println("Unreachable address: "+args[0]);
		}
	}
}
