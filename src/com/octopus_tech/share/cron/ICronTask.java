package com.octopus_tech.share.cron;

import java.io.PrintWriter;

import com.octopus_tech.share.db.DBHelper;

public interface ICronTask 
{
	void run(DBHelper dbHelper, PrintWriter logger, CronScheduleOn whichSchedule) throws Exception;
}
