package com.example.project_todo.domain.tasks.storage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.project_todo.core.TaskDataCore
import com.example.project_todo.currentValue
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.NoTasks
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetAllTasksInteractorTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var taskDataCoreMock: TaskDataCore

    @Mock private lateinit var taskMock: Task

    private lateinit var getAllTasksInteractor: GetAllTasksInteractor

    companion object {
        const val PARENT_COLLECTION_TITLE = "My Tasks"
        const val TASK_TEXT = "Lorem ipsum dolor sit amet"
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        taskMock.apply {
            text = TASK_TEXT
            taskCollectionTitle = PARENT_COLLECTION_TITLE
        }
        getAllTasksInteractor = GetAllTasksInteractor(PARENT_COLLECTION_TITLE, taskDataCoreMock)
    }

    @Test
    fun testSuccess() {
        runBlocking {
            Mockito.`when`(taskDataCoreMock.getTasksByListTitle(taskMock.taskCollectionTitle))
                .thenReturn(mutableListOf(taskMock))

            getAllTasksInteractor.test().apply {
                currentValue { resource ->
                    assertTrue(resource is Resource.Success)
                    resource.inspectFor<Resource.Success<List<Task>>> {
                        assertTrue(it.successData.isNotEmpty())
                        assertTrue(it.successData[0].text == taskMock.text)
                    }
                }
            }
        }
    }

    @Test
    fun testFailure() {
        runBlocking {
            Mockito.`when`(taskDataCoreMock.getTasksByListTitle(PARENT_COLLECTION_TITLE))
                .thenReturn(emptyList())

            getAllTasksInteractor.test().currentValue { resource -> assertTrue(resource is NoTasks) }
        }
    }

    @Test
    fun testError() {
        runBlocking {
            Mockito.`when`(taskDataCoreMock.getTasksByListTitle(PARENT_COLLECTION_TITLE))
                .thenThrow(IllegalThreadStateException::class.java)

            getAllTasksInteractor.test().currentValue { resource -> assertTrue(resource is Error) }
        }
    }
}