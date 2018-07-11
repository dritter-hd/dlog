# Datalog for Java

Maybe the smallest recursive Datalog system available in Java. The module is OSGi-enabled and compatible with Android OS down to API level 7.

Does not cover:
- stratificatation, negation
- magic sets or any other advanced improvements

# Usage

Build using [Maven](https://maven.apache.org/): e.g., mvn clean install

Maven coordinate:
```
<dependency>
  <groupId>dlog</groupId>
  <artifactId>dlog</artifactId>
  <version>1.0.0</version>
</dependency>
```

## License

Apache 2.0 license [here](https://github.com/dritter-hd/dlog/blob/master/LICENSE)

## Citation

If you find this work useful for your research, please cite (for which the library was initially developed):
```
@inproceedings{DBLP:conf/datalog/RitterW12,
  author    = {Daniel Ritter and
               Till Westmann},
  title     = {Business Network Reconstruction Using Datalog},
  booktitle = {Datalog in Academia and Industry - Second International Workshop,
               Datalog 2.0, Vienna, Austria, September 11-13, 2012. Proceedings},
  pages     = {148--152},
  year      = {2012}
}

and later used on Android as well:
@unknown{unknown,
  author = {Ritter, Daniel and Holzleitner, Manuel},
  year = {2018},
  month = {01},
  pages = {278-291},
  title = {Toward Resilient Mobile Integration Processes},
  isbn = {978-3-319-93930-8}
}
```
