package presentation.service.assembler;

import java.util.Map;

import core.model.snapshot.content.ContentSnapshot;
import core.model.view.content.*;

public class ContentViewAssembler implements ViewAssembler<ContentSnapshot, ContentView> {

    @Override
    public ContentView from(ContentSnapshot snapshot) {
        return switch (snapshot.type()) {
            case TEXT -> assembleTextView(snapshot.values());
            case IMAGE -> assembleImageView(snapshot.values());
            case VIDEO -> assembleVideoView(snapshot.values());
        };
    }

    private VideoContentView assembleVideoView(Map<String,Object> values) {
        return new VideoContentView(
            (String) values.get("url")
        );
    }

    private ImageContentView assembleImageView(Map<String,Object> values) {
        return new ImageContentView(
            (String) values.get("url")
        );
    }

    private TextContentView assembleTextView(Map<String,Object> values) {
        return new TextContentView(
            (String) values.get("text")
        );
    }

}
