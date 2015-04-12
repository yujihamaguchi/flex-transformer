# flex-transformer

URL（複数可)で指定されたファイル内の文字列に対し、指定された文字列置換を行い、標準出力および、カレントディレクトリへのファイル出力(output.log)を行う。
  
![Class Diagram](https://github.com/yujihamaguchi/flex-transformer/blob/master/doc/class_diagram.png)

## Building

[leiningen](http://leiningen.org/)を使用する。

```
$ lein deps
$ lein uberjar
```

## Usage

```
$ java -jar flex-transformer-0.1.0-standalone.jar {置換対象文字列} {置換文字列} {URL}(複数可)
```

## Examples

[コマンドラインテストのサンプル](https://github.com/yujihamaguchi/flex-transformer/blob/master/doc/commandline_test_sample.md)を参照

### License

Copyright © 2015 Yuji Hamaguchi

Distributed under the Eclipse Public License, the same as Clojure.
