package com.clinicavet.model.repositories;

import com.clinicavet.model.entities.Rol;
import com.clinicavet.model.entities.User;
import com.clinicavet.util.JsonHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IUserRepository{
	
	private static final String FILE_NAME = "users.json";
	private final List<User> users = new ArrayList<>();
	private int nextId = 1;
	private IRolRepository rolRepository;

	public UserRepository() {}
	
	public void setRolRepository(IRolRepository rolRepository) {
		this.rolRepository = rolRepository;
	}

	@Override
	public void addUser(User user) {
		user.setId(nextId++);
		users.add(user);
	}

	@Override
	public Optional<User> findById(int id) {
		return users.stream().filter(user -> user.getId() == id).findFirst();
	}

	@Override
	public Optional<User> findByName(String name) {
		return users.stream().filter(user -> user.getName().equals(name)).findFirst();
	}
	
	@Override
	public Optional<User> findByEmail(String email) {
	    return users.stream()
	                .filter(user -> user.getEmail().equals(email))
	                .findFirst();
	}

	@Override
	public List<User> findAll() {
		return users;
	}
	
	@Override
	public void updateUser(User user) {
		for (int i = 0; i < users.size(); i++) {
	        if (users.get(i).getId() == user.getId()) {
	            users.set(i, user); 
	            return;
	        }
	    }
	    throw new IllegalArgumentException("Usuario no encontrado con id: " + user.getId());
	}
	
	/**
	 * Guarda los usuarios en archivo JSON.
	 */
	public void save() {
		try {
			StringBuilder json = new StringBuilder("{\n  \"nextId\":" + nextId + ",\n  \"users\":[\n");
			for (int i = 0; i < users.size(); i++) {
				User u = users.get(i);
				json.append("    {\"id\":").append(u.getId())
					.append(",\"name\":").append(JsonHelper.escapeJson(u.getName()))
					.append(",\"email\":").append(JsonHelper.escapeJson(u.getEmail()))
					.append(",\"password\":").append(JsonHelper.escapeJson(u.getPassword()))
					.append(",\"activo\":").append(u.isActivo())
					.append(",\"rolId\":").append(u.getRol() != null ? u.getRol().getId() : 0)
					.append("}");
				if (i < users.size() - 1) json.append(",");
				json.append("\n");
			}
			json.append("  ]\n}");
			JsonHelper.writeJsonFile(FILE_NAME, json.toString());
		} catch (IOException e) {
			System.err.println("Error al guardar usuarios: " + e.getMessage());
		}
	}
	
	/**
	 * Carga los usuarios desde archivo JSON.
	 */
	public void load() {
		try {
			String content = JsonHelper.readJsonFile(FILE_NAME);
			if (content == null || rolRepository == null) return;
			
			users.clear();
			
			// Extraer nextId
			int nextIdStart = content.indexOf("\"nextId\":");
			if (nextIdStart != -1) {
				int valueStart = nextIdStart + "\"nextId\":".length();
				int valueEnd = content.indexOf(",", valueStart);
				nextId = Integer.parseInt(content.substring(valueStart, valueEnd).trim());
			}
			
			// Extraer array de usuarios
			int usersStart = content.indexOf("\"users\":[");
			int usersEnd = content.lastIndexOf("]");
			if (usersStart == -1 || usersEnd == -1) return;
			
			String usersContent = content.substring(usersStart + "\"users\":[".length(), usersEnd);
			String[] items = usersContent.split("\\},");
			
			for (String item : items) {
				item = item.trim();
				if (item.isEmpty()) continue;
				if (!item.endsWith("}")) item += "}";
				
				int id = extractInt(item, "id");
				String name = extractString(item, "name");
				String email = extractString(item, "email");
				String password = extractString(item, "password");
				boolean activo = extractBoolean(item, "activo");
				int rolId = extractInt(item, "rolId");
				
				Optional<Rol> rol = rolRepository.findById(rolId);
				if (rol.isPresent()) {
					User user = new User(name, email, password, rol.get());
					user.setId(id);
					user.setActivo(activo);
					users.add(user);
				}
			}
		} catch (IOException e) {
			System.err.println("Error al cargar usuarios: " + e.getMessage());
		}
	}
	
	private int extractInt(String json, String key) {
		String pattern = "\"" + key + "\":";
		int start = json.indexOf(pattern);
		if (start == -1) return 0;
		start += pattern.length();
		int end = json.indexOf(",", start);
		if (end == -1) end = json.indexOf("}", start);
		return Integer.parseInt(json.substring(start, end).trim());
	}
	
	private boolean extractBoolean(String json, String key) {
		String pattern = "\"" + key + "\":";
		int start = json.indexOf(pattern);
		if (start == -1) return false;
		start += pattern.length();
		int end = json.indexOf(",", start);
		if (end == -1) end = json.indexOf("}", start);
		return Boolean.parseBoolean(json.substring(start, end).trim());
	}
	
	private String extractString(String json, String key) {
		String pattern = "\"" + key + "\":";
		int start = json.indexOf(pattern);
		if (start == -1) return null;
		start = json.indexOf("\"", start + pattern.length()) + 1;
		int end = json.indexOf("\"", start);
		return JsonHelper.unescapeJson(json.substring(start - 1, end + 1));
	}

}
