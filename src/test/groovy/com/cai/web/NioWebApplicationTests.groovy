package com.cai.web

import com.cai.BaseWebApplication
import com.google.common.io.Files
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.util.FileCopyUtils

import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.channels.ByteChannel
import java.nio.channels.FileChannel
import java.nio.charset.Charset

@RunWith(SpringJUnit4ClassRunner)
class NioWebApplicationTests {


    /**
     * Buffer
     * Channel
     */
    @Test
    void nioTest(){
        ByteBuffer cb = ByteBuffer.allocate(2048)

        RandomAccessFile file = new RandomAccessFile("E://d2f42068.ini", "rw")
        RandomAccessFile copyFile = new RandomAccessFile("E://d2f42068.ini.copy", "rw")


        FileChannel channel = file.getChannel()
        channel.transferTo(0, channel.size(), copyFile.getChannel())
        int bs = channel.read(cb)

        while (bs != -1){
            cb.flip()
            while (cb.hasRemaining()){
                print((char)cb.get())
            }
            cb.clear()
            bs = channel.read(cb);
        }

        println "position:${cb.position()} limit:${cb.limit()} capacity:${cb.capacity()}"
        cb.put(new String("\ntest").getBytes("UTF-8"))
        channel.write(cb)

        file.close()

    }



    @Test
    void scatter(){
        /**
         * scatter 分散
         */
        RandomAccessFile file = new RandomAccessFile("E://d2f42068.ini", "rw")
        FileChannel channel = file.getChannel()


        ByteBuffer b1 = ByteBuffer.allocate(512)
        ByteBuffer b2 = ByteBuffer.allocate(512)
        ByteBuffer[] bs = [b1,b2]

        long flag = channel.read(bs)

        while (flag != -1){
            b1.flip()
            b2.flip()
            println "b1---start"
            while (b1.hasRemaining()){
                print((char)b1.get())
            }

            println "b2---start"
            while (b2.hasRemaining()){
                print((char)b2.get())
            }

            b1.clear()
            b2.clear()

            flag = channel.read(bs)
        }

        file.close()

    }


    @Test
    void gather(){
        RandomAccessFile file = new RandomAccessFile("E://nio-test.txt", "rw")
        FileChannel channel = file.getChannel()

        ByteBuffer b1, b2
        b1 = ByteBuffer.allocate(30)
        b2 = ByteBuffer.allocate(30)

        b1.put(new String("aabbccdd\n").getBytes("UTF-8"))
        b2.put(new String("2:aabbccdd\n").getBytes("UTF-8"))

        b1.flip()
        b2.flip()

        ByteBuffer[] bs = [b1,b2]
        channel.write(bs)

        file.close()
    }

}
