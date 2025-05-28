package com.alkrist.maribel.client.components.ui.font;

import com.alkrist.maribel.client.model.Mesh;
import com.alkrist.maribel.client.model.Vertex;

public class TextMeshData {

	private Vertex[] vertices;
    private int[] indices;
    
    // Width and height of the text in screen coordinates
    private float width;
    private float height;
    
    protected TextMeshData(Vertex[] vertices, int[] indices, float width, float height) {
        this.vertices = vertices;
        this.indices = indices;
        this.width = width;
        this.height = height;
    }

    
    public Mesh generateMesh() {
    	return new Mesh(vertices, indices);
    }
    
    public Vertex[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }

    public int getVertexCount() {
        return vertices.length;
    }
    
    public float getWidth() {
        return width;
    }
    
    public float getHeight() {
        return height;
    }
}
