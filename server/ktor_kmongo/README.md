# shellNoteServer
A powerful note application using Kotlin, Ktor, and KMongo.
Refer to [simpleNote](https://github.com/cxyzy1/simpleNoteServer) for simple version.

It's a wonderful combination for build full reactive stack.
Compared to Springboot WebFlux, it's easier to use Ktor.
Usage:
1. Start local mongodb. In case of port other than 27017, change it in Modules.kt`"mongodb://localhost:27017"`
2. In the sample, notes is the db name which can be changed in Constants.kt
3. API List
- user/register 
- user/login
- note/list
- add note: note/add
- get note by note id: note/getNote
- get notes: note/getNotes
- update note: note/update
- trash note(recoverable): note/del
- del note(unrecoverable): note/del
- recover note: note/recoverFromTrash