package com.example.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.entity.GoodEntity;

public interface GoodRepository extends CrudRepository<GoodEntity, Long> {
	Iterable<GoodEntity> findByNameStartingWithIgnoreCase(String name);
	//List<GoodEntity> findByTagStartingWithIgnoreCase(String tag);
}
