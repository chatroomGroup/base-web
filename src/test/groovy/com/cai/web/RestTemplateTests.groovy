package com.cai.web

import com.cai.BaseWebApplication
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.web.client.RestTemplate

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = BaseWebApplication)
class RestTemplateTests {

    private RestTemplate restTemplate

    @Before
    void before(){
        restTemplate = new RestTemplate()
    }

    @Test
    void postObject(){
        String url = "https://localhost:9020/rest/user/login"
        String rsp = restTemplate.postForObject(url, [account: 123, password: 234], String.class)
        println rsp
    }
}
