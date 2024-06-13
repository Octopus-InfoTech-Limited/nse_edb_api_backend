package com.octopus_tech.share.cron;

import java.io.PrintWriter;

import com.octopus_tech.share.db.DBHelper;

public interface CronTask 
{
	void run(DBHelper dbHelper, PrintWriter logger) throws Exception;
}
