# VoucherPoolSvc

Hello!

So sorry that I cannot manage to finish this in time as I have other commitments (aka current job).
However, I've manage to get the bare bones running and hope it is enough for your consideration.

Prerequisite to running this project (besides java):
(Install MongoDB)
- Download and set up a MongoDB server: I'm working with version 4.0
- go to the /bin folder and run it on command prompt:
C:\Program Files\MongoDB\Server\4.0\bin> mongod.exe --dbpath C:\VoucherPoolData

(Create some data)
- To ease set up, having a GUI client helps: I'm using Robo3T free version
- create a db "voucherPool" 
- create collection "specialOffer" with unique index: {"name":1}
- create collection "voucher" with unique index: {"code":1}
- create collection "recipient" with unique index: {"code":1}
- insert several recipient data:
  {"name":"Dave", "email":"dave123@gmail.com"},
  {"name":"Alicia", "email":"allici@gmail.com"},
  {"name":"Sim", "email":"simmy@gmail.com"}

(Build and Run)
- clone this repo and go to the location of the repo
- run command: gradlew build && java -jar voucher-pool-service.app\build\libs\voucher-pool-service-0.1.0.jar

(Test)
- I prepared a swagger page for testing purposes, I sure hope it is intuitive enough.
- Do have a look: http://localhost:8080/swagger-ui.htm

