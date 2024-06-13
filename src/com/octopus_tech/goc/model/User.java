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

import com.octopus_tech.share.annotation.NoJson;
import com.octopus_tech.share.model.DBModel;
import com.octopus_tech.share.util.chrono.Epoch;

@Entity
@Table(name = "user")
public class User implements DBModel 
{
	private static final long serialVersionUID = -4978843142364186234L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "login_id")
	private String loginId;
	@Column(name = "edconnect_profile_id")
	private Integer edConnectProfileId;
	
	@NoJson
	@Column(name = "password")
	private String password;
	@Column(name = "name_zh")
	private String nameZh;
	@Column(name = "name_en")
	private String nameEn;

	@Column(name = "avatar_index")
	private int avatarIndex;
	
	@Column(name = "avatar_image_url")
	private String avatarImageUrl;

	@Column(name = "register_timestamp")
	private Epoch register;
	
	@Column(name = "lastlogin_timestamp")
	private Epoch lastLogin;
	
	@Column(name = "level")
	private String level;
	
	@Column(name = "class_name")
	private String className;
	
	@Column(name = "class_no")
	private Integer classNo;
	
	@Column(name = "roles")
	private String roles;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "school_id", nullable = false)
	private School school;
	
	@Column(name = "school_level")
	private int schoolLevel;
	
	@Column(name = "admin_level")
	private int adminLevel;
	
	private int deleted;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNameZh() {
		return nameZh;
	}

	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public Epoch getRegister() {
		return register;
	}

	public void setRegister(Epoch register) {
		this.register = register;
	}

	public Epoch getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Epoch lastLogin) {
		this.lastLogin = lastLogin;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}
	
	
	
	public int getAvatarIndex() {
		return avatarIndex;
	}

	public void setAvatarIndex(int avatarIndex) {
		this.avatarIndex = avatarIndex;
	}

	public String getAvatarImageUrl() {
		return avatarImageUrl;
	}

	public void setAvatarImageUrl(String avatarImageUrl) {
		this.avatarImageUrl = avatarImageUrl;
	}
	
	

	public int getAdminLevel() {
		return adminLevel;
	}

	public void setAdminLevel(int adminLevel) {
		this.adminLevel = adminLevel;
	}
	
	

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	

	public Integer getEdConnectProfileId() {
		return edConnectProfileId;
	}

	public void setEdConnectProfileId(Integer edConnectProfileId) {
		this.edConnectProfileId = edConnectProfileId;
	}
	
	public int getSchoolLevel() {
		return schoolLevel;
	}

	public void setSchoolLevel(int schoolLevel) {
		this.schoolLevel = schoolLevel;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getClassNo() {
		return classNo;
	}

	public void setClassNo(Integer classNo) {
		this.classNo = classNo;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	@Override
	public String toString() 
	{
		return loginId;
	}
	
}

