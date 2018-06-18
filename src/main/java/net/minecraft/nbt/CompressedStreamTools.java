package net.minecraft.nbt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;

public class CompressedStreamTools {

    public static NBTTagCompound readCompressed(InputStream inputstream) throws IOException {
        DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(inputstream)));

        NBTTagCompound nbttagcompound;

        try {
            nbttagcompound = read((DataInput) datainputstream, NBTSizeTracker.INFINITE);
        } finally {
            datainputstream.close();
        }

        return nbttagcompound;
    }

    public static void writeCompressed(NBTTagCompound nbttagcompound, OutputStream outputstream) throws IOException {
        DataOutputStream dataoutputstream = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(outputstream)));

        try {
            write(nbttagcompound, (DataOutput) dataoutputstream);
        } finally {
            dataoutputstream.close();
        }

    }

    public static NBTTagCompound read(DataInputStream datainputstream) throws IOException {
        return read((DataInput) datainputstream, NBTSizeTracker.INFINITE);
    }

    public static NBTTagCompound read(DataInput datainput, NBTSizeTracker nbtreadlimiter) throws IOException {
        // Spigot start
        if ( datainput instanceof io.netty.buffer.ByteBufInputStream )
        {
            datainput = new DataInputStream(new org.spigotmc.LimitStream((InputStream) datainput, nbtreadlimiter));
        }
        // Spigot end
        NBTBase nbtbase = read(datainput, 0, nbtreadlimiter);

        if (nbtbase instanceof NBTTagCompound) {
            return (NBTTagCompound) nbtbase;
        } else {
            throw new IOException("Root tag must be a named compound tag");
        }
    }

    public static void write(NBTTagCompound nbttagcompound, DataOutput dataoutput) throws IOException {
        writeTag((NBTBase) nbttagcompound, dataoutput);
    }

    private static void writeTag(NBTBase nbtbase, DataOutput dataoutput) throws IOException {
        dataoutput.writeByte(nbtbase.getId());
        if (nbtbase.getId() != 0) {
            dataoutput.writeUTF("");
            nbtbase.write(dataoutput);
        }
    }

    private static NBTBase read(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        byte b0 = datainput.readByte();

        if (b0 == 0) {
            return new NBTTagEnd();
        } else {
            datainput.readUTF();
            NBTBase nbtbase = NBTBase.createNewByType(b0);

            try {
                nbtbase.read(datainput, i, nbtreadlimiter);
                return nbtbase;
            } catch (IOException ioexception) {
                CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
                CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("NBT Tag");

                crashreportsystemdetails.addCrashSection("Tag type", (Object) Byte.valueOf(b0));
                throw new ReportedException(crashreport);
            }
        }
    }
}
