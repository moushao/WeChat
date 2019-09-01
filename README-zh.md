# Homework Overview

## Project Structure

### parkage name:com.tw.wechat
    
*    |----- **MyApplication**: App's Application,App's Application, for initialization GreenDao database and screen adaptation
*    |----- **TweetActivity**: Tweet list Activity
*    |----- **TweetController**: TweetActivity business controller for loading tweets data
*    |----- **adapter**
    * |**BaseRecyclerViewAdapter**: abstract BaseAdapter
    	* |----- **TweetMomentsAdapter**: Adapter for TweetActivity's tweets list
    * |----- **BaseRecyclerViewHolder**: abstract BaseHolder
    	* |----- **CircleBaseViewHolder**: The basic holder of the circle of friends, used to process public data and views
   		* |----- **MultiImageMomentsVH**: Holder of the nine-square pictures
    * |----- **BaseMomentVH**: Abstract interface for TweetMomentsAdapter andr for data passing by subclasses 	
    * |----- **HostViewHolder**: photo wall hodler,the head of Recyclerview,
* |----- **entity**
	* |----- **Comment**: Entity class of friend circle comments
    	* |----- **Photo**: Entity class of friend circle photos
    	* |----- **Tweet**: Entity class of friend circle tweets
        * |----- **User**: Entity class of friend circle user,same with sender
* |----- **dao**: entity class created by GreenDao derived class
* |-----**event**
	* |-----**OnRefreshListener**: the callbackk for refresh and loadMore listener
    * |-----**VCCallBack**: in vc model,the callBack between of Control and View
    * |----- **ViewListener**: the callBack for using in adapter to listening view's regular click event
* |----- **retrofit**
	* |-----**Interceptor**: interceptor,setting request header when Http requets
    * |----- **RetrofitManager**: Retrofit management class, initialize Retrofit, bridge Retrofit and OKHttp
    * |----- **TweetApi**: the callBack for  loading service data
* |-----**widget**
	* |----- **CircleViewHelper**: Used to assist the current item slide position when the comment box pops up and hides
    * |----- **comment**
    	* |----- **CommentBox**: A widget for intput comment text
        * |----- **CommentWidget**: A Widget for comment text display
        * |----- **ContentWidget**: A widget for Tweet's content display,and control it "open" or "close"
    * |----- **popup**
    	* |----- **CommentPopup**: the add comment's or like's pop
        * |----- **DeleteCommentPopup**: the delte comment's pop
    * |----- **pullryc**
    	* |----- **CircleRecyclerView**: Customized drop-down Recycle, loading the Tweet list wirh refreshing animation
        * |----- **PullRefreshFooter**: the footview of CircleRecyclerView's foot
        * |----- **wrapperadapter**:Auxiliary class for header and footer view moving
* |----- **utils** 
	* |----- **Glide的TransForm**: Auxiliary class of Glide TransForm
	* |----- **ViewOffsetHelper**: Auxiliary class of offset
	* |----- **TextStateManager**: Auxiliary class of save Tweet's content displaying state
* |----- **photo**
	* |----- **PhotoWidget**: Picture display widget, single picture
    * |----- **PhotoContents**: Picture list  diasplay widget,contains PhotoWidget
       
       
## Complete Content

* ### Basic
	* Photo Wall, User Information, Tweet List Data Acquisition, Abnormal Data Processing
    * SDK Adaptation(API less than 16,cannot preview picture)
    * Tweet、User information is saved in local database to facilitate quick access to data
    * Drop-down refresh, paging load (only five at a time)
    * Image Abnormal Loading Processing
    
* ### Extend

    * preview pictures(API 16+)
    * the text content's open or close
    * Text comment function
    * Point praise response event, head fillet loading
    * Refreshing small circle icon
    
* ### To Be Perfect
    * The comment input box is partially blocked by the soft keyboard
    * Recycleview slides to the exact location of the comment box

## Others
* 3rd Libs,reference root directory's config.gradle
* In special cases, VPN is needed to access data
* The host address of individual pictures is inaccessible and replaced by a placeholder picture
* Finally, refer to the effect picture in the root directory


### Thanks for everyone!


    
