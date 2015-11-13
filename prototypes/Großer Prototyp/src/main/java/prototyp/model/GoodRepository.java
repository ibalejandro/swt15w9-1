package prototyp.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface GoodRepository extends CrudRepository<GoodEntity, Long> {
	List<GoodEntity> findByNameStartingWithIgnoreCase(String name);
	//List<GoodEntity> findByTagStartingWithIgnoreCase(String tag);
}
