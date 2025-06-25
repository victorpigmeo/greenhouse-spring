package dev.pigmeo.greenhouse.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.pigmeo.greenhouse.BaseIntegrationTest;
import dev.pigmeo.greenhouse.dto.DhtResponse;
import dev.pigmeo.greenhouse.models.Dht;

public class EspControllerTest extends BaseIntegrationTest {
        private MockMvc mockMvc;
        private MongoTemplate mongoTemplate;
        private ObjectMapper objectMapper;

        @Autowired
        public EspControllerTest(MockMvc mockMvc, MongoTemplate mongoTemplate, ObjectMapper objectMapper) {
                this.mockMvc = mockMvc;
                this.mongoTemplate = mongoTemplate;
                this.objectMapper = objectMapper;
        }

        private Dht dhtRead1 = new Dht("1", 25.0, 60.0, 21.0,
                        LocalDateTime.now().minusMinutes(4).toInstant(ZoneOffset.UTC));
        private Dht dhtRead2 = new Dht("2", 24.0, 55.0, 20.0,
                        LocalDateTime.now().minusMinutes(3).toInstant(ZoneOffset.UTC));
        private Dht dhtRead3 = new Dht("3", 24.0, 55.0, 20.0,
                        LocalDateTime.now().minusMinutes(1).toInstant(ZoneOffset.UTC));

        @BeforeEach
        public void setUp() {
                mongoTemplate.dropCollection(Dht.class);
                mongoTemplate.save(dhtRead1);
                mongoTemplate.save(dhtRead2);
                mongoTemplate.save(dhtRead3);
        }

        @Test
        public void testGetDhtReadsByTimestamp() throws Exception {
                String uri = String.format("/api/dht?from=%d&to=%d",
                                LocalDateTime.now().minusMinutes(4).withSecond(0).toInstant(ZoneOffset.UTC)
                                                .toEpochMilli(),
                                LocalDateTime.now().minusMinutes(2).withSecond(0).toInstant(ZoneOffset.UTC)
                                                .toEpochMilli());

                MvcResult result = mockMvc.perform(get(uri)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                List<DhtResponse> dhtResponseList = List
                                .of(objectMapper.readValue(result.getResponse().getContentAsString(),
                                                DhtResponse[].class));

                assertNotNull(dhtResponseList);
                assertThat(dhtResponseList).isNotEmpty();
                assertThat(dhtResponseList).hasSize(2);
        }

        @Test
        public void testGetDhtReadsByTimestamp_shouldReturnZeroReads() throws Exception {
                String uri = String.format("/api/dht?from=%d&to=%d",
                                LocalDateTime.now().minusMinutes(1).toInstant(ZoneOffset.UTC)
                                                .toEpochMilli(),
                                LocalDateTime.now().toInstant(ZoneOffset.UTC)
                                                .toEpochMilli());

                MvcResult result = mockMvc
                                .perform(get(uri).contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                List<DhtResponse> dhtResponseList = List
                                .of(objectMapper.readValue(result.getResponse().getContentAsString(),
                                                DhtResponse[].class));

                assertNotNull(dhtResponseList);
                assertThat(dhtResponseList).isEmpty();
        }
}
