package com.alkrist.maribel.server;

import java.util.Timer;
import java.util.TimerTask;

import com.alkrist.maribel.common.connection.sides.ServerSide;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.event.EventManager;
import com.alkrist.maribel.common.event.events.ServerInitializationEvent;


/**
 * <pre>
 * Server is the core part for all of the server-side processes. Actually it's a LOGICAL SERVER, not the 
 * dedicated server, do not confuse these two.
 * 
 * Things the Server does:
 * - Load/Save level data
 * - Maintain connection with clients
 * - Operate with clients
 * - Update level on a given frequency
 * </pre>
 * @author Mikhail
 *
 */
public class Server {

	private static ServerSide mySide;
	private Timer tickTimer;
	private Engine engine;
	private World world;
	
	public Server() {
		mySide = new ServerSide();
		engine = new Engine();
		EventManager.callEvent(new ServerInitializationEvent(engine, mySide));
	}
		
		
		//Init world data only (create), no server related components
		public boolean createWorld() {
			world = new World(this);
			//init world
			
			/*DEBUG PURPOSE ONLY*/
			//engine.addEntity(world.createEntityWithUID(EntityFactory.MANAGER.getGOIDfor(TestGameObject1.class)));
			
			startWorld();
			return true;
		}
		
		//Init world data only (load from files), no server related components
		public boolean loadWorld() {
			//TODO: world = world.load(path)
			//bla bla bla
			startWorld();
			return true;
		}
		
		//Generate spawn area or something, start updating the world
		private void startWorld() {
			startTimers();
		}
		
		public void close() {
			stopTimers();
			mySide.close();
			save();	
			/*
			 * a) close connection
			 * b) stop timers
			 * c) handle other small things
			 * d) save world
			 */
		}
	
	//start updating world every tick
	private long deltaTime;
	private void startTimers() {
		tickTimer = new Timer();
		tickTimer.scheduleAtFixedRate(new TimerTask() {

			public void run() {
				Thread.currentThread().setName("Server thread");
				long startTime = System.currentTimeMillis();
				
				engine.update(deltaTime);
				mySide.tick();
				
				deltaTime = (System.currentTimeMillis() - startTime);
				if(deltaTime > 50) System.out.println("Server tick took "+deltaTime+"ms");
			}
			
		}, 50, 50);
	}
	
	//Stop updating world every tick.
	private void stopTimers() {
		if(tickTimer!=null) {
			tickTimer.cancel();
			tickTimer = null;
		}
	}
	
	private void save() {
		/*
		 * a) save world chunks
		 * b) save other data
		 * c) save player - related data
		 * d) save world
		 */
	}
	
	public ServerSide getConnection() {
		return mySide;
	}
	
	public Engine getEngine() {
		return engine;
	}
}
