package app.repository;

import org.springframework.data.repository.CrudRepository;

import app.model.Dialog;

public interface DialogRepository extends CrudRepository<Dialog, Long> {
	Iterable<Dialog> findByUserId(long userId);
	void delete(Long id);
}
