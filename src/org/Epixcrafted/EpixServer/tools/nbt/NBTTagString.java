package org.Epixcrafted.EpixServer.tools.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.Epixcrafted.EpixServer.tools.Utils;
import org.jboss.netty.buffer.ChannelBuffer;

public class NBTTagString extends NBTBase
{
    /** The string value for the tag (cannot be empty). */
    public String data;

    public NBTTagString(String par1Str)
    {
        super(par1Str);
    }

    public NBTTagString(String par1Str, String par2Str)
    {
        super(par1Str);
        this.data = par2Str;

        if (par2Str == null)
        {
            throw new IllegalArgumentException("Empty string not allowed");
        }
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    ChannelBuffer write(ChannelBuffer par1DataOutput)
    {
        return Utils.writeStringToBuffer(par1DataOutput, this.data);
    }
    
    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     * @throws IOException 
     */
    DataOutput write(DataOutput par1DataOutput) throws IOException
    {
    	par1DataOutput.writeUTF(this.data);
        return par1DataOutput;
    }


    /**
     * Read the actual data contents of the tag, implemented in NBT extension classes
     */
    void load(DataInput par1DataInput) throws IOException
    {
        this.data = par1DataInput.readUTF();
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return (byte)8;
    }

    public String toString()
    {
        return this.data;
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTBase copy()
    {
        return new NBTTagString(this.getName(), this.data);
    }

    public boolean equals(Object par1Obj)
    {
        if (!super.equals(par1Obj))
        {
            return false;
        }
        else
        {
            NBTTagString var2 = (NBTTagString)par1Obj;
            return this.data == null && var2.data == null || this.data != null && this.data.equals(var2.data);
        }
    }

    public int hashCode()
    {
        return super.hashCode() ^ this.data.hashCode();
    }
}
