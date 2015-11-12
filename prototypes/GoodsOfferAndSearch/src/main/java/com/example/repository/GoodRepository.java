package com.example.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.entity.GoodEntity;

public interface GoodRepository extends CrudRepository<GoodEntity, Long> {
	Iterable<GoodEntity> findByNameStartingWithIgnoreCase(String name);
	Iterable<GoodEntity> findByTagsContainingIgnoreCase(String tag);
	Iterable<GoodEntity> findByUserId(long userId);
	void delete(Long id);
}
