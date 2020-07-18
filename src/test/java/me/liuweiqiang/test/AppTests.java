package me.liuweiqiang.test;

import me.liuweiqiang.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class AppTests {

    @Test
    public void contextLoads() { //简单的上下文加载测试
    }
}
