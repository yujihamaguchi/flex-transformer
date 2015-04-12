## On [Amazon Linux](https://aws.amazon.com/amazon-linux-ami/2014.09-release-notes/)
```
$ java -version
java version "1.7.0_75"
OpenJDK Runtime Environment (amzn-2.5.4.0.53.amzn1-x86_64 u75-b13)
OpenJDK 64-Bit Server VM (build 24.75-b04, mixed mode)
```

# 正常系
- 単一URL
```
$ java -jar ./target/flex-transformer-0.1.0-SNAPSHOT-standalone.jar "hoge" "fuga" "http://flex-transformer-test-html-1.s3-website-ap-northeast-1.amazonaws.com/"
[test1]
fuga
$ cat ./output.log
[test1]
fuga
```

- 複数URL(2つ)
```
$ java -jar ./target/flex-transformer-0.1.0-SNAPSHOT-standalone.jar "hoge" "fuga" "http://flex-transformer-test-html-1.s3-website-ap-northeast-1.amazonaws.com/" "http://flex-transformer-test-html-2.s3-website-ap-northeast-1.amazonaws.com/"
[test1]
fuga
[test2]
fuga
ほげfuga
fugaほげ
$ cat ./output.log
[test1]
fuga
[test2]
fuga
ほげfuga
fugaほげ
```

- 複数URL(3つ)
```
$ java -jar ./target/flex-transformer-0.1.0-SNAPSHOT-standalone.jar "hoge" "fuga" "http://flex-transformer-test-html-1.s3-website-ap-northeast-1.amazonaws.com/" "http://flex-transformer-test-html-2.s3-website-ap-northeast-1.amazonaws.com/" "http://flex-transformer-test-html-3.s3-website-ap-northeast-1.amazonaws.com/"
[test1]
fuga
[test2]
fuga
ほげfuga
fugaほげ
[test3]
捕鯨
fuga
$ cat ./output.log
[test1]
fuga
[test2]
fuga
ほげfuga
fugaほげ
[test3]
捕鯨
fuga
```

- マルチバイト文字列指定置換
```
$ java -jar ./target/flex-transformer-0.1.0-SNAPSHOT-standalone.jar "ほげ" "ホゲ" "http://flex-transformer-test-html-2.s3-website-ap-northeast-1.amazonaws.com/"
[test2]
hoge
ホゲhoge
hogeホゲ
$ cat ./output.log
[test2]
hoge
ホゲhoge
hogeホゲ
```

# 異常系
- パラメータ不足
```
$ java -jar ./target/flex-transformer-0.1.0-SNAPSHOT-standalone.jar "hoge" "fuga"
#<IllegalArgumentException java.lang.IllegalArgumentException: Wrong number of args.>
```

- 指定したURLにリソースが存在しない
```
$ java -jar ./target/flex-transformer-0.1.0-SNAPSHOT-standalone.jar "hoge" "fuga" "http://flex-transformer-test-html-4.s3-website-ap-northeast-1.amazonaws.com/"
#<IllegalArgumentException java.lang.IllegalArgumentException: Can't connect to (http://flex-transformer-test-html-4.s3-website-ap-northeast-1.amazonaws.com/) - Response Code 404.>
```
