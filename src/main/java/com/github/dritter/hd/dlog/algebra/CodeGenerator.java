package com.github.dritter.hd.dlog.algebra;

import com.github.dritter.hd.dlog.algebra.conditions.ComparisonFormula;
import com.github.dritter.hd.dlog.compiler.internal.*;
import com.github.dritter.hd.dlog.BuiltInPredicates;
import com.github.dritter.hd.dlog.Literal;
import com.github.dritter.hd.dlog.Parameter;
import com.github.dritter.hd.dlog.Parameter.Kind;
import com.github.dritter.hd.dlog.Predicate;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public class CodeGenerator {
    private static IdentityHashMap<Class<? extends Operator>, IGenerator> MAP = new IdentityHashMap<Class<? extends Operator>, IGenerator>();
    
    static {
        CodeGenerator.MAP.put(Table.class, new TableGenerator());
        CodeGenerator.MAP.put(UnionAll.class, new UnionGenerator());
        CodeGenerator.MAP.put(XProduct.class, new XProductGenerator());
        CodeGenerator.MAP.put(Projection.class, new ProjectionGenerator());
        CodeGenerator.MAP.put(Selection.class, new SelectionGenerator());
        CodeGenerator.MAP.put(Join.class, new JoinGenerator());
    }
    
    public static DataIterator codeGen(final Operator op, final CodeGenerationContext ctx) {
        final Class<? extends Operator> clazz = op.getClass();
        final IGenerator generator = CodeGenerator.MAP.get(clazz);
        if (generator == null) {
            throw new UnsupportedOperationException("currently no codegen for " + clazz.getSimpleName());
        }
        return generator.gen(op, ctx);
    }

    static List<DataIterator> codeGenChildren(final Operator op, final CodeGenerationContext ctx) {
        final List<DataIterator> plans = new ArrayList<DataIterator>();
        for (Operator child : op.getChildren()) {
            plans.add(codeGen(child, ctx));
        }
        return plans;
    }
}

interface IGenerator {
    DataIterator gen(Operator op, CodeGenerationContext ctx);
}

class TableGenerator implements IGenerator {
    public TableGenerator() {
    }

    public DataIterator gen(final Operator op, final CodeGenerationContext ctx) {
        final CodeGenerationIterator iter = new CodeGenerationIterator();
        ctx.register(((Table) op).getPredicate(), iter);
        return iter;
    }
}

class SelectionGenerator implements IGenerator {
    
    public DataIterator gen(final Operator op, final CodeGenerationContext ctx) {
        
        DataIterator input = CodeGenerator.codeGenChildren(op, ctx).get(0);
        List<Literal> conditionLiterals = ((Selection) op).getLiterals();
        ComparisonFormula[] conditionFormulas = new ComparisonFormula[conditionLiterals.size()];
        final List<Parameter<?>> freeVariables = op.getFreeVariables();
        
        for (int i = 0; i < conditionLiterals.size(); i++) {
            
            Literal conditionLiteral = conditionLiterals.get(i);
            List<Parameter<?>> parameters = conditionLiteral.getParameters();
            Parameter<?> leftParameter  = parameters.get(0);
            Parameter<?> rightParameter = parameters.get(1);
            Predicate builtInPredicate = conditionLiteral.getPredicate();
            Kind leftType  = leftParameter.getKind();
            Kind rightType = rightParameter.getKind();
            ComparisonFormula conditionFormula;
            
            if (Parameter.Kind.VARIABLE.equals(leftType)) {
                
                if (Parameter.Kind.VARIABLE.equals(rightType)) {
                    
                    int leftComponentNumber  = freeVariables.indexOf(leftParameter);
                    int rightComponentNumber = freeVariables.indexOf(rightParameter);
                    conditionFormula = BuiltInPredicates.createComparisonFormula(builtInPredicate, leftComponentNumber, rightComponentNumber);
                    
                } else {
                    
                    int leftComponentNumber = freeVariables.indexOf(leftParameter);
                    ParameterValue<?> rightValue = rightParameter.getParameterValue();
                    conditionFormula = BuiltInPredicates.createComparisonFormula(builtInPredicate, leftComponentNumber, rightValue);
                }
            } else {
                
                if (Parameter.Kind.VARIABLE.equals(rightType)) {
                    
                    ParameterValue<?> leftValue = leftParameter.getParameterValue();
                    int rightComponentNumber = freeVariables.indexOf(rightParameter);
                    conditionFormula = BuiltInPredicates.createComparisonFormula(builtInPredicate, leftValue, rightComponentNumber);
                    
                } else {
                    throw new IllegalArgumentException("Comparison of two values is not supported in selection conditions");
                }
            }
            conditionFormulas[i] = conditionFormula;
        }
        return new SelectionIterator(input, conditionFormulas);
    }
}

class ProjectionGenerator implements IGenerator {
    public ProjectionGenerator() {
    }

    public DataIterator gen(final Operator op, final CodeGenerationContext ctx) {
        final List<DataIterator> plans = CodeGenerator.codeGenChildren(op, ctx);
        final DataIterator input = plans.get(0);
        final Projection projection = (Projection) op;
        return new ProjectionIterator(input, projection.getColumns(), projection.getConstants());
    }
}

class UnionGenerator implements IGenerator {
    public UnionGenerator() {
    }

    public DataIterator gen(final Operator op, final CodeGenerationContext ctx) {
        return new UnionAllIterator(CodeGenerator.codeGenChildren(op, ctx));
    }
}

class JoinGenerator implements IGenerator {
    
    public DataIterator gen(final Operator op, final CodeGenerationContext ctx) {
        
        List<DataIterator> plans = CodeGenerator.codeGenChildren(op, ctx);
        DataIterator leftInput  = plans.get(0);
        DataIterator rightInput = plans.get(1);
        List<Literal> conditionLiterals = ((Join) op).getLiterals();
        ComparisonFormula[] conditionFormulas = new ComparisonFormula[conditionLiterals.size()];
        final List<Parameter<?>> freeVariables = op.getFreeVariables();
        int leftArity = op.getChildren().get(0).getArity();
        
        for (int i = 0; i < conditionLiterals.size(); i++) {
            
            Literal conditionLiteral = conditionLiterals.get(i);
            List<Parameter<?>> parameters = conditionLiteral.getParameters();
            Parameter<?> leftParameter  = parameters.get(0);
            Parameter<?> rightParameter = parameters.get(1);
            Predicate builtInPredicate = conditionLiteral.getPredicate();
            Kind leftType  = leftParameter.getKind();
            Kind rightType = rightParameter.getKind();
            ComparisonFormula conditionFormula;
            
            if (Parameter.Kind.VARIABLE.equals(leftType) && Parameter.Kind.VARIABLE.equals(rightType)) {
                
                int leftComponentNumber = freeVariables.indexOf(leftParameter);
                int rightComponentNumber = freeVariables.indexOf(rightParameter) - leftArity;
                conditionFormula = BuiltInPredicates.createComparisonFormula(builtInPredicate, leftComponentNumber, rightComponentNumber);
                
            } else {
                throw new RuntimeException();
            }
            conditionFormulas[i] = conditionFormula;
        }
        return new NLJoinIterator(leftInput, rightInput, conditionFormulas);
    }
}

class XProductGenerator implements IGenerator {
    
    public DataIterator gen(final Operator op, final CodeGenerationContext ctx) {
        
        List<DataIterator> plans = CodeGenerator.codeGenChildren(op, ctx);
        DataIterator leftInput  = plans.get(0);
        DataIterator rightInput = plans.get(1);
        return new NLJoinIterator(leftInput, rightInput, new ComparisonFormula[0]);
    }
}
