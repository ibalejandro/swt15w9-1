package app.textblocks;

import java.util.List;

/**
 * This class represents a single conversation step that has happened. Multiple text blocks, all with values in them.
 * <p>
 * Created by justusadam on 05/12/15.
 */
public class Chat {
    private List<TextBlockValue> blocks;

    public Chat(List<TextBlockValue> blocks) {
        this.blocks = blocks;
    }

    /**
     * Renders the chat.
     *
     * @return rendered chat
     */
    @Override
    public String toString() {
        return blocks.stream().map(TextBlockValue::toString).map(
                (String s) ->
                        // You may use this function to surround each text block value with a html wrapper,
                        // like some <div> elements
                        s
        ).reduce(String::concat).get();
    }

    public List<TextBlockValue> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<TextBlockValue> blocks) {
        this.blocks = blocks;
    }
}
