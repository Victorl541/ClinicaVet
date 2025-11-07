package com.clinicavet.model.repositories;

import com.clinicavet.model.entities.Rol;
import com.clinicavet.util.JsonHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RolRepository implements IRolRepository{
	
	private static final String FILE_NAME = "roles.json";
	List<Rol> roles = new ArrayList<>();

	@Override
	public void addRol(Rol rol) {
		roles.add(rol);
	}

	@Override
	public Optional<Rol> findById(int id) {
		return roles.stream().filter(rol -> rol.getId() == id).findFirst();
	}

	@Override
	public Optional<Rol> findByName(String name) {
		return roles.stream().filter(rol -> rol.getName().equals(name)).findFirst();
	}

	@Override
	public List<Rol> findAll() {
		return roles;
	}
	
	/**
	 * Guarda los roles en archivo JSON.
	 */
	public void save() {
		try {
			StringBuilder json = new StringBuilder("[\n");
			for (int i = 0; i < roles.size(); i++) {
				Rol r = roles.get(i);
				json.append("  {\"id\":").append(r.getId())
					.append(",\"name\":").append(JsonHelper.escapeJson(r.getName()))
					.append("}");
				if (i < roles.size() - 1) json.append(",");
				json.append("\n");
			}
			json.append("]");
			JsonHelper.writeJsonFile(FILE_NAME, json.toString());
		} catch (IOException e) {
			System.err.println("Error al guardar roles: " + e.getMessage());
		}
	}
	
	/**
	 * Carga los roles desde archivo JSON.
	 */
	public void load() {
		try {
			String content = JsonHelper.readJsonFile(FILE_NAME);
			if (content == null) return;
			
			roles.clear();
			// Parser simple: extraer objetos JSON
			String[] items = content.replace("[", "").replace("]", "").split("\\},");
			for (String item : items) {
				item = item.trim();
				if (item.isEmpty()) continue;
				if (!item.endsWith("}")) item += "}";
				
				// Extraer id y name
				int id = extractInt(item, "id");
				String name = extractString(item, "name");
				
				if (name != null) {
					roles.add(new Rol(id, name));
				}
			}
		} catch (IOException e) {
			System.err.println("Error al cargar roles: " + e.getMessage());
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
	
	private String extractString(String json, String key) {
		String pattern = "\"" + key + "\":";
		int start = json.indexOf(pattern);
		if (start == -1) return null;
		start = json.indexOf("\"", start + pattern.length()) + 1;
		int end = json.indexOf("\"", start);
		return JsonHelper.unescapeJson(json.substring(start - 1, end + 1));
	}

}
