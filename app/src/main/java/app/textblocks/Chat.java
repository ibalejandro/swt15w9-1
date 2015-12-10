package app.textblocks;

import java.util.List;

/**
 * This class represents a single conversation step that has happened. Multiple text blocks, all with values in them.
 *
 * Created by justusadam on 05/12/15.
 */
public class Chat {
    private List<TextBlockValue> blocks;

    public Chat(List<TextBlockValue> blocks) {
        this.blocks = blocks;
    }

    @Override
    public String toString() {
        return blocks.stream().map(TextBlockValue::toString).reduce(String::concat).get();
    }

    public List<TextBlockValue> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<TextBlockValue> blocks) {
        this.blocks = blocks;
    }
}
