package exercise.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.io.IOException;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    // BEGIN
    @Test
    public void testShowTask() throws Exception {
        Task task = createTestTask();
        taskRepository.save(task);
        var request =mockMvc.perform(get("/tasks/"+task.getId()))
                .andExpect(status().isOk())
                .andReturn();
        var body = request.getResponse().getContentAsString();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id", task.getId());
        jsonObject.put("title", task.getTitle());
        jsonObject.put("description", task.getDescription());
        jsonObject.put("createdAt", task.getCreatedAt().toString());
        jsonObject.put("updatedAt", task.getUpdatedAt().toString());
        String expected = jsonObject.toString();
        assertThatJson(body).isEqualTo(expected);
    }

    private static Task createTestTask() {
        Task task=Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .ignore(Select.field(Task::getCreatedAt))
                .ignore(Select.field(Task::getUpdatedAt))
                .create();
        return task;
    }

    //Создание новой задачи
    @Test
    public void testCreate() throws Exception {
        int sizeBefore = taskRepository.findAll().size();
        Task task = createTestTask();
        String content = om.writeValueAsString(task);
        var request = post("/tasks", task)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isCreated());
        assertThat(taskRepository.findAll()).hasSize(sizeBefore+1);
        var actualTask = taskRepository.findByTitle(task.getTitle()).get();
        assertThat(actualTask.getCreatedAt()).isNotNull();
        assertThat(actualTask.getUpdatedAt()).isNotNull();
    }

    @Test
    public void testUpdate() throws Exception {
        Task task = createTestTask();
        task=taskRepository.save(task);
        task.setTitle("Updated Title");
        task.setDescription("Updated Description");

        String content = om.writeValueAsString(task);
        var request = put("/tasks/"+task.getId(), task)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk());

        var actualTask = taskRepository.findById(task.getId()).get();
        assertThat(actualTask.getTitle()).isEqualTo(task.getTitle());
        assertThat(actualTask.getDescription()).isEqualTo(task.getDescription());
        assertThat(actualTask.getCreatedAt()).isNotNull();
        assertThat(actualTask.getUpdatedAt()).isNotNull();
    }

    @Test
    public void testDelete() throws Exception {
        Task task = createTestTask();
        task=taskRepository.save(task);

        var request = delete("/tasks/"+task.getId());
        mockMvc.perform(request)
                .andExpect(status().isOk());

        var actualTask = taskRepository.findById(task.getId());
        assertThat(actualTask).isEmpty();
    }
    // END
}
