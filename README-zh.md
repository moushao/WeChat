# Homework 说明
## 项目架构
### 包名:com.tw.wechat
    
*    |----- MyApplication: App的Application,启动初始化GreenDao数据库和屏幕尺寸适配
*    |----- TweetActivity: Tweet列表界面
*    |----- TweetController: TweetActivity的业务控制器,用于加载tweet数据
*    |----- adapter
	* |-----BaseRecyclerViewAdapter: 抽象的BaseAdapter
   	    * |----- TweetMomentsAdapter: 朋友圈列表的Adapter
    * |----- BaseRecyclerViewHolder: 抽象的BaseHolder
    	* |----- CircleBaseViewHolder: 朋友圈的基本holder,用于处理公共数据和view
   		    * |----- MultiImageMomentsVH: 九宫格类型的Holder
    * |----- BaseMomentVH: TweetMomentsAdapter的抽象接口,用于子类的数据传递 	
    * |----- HostViewHolder: recycle头部、照片墙的hodler,
* |----- entity
	* |----- Comment: 朋友圈评论的实体类
    	* |----- Photo: 朋友圈照片的实体类
    	* |----- Tweet: 朋友圈tweet的实体类
        * |----- User: 用户实体类,与发送者一致
* |----- dao: entity中实体类GreenDao派生类
* |-----event
	* |-----OnRefreshListener: 上拉加载更多、下拉刷新的回调
    * |-----VCCallBack: vc模式,Control层和View层之间的回调借口
    * |----- ViewListener: 用于Adapter中与主界面的交互回调
* |----- retrofit
	* |-----Interceptor: 拦截器,用于Http请求时,请求头的设置
    * |----- RetrofitManager: Retrofit的管理类,初始化Retrofit,桥接Retrofit与OKHttp
    * |----- TweetApi: 朋友圈数据加载结果的回调接口
* |-----widget
	* |----- CircleViewHelper: 用于评论框弹出与隐藏时，辅助当前Item滑动的位置
    * |----- comment
    	* |----- CommentBox: 评论输入框
        * |----- CommentWidget: 文字评论展示控件
        * |----- ContentWidget: 朋友圈文字内容展示控件,实现"全文"和'收起"功能
    * |----- popup
    	* |----- CommentPopup: 朋友圈点赞、添加评论的Pop
        * |----- DeleteCommentPopup: 删除评论的popup
    * |----- pullryc
    	* |----- CircleRecyclerView: 自定义的下拉Recycle,加载Tweet列表
        * |----- PullRefreshFooter: 的加载更多footView
        * |----- wrapperadapter:recyclerview的header和footer试图位移的辅助类包
* |----- utils 
	* |----- Glide的TransForm: Glide的TransForm辅助类
	* |----- ViewOffsetHelper: 位移偏移辅助类
	* |----- TextStateManager: 文字展示状态辅助类
* |----- photo
	* |----- PhotoWidget: 图片展示控件,单图
    * |----- PhotoContents: 图片列表展示控件
       
       
## 完成内容

* ### 基本功能
	* 照片墙、用户信息、Tweet列表数据获取,异常数据处理
    * 机型版本适配,如SDK_INT小于16无法预览照片
    * Tweet、用户信息本地数据库保存,便于快速获取数据
    * 下拉刷新、分页加载(每次只加载五条)
    * 图片异常加载处理
    
* ### 扩展功能
    * 照片预览(API16+)
    * 文字内容的折叠与展示
    * 文字评论功能
    * 点赞响应事件,头像圆角加载
    * 刷新联动小圆圈
    
* ### 待完善
    * 软键盘被部分被遮挡问题
    * recycleview scroll的指定评论框的滑动

## 其他
* 3rd Libs参考根目录下config.gradle
* 特殊情况下需要使用VPN,才能访问到数据
* 个别图片的主机地址无法访问,采用占位图替换
* 最后结果,参考根目录下效果图.


### 谢谢所有人,谢谢!

    
    
    
    
    
