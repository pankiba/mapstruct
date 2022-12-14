package com.pankiba.mapstruct.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pankiba.mapstruct.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
