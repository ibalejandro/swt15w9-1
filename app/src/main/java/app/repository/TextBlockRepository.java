package app.repository;

import org.springframework.data.repository.CrudRepository;

import app.textblocks.TextBlock;

/**
 * <h1>DialogRepository</h1> The TextBlock Repository is a repository where all
 * {@link TextBlock}s are saved. When a dialog is updated or deleted, the
 * TextBlockRepository is also modified.
 * 
 * @author Mario Henze
 */
public interface TextBlockRepository extends CrudRepository<TextBlock, Long> {
}
