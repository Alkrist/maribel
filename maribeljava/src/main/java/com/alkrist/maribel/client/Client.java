package com.alkrist.maribel.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import com.alkrist.maribel.client.updatable.scene.GameScene;
import com.alkrist.maribel.client.updatable.scene.SceneBase;
import com.alkrist.maribel.common.connection.bridge.LocalBridge;
import com.alkrist.maribel.common.connection.packet.PacketRegistry;
import com.alkrist.maribel.common.connection.packet.packets.PacketClientTick;
import com.alkrist.maribel.common.connection.proxy.LocalClient;
import com.alkrist.maribel.common.connection.sides.ClientSide;
import com.alkrist.maribel.common.ecs.builder.EntityProxy;
import com.alkrist.maribel.common.event.EventManager;
import com.alkrist.maribel.common.event.events.ClientTickEvent;
import com.alkrist.maribel.server.Server;
import com.alkrist.maribel.utils.Logging;

/**
 * A representation of the LOGICAL CLIENT.
 * This class stores all of the client level-related contents and dedicated client contents as well.
 * Also the client class contains the side and handles the connection to server. Also the client class
 * has the instance of server which is the local server.
 * @author Mikhail
 *
 */
public class Client {

	//******* SCENES *******//
	private static Map<Integer, SceneBase> scenes = new HashMap<Integer, SceneBase>();
	
	/**
	 * Get the existing scene by it's class name
	 * @param sceneClass - scene's class name
	 * @return scene, if it exists, if not, returns null
	 */
	public static SceneBase getScene(Class<? extends SceneBase> sceneClass) {
		return scenes.get(SceneBase.getIDbyClass(sceneClass));
	}
	
	/**
	 * Add a new scene to this client
	 * @param scene - scene that should be added
	 */
	public static synchronized void addScene(SceneBase scene) {
		scenes.put(scene.getID(), scene);
	}
	
	public static EntityProxy world;
	private static ClientSide mySide;
	private static Server internalServer;
	private static Timer clientTickTimer;
	public static PacketRegistry packetRegistry = new PacketRegistry();
	
	public static Server getServer() {
		return internalServer;
	}
	
	public static ClientSide getSide() {
		return mySide;
	}
	
	/**
	 * Client timer is used for fixed rate input listening (20 ticks) 
	 * and sending updates to server.
	 * 
	 * NOTE: timers should be started right after the game scene is enabled.
	 */
	public static void startTimers() {
		clientTickTimer = new Timer();
		clientTickTimer.scheduleAtFixedRate(new TimerTask() {
			
			public void run() {
				try {
					long start = System.currentTimeMillis();		
					tick();	
					long delta = System.currentTimeMillis() - start;
					if(delta > 50) 
						Logging.getLogger().log(Level.INFO, "Client tick took "+delta+"ms");
				}catch(Exception e) {
					Logging.getLogger().log(Level.SEVERE, "An error occured durning client tick", e);
				}	
			}
			
		}, 50, 50);
	}
	
	/**
	 * Stop the client's timers
	 */
	public static void stopTimers() {
		if(clientTickTimer != null) {
			clientTickTimer.cancel();
			clientTickTimer = null;
		}
	}
	
	/**
	 * Send client's data to server
	 */
	public static void tick() {
		ClientTickEvent event = new ClientTickEvent();
		EventManager.callEvent(event); //Collect all of the input data or just do something
		//TODO: form tick data
		mySide.send(new PacketClientTick(PacketRegistry.getIDFor("CLIENT_TICK"))); //Send the input result to server
	}
	
	//TODO: add path
	public static void loadSingleplayer() {
		internalServer = new Server();
		if(!internalServer.loadWorld())
			Logging.getLogger().log(Level.SEVERE, "Failed to load the world");
		mySide = new ClientSide(Settings.CURRENT.username);
		createLocalConnection();
		login();
	}
	
	//TODO: use parameter - world name, seed, other settings
	public static void createSingleplayer() {
		internalServer = new Server();
		if(!internalServer.createWorld()) {
			Logging.getLogger().log(Level.SEVERE, "Failed to create the world");
			return;
		}
			
		mySide = new ClientSide(Settings.CURRENT.username);
		createLocalConnection();
		login();
	}
	
	/**
	 * Connect this client to some remote server by the given host
	 * @param - host host name presented as: "ip:port"
	 */
	public static void connectMultiplayer(String host) {
		//TODO: add hostname option with default port
		String args[] = host.split(":", 2);
		mySide = new ClientSide(Settings.CURRENT.username);
		
		try {
			mySide.init(args[0], Integer.valueOf(args[1]));
		}catch(Exception e) {
			System.err.println("failed to connect to "+args[0]+", on port "+args[1]);
			e.printStackTrace();
			mySide.close();
			mySide = null;
			return;
		}
		login();
	}
	
	/**
	 * Stop internal server's local connection through shared memory and broadcast it on UDP
	 */
	public static void openForNetwork() {
		if(mySide.isLocal()) {
			stopTimers();
			/*DEBUG*/System.out.println("stopping local connection...");
			mySide.close();
			internalServer.getConnection().close();
		
			/*DEBUG*/System.out.println("Starting remote client...");
			try {
				mySide.init("localhost", Settings.CURRENT.port);
			}catch(Exception e) {
				Logging.getLogger().log(Level.SEVERE, "failed to change localclient to global on localhost at port "+Settings.CURRENT.port, e);
				//TODO: quit the game or change back to local
				mySide = null;
				return;
			}
		
			/*DEBUG*/System.out.println("Starting remote server...");
			internalServer.getConnection().init(Settings.CURRENT.port);
			internalServer.getConnection().getBridge().start();
		
			login();
		}else {
			Logging.getLogger().log(Level.WARNING, "The connection is already global!");
		}
	}
	
	/**
	 * Stop internal server's broadcast on UDP and change to Shared Memory
	 */
	public static void stopNetworking() {
		if(!mySide.isLocal()) {
			stopTimers();
		
			/*DEBUG*/System.out.println("stopping global connection...");
			mySide.close();
			internalServer.getConnection().close();
		
			/*DEBUG*/System.out.println("starting local connection...");
			createLocalConnection();
			login();
			startTimers();
		}else {
			Logging.getLogger().log(Level.WARNING, "The connection is already local!");
		}
		
	}
	
	/*Init the local bridges and start the server bridge*/
	private static void createLocalConnection() {
		
		LocalBridge clientBridge = new LocalBridge();
		LocalBridge serverBridge = new LocalBridge();
		
		clientBridge.init(mySide, serverBridge);
		serverBridge.init(internalServer.getConnection(), clientBridge);
		
		mySide.init(clientBridge, serverBridge);
		internalServer.getConnection().init(serverBridge, clientBridge);
		
		serverBridge.start();
	}
	
	/*Do the login: directly connect if local, send packet if remote*/
	private static void login() {
		mySide.getBridge().start();
		
		if(mySide.isLocal()) {
			if(internalServer.getConnection().addClient(new LocalClient(Settings.CURRENT.username, internalServer.getConnection()))) {
				Logging.getLogger().log(Level.INFO, "Locally logged in successfuly.");
				if(!(Updater.getActiveElement() instanceof GameScene))
					Updater.setActiveElement(getScene(GameScene.class));
			}		
			else Logging.getLogger().log(Level.INFO, "Local login failed.");
		}else {
			mySide.login();
		}
	}
}
