# Datalog for Java [![Build Status](https://travis-ci.org/dritter-hd/dlog.svg?branch=master)](https://travis-ci.org/dritter-hd/dlog)

Maybe the smallest recursive Datalog system available in Java (jar file size < 150kB). The module is OSGi-enabled and compatible with Android OS down to [Android Eclair API level 7](https://en.wikipedia.org/wiki/Android_version_history#Android_2.1_Eclair_(API_7)).

Does not cover:
- stratificatation, negation
- magic sets or any other advanced improvements

# Usage

Build using [Maven](https://maven.apache.org/): e.g., `mvn clean package`

Maven coordinate:
```
<dependency>
  <groupId>com.github.dritter-hd</groupId>
  <artifactId>dlog</artifactId>
  <version>1.0.0</version>
</dependency>
```

The following program code illustrates the usage of the parser and evaluator. Considering a datalog program, the parser allows to differentiate facts and rules, which are then used to parameterize one of the evaluators (e.g., rules -> NaiveRecursiveEvaluator) and then evaluate the rules for the facts or edbRelations. 

```
  final String program = "tc(X,Y) :- edge(X,Y)." + "tc(X,Y) :- edge(X,Z), tc(Z,Y)." 
  	+ "edge(\"a\",\"b\")." + "edge(\"b\",\"c\")."
	+ "edge(\"c\",\"b\")." + "edge(\"c\",\"d\").";
  final DlogParser parser = new DlogParser();
  parser.parse(program);

  final List<IRule> rules = parser.getRules();
  final Collection<IFacts> edbRelations = parser.getFacts();

  final IEvaluator evaluator = new NaiveRecursiveEvaluator(rules);
  final Collection<IFacts> idbRelations = evaluator.eval(edbRelations);
```

The log output will print the actual datalog plan that is executed, which helps to understand, how the rules are evaluated. For the example datalog program, the following plan is constructed:

```
UnionAll {
  Projection {
    0 1 
    Table {
      edge(_tc0, _tc1)
    }
  }
  Projection {
    0 3 
    Join {
      Z_1 = Z_2 
      Table {
        edge(_tc0, Z_1)
      }
      Table {
        tc(Z_2, _tc1)
      }
    }
  }
}
```

## License

Apache 2.0 license [here](https://github.com/dritter-hd/dlog/blob/master/LICENSE)

## Citation

If you find this work useful for your research, please cite (for which the library was initially developed):
```
@incollection{ritter2012business,
  title={Business network reconstruction using datalog},
  author={Ritter, Daniel and Westmann, Till},
  booktitle={Datalog in Academia and Industry},
  pages={148--152},
  year={2012},
  publisher={Springer}
}
```

or when used on Android:
```
@inproceedings{ritter2018toward,
    title={Toward Resilient Mobile Integration Processes},
    author={Ritter, Daniel and Holzleitner, Manuel},
    booktitle={International Conference on Business Information Systems},
    pages={278--291},
    year={2018},
    organization={Springer}
}
```

[![Donate](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=4BHCBCD9WRYS6&source=url)

or

<a href="https://www.buymeacoffee.com/dritter" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me A Coffee" style="height: 60px !important;width: 217px !important;" ></a>
