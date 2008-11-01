/*
 Copyright (c) 2008 Sveimport java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
h, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is furnished
 to do so, subject to the following conditions: The above copyright notice
 and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS",
 WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.yass.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "YASS_USER")
@NamedQuery(name = "getUserById", query = "SELECT u FROM User u where u.id = ?1")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<UserBrowsingContext> browsingContext;
	@Id
	private int id;
	@OneToOne(mappedBy = "user")
	private Library library;
	private String password;
	@Column(name = "ROLE_ID")
	private int roleId;
	@ManyToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@MapKey(name = "trackId")
	private Map<Integer, TrackStat> tracksStats = new LinkedHashMap<Integer, TrackStat>();
	@Column(name = "USER_NAME")
	private String userName;
	@OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private UserSetting userSetting;

	public User() {
		super();
	}

	public Set<UserBrowsingContext> getBrowsingContext() {
		return browsingContext;
	}

	public int getId() {
		return id;
	}

	/**
	 * @return the library
	 */
	public Library getLibrary() {
		return library;
	}

	public String getPassword() {
		return password;
	}

	public int getRoleId() {
		return roleId;
	}

	/**
	 * @return the tracksStats
	 */
	public final Map<Integer, TrackStat> getTracksStats() {
		return tracksStats;
	}

	public String getUserName() {
		return userName;
	}

	public UserSetting getUserSetting() {
		return userSetting;
	}

	public void setBrowsingContext(final Set<UserBrowsingContext> browsingContext) {
		this.browsingContext = browsingContext;
	}

	/**
	 * @param library
	 *          the library to set
	 */
	public void setLibrary(final Library library) {
		this.library = library;
		library.setUser(this);
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setRoleId(final int roleId) {
		this.roleId = roleId;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public void setUserSetting(final UserSetting userSetting) {
		this.userSetting = userSetting;
		userSetting.setUser(this);
	}
}
