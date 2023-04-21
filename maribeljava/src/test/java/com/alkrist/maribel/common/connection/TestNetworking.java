package com.alkrist.maribel.common.connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.alkrist.maribel.api.MaribelRegistry;
import com.alkrist.maribel.client.Client;
import com.alkrist.maribel.client.settings.Settings;
import com.alkrist.maribel.common.connection.bridge.LocalBridge;
import com.alkrist.maribel.common.connection.bridge.RemoteBridge;
import com.alkrist.maribel.common.connection.packet.Packet;
import com.alkrist.maribel.common.connection.packet.PacketRegistry;
import com.alkrist.maribel.utils.Logging;

public class TestNetworking {

	@BeforeAll
	public static void start() {
		Logging.initLogger();
		Settings.load();

		MaribelRegistry.registerPackets();
		PacketRegistry.registerPacket(new PacketTest());
		
		assertNotNull(PacketRegistry.getIDFor("TEST"));
		Client.createSingleplayer();
	}
	
	@Test
	public void localServerTest() {
		assertNotNull(Client.getServer().getConnection().getClients());
		assertNotNull(Client.getSide().getBridge());
		
		Packet packet = new PacketTest(PacketRegistry.getIDFor("TEST"), Settings.CORE.username, "I love you, Server!");
		assertNotNull(packet);
		if(Client.getSide().isLocal()) {
			((LocalBridge)Client.getSide().getBridge()).send(Client.getSide().getBridge().coder.encode(packet));
			System.out.println("sent");
		}
			
		else
			((RemoteBridge)Client.getSide().getBridge()).send(Client.getSide().getBridge().coder.encode(packet), 
					Client.getSide().getServerAddress(), Settings.CORE.port);
	}
	
	@AfterAll
	public static void finish() {
		Client.getSide().logout();
	}
}
