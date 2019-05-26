/*
 * Copyright (c) 2015, Franco Toninato. All rights reserved.
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE LAW. 
 * EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER 
 * PARTIES PROVIDE THE PROGRAM �AS IS� WITHOUT WARRANTY OF ANY KIND, EITHER 
 * EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE 
 * QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU. SHOULD THE PROGRAM PROVE 
 * DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR CORRECTION.
 */
package it.tnnfnc.apps.application; //Package

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * A clock performs many tasks at regular intervals. A periodic action together
 * with a time interval can be added to the clock. When the maximum repetition
 * times is reached the action is removed. This class relays on the
 * javax.swing.Timer so the periodic task execution in performed on event
 * dispatching thread. Be careful to not perform heavy task because the GUI
 * could be low responsive.
 * 
 * @author Franco Toninato
 */
public class TaskScheduler {

	private Timer timer;
	private Map<String, PeriodicTask> taskMap;

	/**
	 * Class constructor.
	 */
	public TaskScheduler() {
		timer = new Timer();
		taskMap = Collections.synchronizedMap(new HashMap<String, PeriodicTask>());
	}

	/**
	 * Adds a new action to the "to do" content.
	 * 
	 * @param task
	 *            the task to be scheduled.
	 * @param period
	 *            time in milliseconds between successive task executions.
	 * @param duration
	 *            the task will stop to be executed again after this time in
	 *            milliseconds.
	 * @param id
	 *            the task ID.
	 */
	public void addTask(PeriodicTask task, long period, long duration, String id) {
		task.setOwner(this);
		task.setDuration(duration);
		// Task t = new Task();
		// t.task = task;
		// synchronized (taskMap) {
		taskMap.put(id, task);
		timer.schedule(task, 0, period);
		// }
	}

	/**
	 * Adds a new endless task.
	 * 
	 * @param task
	 *            the task to be scheduled.
	 * @param period
	 *            time in milliseconds between successive task executions.
	 * @param duration
	 *            the task will stop to be executed again after this time in
	 *            milliseconds.
	 * @param id
	 *            the task ID.
	 */
	public void addTask(PeriodicTask task, long period, String id) {
		task.setOwner(this);
		task.setDuration(Integer.MAX_VALUE);
		// Task t = new Task();
		// t.task = task;
		// synchronized (taskMap) {
		taskMap.put(id, task);
		timer.schedule(task, 0, period);
		// }
	}

	/**
	 * Stops the all timers of the scheduler. Stops all tasks scheduled
	 * preserving the last task execution before stopping.
	 */
	public void stop() {
		// synchronized (taskMap) {
		for (String s : taskMap.keySet()) {
			PeriodicTask t;
			if (taskMap.get(s) != null && (t = taskMap.get(s)) != null) {
				if (t.isRunning())
					t.performLast();
				t.cancel();
			}
		}
		taskMap.clear();
		// }
		// timer.purge();
	}

	/**
	 * Stop the all timers with a specific ID. Stops the tasks scheduled under
	 * an ID preserving the last task execution before stopping.
	 * 
	 * @param id
	 *            the task group ID.
	 */
	public void stop(String id) {
		PeriodicTask t;
		// synchronized (taskMap) {
		if (taskMap.get(id) != null && (t = taskMap.get(id)) != null) {
			taskMap.remove(id);
			if (t.isRunning())
				t.performLast();
			t.cancel();
			// t.running = false;
		}
		// timer.purge();
		// }
	}

	/**
	 * Return true if there are running tasks with a cetain ID.
	 * 
	 * @param id
	 *            the task group ID.
	 */
	public boolean isRunningCheck(String id) {
		// synchronized (taskMap) {
		return (taskMap.get(id) != null && taskMap.get(id).isRunning());
		// }
	}

	/**
	 * Return true if there are running tasks with a cetain ID.
	 * 
	 * @param ID
	 *            the task group ID.
	 */
	public boolean isRunningCheck() {
		return taskMap.size() > 0;
	}

}
