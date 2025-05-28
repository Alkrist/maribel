package com.alkrist.maribel.client.components.ui.font;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.alkrist.maribel.client.components.ui.UITextPanel;
import com.alkrist.maribel.client.model.Vertex;

public class TextMeshBuilder {

	public static final double LINE_HEIGHT = 0.03f;
    protected static final int SPACE_ASCII = 32;

    private FontMetaFile metaData;

    protected TextMeshBuilder(File metaFile) {
        metaData = new FontMetaFile(metaFile);
    }

    protected TextMeshData createTextMesh(UITextPanel text) {
        List<Line> lines = createStructure(text);
        TextMeshData data = createQuadVertices(text, lines);
        return data;
    }

    private List<Line> createStructure(UITextPanel text) {
        char[] chars = text.getTextString().toCharArray();
        List<Line> lines = new ArrayList<Line>();
        Line currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
        Word currentWord = new Word(text.getFontSize());
        for (char c : chars) {
            int ascii = (int) c;
            if (ascii == SPACE_ASCII) {
                boolean added = currentLine.attemptToAddWord(currentWord);
                if (!added) {
                    lines.add(currentLine);
                    currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
                    currentLine.attemptToAddWord(currentWord);
                }
                currentWord = new Word(text.getFontSize());
                continue;
            }
            Glyph character = metaData.getCharacter(ascii);
            currentWord.addCharacter(character);
        }
        completeStructure(lines, currentLine, currentWord, text);
        return lines;
    }

    private void completeStructure(List<Line> lines, Line currentLine, Word currentWord, UITextPanel text) {
        boolean added = currentLine.attemptToAddWord(currentWord);
        if (!added) {
            lines.add(currentLine);
            currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
            currentLine.attemptToAddWord(currentWord);
        }
        lines.add(currentLine);
    }

    private TextMeshData createQuadVertices(UITextPanel text, List<Line> lines) {
        float totalWidth = 0;
        text.setNumberOfLines(lines.size());
        
        double curserX = 0f;
        double curserY = 0f;
        
        List<Vertex> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        int vertexCount = 0;
        
        for (Line line : lines) {
            if (text.isCentered()) {
                curserX = (line.getMaxLength() - line.getLineLength()) / 2;
            }
            for (Word word : line.getWords()) {
                for (Glyph letter : word.getGlyphs()) {
                    // Add vertices and indices for this character
                    addCharacterVertices(curserX, curserY, letter, text.getFontSize(), vertices, indices, vertexCount);
                    vertexCount += 4; // 4 vertices per character quad
                    curserX += letter.getxAdvance() * text.getFontSize();
                }
                curserX += metaData.getSpaceWidth() * text.getFontSize();
            }
            
            // Set width of the text
            if(curserX > totalWidth) {
                totalWidth = (float) curserX;
            }
            
            curserX = 0;
            curserY += LINE_HEIGHT * text.getFontSize();
        }
        
        // Convert lists to arrays
        Vertex[] vertexArray = new Vertex[vertices.size()];
        vertices.toArray(vertexArray);
        
        int[] indexArray = new int[indices.size()];
        for (int i = 0; i < indexArray.length; i++) {
            indexArray[i] = indices.get(i);
        }
        
        return new TextMeshData(vertexArray, indexArray, totalWidth, (float) curserY);
    }

    private void addCharacterVertices(double curserX, double curserY, Glyph character, double fontSize,
                                    List<Vertex> vertices, List<Integer> indices, int vertexCount) {
        // Calculate vertex positions
        double x = curserX + (character.getxOffset() * fontSize);
        double y = curserY + (character.getyOffset() * fontSize);
        double maxX = x + (character.getSizeX() * fontSize);
        double maxY = y + (character.getSizeY() * fontSize);
        
        // Convert to screen coordinates
        double properX = (2 * x) - 1;
        double properY = (-2 * y) + 1;
        double properMaxX = (2 * maxX) - 1;
        double properMaxY = (-2 * maxY) + 1;
        
        // Create 4 vertices for the character quad
        Vector3f pos0 = new Vector3f((float)properX, (float)properY, 0);
        Vector3f pos1 = new Vector3f((float)properX, (float)properMaxY, 0);
        Vector3f pos2 = new Vector3f((float)properMaxX, (float)properMaxY, 0);
        Vector3f pos3 = new Vector3f((float)properMaxX, (float)properY, 0);
        
        // Texture coordinates
        Vector2f tex0 = new Vector2f((float)character.getxTextureCoord(), (float)character.getyTextureCoord());
        Vector2f tex1 = new Vector2f((float)character.getxTextureCoord(), (float)character.getYMaxTextureCoord());
        Vector2f tex2 = new Vector2f((float)character.getXMaxTextureCoord(), (float)character.getYMaxTextureCoord());
        Vector2f tex3 = new Vector2f((float)character.getXMaxTextureCoord(), (float)character.getyTextureCoord());
        
        // Add vertices
        vertices.add(new Vertex(pos0, tex0));
        vertices.add(new Vertex(pos1, tex1));
        vertices.add(new Vertex(pos2, tex2));
        vertices.add(new Vertex(pos3, tex3));
        
        // Add indices (two triangles)
        indices.add(vertexCount);
        indices.add(vertexCount + 1);
        indices.add(vertexCount + 2);
        indices.add(vertexCount + 2);
        indices.add(vertexCount + 3);
        indices.add(vertexCount);
    }

}
