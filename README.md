# 當測試成為開發瓶頸：深入 Spring Boot Test 調教實戰

本專案為 JCConf 2025 議程「當測試成為開發瓶頸：深入 Spring Boot Test 調教實戰」的範例程式碼。

## 緣起

在產品開發過程中，我們的後端功能越來越豐富，單元測試也越寫越多，但伴隨而來的是兩個痛點：

1. **測試粒度太細**：每次重構、架構調整都變得寸步難行。
2. **執行時間過長**：2000+ 個測試需要跑 10+ 分鐘，CI/CD 流程變成了漫長的等待。

測試本該是開發的助力，為何卻成為最大的阻礙？
這是一個在困境中突破重圍的實戰故事，分享我們如何重新思考測試策略，讓測試架構脫胎換骨，並深入 Spring Boot Test 原理，找到效能優化的關鍵。

## 解決方案

這個專案展示了我們為了解決上述痛點所採取的測試優化策略。核心概念是**統一 Spring Boot 的 ApplicationContext**
，避免在不同的測試之間重複啟動容器。

### 主要策略

1. **放大測試粒度**：從針對單一 Method 的測試，轉向以 Use Case 為單位的整合測試。
2. **尋找 Context 分裂的元兇**：分析為何 `@SpringBootTest` 會建立多個不同的 `ApplicationContext`，主要原因通常是每個測試使用了不同的
   Mock Bean。
3. **統一測試基底**：建立一個 `Abstract Base Class` 作為所有整合測試的基礎，共享相同的測試設定。
4. **彈性替換 Bean**：使用 `@SpyBean` 來取代 `@MockBean`。`@SpyBean` 可以在不破壞 `ApplicationContext`
   一致性的前提下，讓我們針對特定測試的需求，動態地指定 Bean 的行為。

### 成果

透過以上優化，我們將產品後端中的單元測試執行時間從 **4 分 02 秒** 縮短至 **1 分 16 秒**，大幅改善了 75% 的執行效率。

# 此範例程式

## 如何開始

### 環境需求

- Java 17
- Maven (專案內已包含 Maven Wrapper，無需額外安裝)

### 建置專案

```bash
./mvnw clean package
```

### 執行測試

```bash
./mvnw clean test
```

## 相關連結

* [講者 Threads](https://www.threads.net/@_i.change)
