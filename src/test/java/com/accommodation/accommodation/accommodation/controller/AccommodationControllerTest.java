package com.accommodation.accommodation.accommodation.controller;

import com.accommodation.accommodation.domain.accommodation.controller.AccommodationController;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationDetailResponse;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationsResponse;
import com.accommodation.accommodation.domain.accommodation.model.type.Category;
import com.accommodation.accommodation.domain.accommodation.service.AccommodationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccommodationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccommodationService accommodationService;

    @InjectMocks
    private AccommodationController accommodationController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accommodationController).build();
    }

/*    @Test
    public void testFindAll() throws Exception {
        AccommodationsResponse mockResponse = new AccommodationsResponse();
        when(accommodationService.findByCategory(any(Category.class), any(), any(), any())).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/accommodation")
                        .param("category", "HOTEL")
                        .param("cursorId", "1")
                        .param("size", "10")
                        .param("cursorMinPrice", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAccommodation() throws Exception {
        AccommodationDetailResponse mockResponse = new AccommodationDetailResponse();
        when(accommodationService.getAccommodationById(any(Long.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/accommodation/{id}", 1L)
                        .param("checkInDate", "2024-07-10")
                        .param("checkOutDate", "2024-07-15")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }*/
}
