package com.adik993.mytorrent.rest;

import com.adik993.mytorrent.providers.TorrentProviderFacade;
import com.adik993.mytorrent.providers.TpbProvider;
import com.adik993.tpbclient.TpbClient;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProvidersControllerTest {
    @MockBean
    private TorrentProviderFacade torrentProviderFacade;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        TpbProvider provider = new TpbProvider(TpbClient.withHost("aaa.com"));
        provider.setId(0L);
        given(torrentProviderFacade.getProviders())
                .willReturn(Collections.singleton(provider));
    }

    @Test
    public void getProviders() throws Exception {
        mockMvc.perform(get("/api/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(0)))
                .andExpect(jsonPath("$[0].name", is("aaa.com")))
                .andExpect(jsonPath("$[0].up", is(true)));
    }
}