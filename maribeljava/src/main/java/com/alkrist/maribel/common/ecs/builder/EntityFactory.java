package com.alkrist.maribel.common.ecs.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.alkrist.maribel.client.Client;
import com.alkrist.maribel.common.connection.serialization.SerialBuffer;
import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.ComponentUID;
import com.alkrist.maribel.common.ecs.Engine;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.utils.Logging;


/**
 * <pre>
 * The Entity factory is used to create/reproduce/update entity from it's Game Object descriptor class.
 * Every Entity Factory has it's own Entity Builder (Game Object descriptor class) attached to it.
 * 
 * Entity Factory has the Factory Manager's singleton object called MANAGER that's in charge of
 * manipulating entity factories. To get a specific entity factory you should call the MANAGER object.
 * 
 * The Entity Factory's operation algorithm:
 * 1)When entity is firstly created, add id components (EntityID, GOID components), then call the builder's create method.
 * 2)When entity is serializaed, write it's id components (entityID, GOID), then call builder's write methods.
 * 3) when entity is scanned on client side, firstly define whether or not the entity exists, then call builder's
 * "reproduce" or "update" methods. In update case, the factory doesn't add id components hence they're already added and can't be changed.
 * </pre>
 * @author Mikhail
 *
 * @param <T> - Game Object descriptor class, extends EntityBuilder
 */
public class EntityFactory <T extends EntityBuilder>{

	public static final EntityFactoryManager MANAGER = new EntityFactoryManager();
	
	private static int globalID = 1;
	
	@SuppressWarnings("rawtypes")
	private static ComponentMapper entityIDmapper = ComponentMapper.getFor(EntityID.class);
	
	private T gameObject;
	private int objectID;
	
	private EntityFactory(T object, int id) {
		if(MANAGER.getProxy() == null) {
			Logging.getLogger().log(Level.SEVERE, "Entity proxy is invalid", new NullPointerException("Entity Proxy is null"));
			System.exit(1);
		}	
		this.gameObject = object;
		this.objectID = id;
	}
	
	/**
	 * A static factory method for creating a new Entity Factory.
	 * @param <T> - corresponding game object type, extends EntityBuilder
	 * @param gameObjectClass - game object class, which will define the game Object stored in this factory.
	 * @return new EntityFactory with the relevant game object type
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("deprecation")
	public static <T extends EntityBuilder> EntityFactory<T> getFactoryFor(Class<T> gameObjectClass) throws InstantiationException, IllegalAccessException{
		T object = gameObjectClass.newInstance();
		return new EntityFactory<T>(object, globalID++);
	}
	
	public int getID() {
		return objectID;
	}
	
	
	// ******* FACTORY TASKS ******* //
	
	/*Important info: Serialized entity values order
	 * 
	 * 1) Game Object ID (GOID)
	 * 2) Entity Unique ID
	 * 3) - N) Components
	 */
	
	
	/**
	 * Create a fresh new entity in a specific manner.
	 * @return created entity
	 */
	public Entity createEntity() {
		Entity entity = gameObject.createEntity(); //Adds non-important data, can even be empty
		if(entity== null) return null;
		
		entity.addComponent(new GameObjectID(objectID)); //Adds essential ID component
		return entity;
	}
	
	/**
	 * Deserialize entity from a serial buffer. Uses the Entity Builder to create an entity in specific
	 * manner.
	 * @param buffer - a serial buffer where the entity is stored as byte array
	 * @return recreated entity
	 */
	public Entity readEntity(SerialBuffer buffer) {
		//OLD IMPLEMENTATION
		/*int entityID = buffer.readInt(); //Read the entity unique id from buffer
		
		Entity entity;
		if(Client.world.hasEntity(entityID)) {//if the entity already exists on client, just update it
			entity = gameObject.updateEntity(Client.world.getEntity(entityID), buffer);
		}else {//If the entity is new, create new entity from buffer
			entity = gameObject.reproduceEntity(buffer);
			
			if(entity == null) return null;
			
			Client.world.addEntity(entity, entityID);
			entity.addComponent(new GameObjectID(objectID)); //Adds essential GOID component
			entity.addComponent(new EntityID(entityID)); //Adds essential Entity UID component
		}*/
		int entityID = buffer.readInt();
		Entity entity;
		if(MANAGER.getProxy().hasEntity(entityID)) {
			GameObjectID goid = MANAGER.getProxy().getEntity(entityID).getComponent(ComponentUID.getFor(GameObjectID.class));
			if(goid == null) {
				Logging.getLogger().log(Level.WARNING, "Received null game object on Client: "+gameObject.getClass().getSimpleName());
				return null;
			}
			
			if(goid.getID() == this.objectID) {
				entity = gameObject.updateEntity(MANAGER.getProxy().getEntity(entityID), buffer);
			}else {
				entity = MANAGER.getProxy().getEntity(entityID);
				//TODO: clean entity;
				entity.addComponent(new GameObjectID(objectID)); //Adds essential GOID component
				entity.addComponent(new EntityID(entityID)); //Adds essential Entity UID component
				entity = gameObject.updateEntity(entity, buffer);
			}
		}else {
			entity = gameObject.reproduceEntity(buffer);
			
			if(entity == null) return null;
			
			MANAGER.getProxy().addEntity(entity, entityID);
			entity.addComponent(new GameObjectID(objectID)); //Adds essential GOID component
			entity.addComponent(new EntityID(entityID)); //Adds essential Entity UID component
		}
		return entity;
	}
	
	/**
	 * Serialize entity, write it to the buffer.
	 * Firstly writes the game object ID, then calls it's entity builder to serialize other components.
	 * Other components can even not be serialized, but the ID always is.
	 * @param entity - entity to serialize
	 * @param buffer - a serial buffer to write the entity data to
	 */
	public void writeEntity(Entity entity, SerialBuffer buffer) {
		buffer.writeInt(objectID); //Write GOID first
		
		if(entityIDmapper.hasComponent(entity)){
			EntityID eid = (EntityID) entityIDmapper.getComponent(entity);
			buffer.writeInt(eid.getID()); //Write Entity UID second
			
			gameObject.serializeEntity(entity, buffer); //Write other things
		}
		else throw new IllegalStateException("Entity must have an EntityID component to be processed on client!");
	}
	
	/**
	 * A singleton class that keeps track of all registered Entity factories.
	 * From this class we can get a needed factory by it's game object ID. 
	 * That's made for a purpose that we call it when reading a packet, or entity, firstly we get it's GOID,
	 * then we call the corresponding EntityFactory by this GOID, which will perform further transformations
	 * on this entity.
	 * @author Mikhail
	 */
	public static class EntityFactoryManager{
		
		private EntityProxy proxy;
		
		public void init(Engine engine) {
			this.proxy = new EntityProxy(engine);
		}
		
		public EntityProxy getProxy() {
			return proxy;
		}
		
		@SuppressWarnings("rawtypes")
		private Map<Integer, EntityFactory> factories;	//All factories stored relevantly to their IDs.
		private Map<Class<? extends EntityBuilder>, Integer> gameObjectIDs;
		
		@SuppressWarnings("rawtypes")
		protected EntityFactoryManager() {
			factories = new HashMap<Integer, EntityFactory>();	
			gameObjectIDs = new HashMap<Class<? extends EntityBuilder>, Integer>();
		}
		
		/**
		 * Register a new entity factory for a game object type.
		 * @param <T> - game object type, must extends EntityBuilder
		 * @param gameObjectClass - game object class
		 * @throws InstantiationException
		 * @throws IllegalAccessException
		 */
		@SuppressWarnings("rawtypes")
		public <T extends EntityBuilder> void registerEntityFactoryFor(Class <T> gameObjectClass) throws InstantiationException, IllegalAccessException {
			
			if(gameObjectIDs.containsKey(gameObjectClass)) {
				Logging.getLogger().log(Level.WARNING, "this class is already registered!");
				return;
			}
			
			EntityFactory factory = EntityFactory.getFactoryFor(gameObjectClass);
			int id = factory.getID();
			if(!factories.containsKey(id)) {
				factories.put(id, factory);
			}
			gameObjectIDs.put(gameObjectClass, id);
			
			System.out.println(id);
		}
		
		/**
		 * Gets the existing factory for the relevant game object ID
		 * @param <T> - game object type, extends EntityBuilder
		 * @param gameObjectID - GOID of the searched object and factory
		 * @return Entity factory which is connected to specific Game Object
		 */
		@SuppressWarnings("unchecked")
		public <T extends EntityBuilder> EntityFactory<T> getFactoryFor(int gameObjectID) {
			return factories.get(gameObjectID);
		}
		
		/**
		 * Return a game object id related to the game object class (which must extends EntityBuilder).
		 * @param gameObjectClass - a game objet descriptor class (extends EntityBuilder)
		 * @return GOID value
		 */
		public int getGOIDfor(Class<? extends EntityBuilder> gameObjectClass) {
			return gameObjectIDs.get(gameObjectClass);
		}
	}
}
