# 나의 게시판 프로젝트

> 코드로 배우는 스프링 웹 프로젝트 공부하면서, 이것저것 바꿔본 프로젝트인데...
>
> 기능을 추가해서 고도화를 해보자!



## 프로젝트 사양

* Maven 빌드
* Java 21
* Spring 7
* Spring Security 7
* Tomcat 11




## Docker Compose 실행 배치 파일 목록

### 데이터베이스 (Oracle Free 23c) 관련

| 파일 | 설명 |
|------|------|
| `db-start.bat` | Oracle DB 컨테이너 생성 및 실행 (이미 있다면 재시작만 함). DB 준비 완료 시까지 대기 |
| `db-stop.bat` | Oracle DB 컨테이너 중지. 데이터 유지됨 |
| `db-clean.bat` | Oracle DB 컨테이너 및 볼륨 삭제. 데이터 완전 초기화 |

**DB 접속 정보:**
- JDBC URL: `jdbc:oracle:thin:@//localhost:1521/FREEPDB1`


### 웹 서버 (Tomcat 11) 관련

| 파일 | 설명 |
|------|------|
| `web-start.bat` | WAR 빌드 (필요시) → DB 준비 대기 → 웹 서비스 시작. 한 번에 모든 과정 완료 |
| `web-stop.bat` | 웹 컨테이너 중지 |
| `web-clean.bat` | 웹 컨테이너 중지 후 삭제 (DB/볼륨 영향 없음) |
| `web-restart.bat` | 웹 컨테이너 재시작 후 로그 표시 |
| `web-logs.bat` | 웹 로그 실시간 표시 (재시작 없음) |

**웹 접속 정보:**
- App URL: `http://localhost:8080/`

### 실행 순서 (처음 시작할 때)

```batch
# 1. 데이터베이스 시작
db-start.bat

# 2. 웹 서버 시작 (자동으로 DB 준비 확인 후 시작)
web-start.bat
```

### 일반적인 개발 워크플로우

```batch
# 시작
web-start.bat              # 자동으로 WAR 빌드 + DB 확인 + 웹 시작

# 개발 중 변경사항 적용
web-restart.bat            # 웹 컨테이너만 재시작

# 로그 확인
web-logs.bat

# 종료
web-stop.bat               # 웹 중지
web-clean.bat              # 웹 컨테이너 정리(삭제)
db-stop.bat                # DB 중지 (데이터 유지)

# 완전히 초기화하고 싶을 때
db-clean.bat               # DB 초기화 (데이터 삭제)
```

### 주의사항

1. **DB 먼저 시작**: `web-start.bat` 실행 전에 `db-start.bat`를 먼저 실행하거나, `web-start.bat`가 자동으로 DB를 준비합니다.
2. **WAR 빌드**: `web-start.bat`는 WAR 파일이 없으면 자동으로 `mvnw clean package -DskipTests`를 실행합니다.
3. **로컬 개발**: 로컬에서 `tomcat-run.bat`로 Cargo + Tomcat을 사용하거나, Docker로 웹 서버를 실행할 수 있습니다.
4. **데이터베이스 접속**: 
   - 로컬 실행: `localhost:1521` (기본값)
   - 컨테이너 실행: `oracle-free:1521` (자동 전환)
5. **✨ JDK/toolchains 설정 ✨**:
  - 프로젝트 루트의 `setenv-custom.properties`가 있으면 그 파일을 우선 사용하고, 없으면 `setenv.properties`를 사용합니다.
  - 선택된 properties 파일에 아래 3개 키를 반드시 설정해야 합니다.
    - `JAVA_HOME`
    - `JDK_VERSION`
    - `JDK_VENDOR`
  - `set-jdk-env.bat` 실행 시 `build-scripts/generate-toolchains.ps1`가 `toolchains.xml`을 자동 생성합니다.
  - `toolchains.xml`은 생성 파일이며 Git 추적 대상이 아닙니다.
  - `setenv-custom.properties`는 로컬 오버라이드용 파일이며 Git 추적 대상이 아닙니다.
