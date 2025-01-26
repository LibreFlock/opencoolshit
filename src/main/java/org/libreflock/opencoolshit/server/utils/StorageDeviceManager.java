package org.libreflock.opencoolshit.server.utils;

import java.io.File;
import java.io.FileNotFoundException;
// import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
// import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

import org.libreflock.opencoolshit.OpenCoolshit;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class StorageDeviceManager {
    public String uuid = "";
    // File file;
    String path;
    public int blksize;
    public int blks;
    // HashMap<Integer, byte[]> cache = new HashMap<Integer, byte[]>(); 

    // RandomAccessFile filehand;

    public StorageDeviceManager(ItemStack stack, String n_uuid, int blockSize, int blocks) {
        CompoundNBT comp = stack.getTag().getCompound("oc:data");

        if (comp.contains("node") && comp.getCompound("node").contains("address")) {
            uuid = comp.getCompound("node").getString("address");
        } else if (n_uuid != null) {
            uuid = n_uuid;
        }

        if (comp.contains("blockSize")) {
            blksize = comp.getInt("blocks");
        } else {
            blksize = blockSize;
        }

        if (comp.contains("blocks")) {
            blks = comp.getInt("blocks");
        } else {
            blks = blocks;
        }

        // if (this.isReady()) {
        //     this.init(stack);
        // }
        // trolling
        this.init(stack);
    }

    // boolean isReady() {
    //     return this.filehand != null;
    // }

    void init(ItemStack stack) {
        CompoundNBT comp = stack.getTag().getCompound("oc:data");

        path = OpenCoolshit.root.toString() + "/ossm_drives/" + uuid + ".bin";

        File file = new File(path);
        file.getParentFile().mkdirs();

        if (!file.exists()) {
            byte[] arr = new byte[blksize*blks];
            Arrays.fill(arr, (byte)0xFF);

            char[] charArray = new String(arr, StandardCharsets.UTF_8).toCharArray();

            try {
                FileWriter writer = new FileWriter(file);
                writer.write(charArray);
                writer.close();
            } catch (IOException e) {
                OpenCoolshit.LOGGER.error("FAILED INIT, StorageDeviceManager");
                e.printStackTrace();
            }
        }

        comp.putInt("blockSize", blksize);
        comp.putInt("blocks", blks);

        stack.getTag().put("oc:data", comp);

        long dif = (blksize*blks)-file.length();
        if (dif >0) {
            byte[] arr = new byte[(int) dif];
            Arrays.fill(arr, (byte)0x0ff);

            CharSequence charArray = new String(arr, StandardCharsets.UTF_8);

            try {
                FileWriter writer = new FileWriter(path);
                writer.append(charArray);
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                OpenCoolshit.LOGGER.error("FAILED INIT 2, StorageDeviceManager");
                e.printStackTrace();
            }
        }
    }

    void rawWriteBlk(int pos, byte[] bytes) {
        // if (cache.containsKey(pos)) {
        //     cache.put(pos, bytes);
        //     return;
        // }

        char[] charArray = new String(bytes, StandardCharsets.UTF_8).toCharArray();
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(charArray, pos*blksize, Math.min(blksize, bytes.length));
            writer.close();
        } catch (IOException e) {
            OpenCoolshit.LOGGER.error("FAILED WRITE, StorageDeviceManager");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void writeBlk(int pos, byte[] bytes) {
        rawWriteBlk(pos, bytes);
    }

    public byte[] readBlk(int pos) {
        // if (cache.containsKey(pos)) {
        //     return cache.get(pos);
        // }

        char[] block = new char[blksize];
        OpenCoolshit.LOGGER.info("READ BLOCK >> {}, {}", block[0], block[1]);

        try {
            FileReader reader = new FileReader(path);
            reader.skip((long)(pos*blksize));
            reader.read(block);
            reader.close();
        } catch (FileNotFoundException e) {
            OpenCoolshit.LOGGER.error("FAILED READ 1, StorageDeviceManager");
            e.printStackTrace();
        } catch (IOException e) {
            OpenCoolshit.LOGGER.error("FAILED READ 2, StorageDeviceManager");
            e.printStackTrace();
        }
        return new String(block).getBytes();
    }

    public void erase(byte fillByte) {
        // cache.clear();
        byte[] arr = new byte[blksize*blks];
        Arrays.fill(arr, fillByte);

        char[] charArray = new String(arr, StandardCharsets.UTF_8).toCharArray();
        try{
            FileWriter writer = new FileWriter(path);
            writer.write(charArray);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        throw new UnknownError("unimplemented flush");
    }
}
