package prototyp.model;

import java.util.Optional;

import org.springframework.data.repository.Repository;

public interface Dialog extends Repository<MessageElement, Long> {
	void delete(Long id);

	MessageElement save(MessageElement el);

	Optional<MessageElement> findOne(Long id);

	Iterable<MessageElement> findAll();

	int count();
}
