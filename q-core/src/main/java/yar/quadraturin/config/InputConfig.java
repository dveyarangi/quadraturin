
package yar.quadraturin.config;


public class InputConfig {

    protected double scrollStep;
    protected double scaleStep;
	
    protected InputBinding [] bindings;

    /**
     * Gets the value of the scrollStep property.
     * 
     */
    public double getScrollStep() { return scrollStep; }

    /**
     * Gets the value of the scaleStep property.
     * 
     */
    public double getScaleStep() { return scaleStep; }

    public InputBinding [] getBindings() { return this.bindings;    }

}
