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
package it.tnnfnc.apps.application;

import java.util.TimerTask;

/**
 * @author  Franco Toninato
 */

/**
 * A periodic task.
 */

public abstract class PeriodicTask extends TimerTask {
	protected TaskScheduler owner;
	protected boolean running = true;
	protected long duration;
	protected long now = -1L;
	protected long left;


	/**
	 * Perfoms a periodic task.
	 */
	public abstract void performTask();

	/**
	 * Perfoms an ending action after the last task.
	 */
	public abstract void performLast();

	@Override
	public void run() {
		if (now == -1L)
			now = System.currentTimeMillis();

		long elapsed = System.currentTimeMillis() - now;

		if (elapsed >= duration) {

			if (running)
				performLast();
			running = false;
			this.cancel();
		} else {
			left = (duration - elapsed);
			performTask();
		}

	}

	/**
	 * @param d
	 */
	public void setDuration(long d) {
		this.duration = d;
	}

	public void setOwner(TaskScheduler owner) {
		this.owner = owner;
	}

	/**
	 * @return
	 */
	public TaskScheduler getOwner() {
		return this.owner;
	}

	/**
	 * @return
	 */
	public boolean isRunning() {
		return this.running;
	}

}