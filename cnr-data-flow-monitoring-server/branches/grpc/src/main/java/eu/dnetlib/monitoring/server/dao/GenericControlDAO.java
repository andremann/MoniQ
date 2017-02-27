package eu.dnetlib.monitoring.server.dao;

import java.util.List;

import eu.dnetlib.monitoring.controls.Control;

public interface GenericControlDAO {

	int create(Control control);

	int update(Control control);

	void delete(int id);

	Control get(String scenario, String controlName);

	List<Control> listControls(String scenario);

	List<Control> listControls(String scenario, String metric);

}
