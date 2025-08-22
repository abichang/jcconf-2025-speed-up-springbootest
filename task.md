# Daily Golden API 開發任務

## 專案需求

實作一個 API，讓我們可以每天送金幣給 user

- 每天只能送一次
- 每次送 10 金幣
- UTC +0 0:00 重設

## 架構設計

Controller → Service → Repository Interface → Repository Impl → Mapper

### 物件分層說明

- **Entity**: 業務邏輯層物件，在 Repository Interface 和 Service 層使用
- **DbDto**: 資料庫層物件，只在 Mapper 層使用
- **Repository Impl**: 負責 DbDto ↔ Entity 轉換

## 開發任務清單

### 資料庫相關

- [x] Task 1: 建立資料庫 schema (user, wallet, daily_gold_reward 表格)
- [x] Task 2: 建立 Flyway migration 檔案
- [x] Task 3: 設定 H2 測試資料庫相關配置

### Mapper 層

- [x] Task 4: 建立 UserDbDto, WalletDbDto, DailyGoldRewardDbDto (用於 Mapper 層)
- [x] Task 5: 建立 UserMapper, WalletMapper, DailyGoldRewardMapper interface (使用 annotation，操作 DbDto)

#### UserMapperTest (使用 @MybatisTest 和 H2)

- [x] Task 6: UserMapperTest - insert_and_selectById

#### WalletMapperTest (使用 @MybatisTest 和 H2)

- [ ] Task 7: WalletMapperTest - shouldInsertAndSelectWallet
- [ ] Task 8: WalletMapperTest - shouldUpdateGold
- [ ] Task 9: WalletMapperTest - shouldReturnNullWhenWalletNotExists

#### DailyGoldRewardMapperTest (使用 @MybatisTest 和 H2)

- [ ] Task 10: DailyGoldRewardMapperTest - shouldInsertReward
- [ ] Task 11: DailyGoldRewardMapperTest - shouldCountByUserAndDate
- [ ] Task 12: DailyGoldRewardMapperTest - shouldReturnZeroWhenNoRewardOnDate
- [ ] Task 13: DailyGoldRewardMapperTest - shouldEnforceUniqueConstraint

### 實體層

- [ ] Task 14: 建立 User, Wallet, DailyGoldReward entity 物件 (用於 Repository Interface + Service 層)

### Repository 層

- [ ] Task 15: 建立 UserRepository, WalletRepository, DailyGoldRewardRepository interface
- [ ] Task 16: 實作 UserRepositoryImpl, WalletRepositoryImpl, DailyGoldRewardRepositoryImpl

#### UserRepositoryImplTest (Mockito.mock)

- [ ] Task 17: UserRepositoryImplTest - shouldGetUserByIdSuccessfully
- [ ] Task 18: UserRepositoryImplTest - shouldReturnNullWhenUserNotExists

#### WalletRepositoryImplTest (Mockito.mock)

- [ ] Task 19: WalletRepositoryImplTest - shouldGetWalletByUserIdSuccessfully
- [ ] Task 20: WalletRepositoryImplTest - shouldUpdateGoldSuccessfully
- [ ] Task 21: WalletRepositoryImplTest - shouldReturnNullWhenWalletNotExists

#### DailyGoldRewardRepositoryImplTest (Mockito.mock)

- [ ] Task 22: DailyGoldRewardRepositoryImplTest - shouldCheckClaimedTodaySuccessfully
- [ ] Task 23: DailyGoldRewardRepositoryImplTest - shouldCreateRewardSuccessfully
- [ ] Task 24: DailyGoldRewardRepositoryImplTest - shouldHandleMapperException

### Service 層

- [ ] Task 25: 實作 DailyGoldRewardService

#### DailyGoldRewardServiceTest (Mockito.mock)

- [ ] Task 26: DailyGoldRewardServiceTest - shouldClaimDailyGoldenWhenUserExistsAndNotClaimedToday
- [ ] Task 27: DailyGoldRewardServiceTest - shouldThrowExceptionWhenUserNotExists
- [ ] Task 28: DailyGoldRewardServiceTest - shouldThrowExceptionWhenAlreadyClaimedToday
- [ ] Task 29: DailyGoldRewardServiceTest - shouldThrowExceptionWhenWalletNotExists
- [ ] Task 30: DailyGoldRewardServiceTest - shouldCalculateUTCDateCorrectly

### Controller 層

- [ ] Task 31: 建立 DailyGoldRewardController

#### DailyGoldRewardControllerTest (使用 @SpringBootTest)

- [ ] Task 32: DailyGoldRewardControllerTest - shouldClaimDailyGoldenSuccessfully
- [ ] Task 33: DailyGoldRewardControllerTest - shouldReturnConflictWhenAlreadyClaimedToday
- [ ] Task 34: DailyGoldRewardControllerTest - shouldReturnNotFoundWhenUserNotExists
- [ ] Task 35: DailyGoldRewardControllerTest - shouldClaimAfterMidnightUTCReset
- [ ] Task 36: DailyGoldRewardControllerTest - shouldHandleTimezoneCorrectly
- [ ] Task 37: DailyGoldRewardControllerTest - shouldValidateUserIdFormat

### 文件

- [x] Task 38: 建立 task.md 檔案記錄所有測試案例和任務

## 測試策略

- **Controller**: @SpringBootTest + Real Database
- **Service**: Mockito.mock() + Mock Repository
- **Repository**: Mockito.mock() + Mock Mapper (UserRepository, WalletRepository, DailyGoldRewardRepository)
- **Mapper**: @MybatisTest + Real H2 Database

## 詳細測試案例設計

### 1. DailyGoldRewardControllerTest (@SpringBootTest + Real Database)

#### shouldClaimDailyGoldenSuccessfully

```
Given: 資料庫有用戶ID=1，錢包有500金幣，今天未領取
When: POST /user/1/daily-golden
Then: 回傳200，body包含{"success": true, "amount": 10, "totalGold": 510}，
      wallet更新為510，daily_gold_reward新增記錄
```

#### shouldReturnConflictWhenAlreadyClaimedToday

```
Given: 資料庫有用戶ID=1，今天已經在daily_gold_reward有記錄
When: POST /user/1/daily-golden
Then: 回傳409，錯誤訊息"Already claimed daily golden today"，資料庫不變
```

#### shouldReturnNotFoundWhenUserNotExists

```
Given: 資料庫沒有用戶ID=999的記錄
When: POST /user/999/daily-golden
Then: 回傳404，錯誤訊息"User not found"
```

#### shouldClaimAfterMidnightUTCReset

```
Given: 用戶昨天已領取，現在是UTC+0新的一天
When: POST /user/1/daily-golden
Then: 回傳200，成功領取，資料庫更新
```

#### shouldHandleTimezoneCorrectly

```
Given: 當前時間為UTC+8 06:00（相當於UTC 22:00前一天）
When: POST /user/1/daily-golden
Then: 系統正確判斷為前一天，允許領取
```

#### shouldValidateUserIdFormat

```
Given: 任何狀況
When: POST /user/invalid/daily-golden
Then: 回傳400，錯誤訊息"Invalid user ID format"
```

### 2. DailyGoldRewardServiceTest (Mockito.mock)

#### shouldClaimDailyGoldenWhenUserExistsAndNotClaimedToday

```
Given: userRepository.getUserById(1L)回傳User，
       dailyGoldRewardRepository.hasClaimedToday(1L, today)回傳false，
       walletRepository.getWalletByUserId(1L)回傳Wallet(gold=500)
When: service.claimDailyGolden(1L)
Then: 呼叫walletRepository.updateGold(1L, 510)，
      呼叫dailyGoldRewardRepository.createReward(1L, today, 10)，回傳成功結果
```

#### shouldThrowExceptionWhenUserNotExists

```
Given: userRepository.getUserById(999L)回傳null
When: service.claimDailyGolden(999L)
Then: 拋出UserNotFoundException
```

#### shouldThrowExceptionWhenAlreadyClaimedToday

```
Given: userRepository.getUserById(1L)回傳User，
       dailyGoldRewardRepository.hasClaimedToday(1L, today)回傳true
When: service.claimDailyGolden(1L)
Then: 拋出DailyGoldenAlreadyClaimedException
```

#### shouldThrowExceptionWhenWalletNotExists

```
Given: userRepository.getUserById(1L)回傳User，
       walletRepository.getWalletByUserId(1L)回傳null
When: service.claimDailyGolden(1L)
Then: 拋出WalletNotFoundException（這是系統Bug，用戶存在必須有錢包）
```

#### shouldCalculateUTCDateCorrectly

```
Given: 當前時間為不同時區
When: service.claimDailyGolden(1L)
Then: 正確轉換為UTC日期進行判斷
```

### 3. Repository 測試 (Mockito.mock)

#### UserRepositoryImplTest

##### shouldGetUserByIdSuccessfully

```
Given: userMapper.selectUserById(1L)回傳User物件
When: userRepository.getUserById(1L)
Then: 回傳User物件
```

##### shouldReturnNullWhenUserNotExists

```
Given: userMapper.selectUserById(999L)回傳null
When: userRepository.getUserById(999L)
Then: 回傳null
```

#### WalletRepositoryImplTest

##### shouldGetWalletByUserIdSuccessfully

```
Given: walletMapper.selectWalletByUserId(1L)回傳Wallet(gold=500)
When: walletRepository.getWalletByUserId(1L)
Then: 回傳Wallet物件
```

##### shouldUpdateGoldSuccessfully

```
Given: walletMapper.updateGold(1L, 510)執行成功
When: walletRepository.updateGold(1L, 510)
Then: 執行無異常
```

##### shouldReturnNullWhenWalletNotExists

```
Given: walletMapper.selectWalletByUserId(999L)回傳null
When: walletRepository.getWalletByUserId(999L)
Then: 回傳null
```

#### DailyGoldRewardRepositoryImplTest

##### shouldCheckClaimedTodaySuccessfully

```
Given: dailyGoldRewardMapper.countByUserAndDate(1L, "2024-01-15")回傳1
When: dailyGoldRewardRepository.hasClaimedToday(1L, LocalDate.of(2024,1,15))
Then: 回傳true
```

##### shouldCreateRewardSuccessfully

```
Given: dailyGoldRewardMapper.insertReward()執行成功
When: dailyGoldRewardRepository.createReward(1L, LocalDate.now(), 10)
Then: 執行無異常
```

##### shouldHandleMapperException

```
Given: mapper操作拋出DataAccessException
When: repository相關方法被呼叫
Then: 拋出RepositoryException
```

### 4. Mapper 測試 (@MybatisTest + Real H2)

#### UserMapperTest

##### shouldInsertUserRecord

```
Given: H2資料庫為空
When: userMapper.insertUser(User(username="testuser"))
Then: 資料庫有一筆user記錄
```

##### shouldSelectUserById

```
Given: 資料庫有user_id=1的記錄
When: userMapper.selectUserById(1L)
Then: 回傳對應的User物件
```

#### WalletMapperTest

##### shouldInsertAndSelectWallet

```
Given: 資料庫有user_id=1的記錄
When: walletMapper.insertWallet(Wallet(userId=1L, gold=500))
      然後walletMapper.selectWalletByUserId(1L)
Then: 回傳Wallet(gold=500)
```

##### shouldUpdateGold

```
Given: 資料庫有wallet記錄gold=500
When: walletMapper.updateGold(1L, 510)
Then: 資料庫記錄更新為gold=510
```

##### shouldReturnNullWhenWalletNotExists

```
Given: 資料庫沒有user_id=999的wallet記錄
When: walletMapper.selectWalletByUserId(999L)
Then: 回傳null
```

#### DailyGoldRewardMapperTest

##### shouldInsertReward

```
Given: 資料庫有user記錄
When: dailyGoldRewardMapper.insertReward(DailyGoldReward(userId=1L, rewardDate="2024-01-15", amount=10))
Then: 資料庫新增daily_gold_reward記錄
```

##### shouldCountByUserAndDate

```
Given: 資料庫有user_id=1在2024-01-15的reward記錄
When: dailyGoldRewardMapper.countByUserAndDate(1L, "2024-01-15")
Then: 回傳1
```

##### shouldReturnZeroWhenNoRewardOnDate

```
Given: 資料庫沒有user_id=1在2024-01-16的reward記錄
When: dailyGoldRewardMapper.countByUserAndDate(1L, "2024-01-16")
Then: 回傳0
```

##### shouldEnforceUniqueConstraint

```
Given: 資料庫已有user_id=1在2024-01-15的記錄
When: 嘗試插入相同user_id和date的記錄
Then: 拋出數據庫約束異常
```

## 資料庫設計

### user 表格

```sql
CREATE TABLE user
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    username   VARCHAR(100) NOT NULL UNIQUE,
    created_at BIGINT       NOT NULL,
    updated_at BIGINT       NOT NULL
);
```

### wallet 表格

```sql
CREATE TABLE wallet
(
    user_id    BIGINT PRIMARY KEY,
    gold       BIGINT NOT NULL DEFAULT 0,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
);
```

### daily_gold_reward 表格

```sql
CREATE TABLE daily_gold_reward
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT NOT NULL,
    reward_date DATE   NOT NULL,
    amount      BIGINT NOT NULL,
    created_at  BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id),
    UNIQUE KEY uk_user_date (user_id, reward_date)
);
```

## 業務規則

- 每天只能領取一次 Daily Golden
- 每次固定給予 10 金幣
- 重設時間為 UTC +0 0:00
- 用戶必須存在才能領取
- 用戶存在時必須要有對應的錢包，否則為系統Bug
- 使用資料庫事務確保 wallet 更新和 reward 記錄的一致性
- 日期比較基於 UTC 時區，避免時區問題
- 所有時間戳記使用 BIGINT 存儲 Instant.toEpochMilli() 結果，避免時區相關Bug