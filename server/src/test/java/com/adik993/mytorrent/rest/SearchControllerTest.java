package com.adik993.mytorrent.rest;

import com.adik993.mytorrent.model.Search;
import com.adik993.mytorrent.model.SearchResult;
import com.adik993.mytorrent.services.SearchService;
import com.adik993.mytorrent.services.SelectService;
import com.adik993.tpbclient.exceptions.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SearchControllerTest {
    @MockBean
    private SearchService searchService;
    @MockBean
    private SelectService selectService;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws IOException, ParseException {
        given(searchService.search(any(), any()))
                .willReturn(Collections.singletonList(SearchResult.builder().
                        magnetLink("aaa")
                        .search(new Search())
                        .build())
                );
    }

    @Test
    public void search() throws Exception {
        mockMvc.perform(get("/api/search?query=zzz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", not(hasKey("search"))))
                .andExpect(jsonPath("$[0].magnetLink", is("aaa")));
    }

    @Test
    public void searchNoQueryBadRequest() throws Exception {
        mockMvc.perform(get("/api/search"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void selecValidRequestShouldReturnOk() throws Exception {
        doNothing().when(selectService).select(0L, true);
        mockMvc.perform(post("/api/result/select?id=0&selected=true"))
                .andExpect(status().isOk());
    }

    @Test
    public void selectNonExistingEntityShouldReturnOk() throws Exception {
        doThrow(new EntityNotFoundException()).when(selectService).select(0L, true);
        mockMvc.perform(post("/api/result/select?id=0&selected=true"))
                .andExpect(status().isNotFound());
    }

}