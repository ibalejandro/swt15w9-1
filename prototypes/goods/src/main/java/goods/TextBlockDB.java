package goods;

import java.util.Optional;

import org.springframework.data.repository.Repository;

public interface TextBlockDB extends Repository<TexBlock, Long> {
	
	void delete(Long id);
	
	TexBlock save(TexBlock textblock);
	
	Optional<TexBlock> findOne(Long id);
	
	Iterable<TexBlock> findAll();
	
	int count();
}
