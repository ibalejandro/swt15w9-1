package prototyp.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface GoodRepository extends CrudRepository<GoodEntity, Long> {
	Iterable<GoodEntity> findByNameStartingWithIgnoreCase(String name);
  Iterable<GoodEntity> findByTagsContainingIgnoreCase(String tag);
  Iterable<GoodEntity> findByUserId(long userId);
  void delete(Long id);
}
