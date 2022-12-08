package br.ce.wcaquino.taskbackend.controller;

import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.repo.TaskRepo;
import br.ce.wcaquino.taskbackend.utils.ValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

public class TaskControllerTest {

    @Mock
    private TaskRepo taskRepository;

    @InjectMocks
    private TaskController taskController;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void deveLancarExceptionQuandoEstiverSemDescricao() {
        Task task = new Task();
        task.setDueDate(LocalDate.now());

        try{
            taskController.save(task);
        } catch (ValidationException e) {
            Assert.assertEquals("Fill the task description" , e.getMessage());
        }

    }


    @Test
    public void deveLancarExceptionQuandoEstiverSemData() {
        Task task = new Task();
        task.setTask("Description tests");

        try{
            taskController.save(task);
        } catch (ValidationException e) {
            Assert.assertEquals("Fill the due date" , e.getMessage());
        }


    }


    @Test
    public void deveLancarExceptionQuandoDataEstiverNoPassado() {
        Task task = new Task();
        task.setTask("Description tests");
        task.setDueDate(LocalDate.of(2010, 10, 15));

        try{
            taskController.save(task);
        } catch (ValidationException e) {
            Assert.assertEquals("Due date must not be in past" , e.getMessage());
        }
    }


    @Test
    public void deveSalvarComSucesso() throws ValidationException {
        Task task = new Task();
        task.setTask("Description one");
        task.setDueDate(LocalDate.now());

        taskController.save(task);
        Mockito.verify(taskRepository).save(task);

    }


}
