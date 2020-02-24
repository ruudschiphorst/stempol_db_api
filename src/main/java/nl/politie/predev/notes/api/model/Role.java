package nl.politie.predev.notes.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ROLES")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(insertable = false, updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "rolename")
	private String roleName;
	
	@Column(name = "may_create")
	private boolean mayCreate;
	
	@Column(name = "may_read")
	private boolean mayRead;
	
	@Column(name = "may_update")
	private boolean mayUpdate;
	
	@Column(name = "may_delete")
	private boolean mayDelete;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public boolean isMayCreate() {
		return mayCreate;
	}

	public void setMayCreate(boolean mayCreate) {
		this.mayCreate = mayCreate;
	}

	public boolean isMayRead() {
		return mayRead;
	}

	public void setMayRead(boolean mayRead) {
		this.mayRead = mayRead;
	}

	public boolean isMayUpdate() {
		return mayUpdate;
	}

	public void setMayUpdate(boolean mayUpdate) {
		this.mayUpdate = mayUpdate;
	}

	public boolean isMayDelete() {
		return mayDelete;
	}

	public void setMayDelete(boolean mayDelete) {
		this.mayDelete = mayDelete;
	}
	
	
	
}
