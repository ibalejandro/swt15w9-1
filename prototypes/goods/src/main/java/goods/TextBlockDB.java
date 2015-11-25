package goods;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TextBlockDB extends JpaRepository<TexBlock, Long> {
	
	void delete(Long id);
	
	TexBlock save(TexBlock textblock);
	
	TexBlock findOne(Long id);
	
	List<TexBlock> findAll();
	
	long count();
}
