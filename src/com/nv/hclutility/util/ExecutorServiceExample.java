package com.nv.hclutility.util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceExample {

	public static void main(String[] args) throws InterruptedException, ExecutionException {  
        ExecutorService executorService = Executors.newCachedThreadPool(); 
        Set<Callable<String>> callables = new HashSet<Callable<String>>();  
        callables.add(new Callable<String>() {  
            public String call() throws Exception {  
                return "Task 1";  
            }  
        });  
        callables.add(new Callable<String>() {  
            public String call() throws Exception {  
                return "Task 2";  
            }  
        });  
        callables.add(new Callable<String>() {  
            public String call() throws Exception {  
                return "Task 3";  
            }  
        });  
  
        String result = executorService.invokeAny(callables);  
  
        System.out.println("result = " + result);  
        
        java.util.List<Future<String>> futures = executorService.invokeAll(callables);  
        
        for(Future<String> future : futures){  
            System.out.println("future.get = " + future.get());  
        }  
  
        executorService.shutdown();  
    }  
}  