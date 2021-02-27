package gov.nasa.worldwind.ogc.gltf;

import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.formats.json.BasicJSONEventParser;
import gov.nasa.worldwind.formats.json.JSONEvent;
import gov.nasa.worldwind.formats.json.JSONEventParserContext;
import gov.nasa.worldwind.util.typescript.TypeScriptImports;
import java.io.IOException;

@TypeScriptImports(imports = "../json/JSONEvent,../json/JSONEventParserContext,../json/BasicJSONEventParser,./GLTFParserContext,../../avlist/AVListImpl")
public class GLTFAsset extends BasicJSONEventParser {

    private String version;
    private String generator;
    private String copyright;

    public GLTFAsset(AVListImpl properties) {
        super();
        for (String propName : properties.getKeys()) {
            switch (propName) {
                case GLTFParserContext.KEY_VERSION:
                    this.version = properties.getStringValue(propName);
                    break;
                case GLTFParserContext.KEY_GENERATOR:
                    this.generator=properties.getStringValue(propName);
                    break;
                case GLTFParserContext.KEY_COPYRIGHT:
                    this.copyright=properties.getStringValue(propName);
                    break;
                default:
                    System.out.println("GLTFAsset: Unsupported "+propName);
                    break;
            }
        }
    }

    @Override
    public Object parse(JSONEventParserContext ctx, JSONEvent event) throws IOException {
        Object parsedObject = super.parse(ctx, event);
        if (parsedObject instanceof AVListImpl) {
            return new GLTFAsset((AVListImpl) parsedObject);
        }
        return parsedObject;
    }

}
