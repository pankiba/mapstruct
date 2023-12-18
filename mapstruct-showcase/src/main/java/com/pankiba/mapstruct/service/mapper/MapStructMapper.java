package com.pankiba.mapstruct.service.mapper;

public interface MapStructMapper<D, E> {

	D toDto(E e);

	E toEntity(D d);
	
}
