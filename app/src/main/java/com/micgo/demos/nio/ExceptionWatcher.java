package com.micgo.demos.nio;

import com.micgo.utils.KTVUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
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
        FileInputStream fis = null;
        FileChannel channel=null;
        String content = "";
        try {
            fis = new FileInputStream(KTVUtility.getMGFile(key));
            channel=fis.getChannel();
            //文件内容的大小
            int size=(int) channel.size();

            //第二步 指定缓冲区
            ByteBuffer buffer=ByteBuffer.allocate(1024);
            //第三步 将通道中的数据读取到缓冲区中
            channel.read(buffer);

            Buffer bf= buffer.flip();
            System.out.println("limt:"+bf.limit());

            byte[] bt=buffer.array();
            content = new String(bt,0,size);

            buffer.clear();
            buffer=null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                channel.close();
                fis.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return content;
    }

    public void register(String key, String content) {
        RandomAccessFile aFile = null;
        try{
            aFile = new RandomAccessFile(KTVUtility.getMGFile(key), "rw");
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
        File file = KTVUtility.getMGFile(key);
        if (file.exists()) {
            file.delete();
        }
    }

}
