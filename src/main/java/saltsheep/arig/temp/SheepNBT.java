package saltsheep.arig.temp;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class SheepNBT {
	
	private NBTTagCompound privateNBT;
	
	public SheepNBT(NBTTagCompound nbtIn) {
		this.privateNBT = nbtIn;
	}
	//*Tips:It will cover the old nbt if it's not a compound.
	public SheepNBT getOrCreateCompound(String key) {
		if(this.privateNBT.getTag(key) == null||(!(this.privateNBT.getTag(key) instanceof NBTTagCompound))) {
			this.privateNBT.setTag(key, new NBTTagCompound());
		}
		return new SheepNBT(privateNBT.getCompoundTag(key));
	}
	public SheepNBT setTag(String key , Object value) {
		if (value instanceof NBTTagCompound) {
			this.privateNBT.setTag(key,(NBTBase)((NBTTagCompound)value));
		} else if (value instanceof String) {
			this.privateNBT.setTag(key,(NBTBase)new NBTTagString((String)value));
		} else if (value instanceof Double) {
			this.privateNBT.setTag(key,(NBTBase)new NBTTagDouble(((Double)value).doubleValue()));
		} else if (value instanceof Float) {
			this.privateNBT.setTag(key,(NBTBase)new NBTTagFloat(((Float)value).floatValue()));
		} else if (value instanceof Integer) {
			this.privateNBT.setTag(key,(NBTBase)new NBTTagInt(((Integer)value).intValue()));
		} else if (value instanceof int[]) {
			this.privateNBT.setTag(key,(NBTBase)new NBTTagIntArray((int[])value));
		}
		return this;
	}
	//*链式调用
	public SheepNBT setList(String key, Object[] value) {
	    NBTTagList list = new NBTTagList();
	    for (Object nbt : value) {
	    	if (nbt instanceof NBTTagCompound) {
	    		list.appendTag((NBTBase)((NBTTagCompound)nbt));
	    	} else if (nbt instanceof String) {
	    		list.appendTag((NBTBase)new NBTTagString((String)nbt));
	    	} else if (nbt instanceof Double) {
	    		list.appendTag((NBTBase)new NBTTagDouble(((Double)nbt).doubleValue()));
	    	} else if (nbt instanceof Float) {
	    		list.appendTag((NBTBase)new NBTTagFloat(((Float)nbt).floatValue()));
	    	} else if (nbt instanceof Integer) {
	    		list.appendTag((NBTBase)new NBTTagInt(((Integer)nbt).intValue()));
	    	} else if (nbt instanceof int[]) {
	    		list.appendTag((NBTBase)new NBTTagIntArray((int[])nbt));
	    	} 
	    }
	    this.privateNBT.setTag(key, (NBTBase)list);
	    return this;
	}
	public boolean getBoolean(String key) {
		return this.privateNBT.getBoolean(key);
	}
	public byte getByte(String key) {
		return this.privateNBT.getByte(key);
	}
	public int getInteger(String key) {
		return this.privateNBT.getInteger(key);
	}
	public long getLong(String key) {
		return this.privateNBT.getLong(key);
	}
	public float getFloat(String key) {
		return this.privateNBT.getFloat(key);
	}
	public double getDouble(String key) {
		return this.privateNBT.getDouble(key);
	}
	public String getString(String key) {
		return this.privateNBT.getString(key);
	}
	public NBTTagCompound getMCNBT() {
		return this.privateNBT;
	}
	public boolean equals(Object obj) {
		if(obj instanceof NBTTagCompound)
			return this.privateNBT.equals(obj);
		if(obj instanceof SheepNBT)
			return this.privateNBT.equals(((SheepNBT) obj).privateNBT);
		return false;
	}
	
}
