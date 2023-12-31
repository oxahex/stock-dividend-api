# 주식 배당금
1. API
2. Entity

## API
1. 특정 회사의 배당금 조회(GET)
2. 배당금 검색 - 자동완성(GET)
3. 회사 리스트 조회(GET)
4. 관리자 기능 - 배당금 저장(POST)
5. 관리자 기능 - 배당금 삭제(DELETE)
6. 회원
   1. 회원가입
   2. 로그인
   3. 로그아웃

### 특정 회사의 배당금 조회
GET /finance/dividend/{companyName}

```json
{
  "companyName": "회사 이름",
  "dividend": [
    {
      "date": "2022.3.21",
      "price": "2,00"
    }
  ]
}
```

### 배당금 검색 - 자동완성
GET /company/autocomplete?keyword=O
키워드를 입력할 때마다 서버로 키워드가 날아가고, 그 때마다 완성할 수 있는 단어가 응답으로 오는 것.

```json
{
  "result": ["O", "OAS"]
}
```

### 회사 리스트 조회
GET /company

```json
{
  "result": [
    {
      "companyName": "회사 이름",
      "ticker": "COM"
    }
  ]
}
```

### 관리자 기능 - 배당금 저장
POST /company
{ ticker: "COM" }

```json
{
  "ticker": "COM",
  "companyName": "회사 이름"
}
```

### 관리자 기능 - 배당금 삭제
DELETE /company?ticker=COM
Request Body로 넘기지 않고 Parameter로 넘기는 이유: POST는 Request Body의 역할이 명시되어 있는데, DELETE의 경우 따로 명시된 것이 없음. 서버에서 바디를 무시하도록 할 수도 있으므로 명시적으로 뭘 할 건지 표시하기 위해서. (RFC)

### 회원 관리
#### 회원가입
#### 로그인
#### 로그아웃


## Entity

### 회사
| column | type   | unique | example   |
|--------|--------|--------|-----------|
| id     | Long   | O      | 1         |
| name   | String | X      | Coca-Cola |
| ticker | String | O      | COKE      |

### 배당금
| column     | type          | unique | example    |
|------------|---------------|--------|------------|
| id         | Long          | O      | 1          |
| company_id | Long          | X      | 1          |
| date       | LocalDateTime | X      | 2023-09-25 |
| dividend   | String        | X      | 2.00       |

company_id로 회사 테이블과 배당금 테이블을 매치시키는 것이 company_name으로 매치하는 것보다 빠름.

분산 데이터베이스로 구성하는 경우 auto로 키를 생성하게 되면 