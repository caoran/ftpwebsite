package com.boco.henan.ftpwebsite;

import com.boco.henan.ftpwebsite.entity.Node;
import com.boco.henan.ftpwebsite.util.RedisClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {

    @Autowired
    private RedisClient<Node> redisClient;

    @Test
    public void test() throws Exception {
        redisClient.set("1",new Node("1","测试1","0"));

        Node node=redisClient.get("1");

        node.setName("局数据参数文件");

        redisClient.set(node.getId(),node);

        Assert.assertEquals("局数据参数文件",redisClient.get("1").getName());

    }

    @Test
    public void testObj() throws Exception {
        Node node=new Node("aa@126.com", "aa", "aa123456", true,null,"123");
        redisClient.set("com.neox", node);
        //redisClient.set("com.neo.f", node,1, TimeUnit.SECONDS);
        Thread.sleep(1000);
        //redisTemplate.delete("com.neo.f");
        boolean exists=redisClient.containsKey("com.neo.f");
        if(exists){
            System.out.println("exists is true");
        }else{
            System.out.println("exists is false");
        }
        // Assert.assertEquals("aa", operations.get("com.neo.f").getUserName());
    }
}
