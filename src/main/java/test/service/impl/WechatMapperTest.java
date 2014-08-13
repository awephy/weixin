/**
 * *****************************************************
 * Copyright (C) Dayan techology Co.ltd - All Rights Reserved
 *
 * This file is part of Dayan techology Co.ltd property.
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * *****************************************************
 */

package test.service.impl;

import mapper.WechatMapper;
import model.AccessToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zhanghj
 */

public class WechatMapperTest {

    @Autowired
    WechatMapper mapper;
    public static ApplicationContext context;

    @Before
    public void setUp() {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        mapper = context.getBean(WechatMapper.class);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test() {
        AccessToken s = mapper.getAccessToken();
        System.out.println(s.getToken());
        s.setToken("1");
        mapper.updateAccessToken(s);
    }

}
