package app.textblocks;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is a conversion step, before it has taken place. It consists of multiple text blocks which can be
 * rendered into a form.
 * <p>
 * Created by justusadam on 05/12/15.
 */
public class ChatTemplate {
    private List<TextBlock> blocks;

    public ChatTemplate(List<TextBlock> blocks) {
        this.blocks = blocks;
    }

    /**
     * Turn all text blocks into a single combined form (without a head)
     *
     * @return headless html form
     */
    public String createForm() {
        return blocks.stream().map(TextBlock::asForm).map(
                (String f) ->
                        // You may use this function to surround each of the text block bits with a wrapper,
                        // some <div> elements for example.
                        "<div class=\"umrandung--nomargin\">" + f + "</div>"
        ).reduce(String::concat).get();
    }

    /**
     * Takes in the response values of the request and constructs the {@link Chat}
     *
     * @param requestValues map of http response data
     * @return new Chat
     */
    public Chat fromForm(Map<String, String> requestValues) {
        return new Chat(
                blocks.stream().filter(
                        t -> t.wasSelected(requestValues)
                ).map(
                        t -> t.fromForm(requestValues)
                ).collect(Collectors.toList()));
    }
}
