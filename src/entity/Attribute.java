/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author nikolaos
 */
public class Attribute {

    private String attributeName;
    private String attributeDataType;
    private int attributeDataTypeSize;

    public Attribute() {
    }

    public Attribute(String attributeName, String attributeDataType, int attributeDataTypeSize) {
        this.attributeName = attributeName;
        this.attributeDataType = attributeDataType;
        this.attributeDataTypeSize = attributeDataTypeSize;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeDataType() {
        return attributeDataType;
    }

    public void setAttributeDataType(String attributeDataType) {
        this.attributeDataType = attributeDataType;
    }

    public int getAttributeDataTypeSize() {
        return attributeDataTypeSize;
    }

    public void setAttributeDataTypeSize(int attributeDataTypeSize) {
        this.attributeDataTypeSize = attributeDataTypeSize;
    }

}
