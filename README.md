# ChaDao-茶岛

<img src="app\src\main\res\mipmap-xhdpi\ic_launcher_round.webp" alt="ChaDao"  />

ChaDao（茶岛） 是一个基于 Jetpack Compose 构建的[X岛揭示板](https://www.nmbxd1.com/Forum)第三方离线阅读客户端。

项目当前以 Android 原生实现为主，围绕离线浏览、二维码导入饼干、图片查看和本地数据搜索做了较完整的移动端体验。

## 项目特性

- 离线下载帖串，按页拉取并持久化到本地 Room 数据库
- 支持增量更新已下载串，避免重复拉取全部内容
- 支持全文关键词搜索，统一检索串头和回复内容
- 支持查看po主与回复详情，并可跳转指定引用回复
- 支持二维码扫码或从相册识别二维码导入饼干
- 支持图片浏览、缩放查看与保存到系统相册
- 使用 DataStore 保存偏好与阅读状态
- 提供 GitHub Actions CI，覆盖单元测试、仪器测试、APK 构建与 Release 发布

## 技术栈

| 分类     | 技术                                |
| -------- | ----------------------------------- |
| 语言     | Kotlin                              |
| UI       | Jetpack Compose                     |
| 导航     | Navigation Compose                  |
| 网络     | Retrofit + OkHttp                   |
| 数据库   | Room + KSP                          |
| 图片加载 | Coil 3                              |
| 图片查看 | `com.jvziyaoyao.scale:image-viewer` |
| 本地存储 | DataStore Preferences               |
| 二维码   | ZXing Android Embedded              |
| 构建     | Gradle Kotlin DSL + AGP 8.6.0       |
| CI       | GitHub Actions                      |

## 项目结构

```text
.
|- app/
|  |- src/main/java/com/example/xddemo/
|  |  |- data/            # Repository、Room、数据模型
|  |  |- network/         # Retrofit API 与拦截器
|  |  |- ui/              # Compose 页面、组件、导航、ViewModel
|  |  |- MainActivity.kt
|  |  `- XDaoApplication.kt
|  |- src/main/res/       # 字符串、主题、图标等资源
|  `- proguard-rules.pro
|- .github/workflows/
|  `- android-ci.yml      # CI / 构建 / 签名发布
|- gradle/libs.versions.toml
`- README.md
```

## 架构说明

项目采用 `MVVM + Repository + 手动依赖注入` 的轻量结构：

1. `XDaoApplication` 初始化应用级依赖
2. `DefaultAppContainer` 组装 Retrofit、OkHttp、Room 与 Repository
3. `AppViewModelProvider.Factory` 为各 ViewModel 提供依赖
4. Compose UI 层通过 `StateFlow` 订阅数据并渲染页面

核心链路如下：

```text
Compose Screen
   -> ViewModel
      -> Repository
         -> Retrofit API
         -> Room Database
```

## 致谢

项目中使用了以下重要开源组件：

- Android Jetpack Compose
- Retrofit
- OkHttp
- Room
- Coil
- ZXing Android Embedded

感谢这些优秀项目为 Android 开发提供的基础能力。
