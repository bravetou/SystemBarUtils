# SystemBarUtils
---
Android toolbar


# Dependency
---
```groovy
	dependencies {
		implementation 'com.brave.system.bar:library:1.0.1'
	}
```


# use
---

 1. Transparent

```java
    BarUtils.setTransparent(this);
```

 2. Transparent

```java
	BarUtils.setTransparent(this);
```

 3. set colors

```java
	BarUtils.setSystemBarColor(this, Color.BLUE);
```

4. Other

```java
	BarUtils.setSystemBarColor(Window, View, boolean, boolean, boolean, boolean, boolean, boolean, int, int)
```

# notice
    
- BarUtils StatusBarUtils NavBarUtils have three types of mutual exclusion and can only be used one of them
- BarUtils status bar and virtual button bar will be changed together
- StatusBarUtils only changes the status bar
- NavBarUtils only changes the virtual button bar