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
@Table(name = "news")
public class News implements DBModel 
{
	private static final long serialVersionUID = 5009007500984763813L;

	public static final String STATUS_SHOW = "SHOW";
	public static final String STATUS_HIDDEN = "HIDDEN";
	public static final String STATUS_DELETED = "DELETED";
	
	public static final String[] STATUS_ALL = new String[]
	{
		STATUS_SHOW,
		STATUS_HIDDEN,
		STATUS_DELETED
	};

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String title;
	private String content;
	private String status;
	
	@Column(name = "release_timestamp")
	private Epoch release;
	
	@Column(name = "add_timestamp")
	private Epoch add;
	
	@Column(name = "lastmod_timestamp")
	private Epoch lastmod;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "school_id")
	private School school;
	
	private String lang;
	
	@Column(name = "`order`")
	private int order;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Epoch getRelease() {
		return release;
	}

	public void setRelease(Epoch release) {
		this.release = release;
	}

	public Epoch getAdd() {
		return add;
	}

	public void setAdd(Epoch add) {
		this.add = add;
	}

	public Epoch getLastmod() {
		return lastmod;
	}

	public void setLastmod(Epoch lastmod) {
		this.lastmod = lastmod;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	
}
