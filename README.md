# springScheduler
#[SpringBatch]

###[Common]
````
Alert : 카카오 알림톡/SMTP 발송 프로세스
Util : 자주 쓰이는 Util 프로세스
````
###[Controller] - 초기 접근 방식 구현
````
BatchController : Spring에서 제공하는 스케쥴러를 따름
CommonController : 공통 컨트롤러
````

###[service] - 실제 서비스 구현
````
BatchService : 스케쥴러에 따른 서비스 구현 
CommonService : 공통 서비스 구현
````

###[dao & daoImp] -  Mapper역할

````
BatchDao : 스케줄러 Interface
BatchDaoImp : 스케줄러 Mapper
CommonDao : 공통 Interface
CommonDaoImp : 공통 Mapper
````

###[model] - 편의성을 위해 필수 Model 구현
````
SmtpBody : 알림발송을 위한 객체 클래스
````
#How to Build
````
Build : maven package
````

#[INFO]
|TYPE|Public IP|Private IP|Port|Service|config|
|--------|:---------:|:---:|:---:|:------|:-----:|
|**운영**|test.ooweat.com |172.29.100.11|7881|springScheduler |application.properties에 따름|

#[PROD]
```
Path: /app/ooweat/springScheduler
ShellScript: service.sh
CMD Option: service.sh start, service.sh stop, service.sh restart 
```
