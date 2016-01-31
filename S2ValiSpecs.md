## 概要 ##



S2Vali（S2 Commons Validator Interface）は、インタフェースに対しアスペクトを適用し、commons-validatorによる検証処理を簡単に実現する事が出来ます。

## セットアップ ##

S2Vali は seasar2と一緒に使います。JDK1.4以上が必要です。

ダウンロードした s2vali-x.x.x.zip を解凍すると s2valiディレクトリが出来ます。
そのなかのlibディレクトリにある「s2vali-x.x.x.jar」をクラスパスに含めて下さい。

他、必要となるjarファイルもlibディレクトリ内にありますが、その内、S2Vali本体が必要とするものは以下になります。

  * commons-validator-1.3.1.jar
  * oro-2.0.8.jar

### maven2を使う場合 ###

maven2から使用することも出来ます。

  * リポジトリの追加
```
		<repository>
			<id>my.honestyworks.jp</id>
			<name>Honestyworks Open Repository</name>
			<url>http://my.honestyworks.jp/maven2</url>
		</repository>
```

  * dependencyの追加
```
        <dependency>
            <groupId>jp.honestyworks</groupId>
            <artifactId>s2vali</artifactId>
            <version>0.6.0</version>
        </dependency> 
```
バージョンは適宜変更してください。


## 基本的な使い方(tutorial) ##

まずは、何もカスタマイズせずに使う方法を見ていきます。

### 1. バリデーション定義ファイルを作る ###

s2vali-formset.xml をclasspath直下(src/main/resourcesなど)にコピーし、検証を行いたいformset(=bean)の定義をします。このファイルは struts-1.2等で使っているcommons-validatorの設定ファイルと同じ形式です。

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE form-validation PUBLIC
     "-//Apache Software Foundation//DTD Commons Validator Rules Configuration 1.3.0//EN"
     "http://jakarta.apache.org/commons/dtds/validator_1_3_0.dtd">

<form-validation>

    <formset>
        <form name="form3">
            <field property="seq_no" depends="required,mask">
                <msg key="s2vali.required_seq_no" name="required" />
                <var>
                    <var-name>mask</var-name>
                    <var-value>^[0-9]+$</var-value>
                </var>
            </field>
        </form>
    </formset>

</form-validation>
```

### 2. インターフェースを定義する ###

次に Validationメソッドのインターフェースを定義します。Validationメソッドの引数には、検証対象のJavaBeans(またはMap)を渡します。

例えば、foo.bar.vali というパッケージに CsvVali というインターフェースを以下のような形で定義します。
```
public interface CsvVali {

        // JavaBeansで検証する場合
	String[] validateForm3(Form3Bean dto);

        // Mapで検証する場合
	String[] validateForm3(Map dto);

}
```

メソッドの定義は以下のルールに従います。

  * メソッド名は validateで始まり、

&lt;form name="form3"&gt;

 で定義したフォーム名(Capitalizeしたもの)が続きます。
  * 引数にはJavaBeansまたはMapをとります。(検証対象のオブジェクト)
  * 戻り値は String[.md](.md) または ErrorMessage[.md](.md)(後述) とします。


### 3. Aspectの設定 ###

2で作成したインターフェースをSeasar2のコンポーネントとして登録します。その際にS2ValiIntercepotorをセットします。 app.dicon等に以下のように定義します。

  * includeの追加
```
    <include path="s2vali.dicon"/>
```
  * componentの追加
```
    <component class="foo.bar.vali.CsvVali">
        <aspect>s2ValiInterceptor</aspect>
    </component>
```

### 4. 使う ###

3で設定したコンポーネントをSeasar2から取得してメソッドを呼び出すことで検証が実行されます。
検証エラーが発生した場合は、String[.md](.md) にエラーメッセージがセットされます。検証がすべてOKだった場合はnullが返ります。
```

    @Resource
    private CsvVali csvVali;

    public void doSomething() {

        String[] errors = csvVali.validateForm3(dto);
        if (errors != null) {
            ～～～～～～～～～
        }

        ～～～～～～～～～～～
    }

```

エラーメッセージは jarファイル内の validatorMessages\_ja.propertiesに定義されています。
(現在のところ、jaロケールにのみ対応しています。)

### 5. 戻り値について ###

検証メソッドの戻り値としては、String[.md](.md) または ErrorMessages[.md](.md) を使用することが出来ます。

  * String[.md](.md)
    * フォーマットされたメッセージの配列です。
    * プレースホルダー({0})の値は、

&lt;arg&gt;

タグの内容で置き換えられます。

  * ErrorMessages[.md](.md)
    * message, messageKey, argsをプロパティに持つオブジェクトです。
    * message: エラーに該当するメッセージです。(リソースファイルから取得したもの)
      * (例) {0}を入力してください。
    * messageKey: XMLで定義されたkeyの値です。
      * (例) errors.required
    * args: 

&lt;arg&gt;

タグで設定した内容の配列です。


## カスタマイズ ##

[カスタマイズ方法](S2ValiCustomize.md)

## 今後の課題 ##
  * 引数を Object... でも大丈夫なようにする。
  * teedaとの連携。(FacesMessageUtil.addErrorMessages() でエラーを書き込む。)
  * SAStrutsとの連携。(できるのか。。)
  * リソースファイルのLocale対応。。