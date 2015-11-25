package app.repository;

import org.springframework.data.repository.CrudRepository;

import app.model.Dialog;
import app.model.User;

public interface DialogRepository extends CrudRepository<Dialog, Long> {
	Iterable<Dialog> findByUserA(User user);
	Iterable<Dialog> findByUserB(User user);
	void delete(Long id);
}
