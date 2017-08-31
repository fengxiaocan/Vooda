第一步: 在根目录下的build.gradle 中添加 JitPack依赖:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
第二步:在app目录下的build.gradle 中添加dependency依赖包:

	dependencies {
	    compile 'com.github.fengxiaocan:Vooda:v1.0.4'
	}

第三步:在AndroidManifest清单文件application键中添加构建时间代码(复制粘贴):

    <application ...>
        <!--构建时间-->
        <meta-data
              android:name="BUILD_TIME"
              android:value="${EVIL_BUILD_TIME}"/>
    </application>
    
第四步:在app目录下的build.gradle 中最下面添加这段代码(复制粘贴):

    def buildTime() {
        def df = "evil"+System.currentTimeMillis()
        return df
    }

第五步:在app目录下的build.gradle 中android{}里添加这段代码:
    其中在debug{}内部添加可以在调试的时候判断带上有效期,TestVersion的名字可以自定义,但名字首字母必须大写

    android {
        ......
        buildTypes {
            debug {
                ......
                //这是在调试的时候带上有效期
                manifestPlaceholders = [EVIL_BUILD_TIME: buildTime()]
            }
        }
        productFlavors {
            ......
            /*客户验证版,有效期为自定义*/
            TestVersion {
                manifestPlaceholders = [EVIL_BUILD_TIME: buildTime()]
            }
        }
    }
    
第六步:在某个Activity内调用CheckOutTime.checkTime()方法:

    /**
     * 检测时间是否过期
     *
     * @param outTime
     *        时间,以毫秒为单位,1000L为去1秒;
     * @param callback
     *        结果回调,如果为true,则finish所有Activity,并跳转VoodaActivity
     */
    checkTime(long outTime,TimeOutCallback callback);

    /**
     * 检测时间是否过期
     *
     * @param outDay
     *        时间,以天为单位,1为去1天;
     * @param callback
     *        结果回调,如果为true,则finish所有Activity,并跳转VoodaActivity
     */
    checkTime(int outDay,TimeOutCallback callback)
        
实现TimeOutCallback接口,在mothod(boolean b)方法中判断b是否为true,
如果为true,跳转VoodaActivity,并关闭所有的Activity,取消所有延迟跳转到其他Activityd的方法或线程.
ok,就这样!打正式包的时候选择TestVersion版本号,即为打上有效期。无限期正式版的时候请去除CheckOutTime.checkTime()方法。