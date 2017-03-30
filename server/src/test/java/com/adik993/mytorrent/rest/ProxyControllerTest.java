package com.adik993.mytorrent.rest;

import com.adik993.mytorrent.services.ProxyService;
import com.adik993.tpbclient.proxy.model.Proxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProxyControllerTest {
    private static final List<Proxy> proxies = Collections.singletonList(
            Proxy.builder().id(0).country("PL").domain("aaa.com").build());
    @MockBean
    private ProxyService service;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        given(service.getProxyList())
                .willReturn(proxies);
    }

    @Test
    public void getProxies() throws Exception {
        mockMvc.perform(get("/api/proxy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(0)))
                .andExpect(jsonPath("$[0].country", is("PL")))
                .andExpect(jsonPath("$[0].domain", is("aaa.com")));
    }

}