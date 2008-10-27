package org.yass.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "YASS_USER")
@NamedQuery(name = "getUserById", query = "SELECT u FROM User u where u.id = ?1")
public class User implements Serializable {

	@Id
	private int id;
	@Column(name = "USER_NAME")
	private String userName;
	private String password;
	@Column(name = "ROLE_ID")
	private int roleId;
	@OneToMany(mappedBy = "user")
	private Set<UserBrowsingContext> browsingContext;
	@OneToOne(mappedBy = "user")
	private UserSetting userSetting;
	private static final long serialVersionUID = 1L;
	@OneToOne(mappedBy = "user")
	private Library library;

	public User() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(final int roleId) {
		this.roleId = roleId;
	}

	public Set<UserBrowsingContext> getBrowsingContext() {
		return browsingContext;
	}

	public void setBrowsingContext(final Set<UserBrowsingContext> browsingContext) {
		this.browsingContext = browsingContext;
	}

	public UserSetting getUserSetting() {
		return userSetting;
	}

	public void setUserSetting(final UserSetting userSetting) {
		this.userSetting = userSetting;
		userSetting.setUser(this);
	}

	/**
	 * @param library
	 *          the library to set
	 */
	public void setLibrary(final Library library) {
		this.library = library;
		library.setUser(this);
	}

	/**
	 * @return the library
	 */
	public Library getLibrary() {
		return library;
	}
}
