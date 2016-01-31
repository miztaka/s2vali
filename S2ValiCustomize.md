# カスタマイズについて #

## 1. エラーメッセージを変更したい ##

エラーメッセージを変更するには、validatorMessages\_ja.properties を上書きします。
このファイルはs2valiのjarファイル内に含まれていて、デフォルトではそちらが使われます。

また、プロパティファイルのファイル名を変更したい場合は、s2valiConfig.diconを上書きすることで可能です。

例えば、appMessages\_ja.propertiesを使いたい場合は以下のように resourceBundlePropertiesの値を変更します。

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE components PUBLIC "-//SEASAR//DTD S2Container 2.4//EN"
    "http://www.seasar.org/dtd/components24.dtd">
<components namespace="s2vali">

    <component name="validatorConfig"
        class="jp.honestyworks.s2vali.ValidatorConfig">
        
        <property name="resourceBundleProperties">
            "appMessages"
        </property>
        
        <property name="validationRules">
            "s2vali-rules.xml, s2vali-formset.xml"
        </property>
        
    </component>

</components>
```

## 2. s2vali-formset.xml のファイル名を変えたい ##

同じく s2valiConfig.diconを上書きすることで可能です。

validationRulesの値を変更します。

※ s2vali-rules.xmlは jarファイル内に含まれていて、Validatorを定義していますので含めるようにしてください。

## 3. 独自のValidatorを使用したい ##

s2vali-formset.xmlに追加定義することで使用可能です。
```
        <validator name="required"
                   classname="jp.honestyworks.s2vali.validator.DefaultValidator"
                   method="validateRequired"
                   methodParams="java.lang.Object, org.apache.commons.validator.Field"
                   msg="errors.required" />
```