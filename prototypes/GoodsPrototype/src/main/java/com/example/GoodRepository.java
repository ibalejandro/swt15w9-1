package com.example;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface GoodRepository extends CrudRepository<Good, Long> {
	List<Good> findByName(String name);
	List<Good> findByTag(String tag);
	List<Good> findAllByOrderByTagAsc();
}
