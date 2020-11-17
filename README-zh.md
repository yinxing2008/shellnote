# [客户端方案介绍](https://www.jianshu.com/p/133b0a42dc34)

# 背景介绍
出于对技术的热忱,一直再研究笔记类和清单类应用,希望可以打造一个类似的应用.笔记类应用和其他应用有一个很大的不同,数据会涉及到多端同步(一般app每次直接从服务器获取数据,不在本地保存数据). 当某个设备上,数据发生变更后,就需要同步到服务器.当服务器上有数据变更后,其他设备就应该在合适的时机把数据同步下来. 这种机制适用于一大类应用,而不止局限于笔记app,如网盘app也类似,只是具体同步的数据结构存在差异.
在研究实现方案的过程中,帮助最大是[印象笔记开放同步方案](http://dev.evernote.com/media/pdf/edam-sync.pdf)和[Leanote开发实现源码](https://github.com/leanote).

# 整体方案简述
每个用户上记录一个userMaxUsn;每条用户数据都有一个usn,每次数据变更,就把用户上的userMaxUsn加1,然后将此值作为该条数据的usn.客户端通过usn比较,判断本地和服务器的数据,到底哪个新,如果服务器新,就下载更新.

# 客户端实现方案详述
1. 同步时机
  1). 启动app进行一次同步
  2). 每次变更本地笔记,进行一次同步
  3). 每次刷新,进行一次同步
2. [同步流程详图](https://www.processon.com/view/link/5cde837be4b06c0492f446ee)
![](https://upload-images.jianshu.io/upload_images/6169789-4d338d4298f477dd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
3. 主要技术点
    Coroutines、ViewModel、LiveData、Retrofit、Paging、Room

# 源代码
https://github.com/cxyzy1/shellnote/tree/master/client/kotlin

#附录
1. [印象笔记开放同步方案](http://dev.evernote.com/media/pdf/edam-sync.pdf)
2. [Leanote开发实现源码](https://github.com/leanote)
