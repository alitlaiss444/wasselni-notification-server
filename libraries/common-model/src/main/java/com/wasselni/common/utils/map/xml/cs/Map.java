package com.wasselni.common.utils.map.xml.cs;

public class Map {

	private String Id;
	private String Description;
	private DefaultsFrom DefaultsFrom;
	private DefaultsTo DefaultsTo;
	private Mappings Mappings;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public Mappings getMappings() {
		return Mappings;
	}

	public void setMappings(Mappings mappings) {
		Mappings = mappings;
	}

	public DefaultsFrom getDefaultsFrom() {
		return DefaultsFrom;
	}

	public void setDefaultsFrom(DefaultsFrom defaultsFrom) {
		DefaultsFrom = defaultsFrom;
	}

	public DefaultsTo getDefaultsTo() {
		return DefaultsTo;
	}

	public void setDefaultsTo(DefaultsTo defaultsTo) {
		DefaultsTo = defaultsTo;
	}

}
