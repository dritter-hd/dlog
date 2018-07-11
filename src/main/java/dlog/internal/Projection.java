package dlog.internal;

import dlog.Parameter;
import dlog.algebra.ParameterValue;

import java.util.ArrayList;
import java.util.List;

public final class Projection extends NaryOperator {

    private int[] columns;
    private ParameterValue<?>[] constants;

    public Projection(final Operator input, final int[] columns, final ParameterValue<?>[] constants) {
        super(Util.singletonList(input));

        for (int i = 0; i < columns.length; ++i) {
            if (columns[i] == -1 && constants[i] == null) {
                throw new IllegalStateException();
            }
        }
        this.columns = columns;
        this.constants = constants;
    }

    public int[] getColumns() {
        return this.columns;
    }

    public ParameterValue<?>[] getConstants() {
        return this.constants;
    }
    
    @Override
    public List<Parameter<?>> getFreeVariables() {
        
        List<Parameter<?>> freeVariables = new ArrayList<Parameter<?>>();
        
        for (int i = 0; i < this.columns.length; i++) {
            
            int componentNumber = this.columns[i];
            Parameter<?> freeVariable;
            if (componentNumber >= 0) {
                freeVariable = this.getChildren().get(0).getFreeVariables().get(componentNumber);
            } else {
                freeVariable = Parameter.createVariable("component" + (i +1));
            }
            freeVariables.add(freeVariable);
        }
        return freeVariables;
    }
    
    @Override
    public int getArity() {
        return this.columns.length;
    }

    @Override
    void appendAdditionalMembers(StringBuilder sb, int indent) {
        indent(sb, indent);
        for (int i = 0; i < columns.length; ++i) {
            if (columns[i] < 0) {
                sb.append('"').append(constants[i]).append('"');
            } else {
                sb.append(columns[i]);
            }
            sb.append(' ');
        }
        sb.append('\n');
    }
}
