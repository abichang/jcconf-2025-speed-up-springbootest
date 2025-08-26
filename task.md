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

- [x] Task 7: WalletMapperTest - insert_and_selectByUserId
- [x] Task 8: WalletMapperTest - addGold_all_ok
- [x] Task 9: WalletMapperTest - selectByUserId_wallet_not_exists

#### DailyGoldRewardMapperTest (使用 @MybatisTest 和 H2)

- [x] Task 10: DailyGoldRewardMapperTest - insert_and_countByUserAndDate
- [x] Task 11: DailyGoldRewardMapperTest - countByUserAndDate_not_exists
- [x] Task 12: DailyGoldRewardMapperTest - insert_reward_unique_constraint_violation

### 實體層

- [x] Task 13: 建立 User, Wallet, DailyGoldReward entity 物件 (用於 Repository Interface + Service 層)

### Repository 層

- [x] Task 14: 建立 UserRepository interface
- [x] Task 15: 實作 UserRepositoryImpl
- [x] Task 16: UserRepositoryImplTest  (Mockito.mock) - getById_all_ok
- [x] Task 17: UserRepositoryImplTest  (Mockito.mock) - getById_user_not_existed
- [x] Task 18: 建立 WalletRepository interface
- [x] Task 19: 實作 WalletRepositoryImpl
- [x] Task 20: WalletRepositoryImplTest  (Mockito.mock) - getByUserId_all_ok
- [x] Task 21: WalletRepositoryImplTest  (Mockito.mock) - addGold_all_ok
- [x] Task 22: WalletRepositoryImplTest  (Mockito.mock) - getByUserId_wallet_not_existed
- [x] Task 23: 建立 DailyGoldRewardRepository interface
- [x] Task 24: 實作 DailyGoldRewardRepositoryImpl
- [x] Task 25: DailyGoldRewardRepositoryImplTest  (Mockito.mock) - hasClaimedToday_all_ok
- [x] Task 26: DailyGoldRewardRepositoryImplTest  (Mockito.mock) - createReward_all_ok
- [x] Task 27: DailyGoldRewardRepositoryImplTest  (Mockito.mock) - mapper_exception_handling

### Service 層

- [x] Task 28: 實作 DailyGoldRewardService

#### DailyGoldRewardServiceTest (Mockito.mock)

- [x] Task 29: DailyGoldRewardServiceTest - claim_all_ok
- [x] Task 30: DailyGoldRewardServiceTest - duplicate_claim
- [x] Task 31: DailyGoldRewardServiceTest - handle_user_not_found
- [x] Task 32: DailyGoldRewardServiceTest - handle_wallet_not_found

#### DailyGoldRewardServiceTransactionalTest (@SpringBootTest)

- [x] Task 33: DailyGoldRewardServiceTransactionalTest - claim_fail_wallet_rollback

### Controller 層

- [x] Task 34: 建立 DailyGoldRewardController

#### DailyGoldRewardControllerTest (使用 @SpringBootTest)

- [x] Task 35: DailyGoldRewardControllerTest - claim_all_ok
- [x] Task 36: DailyGoldRewardControllerTest - duplicate_claim
- [x] Task 37: DailyGoldRewardControllerTest - user_not_found
- [x] Task 38: DailyGoldRewardControllerTest - can_claim_again_after_utc_midnight
- [x] Task 39: DailyGoldRewardControllerTest - invalid_user_id

### 重構 Service 使用 Domain Model 方式

- [x] Task 40: 在 WalletDbDto 加入 version 欄位並建立資料庫 migration
- [x] Task 41: 在 WalletMapper 加入 selectByUserId 的 version 欄位查詢
- [x] Task 42: 更新 WalletMapperTest 確保 version 欄位測試通過
- [x] Task 43: 在 Wallet entity 加入 version 欄位
- [x] Task 44: 在 Wallet entity 加入 addGold(amount, updatedAt) 方法
- [x] Task 45: 在 WalletRepository interface 加入 save(wallet) 方法
- [x] Task 46: 在 WalletMapper 加入 update(wallet) 方法 (使用 version 樂觀鎖)
- [x] Task 47: 更新 WalletMapperTest 測試：update_success、update_optimistic_lock_conflict
- [x] Task 48: 在 WalletRepositoryImpl 實作 save(wallet) 方法 (處理樂觀鎖衝突)
- [x] Task 49: 更新 WalletRepositoryImplTest 測試：save_success、save_optimistic_lock_conflict
- [x] Task 50: 修改 DailyGoldRewardService 使用新方式 (wallet.addGold + walletRepository.save)
- [x] Task 51: 更新 DailyGoldRewardServiceTest 相關測試：claim_all_ok、handle_optimistic_lock_conflict
- [x] Task 52: 實作 DailyGoldRewardServiceTest - handle_wallet_not_found 測試案例
- [x] Task 53: 運行所有測試確保重構成功，移除專案裡所有未被使用的程式碼

### 文件

- [x] Task 55: 建立 task.md 檔案記錄所有測試案例和任務

## 測試策略

- **Controller**: @SpringBootTest + MockMvc + Service @MockBean
- **Service**: Mockito.mock() + Mock Repository
- **Service Transactional**: @SpringBootTest + Real WalletRepository + Mock Other Dependencies (驗證事務回滾)
- **Repository**: Mockito.mock() + Mock Mapper (UserRepository, WalletRepository, DailyGoldRewardRepository)
- **Mapper**: @MybatisTest + Real H2 Database

## 重構相關新測試案例

### WalletTest (單元測試)

#### addGold_should_update_gold_and_version

```
Given: Wallet(gold=100, version=1, updatedAt=t1)
When: wallet.addGold(50, t2)
Then: gold=150, version=2, updatedAt=t2
```

#### addGold_should_handle_negative_amount

```
Given: Wallet(gold=100, version=1)
When: wallet.addGold(-30, now)
Then: gold=70, version=2
```

### WalletMapperTest 新增測試

#### update_success

```
Given: 資料庫有 wallet(user_id=1, gold=100, version=1)
When: walletMapper.update(Wallet(user_id=1, gold=150, version=1))
Then: 資料庫更新為 gold=150, version=2, 回傳 affected rows = 1
```

#### update_optimistic_lock_conflict

```
Given: 資料庫有 wallet(user_id=1, gold=100, version=2)
When: walletMapper.update(Wallet(user_id=1, gold=150, version=1))
Then: 更新失敗，affected rows = 0 (版本不匹配)
```

### WalletRepositoryImplTest 新增測試

#### save_success

```
Given: walletMapper.update() 回傳 1 (成功更新)
When: walletRepository.save(wallet)
Then: 執行無異常
```

#### save_optimistic_lock_conflict

```
Given: walletMapper.update() 回傳 0 (版本衝突)
When: walletRepository.save(wallet)
Then: 拋出 OptimisticLockException
```

### DailyGoldRewardServiceTest 新增測試

#### handle_optimistic_lock_conflict

```
Given: walletRepository.getByUserId() 回傳 wallet
       wallet.addGold() 執行成功
       walletRepository.save() 拋出 OptimisticLockException
When: service.claim(1L)
Then: 拋出 OptimisticLockException，不呼叫 dailyGoldRewardRepository.claim()
```

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

#### claim_all_ok

```
Given: dailyGoldRewardRepository.hasClaimed(1L, now)回傳false
When: service.claim(1L)
Then: 呼叫walletRepository.addGold(1L, 10, now)，
      呼叫dailyGoldRewardRepository.claim(reward)，執行成功
```

#### duplicate_claim

```
Given: dailyGoldRewardRepository.hasClaimed(1L, now)回傳true
When: service.claim(1L)
Then: 拋出DailyGoldenClaimedException("userId=1")
```

#### handle_user_not_found

```
Given: userRepository.getById(999L)拋出UserNotFoundException
When: service.claim(999L)
Then: 拋出UserNotFoundException("userId=999")
```

#### handle_wallet_not_found

```
Given: userRepository.getById(1L)回傳正常User物件
       walletRepository.getByUserId(1L)拋出WalletNotFoundException
When: service.claim(1L)
Then: 拋出WalletNotFoundException("userId=1")，
      不呼叫dailyGoldRewardRepository相關方法
```

### 3. DailyGoldRewardServiceTransactionalTest (@SpringBootTest)

#### claim_fail_wallet_rollback

```
Given: 資料庫有用戶ID=1，錢包有500金幣，
       walletRepository為真實實作（操作真實資料庫），
       userRepository.getById()正常回傳User物件，
       dailyGoldRewardRepository.hasClaimed()回傳false，
       dailyGoldRewardRepository.claim()拋出RuntimeException
When: service.claim(1L)
Then: 拋出RuntimeException，
      錢包金幣數回滾為原始值500（驗證@Transactional生效）
```

### 4. Repository 測試 (Mockito.mock)

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

##### shouldAddGoldSuccessfully

```
Given: walletMapper.addGold(1L, 10, updatedAt)執行成功
When: walletRepository.addGold(1L, 10)
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
Given: dailyGoldRewardMapper.countByUserAndDate(1L, 20240115)回傳1
When: dailyGoldRewardRepository.hasClaimedToday(1L, 20240115)
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

### 5. Mapper 測試 (@MybatisTest + Real H2)

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

##### shouldAddGold

```
Given: 資料庫有wallet記錄gold=500
When: walletMapper.addGold(1L, 10, updatedAt)
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
When: dailyGoldRewardMapper.insertReward(DailyGoldReward(userId=1L, rewardDate=20240115, amount=10))
Then: 資料庫新增daily_gold_reward記錄
```

##### shouldCountByUserAndDate

```
Given: 資料庫有user_id=1在20240115的reward記錄
When: dailyGoldRewardMapper.countByUserAndDate(1L, 20240115)
Then: 回傳1
```

##### shouldReturnZeroWhenNoRewardOnDate

```
Given: 資料庫沒有user_id=1在20240116的reward記錄
When: dailyGoldRewardMapper.countByUserAndDate(1L, 20240116)
Then: 回傳0
```

##### shouldEnforceUniqueConstraint

```
Given: 資料庫已有user_id=1在20240115的記錄
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
    reward_date INT    NOT NULL,
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

## 新的測試策略

- **Controller**
    - IsolatedTest - @SpringBootTest + MockMvc + Service @MockBean
    - IntegrationTest - 使用 @SystemDbTest + New Controller Instance + 真實 H2 資料庫進行整合測試

## 新增待辦事項 - DailyGoldRewardControllerIntegrationTest

### 使用 @SystemDbTest + 真實 H2 資料庫整合測試

- [x] Task 56: 設置 @SystemDbTest 測試基礎架構
- [ ] Task 57: 重寫 claim_all_ok - 使用真實資料庫驗證完整流程
- [ ] Task 58: 重寫 claim_duplicate_same_day - 測試同一天重複領取的錯誤處理
- [ ] Task 59: 重寫 claim_user_not_found - 測試使用者不存在的異常處理
- [ ] Task 60: 重寫 claim_wallet_not_found - 測試錢包不存在的異常處理
- [ ] Task 61: 新增 claim_utc_midnight_reset - 測試 UTC 午夜重設功能
- [ ] Task 62: 新增 claim_multiple_users_same_day - 測試多用戶同一天領取
- [ ] Task 63: Rename DailyGoldRewardControllerTest to DailyGoldRewardControllerIsolatedTest
