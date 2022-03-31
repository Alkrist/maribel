package com.alkrist.maribel.common.connection.sides;

import com.alkrist.maribel.common.connection.bridge.Bridge;
import com.alkrist.maribel.common.connection.bridge.LocalBridge;
import com.alkrist.maribel.common.connection.bridge.RemoteBridge;

/**
 * Side is a specific space that's in charge of all side-related operations, such as connect and disconnect other sides, 
 * send and receive data.
 * Every side has Bridge, that is used to transmit the data, also it activity state (active/inactive) that is used
 * to check whether the data can be transmitted or not. Also the side has it's Local/Remote state, that is used
 * to identify the way the data is transmitted (through shared memory or over UDP). Side can never send/receive
 * data directly, every operation is performed through bridges.
 * @author Mikhail
 */
public abstract class Side {

	protected Bridge bridge;
	
	private boolean local;
	private boolean active = false;
	
	protected void initLocal(LocalBridge myBridge) {
		local = true;
		bridge = myBridge;
	}
	
	protected void initRemote() {
		local = false;
		bridge = new RemoteBridge();
		active = true;
	}
	
	/**
	 * @return bridge of this side
	 */
	public final Bridge getBridge() {
		return bridge;
	}
	
	/**
	 * @return true = local, false = remote
	 */
	public final boolean isLocal() {
		return local;
	}
	
	/**
	 * @return true = active, false = inactive
	 */
	public final boolean isActive() {
		return active;
	}
	
	protected void close() {
		active = false;
	}
	
	public void open() {
		active = true;
	}
}
