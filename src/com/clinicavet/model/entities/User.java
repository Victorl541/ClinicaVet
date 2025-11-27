package com.clinicavet.model.entities;

import java.util.Optional;

public class User {
	  
	private int id;
	private String name;
	private String password;
	private String eMail;
	private Rol rol;
	private boolean activo;
	
	public User() {}
	
	public User(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public User(String name, String eMail, String password) {
		this.name = name;
		this.eMail = eMail;
		this.password = password;
	}
	
	public User(String name, String eMail, String password, Rol rol) {
		this.name = name;
		this.eMail = eMail;
		this.password = password;
		this.rol = rol;
		this.activo = true;
	}
	
	public Rol getRol() {
		return rol;
	}
	
	public boolean isActivo() {
        return activo;
    }
	
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
        return password;
    }

	public String getEmail() {
		return eMail;
	}

	public void setEmail(String email) {
		this.eMail = email;
	}

	public int getId() {
	    return id;
	}
	
	public void setId(int id) {
	    this.id = id;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRol(Optional<Rol> rol) {
		this.rol = rol.orElse(null);
		
	}

	@Override
	public String toString() {
		return "Usuario con: id =" + id + ", nombre =" + name + ", e-mail = " + eMail + ", rol = " + rol + ", activo = " + activo;
	}
	
				 
	
}
