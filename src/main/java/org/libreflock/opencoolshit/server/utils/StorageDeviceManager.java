package org.libreflock.opencoolshit.server.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
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
        if (stack.getTag() == null) {
            stack.save(new CompoundNBT());
        }
        CompoundNBT comp = stack.getTag().getCompound("oc:data"); // THIS line is breaking

        if (comp.contains("node") && comp.getCompound("node").contains("address")) {
            uuid = comp.getCompound("node").getString("address");
        } else if (n_uuid != null) {
            uuid = n_uuid;
        }

        if (comp.contains("blockSize")) {
            blksize = comp.getInt("blockSize");
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

        path = OpenCoolshit.root.toString() + "/opencoolshit/ossm_drives/" + uuid + ".bin";

        File file = new File(path);
        file.getParentFile().mkdirs();

        if (!file.exists()) {
            byte[] arr = new byte[blksize*blks];
            Arrays.fill(arr, (byte)0xFF);

            try {
                FileOutputStream writer = new FileOutputStream(file);
                writer.write(arr);
                writer.close();
            } catch (IOException e) {
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

            try {
                FileOutputStream output = new FileOutputStream(path);
                output.write(arr, (int)file.length(), (int)dif);
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void rawWriteBlk(int pos, byte[] bytes) {
        // if (cache.containsKey(pos)) {
        //     cache.put(pos, bytes);
        //     return;
        // }

        // char[] charArray = new String(bytes, StandardCharsets.US_ASCII).toCharArray();
        try {
            // // WHY IS JAVA SUCH A PAIN
            // // I DONT WANT TO DO THIS
            byte[] EVERYTHING = new byte[blksize*blks];
            FileInputStream reader = new FileInputStream(path);
            reader.read(EVERYTHING);
            reader.close();


            for (int i=0;i<Math.min(this.blksize,bytes.length);i++) {
                EVERYTHING[pos+i] = bytes[i];
            }

            FileOutputStream writer = new FileOutputStream(path);
            writer.write(EVERYTHING);
            writer.close();
        } catch (IOException e) {
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

        byte[] block = new byte[blksize];

        try {
            FileInputStream reader = new FileInputStream(path);
            reader.skip((long)(pos*blksize));
            reader.read(block);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return block;
    }

    public void erase(byte fillByte) {
        // cache.clear();
        byte[] arr = new byte[blksize*blks];
        Arrays.fill(arr, fillByte);
        try{
            FileOutputStream writer = new FileOutputStream(path);
            writer.write(arr);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        throw new UnknownError("unimplemented flush");
    }
}
