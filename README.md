# SystemBarUtils
---
安卓系统栏工具


# 依赖
---
```groovy
	dependencies {
		implementation 'com.brave.system.bar:library:1.0.1'
	}
```


# 使用
---

 1. 半透明

```java
    BarUtils.setTransparent(this);
```

 2. 透明

```java
	BarUtils.setTransparent(this);
```

 3. 设置颜色

```java
	BarUtils.setSystemBarColor(this, Color.BLUE);
```

4. 其他

```java
	BarUtils.setSystemBarColor(Window, View, boolean, boolean, boolean, boolean, boolean, boolean, int, int)
```

# 注意
    
- BarUtils、StatusBarUtils、NavBarUtils三类互斥，只能使用其中一种
- BarUtils 状态栏与虚拟按键栏会一起改变
- StatusBarUtils 只改变状态栏
- NavBarUtils 只改变虚拟按键栏