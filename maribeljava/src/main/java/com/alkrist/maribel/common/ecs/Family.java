package com.alkrist.maribel.common.ecs;

import java.util.HashMap;
import java.util.Map;

import com.alkrist.maribel.utils.Bits;


/**
 * The Family is NOT a group of entities, it's a FILTER OF COMPONENTS. The Family can make a selection
 * of some specified components and then match any entity to see if it suits this family or not.
 * 
 * To construct the Family and create a component selection we use Builder. The Builder operates with 3 
 * conditions: 
 * 
 * 1) All - a set of components the entity must have all of them.
 * 2) One - a set of components the entity must have at least one.
 * 3) Exclude - a set of components the entity must not have.
 * 
 * The example syntax to create a Family: 
 * Family family = Family.all(PositionComponent.class)
 *				  .one(TextureComponent.class, ParticleComponent.class)
 *				  .exclude(InvisibleComponent.class)
 *				  .get();
 *
 * That means that we want to make a Family which has PositionComponent for sure, has TextureComponent OR
 * ParticleComponent and doesn't have InvisibleComponent. "get" at the end means that it's the end of
 * condition block and will return the specified family.
 *				  
 * @author Mikhail
 * 
 * Original idea in LibGDX Ashley.
 */
public class Family {

	private static final Builder BUILDER = new Builder();
	
	private static Map<String, Family> families = new HashMap<String, Family>();
	
	private static int currentUID = 0;
	
	private static final Bits reset = new Bits(); //bits reset, has empty bitset
		
	private final int UID;
	
	private Bits all; 
	private Bits one;
	private Bits exclude;
	
	//For static factory method
	private Family(Bits all, Bits any, Bits exclude) {
		this.all = all;
		this.one = any;
		this.exclude = exclude;
		this.UID = currentUID++;
	}
	
	/**
	 * Checks if the entity matches this family conditions.
	 * 
	 * @param entity - an entity to check
	 * @return check result: is this entity a member of this family or not
	 */
	public boolean isMember(Entity entity) {
		Bits componentBits = entity.getComponentBits();
		
		if(!componentBits.containsAll(all)) return false;
		
		if(!one.isEmpty() && !one.intersects(componentBits)) return false;
		
		if(!exclude.isEmpty() && exclude.intersects(componentBits)) return false;
		
		return true;
	}
	
	/**
	 * @return this family's UID
	 */
	public int getUID() {
		return this.UID;
	}
	
	//Family hash is the String like: {all: 1,0,0,1}{one: 1,0,0,0}{exclude: 0,1,0,0}
	//Used as a key in Family map.
	private static String getFamilyHash(Bits all, Bits one, Bits exclude) {
		StringBuilder builder = new StringBuilder();
		
		if(!all.isEmpty())
			builder.append("{all: ").append(getBitsString(all)).append("}");
		if(!one.isEmpty())
			builder.append("{one: ").append(getBitsString(one)).append("}");
		if(!exclude.isEmpty())
			builder.append("{exclude: ").append(getBitsString(exclude)).append("}");
		return builder.toString();
	}
	
	//Appends a string format of the bitset.
	private static String getBitsString(Bits bits) {
		StringBuilder builder = new StringBuilder();
		int length = bits.length();
		
		for(int i=0; i<length; ++i)
			builder.append(bits.get(i) ? "1" : "0");
		return builder.toString();
	}
	
	//All of the specified components
	@SafeVarargs
	public static final Builder all(Class<? extends Component>... componentTypes) {
		return BUILDER.reset().all(componentTypes);
	}
		
	//At least one of the specified components
	@SafeVarargs
	public static final Builder one(Class<? extends Component>... componentTypes) {
		return BUILDER.reset().one(componentTypes);
	}
		
	//None of the specified components
	@SafeVarargs
	public static final Builder exclude(Class<? extends Component>... componentTypes) {
		return BUILDER.reset().exclude(componentTypes);
	}
	
	@Override
	public boolean equals(Object object) {
		return this == object;
	}
	
	@Override
	public int hashCode() {
		return UID;
	}
	
	/**
	 * Builder class is the internal class of Family, which is responsible for constructing a new family using
	 * All, One, Exclude conditions.
	 * 
	 * Creates bitsets of component sets for corresponding condition bitsets (all, one, exclude).
	 * 
	 * @author Mikhail
	 *
	 */
	public static class Builder{
		
		private Bits all = reset;
		private Bits one = reset;
		private Bits exclude = reset;
		
		Builder(){};
		
		/**
		 * Set all condition bitsets to empty.
		 * 
		 * @return Builder object with all clean bitsets.
		 */
		public Builder reset() {
			all = reset;
			one = reset;
			exclude = reset;
			return this;
		}
		
		/**
		 * Condition, where an entity must have ALL of the given components.
		 * 
		 * @param componentTypes - given component types, which entity must have all.
		 * @return Builder object with created "all" bitset.
		 */
		@SafeVarargs
		public final Builder all(Class<? extends Component>... componentTypes) {
			all = ComponentUID.getBitsetFor(componentTypes);
			return this;
		}
		
		/**
		 * Condition, where an entity must have AT LEAST ONE of the given components.
		 * 
		 * @param componentTypes - given component types, which entity must have at least one.
		 * @return Builder object with created "one" bitset.
		 */
		@SafeVarargs
		public final Builder one(Class<? extends Component>... componentTypes) {
			one = ComponentUID.getBitsetFor(componentTypes);
			return this;
		}
		
		/**
		 * Condition, where an entity must NOT have ALL of the given components.
		 * 
		 * @param componentTypes - given component types, which entity must not have.
		 * @return Builder object with created "exclude" bitset.
		 */
		@SafeVarargs
		public final Builder exclude(Class<? extends Component>... componentTypes) {
			exclude = ComponentUID.getBitsetFor(componentTypes);
			return this;
		}
		
		/**
		 * Final word in Family constructing. Finishes the conditioning process and returns a resulting family.
		 * 
		 * @return Family object which contains all conditions set before.
		 */
		public Family get() {
			String hash = getFamilyHash(all, one, exclude);
			Family family = families.get(hash);
			if(family == null) {
				family = new Family(all, one, exclude);
				families.put(hash, family);
			}
			return family;
		}
	}	
}
