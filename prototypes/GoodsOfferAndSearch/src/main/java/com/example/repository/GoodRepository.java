package com.example.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.entity.GoodEntity;

public interface GoodRepository extends CrudRepository<GoodEntity, Long> {
	Iterable<GoodEntity> findByNameStartingWithIgnoreCase(String name);
	
	//List<GoodEntity> findByTagStartingWithIgnoreCase(String tag);
	@Query("select p from GoodEntity p where p.tags = ?1")
	Iterable<GoodEntity> findByAttributeAndValue(String tag);
	
	Iterable<GoodEntity> findByUserId(long userId);
	void delete(Long id);
}
