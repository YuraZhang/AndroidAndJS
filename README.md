## 关于通过WebView实现Android（Java）与JavaScript（HTML）交互

### WebView简介
1. WebView是一个基于webkit引擎、展现web页面的控件，作用是显示和渲染Web页面，直接使用html文件（网络上或本地assets中）作布局，可和JavaScript交互调用。
2. 一般来说WebView可单独使用，可联合其子类一起使用，WebView的最常用的子类为WebSettings类、WebViewClient类、WebChromeClient类。

### WebView配置
#### WebSettings类（作用：对WebView进行配置和管理）
1.  配置步骤1：添加访问网络权限(AndroidManifest.xml)

`<uses-permission android:name="android.permission.INTERNET" />`

2. 配置步骤2：生成一个WebView组件（）

//方式1：直接在Activity中生成

`WebView mWebView = new WebView(this);`


//方法2：在Activity的layout文件里添加webview控件

`WebView mWebview = (WebView) findViewById(R.id.webView);`

3. 配置步骤3：进行配置-利用WebSettings子类

//声明WebSettings子类

`WebSettings webSettings = mWebView.getSettings();` 

//如果访问的页面中要与Javascript交互，则webview必须设置ture

`webSettings.setJavaScriptEnabled(true);`

4. WebView加载方式 

//方式1. 加载一个网页

`mWebView.loadUrl("http://www.google.com/"); `

//方式2：加载apk包中的html页面 （将需要调用的JS代码以.html格式放到src/main/assets文件夹里）

`mWebView.loadUrl("file:///android_asset/web.html"); `


//方式3：加载手机本地的html页面

`mWebView.loadUrl("content://com.android.htmlfileprovider/sdcard/test.html");`

### Android（Java）与JavaScript（HTML）交互
  有以下四种情况：
1. Android（Java）调用HTML中js代码
2. Android（Java）调用HTML中js代码（带参数）
3. HTML中js调用Android（Java）代码
4. HTML中js调用Android（Java）代码（带参数）

**注意**：在3和4中 HTML中js调用Android（Java）代码 需要使用到如下方式：
通过WebView的addJavascriptInterface()进行对象映射,代码如下：

`mWebView.addJavascriptInterface(MainActivity.this,"android");`

其中“android”对应于下图中画横线部分



#### 第一种：Android（Java）调用HTML中js代码
`MainActivity.Java`

`mWebView.loadUrl("javascript:javacalljs()");`

javacalljs()方法在HTML中显示如下：

`web.html`

`function javacalljs(){
    document.getElementById("content").innerHTML =
         "<br\>JAVA调用了JS的无参函数";
	}`




#### 第二种：Android（Java）调用HTML中js代码（带参数）
`MainActivity.Java`

`mWebView.loadUrl("javascript:javacalljswith("+"'http://www.baidu.com/'" + ")");`

javacalljswith()方法在HTML中显示如下：

`web.html`

`function javacalljswith(arg){
    document.getElementById("content").innerHTML =
         ("<br\>"+arg);
	}`
	




#### 第三种：HTML中js调用Android（Java）代码
`MainActivity.Java`

`@JavascriptInterface
	public void startFunction(){
    	runOnUiThread(new Runnable() {
        	@Override
        	public void run() {
Toast.makeText(MainActivity.this,"show",Toast.LENGTH_SHORT).show();
	       }
    });
}`

`web.html`

`<body>
HTML 内容显示 <br/>
<h1><div id="content">内容显示</div></h1>
<br/>
<input type="button"  value="点击调用java代码" onclick=
"android.startFunction()" />
<br/>
<input type="button"  value="点击调用java代码并传递参数" onclick=
"android.startFunction('http://www.baidu.com/')"  />
</body>
`




#### 第四种：HTML中js调用Android（Java）代码（带参数）
`MainActivity.Java`

`@JavascriptInterface
	public void startFunction(final String text){
    	runOnUiThread(new Runnable() {
       	 @Override
        public void run() {
new AlertDialog.Builder(MainActivity.this).setMessage(text).show();
        }
    });
}`

`web.html`

`<body>
HTML 内容显示 <br/>
<h1><div id="content">内容显示</div></h1>
<br/>
<input type="button"  value="点击调用java代码" onclick=
"android.startFunction()" />
<br/>
<input type="button"  value="点击调用java代码并传递参数" onclick=
"android.startFunction('http://www.baidu.com/')"  />
</body>
`
