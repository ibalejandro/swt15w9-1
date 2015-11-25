package com.example.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.example.entity.GoodEntity;

public interface GoodRepository extends CrudRepository<GoodEntity, Long> {
	List<GoodEntity> findByNameStartingWithIgnoreCase(String name);
	//List<GoodEntity> findByTagStartingWithIgnoreCase(String tag);
}
