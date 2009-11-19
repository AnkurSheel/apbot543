/*
 * RandomPokerClient.java
 *
 * Created on April 19, 2006, 2:04 PM
 */

package ca.ualberta.cs.poker.free.client;
import java.io.*;
import java.net.*;
import java.security.*;

import ca.ualberta.cs.poker.free.dynamics.LimitType;
import ca.ualberta.cs.poker.free.dynamics.MatchType;

/**
 * Plays actions uniformly at random. Useful for debugging purposes.
 * 
 * @author Martin Zinkevich
 */
public class myClient extends PokerClient {
    SecureRandom random;

    /**
     * A reproduction of what is happening on the server side.
     */
    public ClientPokerDynamics state;
    
    
    /** 
     * Handles the state change. 
     * Updates state and calls takeAction()
     * @throws IOException 
     * @throws SocketException 
     */
    public void handleStateChange() throws IOException, SocketException{
        state.setFromMatchStateMessage(currentGameStateString);
        //System.out.println(state.hole[0][0] + " " + state.hole[1][0]);
        if (state.isOurTurn()){
            takeAction();
        }
    }
    
    /**
     * Overload to take actions.
     * @throws IOException 
     * @throws SocketException 
     * @throws IOException 
     * @throws SocketException 
     */
    public void takeAction() throws SocketException, IOException{
    	double dart = random.nextDouble();
        if (dart<0.05){
            sendFold();
        } else if (dart<0.55){
            sendCall();
        } else {
        	// A lot of these raises WILL be illegal, but the server should intercept
        	// them.
        	int finalPot = random.nextInt(1001);
            sendRaise(finalPot);
        }
    }
    
    
    /** 
     * Creates a new instance of RandomPokerClient 
     */
    public myClient(){
      super();
  	MatchType type = new MatchType(LimitType.DOYLE,false,0,1000);
      state = new ClientPokerDynamics(type);
      random = new SecureRandom();  
    }
    
    /**
     * @param args the command line parameters (IP and port)
     */
    public static void main(String[] args) throws Exception{
        myClient rpc = new myClient();
        System.out.println("Attempting to connect to "+args[0]+" on port "+args[1]+"...");

        rpc.connect(InetAddress.getByName(args[0]),Integer.parseInt(args[1]));
        System.out.println("Successful connection!");
        rpc.run();
    }
    
}
