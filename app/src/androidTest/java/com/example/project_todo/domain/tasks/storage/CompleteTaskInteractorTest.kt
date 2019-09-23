package com.example.project_todo.domain.tasks.storage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.project_todo.core.TaskDataCore
import com.example.project_todo.currentValue
import com.example.project_todo.entity.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CompleteTaskInteractorTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var taskDataCore: TaskDataCore

//    @Mock
    private lateinit var taskMock: Task

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        taskMock = Task()
    }

    @Test
    fun testSuccessCompleted() {
        runBlocking {
            CompleteTaskInteractor(taskMock.apply { isCompleted = false },
                true, 0, taskDataCore).test()
                .currentValue {resource ->
                    assertTrue(resource is TaskCompleted)
                    resource.inspectFor<TaskCompleted> {
                        assertTrue(it.successData == 0)
                        assertTrue(it.task.isCompleted)
                    }
                }
        }
    }

    @Test
    fun testSuccessUndoComplete() {
        runBlocking {
            CompleteTaskInteractor(taskMock.apply { isCompleted = true },
                false, 0, taskDataCore).test()
                .currentValue {resource ->
                    assertTrue(resource is TaskUndoCompleted)
                    resource.inspectFor<TaskCompleted> {
                        assertTrue(it.successData == 0)
                        assertTrue(!it.task.isCompleted)
                    }
                }
        }
    }

    @Test
    fun testErrorCompleteInteractor() {
        runBlocking {
            Mockito.`when`(taskDataCore.updateTask(taskMock))
                .thenThrow(IllegalThreadStateException::class.java)

            CompleteTaskInteractor(taskMock.apply { isCompleted = false },
                true, 0, taskDataCore).test()
                .currentValue {resource ->
                    assertTrue(resource is ErrorCompleted)
                    resource.inspectFor<ErrorCompleted> {
                        assertTrue(it.updatePosition == 0)
                        assertTrue(!taskMock.isCompleted)
                    }
                }
        }
    }


}