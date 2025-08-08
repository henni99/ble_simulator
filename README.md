
## 목차
1. [개발 환경 및 빌드/실행 방법](#개발-환경-및-빌드실행-방법)  
2. [앱 구조 및 상태 관리 설계](#앱-구조-및-상태-관리-설계)  
3. [Mock 데이터 및 연결 시뮬레이션 구현](#mock-데이터-및-연결-시뮬레이션-구현)  
   - [DeviceListActivity](#devicelistactivity)  
   - [DataTransferActivity](#datatransferactivity)  
4. [테스트 방법 및 커버리지](#테스트-방법-및-커버리지)  

---

## 개발 환경 및 빌드/실행 방법

- **Android Studio**: Meerkat (2024.3.1) 이상  
- **Android SDK**: API 35 (Android 15)  
- **Gradle**: 8.9.2 이상  
- **Kotlin**: 2.1.21

### 주요 Gradle 명령어
```bash
# 프로젝트 클린
./gradlew clean

# 디버그 빌드
./gradlew assembleDebug

# 릴리즈 빌드
./gradlew assembleRelease

# 테스트 실행
./gradlew test                    # 단위 테스트
./gradlew connectedAndroidTest    # Android 테스트

# 전체 빌드 및 테스트
./gradlew build
```

---

## 앱 구조 및 상태 관리 설계

### 앱 구조

```
ble_simulator/
├── app/                          # 메인 애플리케이션 모듈
├── core/                         # 핵심 모듈들
│   ├── data/                     # 데이터 레이어
│   ├── domain/                   # 도메인 레이어 (UseCase, Repository)
│   ├── model/                    # 데이터 모델
│   ├── testing/                  # 테스트 유틸리티
│   ├── virtual-api/              # 가상 API 구현
│   └── designsystem/             # 디자인 시스템
├── feature/                      # 기능 모듈들
│   ├── device-list/              # BLE 장치 목록 화면
│   └── data-transfer/            # 데이터 전송 화면
├── build-logic/                  # 빌드 로직
├── scripts/                      # 유틸리티 스크립트
└── gradle/                       # Gradle 설정
```


<img width="2529" height="1456" alt="module-dependencies" src="https://github.com/user-attachments/assets/7c2cb1ea-3ffe-4a5c-8623-9f14021c60aa" />

### Clean Architecture 패턴
```
Presentation Layer (UI, ViewModel)
   ↓
Domain Layer (UseCase, Repository Interface)
   ↓
Data Layer (Repository 구현, DataSource)
```

### 상태 관리 설계

- **UI**는 상태(`UiState`)를 표시하고, 사용자의 화면 상호작용 발생 시 `handleIntent`를 호출합니다.
- `handleIntent`는 `ViewModel` 내부의 함수를 호출하여 로직을 처리합니다.
- 외부 데이터 송수신 결과를 `ViewModel`이 상태로 관리하며, 각각의 상태 변경은 `combine`을 통해 단일 상태로 합칩니다.
- 단일 상태 변경 시, 이를 참조하는 Compose UI가 **자동으로 리컴포지션**됩니다.

---

## Mock 데이터 및 연결 시뮬레이션 구현

### DeviceListActivity
1. **스캔 동작**
   - 첫 화면 진입 시 또는 *스캔 시작* 버튼 클릭 시 **스캔 BottomSheet**가 나타납니다.
   - 시뮬레이션에서는 5초 동안 주변 기기를 스캔한다고 가정하며, 실제 BLE 콜백 대신 **Flow 기반 비동기 스트림**으로 구현했습니다.
   - **고려 사항**
      - 초기 진입 시 기기 자동 수신 여부 대응
      - 새 기기 탐색 요청 대응
      - 일정 시간 스캔 후 자동 종료 시나리오 대응
2. **RSSI 조건 처리**
   - RSSI < -75 → 연결 불가능 다이얼로그 표시
   - RSSI ≥ -75 → 연결 가능 (1초 `delay`로 연결 진행 애니메이션 효과 제공)
3. 연결 성공 시 선택한 기기 정보를 넘겨 **데이터 송수신 화면**으로 이동

---

### DataTransferActivity
1. 이전 화면에서 전달받은 기기 정보를 표시
2. **송신 로그**와 **수신 로그**를 구분하여 관리
3. 송신:
   - 입력 후 *전송* 시 송신 로그에 추가
4. 수신:
   - 다른 기기에서 1초마다 랜덤 메시지를 받는 시뮬레이션
5. 입력창 포커스 시 키보드 표시, *전송* 버튼 클릭 시 송신 로그 반영
6. 세로/가로 모드 전환 시 UI 깨짐 방지
7. 뒤로 가기 클릭 시 이전 화면으로 복귀

---

## 테스트 방법 및 커버리지

### 테스트 설명

- **DataSource 단위 테스트**
   - Mock API를 통해 정상 데이터 수신 여부 테스트

- **Repository 단위 테스트**
   - DataSource로부터 받은 데이터를 Flow로 emit하는 로직 검증

- **UseCase 단위 테스트**
   - Repository 데이터를 기반으로 한 비즈니스 로직 테스트
   - 현재는 단순 전달이지만, 향후 유지보수 확장성을 고려하여 작성

- **ViewModel 단위 테스트**
   - UseCase 및 Intent 처리 결과가 `uiState`, `SideEffect`에 올바르게 반영되는지 검증

- **UI 테스트**
   - ViewModel 상태 변경 시 UI 변화 확인
   - 상태 변경에 따른 컴포넌트 표시 여부 검증

### 테스트 실행

- **단위 테스트**: `src/test/`
- **Android 테스트**: `src/androidTest/`

```bash
# 모든 단위 테스트 실행
./gradlew test

# 특정 모듈 테스트
./gradlew :feature:device-list:test
./gradlew :feature:data-transfer:test

# Android 테스트 (연결된 디바이스 필요)
./gradlew connectedAndroidTest

```


