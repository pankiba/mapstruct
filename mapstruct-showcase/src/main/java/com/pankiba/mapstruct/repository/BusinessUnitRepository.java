package com.pankiba.mapstruct.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pankiba.mapstruct.domain.BusinessUnit;

public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, Long> {

}
