package me.liuweiqiang;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


//REST控制器，处理/demo请求
@RestController
@RequestMapping("/demo")
public class Demo {

    @RequestMapping(method = RequestMethod.GET)
    public Student hellow() {
        Student student = new Student();
        student.setName("Qiang");
        return student; //默认的方式序列化、反序列化
    }
}
