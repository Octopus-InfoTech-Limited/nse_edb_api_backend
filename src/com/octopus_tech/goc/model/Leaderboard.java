package com.octopus_tech.goc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.octopus_tech.share.model.DBModel;
import com.octopus_tech.share.util.chrono.Epoch;

@Entity
@Table(name = "leaderboard")
public class Leaderboard implements DBModel
{
	private static final long serialVersionUID = 1142880670805504621L;
	
	public static final String TYPE_PERSONAL_SCORE = "PERSONAL SCORE";
	public static final String TYPE_SCHOOL_SCORE = "SCHOOL SCORE";
	
	public static final String[] TYPE_ALL = new String[]
	{
			TYPE_PERSONAL_SCORE, TYPE_SCHOOL_SCORE
	};
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String type;
	
	private double score;
	
	private Epoch timestamp;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "game_id")
	private Game game;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "school_id")
	private School school;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = true)
	private User user;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Epoch getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Epoch timestamp) {
		this.timestamp = timestamp;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
