# MVVM By Kotlin

## Line

![MVVM流程图](mvvm-line.drawio.png)

## MVVM Basic Theme

### Build.Gradle

> **添加项目对`jetpack-databinding`的支持**

```gradle
android {
    dataBinding {
        enabled = true
    }
}
```

### Model(实体类)

```kotlin
data class Animal(val name: String, var shoutCount: Int)
```

### View-Model(视图模型类)

- **用来处理Model和View层交互的中间类型**

```kotlin
class AnimalViewModal(private val animal: Animal) {
    // data
    val info = ObservableField<String> ("${animal.name} shout ${animal.shoutCount}")
    // binding
    fun shout() {
        animal.shoutCount++
        info.set("${animal.name} shout ${animal.shoutCount}")
    }
}
```

### View(视图类)

```kotlin
class AnimalActivity : AppCompatActivity() {
    lateinit var mViewMode: AnimalViewModal
    lateinit var mBinding: AnimalActivityBinding // Build过程中生成
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main) // xml与Binding对象映射对应
        val animal = Animal("Dog", 0) // 初始化对象
        mViewMode = AnimalViewModal(animal) // 赋值给变量
        mBinding.vm = mViewMode // vm与UI的xml文件的vm, 必须先声明UI里的, 才能再View层调用
    }
}
```

### UI

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <!-- 自定义对应映射的Binding类名称, 给View层使用和映射 -->
    <data class="com.example.app.view.AnimalActivityBinding">
        <!-- 
			name: 定义变量名字, 给view层调用
			type: 指定绑定的ViewModel层location
		-->
        <variable
            name="vm"
            type="com.example.app.view_modal.AnimalViewModal" />
    </data>
    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context="com.example.app.view.AnimalActivity">
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/info_tv"
            android:text="@{vm.info}" // 调用ViewModel层里的info属性
            android:layout_marginBottom="24dp"
            android:layout_gravity="center"/>
        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/action_btn"
            android:textAllCaps="false"
            android:text="shout"
            android:layout_marginTop="24dp"
            android:layout_gravity="center"
            android:onClick="@{() -> vm.shout()}" // lambda表达式, 指向和调用viewModel层里的shout()方法
                />
    </FrameLayout>
</layout>
```

-----



## MVVM Network Request with Retrofit & Rx Things

### Basic Demo

#### Implements

```gradle
// Rx Things
implementation 'io.reactivex.rxjava3:rxkotlin:3.0.1'
implementation group: 'io.reactivex.rxjava2', name: 'rxandroid', version: '2.1.1'
// Retrofit
implementation group: 'com.squareup.retrofit2', name: 'retrofit', version: '2.9.0'
implementation group: 'com.squareup.retrofit2', name: 'converter-gson', version: '2.9.0'
implementation group: 'com.squareup.retrofit2', name: 'adapter-rxjava2', version: '2.9.0'
// Gson
implementation group: 'com.google.code.gson', name: 'gson', version: '2.8.6'
```

#### Model

```kotlin
data class Article(
    var id: Int = 0,
    var title: String?,
    var readme: String?,
    var describe: String?,
    var click: Int = 0,
    var channel: Int = 0,
    var comments: Int = 0,
    var stow: Int = 0,
    var upvote: Int = 0,
    var downvote: Int = 0,
    var url: String?,
    var pubDate: String?,
    var thumbnail: String?
) {
    var content: String? = null
}
```

#### Service

```kotlin
import com.example.app_2.model.Article
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PaoService {
    @GET("article_detail.php")
    fun getArticleDetail(@Query("id") id: Int): Single<Article>
}
```

#### View-Model

```kotlin
import android.annotation.SuppressLint
import androidx.databinding.ObservableField
import com.example.app_2.model.Article
import com.example.app_2.service.PaoService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PaoViewModel(private val remote: PaoService) {
    val articleDetail = ObservableField<String>()
    @SuppressLint("CheckResult")
    fun loadArticle() {
        remote.getArticleDetail(8773)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                t: Article? -> articleDetail.set(t?.toString())
            }, {
                t: Throwable? -> articleDetail.set(t?.message?:"error")
            })
    }
}
```

#### View

```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var mBinding: MainActivityBinding
    lateinit var mViewMode: PaoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val remote = Retrofit.Builder()
            .baseUrl("http://api.jcodecraeer.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PaoService::class.java)
        mViewMode = PaoViewModel(remote)
        mBinding.vm = mViewMode
    }
}
```

#### UI

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <data class="com.example.app_2.view.MainActivityBinding">
        <variable
            name="vm"
            type="com.example.app_2.view_model.PaoViewModel" />
    </data>
    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context="com.example.app_2.view.MainActivity">

        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/action_btn"
            android:textAllCaps="false"
            android:text="shout"
            android:layout_marginTop="24dp"
            android:layout_gravity="center"
            android:onClick="@{() -> vm.loadArticle()}"/>
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/info_tv"
            android:text="@{vm.articleDetail}"
            android:layout_marginBottom="24dp"
            android:layout_gravity="center"
            tools:text="Click Button"/>
    </FrameLayout>
</layout>
```

#### Retrofit Description

##### 1. PaoService included

```kotlin
interface PaoService {
    @GET("article_detail.php")
    fun getArticleDetail(@Query("id") id: Int): Single<Article>
}
```

- **`@Get`** 类似于**`SpringBoot`**的**`@GetMapping()`**
- **`@Guery()`** 类似于**`SpringBoot`**的**`@RequestParam`**

##### 2. MainActivity included

```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var mBinding: MainActivityBinding
    lateinit var mViewMode: PaoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val remote = Retrofit.Builder()
            .baseUrl("http://api.jcodecraeer.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PaoService::class.java)
        mViewMode = PaoViewModel(remote)
        mBinding.vm = mViewMode
    }
}
```

- **`Builder()`** 构建Retrofit对象
- **`baseUrl()`** 请求的地址 根路径
- **`addcallAdapterFactory()`**
- **`GsonConverterFactory()`** 实现Gson转换, 将请求来的数据映射到Single<Article>

##### 3. Step By Step to use Retrofit

- **创建服务接口** 用来存放请求
- **定义方法和方法的请求方式**
- **方法参数为请求的Request Parameter**
- **再使用方法的地方, 构建Retrofit对象**