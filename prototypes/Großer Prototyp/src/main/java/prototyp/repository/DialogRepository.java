package prototyp.repository;

import org.springframework.data.repository.CrudRepository;

import prototyp.model.Dialog;

public interface DialogRepository extends CrudRepository<Dialog, Long> {
	Iterable<Dialog> findByUserId(long userId);
	void delete(Long id);
}
