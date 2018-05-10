# React Native: react-native-idrecognition (Android)
react-native Android 身份证号码（正面）/有效期限（反面）识别 （实时）

### 本地识别基于tess_two，位置对的话识别准确率达到90%以上


###Getting started
-
package.json 添加

```
  "dependencies": {
    "react-native-IDRecognition": "git+https://github.com/ribuluo000/IDCardRecognition.git",
  },
```

```
$ npm i
$ react-native link react-native-android-auto-update
```

####Android

- Add below snippet in your app setting.gradle file

```
include ':react-native-IDRecognition'
project(':react-native-IDRecognition').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-IDRecognition/android')
```


- Insert the following lines inside the dependencies block in android/app/build.gradle:

```
    compile project(':react-native-IDRecognition')
```


- Add below snippet in your app app/***/MainApplication.java file

Add 
`import com.nick.idcard.IDRecognitionPackage;
 import com.nick.idcard.AssestUtils;`
 to the imports at the top of the file

```



@Override
protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
        new MainReactPackage(),
        new IDRecognitionPackage()
        );
}



  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    new AssestUtils(this).init();
  }
  
```

- copy files in `tessdata` to `android/app/src/main/assets/tessdata`

###Usage

```

import React, { Component } from "react";
import { StyleSheet, Text, TouchableOpacity, View } from "react-native";
import RNIDRecognition from "react-native-IDRecognition";

export default class App extends Component<> {
    render() {
        return (
            <View style={styles.container}>
                <View style={{ flex : 0, flexDirection : 'row', justifyContent : 'center', }}>
                    <TouchableOpacity
                        onPress={this.showIdRecognitionFrontScreen.bind(this)}
                        style={styles.capture}
                    >
                        <Text style={{ fontSize : 14 }}> showIdRecognitionFrontScreen </Text>
                    </TouchableOpacity>
                </View>
                <View style={{ flex : 0, flexDirection : 'row', justifyContent : 'center', }}>
                    <TouchableOpacity
                        onPress={this.showIdRecognitionBackScreen.bind(this)}
                        style={styles.capture}
                    >
                        <Text style={{ fontSize : 14 }}> showIdRecognitionBackScreen </Text>
                    </TouchableOpacity>
                </View>
            </View>
        );
    }

    showIdRecognitionFrontScreen = async function () {
        // alert('111');
        RNIDRecognition.showIdRecognitionFrontScreen({}, (data) => {
            console.log(data);
            alert(data);
        });
    };
    showIdRecognitionBackScreen = async function () {
        // alert('111');
        RNIDRecognition.showIdRecognitionBackScreen({}, (data) => {
            console.log(data);
            alert(data);
        });
    };

}

const styles = StyleSheet.create({
    container : {
        flex : 1,
        flexDirection : 'column',
        backgroundColor : 'black'
    },
    preview : {
        flex : 1,
        justifyContent : 'flex-end',
        alignItems : 'center'
    },
    capture : {
        flex : 0,
        backgroundColor : '#fff',
        borderRadius : 5,
        padding : 15,
        paddingHorizontal : 20,
        alignSelf : 'center',
        margin : 20
    }
});

```


###Example
-
Try the included [example/TestCamera](https://github.com/ribuluo000/IDCardRecognition/tree/master/example/TestCamera)

- `cd example/TestCamera/`
- `npm i`
- `react-native run-android`





###参考链接

[React Native 与 Android原生Activity互相跳转页面 https://www.2cto.com/kf/201710/694451.html](https://www.2cto.com/kf/201710/694451.html)

[Android中保存图片到本地功能实现 https://blog.csdn.net/samuel__liu/article/details/50743106](https://blog.csdn.net/samuel__liu/article/details/50743106)

[压缩图片 Android压缩图片后再上传图片 https://blog.csdn.net/dzsw0117/article/details/51492172](https://blog.csdn.net/dzsw0117/article/details/51492172)

[RN 和原生端通信 https://reactnative.cn/docs/0.51/communication-android.html#content](https://reactnative.cn/docs/0.51/communication-android.html#content)

[RN 原生模块 https://reactnative.cn/docs/0.51/native-modules-android.html](https://reactnative.cn/docs/0.51/native-modules-android.html)

[Android里使用正则表达式 https://blog.csdn.net/lyy1104/article/details/40507537](https://blog.csdn.net/lyy1104/article/details/40507537)

[15/18位身份证号码验证的正则表达式总结（详细版） http://www.jb51.net/article/109384.htm](http://www.jb51.net/article/109384.htm)
[身份证后面有效期限验证的正则表达式 ([0-9]{4}).([0-9]{2}).([0-9]{2})-([0-9]{4}).([0-9]{2}).([0-9]{2})]

[Android 身份证号码识别 （本地，在线，实时） https://github.com/bertsir/IDCardRecognition](https://github.com/bertsir/IDCardRecognition)


[Tesseract OCR wrapper for React Native https://github.com/jonathanpalma/react-native-tesseract-ocr](https://github.com/jonathanpalma/react-native-tesseract-ocr)


