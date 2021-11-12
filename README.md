# alpha-color

![Build](https://github.com/chengzijian/alpha-color/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

## Description

<!-- Plugin description -->
This is an android studio plugin that allows you to creates new color in hex format based on a percentage (0-100) and a
base color you specify i.e. 

#### Example 1:

```xml

<color name="black">#000000</color>
```

when you supply **87** as the percentage, results in

```xml

<color name="black_87">#DE000000</color>
```

#### Example 2:

```kotlin
val textView = TextView(context)
textView.setTextColor(Color.parseColor("#000000"))
textView.setTextColor(0XFF000000)
```

when you supply **50** as the percentage, results in

```kotlin
val textView = TextView(context)
textView.setTextColor(Color.parseColor("#80000000"))
textView.setTextColor(0X80000000)
```

#### Example 3:

```xml

<TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="alpha-color"
        android:textColor="#000000"/>
```

when you supply **10** as the percentage, results in

```xml

<TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="alpha-color"
        android:textColor="#1A000000"/>
```

<!-- Plugin description end -->

### Usage

1. Hop over to your "color code" and press alt + cmd (on windows Enter).
2. Click on `Generate alpha variant`.
3. Key in the percentage and click OK

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "alpha-color"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/chengzijian/alpha-color/releases/latest) and install it manually
  using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

---

- Plugin based on the [IntelliJ Platform Plugin Template][template].
- Plugin based on the [alpha-bet][template2].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

[template2]: https://github.com/humblerookie/alpha-bet