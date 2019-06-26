package com.zxh.mybatis.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
// 配置事务的回滚,对数据库的增删改都会回滚,便于测试用例的循环利用
@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = true)
@Transactional
public class OrderControllerTest extends AbstractContextController {
    //web test
    private MockMvc mockMvc;

    @Before
    public void setUp(){
        this.mockMvc = webAppContextSetup(this.applicationContext).alwaysExpect(status().isOk()).alwaysDo(print()).build();
    }

    @Test
    public void order() throws Exception {
        this.mockMvc.perform(get("/orders/buy")).andExpect(view().name("buy"));

    }
}
