//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.08 at 12:57:18 AM IDT 
//


package yar.quadraturin.config;
public class InputBinding {

    protected String actionId;
    protected int buttonId;
    protected int modeId;
    protected int modifiers = 0;
    
    /**
     * Gets the value of the actionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionId() {
        return actionId;
    }

    /**
     * Sets the value of the actionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionId(String value) {
        this.actionId = value;
    }

    /**
     * Gets the value of the buttonId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getButtonId() {
        return buttonId;
    }

    /**
     * Sets the value of the buttonId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setButtonId(int value) {
        this.buttonId = value;
    }

    /**
     * Gets the value of the modeId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getModeId() {
        return modeId;
    }

    /**
     * Sets the value of the modeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setModeId(int value) {
        this.modeId = value;
    }
    
    public int getModifiers() { return modifiers; }
    public void setModifiers(int modifiers) { this.modifiers = modifiers; }

    
    @Override
	public String toString()
    {
    	return "action id: " + getActionId() + "; button id: " + getButtonId() + "; button mode id: " + getModeId();

    }
}