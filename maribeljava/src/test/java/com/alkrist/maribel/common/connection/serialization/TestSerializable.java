package com.alkrist.maribel.common.connection.serialization;

public class TestSerializable implements Serializable{

	public int number = 70;
	public float fNumber = 4.3f;
	public String str = "Maribel";
	public boolean expr = true;
	
	@Override
	public boolean read(SerialBuffer buffer) {
		number = buffer.readInt();
		int strlen = buffer.readInt();
		str = buffer.readString(strlen);
		fNumber = buffer.readFloat();
		expr = buffer.readBoolean();
		return true;
	}

	@Override
	public boolean write(SerialBuffer buffer) {
		buffer.writeInt(number);
		buffer.writeInt(str.length());
		buffer.writeString(str);
		buffer.writeFloat(fNumber);
		buffer.writeBoolean(expr);
		return true;
	}

}
