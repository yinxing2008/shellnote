# Preface
For interest on tech, I've done a lot of research on cloud notepad app. The most challenge for such app is data synchronization.
To make the experience more smooth, we have to store data in local devices instead of query from server each time.
And we have to take into account multiple devices which will make the situation more complicated.
After deep research on [evernote](http://dev.evernote.com/media/pdf/edam-sync.pdf) and [Leanote](https://github.com/leanote), I start this project.

# Introduction
- On Server Side  
Keep an userMaxUsn on each user record.  
Keep an usn on each note item which bound to one user.  
When note changes, increase userMaxUsn by one on user, and take the result as usn of the note.  
- On Client Side  
1. description
Client have to compare userMaxUsn on user and usn on each record to determine whether should download data.
Client should upload local changed too.
2. Synchronization  
  1). do it when app startup  
  2). when any changes happen  
  3). when user manually refresh  
[Synchronization Diagram](https://www.processon.com/view/link/5fb250fc637689283f8d5afc)
![](https://upload-images.jianshu.io/upload_images/6169789-c1d4aacce32fd466.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
3. Key Points
- Retrofit+okHttp: fetch data from network
- Coroutines: make asynchronous operations
- ViewPager2: swipe to change page
- Glide: show images
- Room: read and write on sqLite database
- BottomNavigationView: show bottom navigation
- Recyclerview: show list

# License
Copyright 2020 android007-cn

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.