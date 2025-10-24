package com.taskmanagementsystem;

import com.taskmanagementsystem.dto.TaskDTO;
import com.taskmanagementsystem.model.Task;
import com.taskmanagementsystem.model.TaskStatus;
import com.taskmanagementsystem.repository.TaskRepository;
import com.taskmanagementsystem.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskmanagementsystemApplicationTests {

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskService taskService;

	private Task task;
	private TaskDTO dto;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		task = new Task();
		task.setId(1L);
		task.setTitle("Test Task");
		task.setDescription("Test Description");
		task.setStatus(TaskStatus.NOT_DONE);

		dto = new TaskDTO();
		dto.setId(1L);
		dto.setTitle("Test Task");
		dto.setDescription("Test Description");
		dto.setStatus(TaskStatus.NOT_DONE);
	}

	@Test
	void createTask_ShouldReturnSavedDTO() {
		when(taskRepository.save(any(Task.class))).thenReturn(task);

		TaskDTO result = taskService.createTask(dto);

		assertNotNull(result);
		assertEquals(dto.getTitle(), result.getTitle());
		assertEquals(dto.getDescription(), result.getDescription());
		assertEquals(TaskStatus.NOT_DONE, result.getStatus());
		verify(taskRepository, times(1)).save(any(Task.class));
	}

	// ✅ Get All Tasks
	// @Test
	// void getAllTasks_ShouldReturnPagedDTOs() {
	// 	List<Task> tasks = Arrays.asList(task);
	// 	Page<Task> page = new PageImpl<>(tasks);
	// 	Pageable pageable = PageRequest.of(0, 10);

	// 	when(taskRepository.findAll(pageable)).thenReturn(page);

	// 	Page<TaskDTO> result = taskService.getAllTasks(pageable);

	// 	assertEquals(1, result.getContent().size());
	// 	assertEquals(task.getTitle(), result.getContent().get(0).getTitle());
	// 	verify(taskRepository, times(1)).findAll(pageable);
	// }

	// ✅ Get Task by ID
	@Test
	void getTaskById_ShouldReturnTaskDTO_WhenFound() {
		when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

		TaskDTO result = taskService.getTaskById(1L);

		assertNotNull(result);
		assertEquals(task.getId(), result.getId());
		assertEquals(task.getTitle(), result.getTitle());
		verify(taskRepository, times(1)).findById(1L);
	}

	// ✅ Update Task
	@Test
	void updateTask_ShouldReturnUpdatedTaskDTO() {
		when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
		when(taskRepository.save(any(Task.class))).thenReturn(task);

		dto.setDescription("Updated Description");
		dto.setStatus(TaskStatus.DONE);

		TaskDTO result = taskService.updateTask(1L, dto);

		assertNotNull(result);
		assertEquals("Updated Description", result.getDescription());
		assertEquals(TaskStatus.DONE, result.getStatus());
		verify(taskRepository, times(1)).save(any(Task.class));
	}

	// ✅ Delete Task
	@Test
	void deleteTask_ShouldCallRepositoryDelete() {
		when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

		taskService.deleteTask(1L);

		verify(taskRepository, times(1)).findById(1L);
		verify(taskRepository, times(1)).delete(task);
	}
}
