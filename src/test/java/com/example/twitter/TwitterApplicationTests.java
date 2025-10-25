package com.example.twitter;

import com.example.twitter.model.PostRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

class TwitterApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Test para el endpoint POST /twitter/createPost.
	 * Verifica que se pueda crear un post y que los datos sean correctos en la respuesta.
	 */
	@Test
	void testCreatePost() throws Exception {
		PostRequest postRequest = new PostRequest();
		postRequest.setUsername("usuarioTest");
		postRequest.setText("Hola mundo!");

		mockMvc.perform(post("/twitter/createPost")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(postRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.text").value("Hola mundo!"))
				.andExpect(jsonPath("$.user.username").value("usuarioTest"));
	}

	/**
	 * Test para el endpoint GET /twitter/getPosts.
	 * Verifica que se puedan recuperar los posts creados correctamente.
	 */
	@Test
	void testGetPosts() throws Exception {
		// Creamos un post primero para que no esté vacío
		PostRequest postRequest = new PostRequest();
		postRequest.setUsername("user2");
		postRequest.setText("Otro post");

		mockMvc.perform(post("/twitter/createPost")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(postRequest)))
				.andExpect(status().isOk());

		// Ahora obtenemos los posts
		mockMvc.perform(get("/twitter/getPosts"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].text").value("Otro post"));
	}

	@Test
	void contextLoads() {
	}

}
