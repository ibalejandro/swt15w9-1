package com.example;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface GoodRepository extends CrudRepository<Good, Long> {
	List<Good> findAllByOrderByTagAsc();
	List<Good> findByNameStartingWithIgnoreCase(String name);
	List<Good> findByTagStartingWithIgnoreCase(String tag);
}
