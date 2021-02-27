package gov.nasa.worldwind.ogc.gltf;

import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.geom.Matrix;
import gov.nasa.worldwind.util.typescript.TypeScriptImports;

@TypeScriptImports(imports = "./GLTFArray,./GLTFRoot,./GLTFParserContext,../../geom/Matrix,./GLTFMesh,./GLTFUtil,../../avlist/AVListImpl")
public class GLTFNode extends GLTFArray {

    private int meshIdx;
    private GLTFMesh mesh;
    private Matrix matrix;
    private int[] childIndices;
    private GLTFNode[] children;
    private int cameraIdx;
    private double[] rotation;
    private double[] scale;
    private double[] translation;
    private String name;

    public GLTFNode(AVListImpl properties) {
        super();
        this.meshIdx = -1;
        for (String propName : properties.getKeys()) {
            switch (propName) {
                case GLTFParserContext.KEY_MESH:
                    this.meshIdx = GLTFUtil.getInt(properties.getValue(propName));
                    break;
                case GLTFParserContext.KEY_MATRIX:
                    double[] matrixValues = GLTFUtil.retrieveDoubleArray((Object[]) properties.getValue(propName));
                    this.matrix = Matrix.fromArray(matrixValues, 0, false);
                    // System.out.println(this.matrix.toString());
                    break;
                case GLTFParserContext.KEY_CHILDREN:
                    this.childIndices = GLTFUtil.retrieveIntArray((Object[]) properties.getValue(propName));
                    break;
                case GLTFParserContext.KEY_CAMERA:
                    this.cameraIdx = GLTFUtil.getInt(properties.getValue(propName));
                    break;
                case GLTFParserContext.KEY_ROTATION:
                    this.rotation = GLTFUtil.retrieveDoubleArray((Object[]) properties.getValue(propName));
                    break;
                case GLTFParserContext.KEY_SCALE:
                    this.scale = GLTFUtil.retrieveDoubleArray((Object[]) properties.getValue(propName));
                    break;
                case GLTFParserContext.KEY_TRANSLATION:
                    this.translation = GLTFUtil.retrieveDoubleArray((Object[]) properties.getValue(propName));
                    break;
                case GLTFParserContext.KEY_NAME:
                    this.name = properties.getStringValue(propName);
                    break;
                default:
                    System.out.println("GLTFNode: Unsupported " + propName);
                    break;
            }
        }
    }

    public void assembleGeometry(GLTFRoot root) {
        if (this.meshIdx >= 0) {
            this.mesh = root.getMeshForIdx(this.meshIdx);
            this.mesh.assembleGeometry(root);
        }
        if (this.childIndices != null) {
            GLTFNode[] allNodes = root.getNodes();
            this.children = new GLTFNode[this.childIndices.length];
            for (int i = 0; i < this.childIndices.length; i++) {
                this.children[i] = allNodes[this.childIndices[i]];
                this.children[i].assembleGeometry(root);
            }
        }
    }

    public GLTFNode[] getChildren() {
        return this.children;
    }

    public GLTFMesh getMesh() {
        return this.mesh;
    }

    public boolean hasMatrix() {
        return this.matrix != null;
    }

    public Matrix getMatrix() {
        return this.matrix;
    }

}
