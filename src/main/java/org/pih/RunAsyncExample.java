package org.pih;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pih.dto.Employee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class RunAsyncExample {

	public Void saveEmployees(File jsonFile) throws InterruptedException, ExecutionException {
		ObjectMapper objectMapper = new ObjectMapper();
		CompletableFuture<Void> runAsyncFuture = CompletableFuture.runAsync(new Runnable() {
			
			@Override
			public void run() {
				try {

					List<
							Employee> employees = objectMapper.readValue(jsonFile, new TypeReference<List<Employee>>() {
					});
					// write logic to save list of employee to database
					// repository.saveAll();
					System.out.println("Global Thread ::"+ Thread.currentThread().getName());
					employees.stream().forEach(System.out::println);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		return runAsyncFuture.get();
	}


	public Void saveEmployeesWithCustomExecutor(File jsonFile) throws InterruptedException, ExecutionException {
		ObjectMapper objectMapper = new ObjectMapper();
		Executor executor = Executors.newFixedThreadPool(5);
		CompletableFuture<Void> runAsyncFuture = CompletableFuture.runAsync(() -> {
				try {
					List<Employee> employees = objectMapper.readValue(jsonFile, new TypeReference<List<Employee>>() {
					});
					// write logic to save list of employee to database
					// repository.saveAll();
					System.out.println("Custom Thread ::"+ Thread.currentThread().getName());
					employees.stream().forEach(System.out::println);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}, executor);
		return runAsyncFuture.get();
	}
	
//	public static void main(String[] args) throws FileNotFoundException, InterruptedException, ExecutionException {
//		RunAsyncExample runAsyncExample = new RunAsyncExample();
//		runAsyncExample.saveEmployees(new File("employee.json"));
//		runAsyncExample.saveEmployeesWithCustomExecutor(new File("employee.json"));
//	}
}
