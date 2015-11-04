package goods;

import java.util.Optional;

import org.springframework.data.repository.Repository;

public interface TextBlockDB extends Repository<TextBlock, Long> {
	
	void delete(Long id);
	
	TextBlock save(TextBlock textblock);
	
	Optional<TextBlock> findOne(Long id);
	
	Iterable<TextBlock> findAll();
	
	int count();
}
