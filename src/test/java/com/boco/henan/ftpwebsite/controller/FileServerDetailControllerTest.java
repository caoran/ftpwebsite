package com.boco.henan.ftpwebsite.controller;

import com.boco.henan.ftpwebsite.FtpwebsiteApplication;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FtpwebsiteApplication.class)
@WebAppConfiguration
public class FileServerDetailControllerTest {


    private MockMvc mvc;

    @Autowired
    private FileServerDetailController fileServerDetailController;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        //mvc = MockMvcBuilders.standaloneSetup(fileServerDetailController).build();
        mvc = MockMvcBuilders.webAppContextSetup(context).build();//建议使用这种
    }

    @Test
    public void addFolder() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/data/tree")
                .param("busiNo", "123")
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        MvcResult result = mvc.perform(request)
                .andReturn();
        int statusCode = result.getResponse().getStatus();
        Assert.assertEquals(statusCode, 200);
        String body = result.getResponse().getContentAsString();
        System.out.println("body:"+body);
    }

    @Test
    public void getFileDetailPage() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/data/list")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("fileName", "新建")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("SUCCESS")));

    }

    @Test
    public void test() throws Exception{
        fileServerDetailController.test();
    }

}