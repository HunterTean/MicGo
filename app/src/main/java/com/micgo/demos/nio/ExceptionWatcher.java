package com.micgo.demos.nio;

import com.micgo.utils.KTVUtility;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by liuhongtian on 17/5/8.
 */

public class ExceptionWatcher {

    private ExceptionWatcher() {}

    public static ExceptionWatcher getInstance() {
        return LAZY_HOLDER.INST;
    }

    private static class LAZY_HOLDER {
        private static final ExceptionWatcher INST;

        static {
            INST = new ExceptionWatcher();
        }
    }

    public String check(String key) {
        return "";
    }

    public void register(String key, String content) {
        RandomAccessFile aFile = null;
        try{
            aFile = new RandomAccessFile(KTVUtility.getMGFileDir().getAbsolutePath()+ File.separator+key,"rw");
            FileChannel fileChannel = aFile.getChannel();
            ByteBuffer buf = ByteBuffer.wrap(content.getBytes());
            buf.put(content.getBytes());
            buf.flip();
            fileChannel.write(buf);
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(aFile != null){
                    aFile.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void unregister(String key) {

    }

}
