package com.pankiba.mapstruct.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class BusinessUnit implements Serializable {

	private static final long serialVersionUID = -2965336492593146084L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "businessunit_id_generator")
	@SequenceGenerator(name = "businessunit_id_generator", sequenceName = "businessunit_id_seq", allocationSize = 50, initialValue = 1001)
	private Long businessUnitId;

	@Column(unique = true)
	@NotNull
	private String businessUnitName;

	/**
	 * OneToMany denotes that one BusinessUnit can have many Employees. Cascade means dependent object(Employee) will be
	 * persisted/updated/deleted automatically on subsequent persist/update/delete on BusinessUnit object. No need to
	 * perform operation separately on Employee.
	 * 
	 * OneToMany annotation is used to define a multi-value association to a Collection of Entities
	 * 
	 * @JsonManagedReference is the forward part of reference – the one that gets serialized normally
	 */
	@ToString.Exclude
	@JsonManagedReference
	@OneToMany(mappedBy = "businessUnit", cascade = CascadeType.ALL)
	private Set<Employee> employees = new HashSet<>();

	public BusinessUnit(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	/**
	 * Parent side should contain the addChild and removeChild combo. These methods ensure we always synchronize both
	 * sides of the association, to avoid object or relational data corruption issues
	 */
	public void addEmployee(Employee employee) {
		employees.add(employee);
		employee.setBusinessUnit(this);
	}

	public void removeEmployee(Employee employee) {
		employees.remove(employee);
		employee.setBusinessUnit(null);
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + Objects.hashCode(businessUnitId);
		result = prime * result + Objects.hashCode(businessUnitName);
		return result;
	}

	@Override
	public boolean equals(Object object) {

		// self check
		if (this == object)
			return true;

		// null check
		if (object == null)
			return false;

		// type check. we can do instance of check instead but it will pass for subclass
		// too
		if (getClass() != object.getClass())
			return false;

		// cast
		BusinessUnit businessUnit = (BusinessUnit) object;

		return Objects.equals(businessUnitId, businessUnit.getBusinessUnitId())
				&& Objects.equals(businessUnitName, businessUnit.getBusinessUnitName());
	}

}
