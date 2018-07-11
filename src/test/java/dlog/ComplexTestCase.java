package dlog;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dlog.parser.DlogParser;
import org.junit.Test;

import dlog.algebra.DataIterator;
import dlog.utils.Utils;

public class ComplexTestCase {
    
    @Test
    public void testHarryPotterExample() {
        
        Predicate sibling = Predicate.create("sibling", 2);
        Predicate parent  = Predicate.create("parent", 2);
        Predicate cousin  = Predicate.create("cousin", 2);
        Predicate related = Predicate.create("related", 2);
        
        Parameter<?> argumentX  = Parameter.createVariable("X");
        Parameter<?> argumentY  = Parameter.createVariable("Y");
        Parameter<?> argumentZ  = Parameter.createVariable("Z");
        Parameter<?> argumentXp = Parameter.createVariable("Xp");
        Parameter<?> argumentYp = Parameter.createVariable("Yp");
        
        Literal head1     = Literal.create(sibling, argumentX, argumentY);
        Literal subgoal11 = Literal.create(parent, argumentX, argumentZ);
        Literal subgoal12 = Literal.create(parent, argumentY, argumentZ);
        Literal subgoal13 = Literal.create(BuiltInPredicates.NOT_EQUALS, argumentX, argumentY);
        
        Literal head2     = Literal.create(cousin, argumentX, argumentY);
        Literal subgoal21 = Literal.create(parent, argumentX, argumentXp);
        Literal subgoal22 = Literal.create(parent, argumentY, argumentYp);
        Literal subgoal23 = Literal.create(sibling, argumentXp, argumentYp);
        
        Literal head3     = Literal.create(cousin, argumentX, argumentY);
        Literal subgoal31 = Literal.create(parent, argumentX, argumentXp);
        Literal subgoal32 = Literal.create(parent, argumentY, argumentYp);
        Literal subgoal33 = Literal.create(cousin, argumentXp, argumentYp);
        
        Literal head4     = Literal.create(related, argumentX, argumentY);
        Literal subgoal41 = Literal.create(sibling, argumentX, argumentY);
        
        Literal head5     = Literal.create(related, argumentX, argumentY);
        Literal subgoal51 = Literal.create(related, argumentX, argumentZ);
        Literal subgoal52 = Literal.create(parent, argumentY, argumentZ);
        
        Literal head6     = Literal.create(related, argumentX, argumentY);
        Literal subgoal61 = Literal.create(related, argumentZ, argumentY);
        Literal subgoal62 = Literal.create(parent, argumentX, argumentZ);
        
        IRule rule1 = Rule.create(head1, subgoal11, subgoal12, subgoal13);
        IRule rule2 = Rule.create(head2, subgoal21, subgoal22, subgoal23);
        IRule rule3 = Rule.create(head3, subgoal31, subgoal32, subgoal33);
        IRule rule4 = Rule.create(head4, subgoal41);
        IRule rule5 = Rule.create(head5, subgoal51, subgoal52);
        IRule rule6 = Rule.create(head6, subgoal61, subgoal62);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule1);
        rules.add(rule2);
        rules.add(rule3);
        rules.add(rule4);
        rules.add(rule5);
        rules.add(rule6);
        
        String[][] relationP = {
            
            {"Bill Weasley",      "Molly Prewett"},
            {"Bill Weasley",      "Arthur Weasley"},
            {"Charlie Weasley",   "Molly Prewett"},
            {"Charlie Weasley",   "Arthur Weasley"},
            {"Percy Weasley",     "Molly Prewett"},
            {"Percy Weasley",     "Arthur Weasley"},
            {"Fred Weasley, Sr.", "Molly Prewett"},
            {"Fred Weasley, Sr.", "Arthur Weasley"},
            {"George Weasley",    "Molly Prewett"},
            {"George Weasley",    "Arthur Weasley"},
            {"Ron Weasley",       "Molly Prewett"},
            {"Ron Weasley",       "Arthur Weasley"},
            {"Ginny Weasley",     "Molly Prewett"},
            {"Ginny Weasley",     "Arthur Weasley"},
            
            {"Victoire Weasley",  "Bill Weasley"},
            {"Victoire Weasley",  "Fleur Delacour"},
            {"Dominique Weasley", "Bill Weasley"},
            {"Dominique Weasley", "Fleur Delacour"},
            {"Louis Weasley",     "Bill Weasley"},
            {"Louis Weasley",     "Fleur Delacour"},
            
            {"Molly Weasley", "Percy Weasley"},
            {"Molly Weasley", "Audrey Weasley"},
            {"Lucy Weasley",  "Percy Weasley"},
            {"Lucy Weasley",  "Audrey Weasley"},
            
            {"Fred Weasley, Jr.", "George Weasley"},
            {"Fred Weasley, Jr.", "Angelina Johnson"},
            {"Roxanne Weasley",   "George Weasley"},
            {"Roxanne Weasley",   "Angelina Johnson"},
            
            {"Rose Weasley", "Ron Weasley"},
            {"Rose Weasley", "Hermione Granger"},
            {"Hugo Weasley", "Ron Weasley"},
            {"Hugo Weasley", "Hermione Granger"},
            
            {"James Sirius Potter",  "Harry Potter"},
            {"James Sirius Potter",  "Ginny Weasley"},
            {"Albus Severus Potter", "Harry Potter"},
            {"Albus Severus Potter", "Ginny Weasley"},
            {"Lily Luna Potter",     "Harry Potter"},
            {"Lily Luna Potter",     "Ginny Weasley"},
            
            {"Scorpius Hyperion Malfoy", "Draco Malfoy"},
            {"Scorpius Hyperion Malfoy", "Astoria Greengrass"},
            
            {"Lorcan Scamander",   "Luna Lovegood"},
            {"Lorcan Scamander",   "Rolf Scamander"},
            {"Lysander Scamander", "Luna Lovegood"},
            {"Lysander Scamander", "Rolf Scamander"}
        };
        
        DataIterator relationPIterator = Utils.createRelationIterator(relationP);
        
        IFacts relationPFacts = Facts.create(parent, relationPIterator);
        
        Collection<IFacts> edbRelations = new ArrayList<IFacts>();
        edbRelations.add(relationPFacts);
        
        IEvaluator evaluator = new NaiveRecursiveEvaluator(rules);
        Collection<IFacts> idbRelations = evaluator.eval(edbRelations);
        
        assertEquals(3, idbRelations.size());
    }
    
    @Test
    public void testExampleLogicProgram() {
        
        String program =
              "manages(E,M) :- employees(E,D), departments(D,M)."
            
            + "boss(E,M) :- manages(E,M)."
            + "boss(E,M) :- boss(E,N), manages(N,M)."
            
            + "employees(\"Olivia\",\"Department A\")."
            + "employees(\"Olivier\",\"Department B\")."
            + "employees(\"Sophie\",\"Department B\")."
            + "employees(\"Jack\",\"Department B\")."
            + "employees(\"Emily\",\"Department C\")."
            + "employees(\"Harry\",\"Department C\")."
            + "employees(\"Lily\",\"Department C\")."
            + "employees(\"Alfie\",\"Department D\")."
            + "employees(\"Amelia\",\"Department D\")."
            + "employees(\"Charlie\",\"Department D\")."
            + "employees(\"Jessica\",\"Department E\")."
            + "employees(\"Thomas\",\"Department E\")."
            + "employees(\"Ruby\",\"Department E\")."
            
            + "departments(\"Department A\",\"Olivia\")."
            + "departments(\"Department B\",\"Olivia\")."
            + "departments(\"Department C\",\"Olivier\")."
            + "departments(\"Department D\",\"Sophie\")."
            + "departments(\"Department E\",\"Jack\").";
        
        DlogParser parser = new DlogParser();
        parser.parse(program);
        
        List<IRule> rules = parser.getRules();
        
        Collection<IFacts> edbRelations = parser.getFacts();
        
        IEvaluator evaluator = new NaiveRecursiveEvaluator(rules);
        Collection<IFacts> idbRelations = evaluator.eval(edbRelations);
        
        assertEquals(2, idbRelations.size());
    }
    
    @Test
    public void testSomeNewBuiltInPredicatesAndDataTypes() {
        
        String program =
              "manages(E,M) :- employees(E,B,D), departments(D,M), !=(E,M)."
            
            + "boss(E,M) :- manages(E,M)."
            + "boss(E,M) :- boss(E,N), manages(N,M)."
            
            + "youngPersonsBoss(M) :- boss(E,M), employees(E,B,D), >(B,13.09.1993)."
            
            + "employees(\"Olivia\",30.01.1988,\"Department A\")."
            + "employees(\"Olivier\",04.10.1973,\"Department B\")."
            + "employees(\"Sophie\",03.02.1987,\"Department B\")."
            + "employees(\"Jack\",17.06.1973,\"Department B\")."
            + "employees(\"Emily\",02.05.1978,\"Department C\")."
            + "employees(\"Harry\",10.08.1987,\"Department C\")."
            + "employees(\"Lily\",08.03.1976,\"Department C\")."
            + "employees(\"Alfie\",07.09.1980,\"Department D\")."
            + "employees(\"Amelia\",10.10.1988,\"Department D\")."
            + "employees(\"Charlie\",01.12.1972,\"Department D\")."
            + "employees(\"Jessica\",17.10.1971,\"Department E\")."
            + "employees(\"Thomas\",25.01.1995,\"Department E\")."
            + "employees(\"Ruby\",12.04.1986,\"Department E\")."
            
            + "departments(\"Department A\",\"Olivia\")."
            + "departments(\"Department B\",\"Olivia\")."
            + "departments(\"Department C\",\"Olivier\")."
            + "departments(\"Department D\",\"Sophie\")."
            + "departments(\"Department E\",\"Jack\").";
        
        DlogParser parser = new DlogParser();
        parser.parse(program);
        
        List<IRule> rules = parser.getRules();
        
        Collection<IFacts> edbRelations = parser.getFacts();
        
        IEvaluator evaluator = new NaiveRecursiveEvaluator(rules);
        Collection<IFacts> idbRelations = evaluator.eval(edbRelations);
        
        assertEquals(3, idbRelations.size());
    }
}
