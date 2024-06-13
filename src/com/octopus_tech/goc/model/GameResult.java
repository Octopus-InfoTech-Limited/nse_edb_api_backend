package com.octopus_tech.goc.model;

import java.util.Date;

public class GameResult {
	private int score;
	private Date finsih_date;
	public int getScore() {
		return score;
	}
	public Date getFinsih_date() {
		return finsih_date;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public void setFinsih_date(Date finsih_date) {
		this.finsih_date = finsih_date;
	}
	
}
