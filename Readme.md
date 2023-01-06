## DexDynamicLoad

dex动态加载(Kotlin版):<https://blog.csdn.net/DeMonliuhui/article/details/128255887>  

Android Kotlin反射全解析:<https://demon.blog.csdn.net/article/details/128257378>  

Android 动态加载启动dex中的Activity解决方案:<https://demon.blog.csdn.net/article/details/128567647>

Android 使用dx/d8将jar转换为dex:<https://demon.blog.csdn.net/article/details/128580370>

### jar转dex

#### dx
```
dx --dex --output=输出dex.jar 目标.jar
```
如：  
```
dx --dex --output=dexlib_dex.jar dexlib.jar
```

#### d8
```
d8 --output 输出 目标.jar
```

如：

```
d8 --output dexlib_dex.jar dexlib.jar
```