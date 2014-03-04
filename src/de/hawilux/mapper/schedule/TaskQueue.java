package de.hawilux.mapper.schedule;

import java.util.concurrent.LinkedBlockingQueue;

public class TaskQueue {
	final LinkedBlockingQueue<Runnable> tasks;

	public TaskQueue() {
		tasks = new LinkedBlockingQueue<Runnable>();
	}
	
	public void queueTask(Runnable task_){
//		PApplet.println("New Task");
		tasks.offer(task_);
	}
	
	public void execute() {
        while (!tasks.isEmpty()) {
            Runnable t = tasks.poll();
            if (t != null) {
                t.run();
            }
        }
	}

}
