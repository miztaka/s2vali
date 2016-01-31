# S2Vali #

Seasar2で commons-validatorを簡単に使うためのフレームワークです。

主な用途：
  * アップロードしたCSVファイルの検証に
  * SOAPサービスの入力値検証に
  * バッチプログラム等の入力値検証に

各種MVCフレームワークに付属しているValidatorで実現できない箇所に使用することを想定しています。

## ニュース ##

  * [2008/12/04] s2vali-0.6.0 をリリースしました。

## 使い方 ##

  1. 検証用XMLを書いて、
  1. インターフェースを定義して、
  1. s2valiInterceptorをaspectで追加する。

...詳しくは[こちら](S2ValiSpecs.md)

## リンク ##

  * [ドキュメント](S2ValiSpecs.md)
  * [カスタマイズ方法](S2ValiCustomize.md)

## ダウンロード ##

  * [s2vali-0.6.0-bin.zip](http://s2vali.googlecode.com/files/s2vali-0.6.0-bin.zip)

## maven2から使用 ##

  * リポジトリ
```
<repository>
  <id>my.honestyworks.jp</id>
  <name>Honestyworks Open Repository</name>
  <url>http://my.honestyworks.jp/maven2</url>
</repository>
```

  * dependency
```
<dependency>
  <groupId>jp.honestyworks</groupId>
  <artifactId>s2vali</artifactId>
  <version>0.6.0</version>
</dependency> 
```
バージョンは適宜変更してください。