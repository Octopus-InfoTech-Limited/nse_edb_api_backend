package com.octopus_tech.goc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.octopus_tech.share.model.DBModel;
import com.octopus_tech.share.util.chrono.Epoch;

@Entity
@Table(name = "announcement")
public class Announcement implements DBModel
{
	private static final long serialVersionUID = -3630434641079655260L;


	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	private String title;
	private String content;
	
	@Column(name = "start_timestamp")
	private Epoch start;
	
	@Column(name = "end_timestamp")
	private Epoch end;

	
	@Column(name = "lastmod_timestamp")
	private Epoch lastmod;
	
	private int deleted;
	
	@Override
	public int getId() 
	{
		return 0;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Epoch getStart() {
		return start;
	}

	public void setStart(Epoch start) {
		this.start = start;
	}

	public Epoch getEnd() {
		return end;
	}

	public void setEnd(Epoch end) {
		this.end = end;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Epoch getLastmod() {
		return lastmod;
	}

	public void setLastmod(Epoch lastmod) {
		this.lastmod = lastmod;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	
	
}
